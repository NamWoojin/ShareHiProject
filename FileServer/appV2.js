const express = require('express');
const app = express();

const server = require('http').createServer(app);
const io = require('socket.io')(server, {
  cors: {
    origin: '*',
  },
});
const net = require('net');
const HashMap = require('hashmap');
const { v4: uuidv4 } = require('uuid');
const KEY = require('./src/config/key/key');

//////////////// socket map system /////////////
let flag = 0;
function SocketInfo(id, name, socket, type, isShare, fileSender, fileReceiver, targetId) {
  this.id = id; // 소켓 자체를 유일하게 구분하는 PK
  this.name = name; // ad_id
  this.socket = socket; // 소켓 자체의 객체
  this.type = type; // android web
  this.isShare = isShare;
  this.fileSender = fileSender;
  this.fileReceiver = fileReceiver;
  this.targetId = targetId; // 웹이나 안드로이드가 공유중인 디바이스에 접근하고 싶을 때 공유중인 디바이스의 id를 저장하는 변수
  this.size = 0;
  this.tmpfileSize = 0;
  this.flag = 0;
} // 오프라인인 것은 못봄 : -> 소켓 연결이 되면 => 조회 : 현재 공유 중인 디바이스를(나를 제외한), 공유 중이 아닌것들(나를 제외한)
// API => 오프라인,
let sockets = [];

console.log('서버 가동중...');

////////// Android Server /////////////////
let andServer = net.createServer((socket) => {
  /**
   * Android
   * 연결된 직후 수행되는 코드
   * 설명 : 소켓을 등록하고, 연결이 잘 되었다는 메시지를 클라이언트로 전송한다
   * 메시지 : {"namespace":1010,"status":200,"message":"OK"}
   */
  console.log('Success - Android Connect');
  registSocket(socket, 'android');
  printSocket();

  /**
   * @type Android
   * @Init 파일 전송인지, JSON 제어인지 확인한다
   * @description 서버의 flag를 본다
   */
  if (flag === 1) {
    setFileReceiver(socket);
    let controlSocket = getControlSocket(socket);
    if (controlSocket === undefined) return;
    controlSocket.write(
      JSON.stringify({
        namespace: 7200,
      }) + '\n'
    );
    return;
  }
  responseOK(socket, 'android');

  /**
   * Android
   * 연결이 에러로 인해 끊겼을 경우 수행되는 코드
   * 설명 : 소켓 연결이 끊기면, 등록을 해제한다.
   */
  socket.on('error', (err) => {
    deleteSocket(socket);
    printSocket();
  });
  /**
   * Android
   * 연결이 끊겼을 경우 수행되는 코드
   * 설명 : 소켓 연결이 끊기면, 등록을 해제한다.
   */
  socket.on('end', () => {
    deleteSocket(socket);
    printSocket();
  });

  /**
   * Android
   * 커스텀 프로토콜로 정의된 콜백함수들이 위치하는 곳
   * 설명 : namespace로 정의된 모든 콜백함수들이 모여있다
   */

  socket.on('data', (data) => {
    /**
     * @type Android
     * @description 데이터 전송 socket인지 구분한다.
     */

    /**
     * Android
     * 4001
     * 설명 : JSON 파싱이 실패할 경우 부른다
     * 메시지 :
     */
    if (!isJsonString(data)) {
      responseBad(socket, 'android');
      return;
    }
    data = JSON.parse(data);
    console.log(data);
    let id = parseInt(data.namespace);

    switch (id) {
      /**
       * Android
       * 1020
       * 설명 : 공유 디바이스로 지정한다.
       * 메시지 : {"namespace":1010,"status":200,"message":"OK"}
       */
      case KEY.SET_SHARE_ID:
        setShareDevice(socket);
        //responseOK(socket, 'android');
        printSocket(socket);
        break;

      /**
       * @type Android
       * @namespace 7200
       * @description 파일 전송 전처리 작업
       */
      case KEY.SET_SHARE_DATA:
        shareData = socketMap.get(socket);
        console.log('share data : ' + shareData);
        idMap.get(shareDevice).write(
          JSON.stringify({
            namespace: KEY.SET_SHARE_DATA,
          }) + '\n'
        );
        break;
      /**
       * Android
       * 1050
       * 설명 : 내 디바이스를 제외한 현재 공유 중인 모든 디바이스의 정보를 클라이언트에게 제공한다
       * 메시지 :{"devices":[{"id":"c69ad27e-48ff-4849-b9ed-568a6935e794","name":"c69ad27e-48ff-4849-b9ed-568a6935e794"}]}
       */
      case 1050:
        let val = getShareDevices(socket);
        socket.write(
          JSON.stringify({
            namespace: 1050,
            devices: val,
          }) + '\n'
        );
        break;
      /**
       * Android
       * 1060
       * 설명 : 내 디바이스를 제외한 현재 공유 중이 '아닌' 모든 디바이스의 정보를 클라이언트에게 제공한다
       * 메시지 :{"namespace":1060,"devices":[{"id":"c45f6a42-259c-4f48-a469-18f26d89a561","name":"c45f6a42-259c-4f48-a469-18f26d89a561"}]}
       */
      case 1060:
        let val2 = getNotShareDevices(socket);
        socket.write(
          JSON.stringify({
            namespace: 1060,
            devices: val2,
          }) + '\n'
        );
        break;
      /**
       * Android
       * 1070
       * 설명 : 공유된 특정 디바이스에 나의 디바이스를 연결한다.
       * 메시지 : {"namespace":1010,"status":200,"message":"OK"}
       */
      case 1070:
        connectToShareDevice(socket, data.id);
        responseOK(socket);
        break;

      /**
       * Android
       * 2000
       * 설명 : 폴더 디렉토리 구조를 공유 디바이스에게 요청한다.
       * 메시지 :{"namespace":2100,"targetId":"9ebe9cf8-61aa-43ee-bcfc-66005e81f287","path":"./"}
       */
      case KEY.GET_TREE_OF_FOLDERS:
        let targetSocket = getTargetSocket(socket);
        targetSocket.write(
          JSON.stringify({
            namespace: 2100,
            targetId: getId(socket),
            path: data.path,
          }) + '\n'
        );
        break;

      /**
       * Android
       * 2100
       * 설명 : 폴더 디렉토리 구조를 받아서 클라이언트에게 제공한다
       * 메시지 :{"data":"folder directory JSON object "}
       */
      case 2100:
        getSocket(data.targetId, data.data);
        break;

      /**
       * Android
       * 2001
       * 설명 : 폴더 이름 수정을 요청한다
       * 메시지 :{"namespace":2001,"targetId":"9ebe9cf8-61aa-43ee-bcfc-66005e81f287","path":"./"}
       */
      case KEY.UPDATE_NAME_OF_FOLDER:
        let targetSocket2 = getTargetSocket(socket);
        targetSocket2.write(
          JSON.stringify({
            namespace: 2101,
            targetId: getId(socket),
            path: data.path,
            newPath: data.newPath,
          }) + '\n'
        );
        break;

      /**
       * Android
       * 2002
       * 설명 : 폴더를 삭제한다
       * 메시지 :{"namespace":2002,"targetId":"9ebe9cf8-61aa-43ee-bcfc-66005e81f287","path":"./"}
       */
      case KEY.DELETE_FOLDERS:
        let targetSocket3 = getTargetSocket(socket);
        targetSocket3.write(
          JSON.stringify({
            namespace: 2102,
            targetId: getId(socket),
            path: data.path,
          }) + '\n'
        );
        break;

      /**
       * Android
       * 2003
       * 설명 : 폴더를 추가한다
       * 메시지 :{"namespace":2002,"targetId":"9ebe9cf8-61aa-43ee-bcfc-66005e81f287","path":"./"}
       */
      case KEY.ADD_FOLDERS:
        let targetSocket4 = getTargetSocket(socket);
        targetSocket4.write(
          JSON.stringify({
            namespace: 2103,
            targetId: getId(socket),
            path: data.path,
            newFolder: data.newFolder,
          }) + '\n'
        );
        break;

      /**
       * @type Android
       * @namespace 7100
       * @description 파일 전송 전처리 최종작업
       */
      case 7100:
        console.log('7100');
        setSenderTmpfileSize(socket, data.tmpfileSize, data.size);

        let sender = getSender(socket);
        if (sender === undefined) break;
        sender.emit(
          KEY.SEND_FILE_STAT,
          JSON.stringify({
            tmpfileSize: data.tmpfileSize,
          })
        );
        break;
    }
  });
});

/**
 * 안드로이드와 웹 소켓 연결을 실시한다
 */
server.listen(9002, () => {
  console.log('웹-서버 socket연결');
});
andServer.listen(9003, () => {
  console.log('안드-서버 socket연결');
});

io.on('connection', (socket) => {
  /**
   * Web
   * 요청이 성공할 때 수행되는 코드
   * 설명 : 요청에 대한 성공 응답
   * 메시지 : {"namespace":1010,"status":200,"message":"OK"}
   */
  console.log('Success Web Connect');
  registSocket(socket, 'web');
  responseOK(socket, 'web');
  printSocket();

  /**
   * Web
   * 연결이 끊겼다는 코드
   * 설명 : 소켓을 해제하고 연결을 끊는다
   */
  socket.on('disconnect', () => {
    deleteSocket(socket);
    printSocket();
  });

  /**
   * Web
   * 1050
   * 설명 : 내 디바이스를 제외한 공유중인 모든 디바이스들의 정보를 클라이언트에게 제공한다
   * 메시지 : {"devices":[{"id":"c69ad27e-48ff-4849-b9ed-568a6935e794","name":"c69ad27e-48ff-4849-b9ed-568a6935e794"}]}
   */
  socket.on(1050, () => {
    let val = getShareDevices(socket);
    console.log(val);
    socket.emit(
      1050,
      JSON.stringify({
        devices: val,
      })
    );
  });

  /**
   * Web
   * 1060
   * 설명 : 내 디바이스를 제외한 현재 공유 중이 '아닌' 모든 디바이스의 정보를 클라이언트에게 제공한다
   * 메시지 :{"namespace":1060,"devices":[{"id":"c45f6a42-259c-4f48-a469-18f26d89a561","name":"c45f6a42-259c-4f48-a469-18f26d89a561"}]}
   */
  socket.on(1060, (data) => {
    let val = getNotShareDevices(socket);
    socket.emit(
      1060,
      JSON.stringify({
        devices: val,
      })
    );
  });

  /**
   * Web
   * 1070
   * 설명 : 공유된 특정 디바이스에 나의 디바이스를 연결한다.
   * 메시지 :{"namespace":1010,"status":200,"message":"OK"}
   */
  socket.on(1070, (data) => {
    data = JSON.parse(data);
    connectToShareDevice(socket, data.id);
    socket.emit(1070);
  });

  /**
   * Web
   * 2000
   * 설명 : 폴더 디렉토리 구조를 공유 디바이스에게 요청한다.
   * 메시지 :{"data":"folder directory JSON object "}
   */
  socket.on(KEY.GET_TREE_OF_FOLDERS, (data) => {
    if (!isJsonString(data)) {
      responseBad(socket, 'web');
      return;
    }
    data = JSON.parse(data);
    if (!checkSocket(getId(getTargetSocket(socket)))) {
      responseBad(socket, 'web');
      return;
    }
    let targetSocket = getTargetSocket(socket);
    targetSocket.write(
      JSON.stringify({
        namespace: 2100,
        targetId: getId(socket),
        path: data.path,
      }) + '\n'
    );
  });

  /**
   * Web
   * 2001
   * 설명 : 폴더 이름 수정을 요청한다
   * 메시지 :{"namespace":2001,"targetId":"9ebe9cf8-61aa-43ee-bcfc-66005e81f287","path":"./"}
   */
  socket.on(KEY.UPDATE_NAME_OF_FOLDER, (data) => {
    if (!isJsonString(data)) {
      responseBad(socket, 'web');
      return;
    }
    data = JSON.parse(data);
    if (!checkSocket(getId(getTargetSocket(socket)))) {
      responseBad(socket, 'web');
      return;
    }
    data = JSON.parse(data);
    let targetSocket = getTargetSocket(socket);
    targetSocket.write(
      JSON.stringify({
        namespace: 2101,
        targetId: getId(socket),
        path: data.path,
        newPath: data.newPath,
      }) + '\n'
    );
  });

  /**
   * Web
   * 2002
   * 설명 : 폴더 삭제를 요청한다
   * 메시지 :
   */
  socket.on(KEY.DELETE_FOLDERS, (data) => {
    if (!isJsonString(data)) {
      responseBad(socket, 'web');
      return;
    }
    data = JSON.parse(data);
    if (!checkSocket(getId(getTargetSocket(socket)))) {
      responseBad(socket, 'web');
      return;
    }
    data = JSON.parse(data);
    let targetSocket = getTargetSocket(socket);
    targetSocket.write(
      JSON.stringify({
        namespace: 2102,
        targetId: getId(socket),
        path: data.path,
      }) + '\n'
    );
  });

  /**
   * Web
   * 2003
   * 설명 : 폴더 추가를 요청한다
   * 메시지 :
   */
  socket.on(KEY.ADD_FOLDERS, (data) => {
    if (!isJsonString(data)) {
      responseBad(socket, 'web');
      return;
    }
    data = JSON.parse(data);
    if (!checkSocket(getId(getTargetSocket(socket)))) {
      responseBad(socket, 'web');
      return;
    }
    data = JSON.parse(data);
    let targetSocket = getTargetSocket(socket);
    targetSocket.write(
      JSON.stringify({
        namespace: 2103,
        targetId: getId(socket),
        path: data.path,
        newFolder: data.newFolder,
      }) + '\n'
    );
  });

  /**
   * @type Web
   * @namespace 7000
   * @param {path, name, ext, size} : 저장할 안드로이드 스토리지 경로, 파일 이름, 파일 확장자, 파일 총 사이즈
   * @description 파일 전송을 실시한다
   * @data
   */
  socket.on(KEY.SEND_FILE_STAT, (data) => {
    if (!isJsonString(data)) {
      responseBad(socket, 'web');
      return;
    }
    data = JSON.parse(data);
    if (!checkSocket(getId(getTargetSocket(socket)))) {
      responseBad(socket, 'web');
      return;
    }

    setSocketFlag(socket);

    let targetSocket = getTargetSocket(socket);
    targetSocket.write(
      JSON.stringify({
        namespace: 7100,
        targetId: getId(socket),
        path: data.path,
        name: data.name,
        size: data.size,
        ext: data.ext,
      }) + '\n'
    );
  });

  /**
   * @type Web
   * @namespace 7001
   * @description 파일 전송을 실시한다
   * @data
   */
  socket.on(KEY.SEND_FILE, (data) => {
    if (!checkSocket(getId(getTargetSocket(socket)))) {
      responseBad(socket, 'web');
      return;
    }

    let percent = getFilePercent(socket, data.length);

    socket.emit(
      KEY.SEND_FILE,
      JSON.stringify({
        percent: percent,
      })
    );

    getFileReceiver(socket).write(data);
  });
});

let getFilePercent = (socket, length) => {
  let size = 0;
  let tmpfileSize = 0;

  for (let i in sockets) {
    if (sockets[i]['socket'] === socket) {
      size = sockets[i]['size'];
      sockets[i]['tmpfileSize'] += length;
      tmpfileSize = sockets[i]['tmpfileSize'];
      break;
    }
  }

  let ans = Math.floor((tmpfileSize / size) * 100);
  console.log(ans);
  return ans;
};

////////////// LOGIC ///////////////////
function isJsonString(str) {
  try {
    var json = JSON.parse(str);
    return typeof json === 'object';
  } catch (e) {
    return false;
  }
}
let deleteSocket = (socket) => {
  for (let i in sockets) {
    if (sockets[i]['socket'] === socket) {
      sockets.splice(i, 1);
      return;
    }
  }
};
let registSocket = (socket, type) => {
  let uuid = uuidv4();
  let obj = searchBySocket(socket);
  if (obj !== null) return;
  sockets.push(new SocketInfo(uuid, uuid, socket, type));
};
let searchBySocket = (socket) => {
  for (i in sockets) {
    if (sockets[i] === socket) {
      return sockets[i];
    }
  }
  return null;
};
let searchById = (id) => {
  for (obj in sockets) {
    if (obj.id === id) {
      return obj;
    }
  }
  return null;
};
let checkSocket = (id) => {
  for (let i in sockets) {
    if (sockets[i]['id'] === id) return true;
  }
  return false;
};
let printSocket = (socket) => {
  console.log('count : ' + sockets.length);
  console.log('---------print--------------');
  for (let i in sockets) {
    for (let obj in sockets[i]) {
      if (obj === 'socket') continue;
      console.log(obj + ' : ' + sockets[i][obj]);
    }
  }
  console.log('----------------------------');
};
let responseOK = (socket, type) => {
  if (type === 'android') {
    socket.write(
      JSON.stringify({
        namespace: KEY.CONNECT,
        status: 200,
        message: 'OK',
      }) + '\n'
    );
  } else {
    socket.emit(
      1010,
      JSON.stringify({
        namespace: KEY.CONNECT,
        status: 200,
        message: 'OK',
      })
    );
  }
};
let responseBad = (socket, type) => {
  if (type === 'android') {
    socket.write(
      JSON.stringify({
        namespace: KEY.INVALID_JSON,
        status: 400,
        detail: 'INVALID JSON',
        message: 'BAD REQUEST',
      })
    );
  } else {
    io.to(socket.id).emit(
      KEY.INVALID_JSON,
      JSON.stringify({
        status: 400,
        detail: 'INVALID JSON',
        message: 'BAD REQUEST',
      })
    );
  }
};
let setShareDevice = (socket) => {
  for (let i in sockets) {
    if (sockets[i]['socket'] === socket) {
      sockets[i]['isShare'] = 'true';
      return;
    }
  }
};
let getShareDevices = (socket) => {
  let val = [];
  for (let i in sockets) {
    if (sockets[i]['socket'] === socket) continue;
    if (sockets[i]['isShare'] === 'true') {
      val.push({
        id: sockets[i].id,
        name: sockets[i].name,
      });
    }
  }
  return val;
};
let getNotShareDevices = (socket) => {
  let val = [];
  for (let i in sockets) {
    if (sockets[i]['socket'] === socket) continue;
    if (sockets[i]['isShare'] !== 'true') {
      val.push({
        id: sockets[i].id,
        name: sockets[i].name,
      });
    }
  }
  return val;
};
let connectToShareDevice = (socket, targetId) => {
  for (let i in sockets) {
    if (sockets[i]['socket'] === socket) {
      sockets[i]['targetId'] = targetId;
      return;
    }
  }
};

let getTargetSocket = (socket) => {
  let socketId = '';
  for (let i in sockets) {
    if (sockets[i]['socket'] === socket) {
      socketId = sockets[i]['targetId'];
      break;
    }
  }
  for (let i in sockets) {
    if (sockets[i]['id'] === socketId) {
      return sockets[i]['socket'];
    }
  }
};

let getId = (socket) => {
  for (let i in sockets) {
    if (sockets[i]['socket'] === socket) {
      return sockets[i]['id'];
    }
  }
};
let getSocket = (id, data) => {
  let mySocket = '';
  for (let i in sockets) {
    if (sockets[i]['id'] === id) {
      mySocket = sockets[i]['socket'];
      break;
    }
  }

  if (mySocket['type'] === 'android') {
    mySocket.write(
      JSON.stringify({
        namespace: 2000,
        data: data,
      })
    );
  } else {
    mySocket.emit(
      2000,
      JSON.stringify({
        data: data,
      })
    );
  }
};

let setSocketFlag = (socket) => {
  flag = 1;
  for (let i in sockets) {
    if (sockets[i]['socket'] === socket) {
      sockets[i]['flag'] = 1;
      sockets[i]['fileSender'] = sockets[i]['id'];
      return;
    }
  }
};

let setFileReceiver = (socket) => {
  flag = 0;

  let id = getId(socket);

  for (let i in sockets) {
    if (sockets[i]['flag'] === 1) {
      sockets[i]['flag'] = 0;
      sockets[i]['fileReceiver'] = id;
    }
  }
};

let getControlSocket = (socket) => {
  let targetId;
  let receiverId;
  for (let i in sockets) {
    if (sockets[i]['socket'] === socket) {
      receiverId = sockets[i]['id'];
      break;
    }
  }
  for (let i in sockets) {
    if (sockets[i]['fileReceiver'] === receiverId) {
      targetId = sockets[i]['targetId'];
      break;
    }
  }
  for (let i in sockets) {
    if (sockets[i]['id'] === targetId) {
      return sockets[i]['socket'];
    }
  }
  return undefined;
};

let setSenderTmpfileSize = (socket, tmpfileSize, size) => {
  let receiver = '';
  let targetId = '';
  for (let i in sockets) {
    if (sockets[i]['socket'] === socket) {
      targetId = sockets[i]['id'];
      break;
    }
  }
  for (let i in sockets) {
    if (sockets[i]['targetId'] === targetId) {
      sockets[i]['tmpfileSize'] = tmpfileSize;
      sockets[i]['size'] = size;
      break;
    }
  }
};

let getSender = (socket) => {
  let targetId = '';
  for (let i in sockets) {
    if (sockets[i]['socket'] === socket) {
      targetId = sockets[i]['id'];
      break;
    }
  }
  for (let i in sockets) {
    if (sockets[i]['targetId'] === targetId) {
      return sockets[i]['socket'];
    }
  }
};

let getFileReceiver = (socket) => {
  let receiverId = '';
  for (let i in sockets) {
    if (sockets[i]['socket'] === socket) {
      receiverId = sockets[i]['fileReceiver'];
      break;
    }
  }
  for (let i in sockets) {
    if (sockets[i]['id'] === receiverId) {
      return sockets[i]['socket'];
    }
  }
};