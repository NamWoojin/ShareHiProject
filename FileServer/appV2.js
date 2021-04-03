const express = require('express');
const app = express();
const fs = require('fs');

// const option = {
//   ca: fs.readFileSync('/volumes/key/fullchain.pem'),
//   key: fs.readFileSync('/volumes/key/privkey.pem'),
//   cert: fs.readFileSync('/volumes/key/cert.pem'),
// };

// const server = require('https').Server(option, app);
const server = require('https').Server(app);

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
let pathData = '';
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
  //printSocket();

  /**
   * @type Android
   * @Init 파일 전송인지, JSON 제어인지 확인한다
   * @description 서버의 flag를 본다
   */
  if (flag === 1) {
    console.log('flag 1');
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
  if (flag === 2) {
    console.log('connected socket id : ' + getId(socket));
    setFileSender(socket);
    let controlSocket = getControlSocketDownload(socket);
    if (controlSocket === undefined) return;
    console.log('sender socket id : ' + getId(socket));
  }
  responseOK(socket, 'android');

  /**
   * Android
   * 연결이 에러로 인해 끊겼을 경우 수행되는 코드
   * 설명 : 소켓 연결이 끊기면, 등록을 해제한다.
   */
  socket.on('error', (err) => {
    console.log('error');
    deleteSocket(socket);
    broadCastToAllSocket();
  });
  /**
   * Android
   * 연결이 끊겼을 경우 수행되는 코드
   * 설명 : 소켓 연결이 끊기면, 등록을 해제한다.
   */
  socket.on('end', () => {
    console.log('end');
    deleteSocket(socket);
    broadCastToAllSocket();
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
     * @logic socket이 과연 fileReceiver에 포함되는지 검사
     */
    console.log(data);
    if (isFileSender(socket)) {
      let fileSender = getFileSender(socket);
      console.log('sending');
      fileSender.emit(8000, data);
      return;
    }

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
        /**
         * 연결 되면 모든 디바이스들한테 브로드 캐스팅
         */
        broadCastToAllSocket();
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
       * 설명 : 안드로이드에서 공유폴더의 최상위 디렉토리 path를 웹으로 전송한다
       * 메시지 : {"namespace":1010,"status":200,"message":"OK"}
       */
      case 1070:
        path = data.path;
        targetId = data.targetId;
        console.log('path : ' + path);
        console.log('targetId : ' + targetId);

        getSocketByTargetId(targetId).emit(
          1070,
          JSON.stringify({
            path: path,
          })
        );

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
        console.log(7100);
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

      /**
       * @type Android
       * @namespace 8100
       * @description size를 세팅하고 파일 다운로드 준비를 최종적으로 마친다
       * @data {path, name, exe}
       */
      case 8100:
        console.log(8100);

        setDownloadReceiverSize(data.size, data.targetId);
        getSocketByTargetId(data.targetId).emit(
          8001,
          JSON.stringify({
            size: data.size,
          })
        );

        break;
      case 7002:
        if (percent >= 100) {
          initSocketData(socket);
        }
        break;
      case 2100:
        pathData = '';
        pathDataChunkCount = data.chunkCount;
        socket.write(
          JSON.stringify({
            namespace: 2150,
            path: data.path,
            targetId: data.targetId,
            chunkCount: pathDataChunkCount,
            pathDataChunkCount: pathDataChunkCount,
          }) + '\n'
        );
        break;

      case 2150:
        pathData += data.data;
        --pathDataChunkCount;
        if (pathDataChunkCount == 0) {
          console.log(pathData.length);
          getSocket(data.targetId, pathData);
        } else {
          socket.write(
            JSON.stringify({
              namespace: 2150,
              path: data.path,
              targetId: data.targetId,
              chunkCount: data.chunkCount,
              pathDataChunkCount: pathDataChunkCount,
            }) + '\n'
          );
        }
        break;
    }
  });
});

/**
 * 안드로이드와 웹 소켓 연결을 실시한다
 */
// server.listen(9002, () => {
//   console.log('웹-서버 socket연결');
// });
// andServer.listen(9003, () => {
//   console.log('안드-서버 socket연결');
// });
// server.listen(443);
server.listen(9002);

andServer.listen(9003);

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
  //printSocket();

  /**
   * Web
   * 연결이 끊겼다는 코드
   * 설명 : 소켓을 해제하고 연결을 끊는다
   */
  socket.on('disconnect', () => {
    deleteSocket(socket);
  });

  /**
   * Web
   * 1050
   * 설명 : 내 디바이스를 제외한 공유중인 모든 디바이스들의 정보를 클라이언트에게 제공한다
   * 메시지 : {"devices":[{"id":"c69ad27e-48ff-4849-b9ed-568a6935e794","name":"c69ad27e-48ff-4849-b9ed-568a6935e794"}]}
   */
  socket.on(1050, () => {
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
    if (!data) return;
    data = JSON.parse(data);
    connectToShareDevice(socket, data.id);
    getTargetSocket(socket).write(
      JSON.stringify({
        namespace: 1070,
        targetId: getId(socket),
      }) + '\n'
    );
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
    console.log(data);

    if (!checkSocket(getId(getTargetSocket(socket)))) {
      responseBad(socket, 'web');
      return;
    }

    let percent = getFilePercent(socket, data.length);
    console.log('percent : ' + percent);
    socket.emit(
      KEY.SEND_FILE,
      JSON.stringify({
        percent: percent,
      })
    );

    getTargetSocket(socket).write(
      JSON.stringify({
        namespace: 7001,
        percent: percent,
      }) + '\n'
    );

    let fileReceiverMan = getFileReceiver(socket);

    fileReceiverMan.write(data);

    // if (percent >= 100) {
    //   initSocketData(socket);
    // }
  });

  /**
   * @type Web
   * @namespace 8000
   * @description 웹에서 파일 다운로드를 요청한다
   * @data {path, name, exe}
   */
  socket.on(8000, (data) => {
    if (!isJsonString(data)) {
      responseBad(socket, 'web');
      return;
    }
    data = JSON.parse(data);
    if (!checkSocket(getId(getTargetSocket(socket)))) {
      responseBad(socket, 'web');
      return;
    }
    flag = 2;
    if (!data) return;
    setDownloadReceiver(socket);

    getTargetSocket(socket).write(
      JSON.stringify({
        namespace: 8100,
        targetId: getId(socket),
        path: data.path,
        name: data.name,
        ext: data.ext,
      }) + '\n'
    );
  });
});

/**
 * @type Web
 * @namespace 8001
 * @description 웹에서 파일 사이즈를 보낸 것을 동기화한다.
 * @data
 */
socket.on(8001, () => {
  getFileReceiver(socket).write(
    JSON.stringify({
      namespace: 8200,
    }) + '\n'
  );
});
////////////// LOGIC ///////////////////

let getFilePercent = (socket, length) => {
  let size = 0;
  let tmpfileSize = 0;

  for (let i in sockets) {
    if (sockets[i]['socket'] === socket) {
      size = sockets[i]['size'];
      if (size === 0) return -1;
      sockets[i]['tmpfileSize'] += length;
      tmpfileSize = sockets[i]['tmpfileSize'];
      let ans = Math.floor((tmpfileSize / size) * 100);
      return ans;
    }
  }
  return -1;
};

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
      console.log('target id : ' + targetId);
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

  mySocket.emit(
    2000,
    JSON.stringify({
      data: data,
    })
  );
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

let initSocketData = (socket) => {
  for (let i in sockets) {
    if (sockets[i]['socket'] === socket) {
      sockets[i]['flag'] = 0;
      sockets[i]['fileReceiver'] = undefined;
      sockets[i]['fileSender'] = undefined;
      sockets[i]['size'] = 0;
      sockets[i]['tmpfileSize'] = 0;
    }
  }
};

let setDownloadReceiver = (socket) => {
  let id;
  for (let i in sockets) {
    if (sockets[i]['socket'] === socket) {
      id = sockets[i]['id'];
      sockets[i]['flag'] = 2;
      sockets[i]['fileReceiver'] = id;
      return;
    }
  }
};

let setDownloadReceiverSize = (size, targetId) => {
  for (let i in sockets) {
    if (sockets[i]['id'] === targetId) {
      sockets[i]['size'] = size;
    }
  }
};

let setFileSender = (socket) => {
  flag = 0;

  let id = getId(socket);

  for (let i in sockets) {
    if (sockets[i]['flag'] === 2) {
      sockets[i]['flag'] = 0;
      sockets[i]['fileSender'] = id;
    }
  }
};

let getControlSocketDownload = (socket) => {
  let targetId;
  let senderId;
  for (let i in sockets) {
    if (sockets[i]['socket'] === socket) {
      senderId = sockets[i]['id'];
      break;
    }
  }
  for (let i in sockets) {
    if (sockets[i]['fileSender'] === senderId) {
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
let isFileSender = (socket) => {
  let id = '';
  for (let i in sockets) {
    if (sockets[i]['socket'] === socket) {
      id = sockets[i]['id'];
      break;
    }
  }
  for (let i in sockets) {
    if (sockets[i]['fileSender'] === id) {
      return true;
    }
  }
  return false;
};
let getFileSender = (socket) => {
  let fileSender = '';

  for (let i in sockets) {
    if (sockets[i]['socket'] === socket) {
      fileSender = sockets[i]['id'];
      break;
    }
  }
  for (let i in sockets) {
    if (sockets[i]['fileSender'] === fileSender) {
      return sockets[i]['socket'];
    }
  }
};

let getSocketByTargetId = (targetId) => {
  for (let i in sockets) {
    if (sockets[i]['id'] === targetId) {
      return sockets[i]['socket'];
    }
  }
};

let broadCastToAllSocket = (socket) => {
  console.log('broadcasting all shared android device...');
  let val = getShareDevicesAll();
  for (let i in sockets) {
    if (sockets[i]['type'] === 'web') {
      sockets[i]['socket'].emit(
        1050,
        JSON.stringify({
          devices: val,
        })
      );
    }
  }
};
let getShareDevicesAll = () => {
  let val = [];
  for (let i in sockets) {
    if (sockets[i]['isShare'] === 'true') {
      val.push({
        id: sockets[i].id,
        name: sockets[i].name,
      });
    }
  }
  return val;
};
