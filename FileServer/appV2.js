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
function SocketInfo(id, name, socket, type, isShare, fileSender, fileReceiver, targetId) {
  this.id = id;
  this.name = name;
  this.socket = socket;
  this.type = type;
  this.isShare = isShare;
  this.fileSender = fileSender;
  this.fileReceiver = fileReceiver;
  this.targetId = targetId;
}
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
  responseOK(socket, 'android');
  printSocket();

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
      case KEY.SET_SHARE_DATA:
        // 1030 공유할 데이터 tcp를 연다
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
        let targetSocket = getTargetSocket(socket);
        targetSocket.write(
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
        let targetSocket = getTargetSocket(socket);
        targetSocket.write(
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
        let targetSocket = getTargetSocket(socket);
        targetSocket.write(
          JSON.stringify({
            namespace: 2103,
            targetId: getId(socket),
            path: data.path,
            newFolder: data.newFolder,
          }) + '\n'
        );
        break;

      case KEY.SEND_FILE_STAT:
        // 7000 파일 스텟에 대한 응답
        size = data.size;
        tmpfileSize = data.tmpfileSize;

        io.to(idMap.get(data.targetId).id).emit(
          KEY.SEND_FILE_STAT,
          JSON.stringify({
            tmpfileSize: data.tmpfileSize,
            status: data.status,
            message: data.message,
            detail: data.detail,
            content: data.content,
          })
        );
        break;
    }
    //////////////////////////////////////////
    //////////////////////////////////////////
    //////////////////////////////////////////
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
   * 연결된 직후 수행되는 코드
   * 설명 : 소켓을 등록하고, 연결이 잘 되었다는 메시지를 클라이언트로 전송한다
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
   * 메시지 :{"devices":[{"id":"c69ad27e-48ff-4849-b9ed-568a6935e794","name":"c69ad27e-48ff-4849-b9ed-568a6935e794"}]}
   */
  socket.on(1050, (data) => {
    let val = getShareDevices(socket);
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
    //responseOK(socket);
    socket.emit(1111);
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
  socket.on(KEY.SEND_FILE_STAT, (data) => {
    // 7000 파일 스텟 전송
    if (!isJsonString(data)) {
      io.to(socket.id).emit(
        KEY.INVALID_JSON,
        JSON.stringify({
          status: 400,
          detail: 'INVALID JSON',
          message: 'BAD REQUEST',
        })
      );
      return;
    }
    data = JSON.parse(data);
    console.log(data);
    if (!checkSocket(shareDevice)) {
      // 4000 - 공유 디바이스 연결이 되어있지 않음.
      io.to(socket.id).emit(
        KEY.NOT_SHARE_DEVICE,
        JSON.stringify({
          status: 204,
          message: 'NO SHARE DEVICE',
        })
      );
      return;
    }
    idMap.get(shareDevice).write(
      JSON.stringify({
        namespace: KEY.SEND_FILE_STAT,
        targetId: socketMap.get(socket),
        path: data.path,
        name: data.name,
        size: data.size,
        ext: data.ext,
      }) + '\n'
    );
  });
  socket.on(KEY.SEND_FILE, (data) => {
    console.log(data);
    // 7001 파일 전송
    if (!checkSocket(shareDevice)) {
      // 4000 - 공유 디바이스 연결이 되어있지 않음.
      io.to(socket.id).emit(
        KEY.NOT_SHARE_DEVICE,
        JSON.stringify({
          status: 204,
          message: 'NO SHARE DEVICE',
        })
      );
      return;
    }
    tmpfileSize += data.length;
    console.log(data.length);
    let percent = getFilePercent();
    console.log(percent);
    idMap.get(shareDevice).write(
      JSON.stringify({
        namespace: KEY.SEND_FILE,
        percent: percent,
      }) + '\n'
    );
    io.to(socket.id).emit(
      KEY.SEND_FILE,
      JSON.stringify({
        percent: percent,
      })
    );

    /**
     * 안드로이드와 새로운 TCP 연결 후, 전송 로직이 필요
     */
    idMap.get(shareData).write(data);
  });
});

let getFilePercent = () => {
  let ans = Math.floor((tmpfileSize / size) * 100);
  //console.log(ans);
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
