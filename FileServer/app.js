//////////////// start jae hyuk code /////////////////
//////////////////////////////////////////////////////
const express = require('express');
const app = express();

const server = require('http').createServer(app);
// const io = require('socket.io')(server);
const io = require('socket.io')(server, {
  cors: {
    origin: '*',
  },
});
const ss = require('socket.io-stream');
const net = require('net');
const HashMap = require('hashmap');
const { v4: uuidv4 } = require('uuid');

const KEY = require('./src/config/key/key');

//////////////// socket map system /////////////
let shareDevice;
let shareData;
let socketMap = new HashMap(); // socket - id
let idMap = new HashMap(); // id - socket
let size = 0;
let tmpfileSize = 0;
///////^^^^^^^^^^^^^^^^^^^^^^^^^^^^/////////////

console.log('서버 가동중...');

let andServer = net.createServer((socket) => {
  /////////// 초기 연결 /////////////////
  console.log('Success - Android Connect');
  registSocket(socket);
  if (shareDevice !== undefined) {
    shareData = socketMap.get(socket);
    idMap.get(shareDevice).write(
      JSON.stringify({
        namespace: KEY.SET_SHARE_DATA,
      }) + '\n'
    );
  } else {
    socket.write(
      JSON.stringify({
        namespace: KEY.CONNECT,
        status: 200,
        message: 'connect',
      }) + '\n'
    );
  }
  printSocket();
  ////////^^^^^^^^^^^^^^^^^^^^^^^///////

  socket.on('error', (err) => {
    console.log('socket 연결 끊김 : ' + socketMap.get(socket));
    deleteSocket(socket);
    printSocket();
  });
  socket.on('end', () => {
    deleteSocket(socket);
    console.log('end socket : ' + socket);
    printSocket();
  });

  socket.on('data', (data) => {
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
    let id = parseInt(data.namespace);
    ///////////////////////////////////////////
    ///////////// 프로토콜 /////////////////////
    ///////////////////////////////////////////
    switch (id) {
      case KEY.SET_SHARE_ID:
        // 1020 공유 디바이스의 아이디를 설정한다
        shareDevice = socketMap.get(socket);
        console.log('shareDevice : ' + shareDevice);
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
      case KEY.GET_TREE_OF_FOLDERS:
        // 2000 폴더 디렉토리 요청에 대한 응답
        io.to(idMap.get(data.targetId).id).emit(
          KEY.GET_TREE_OF_FOLDERS,
          JSON.stringify({
            status: data.status,
            message: data.message,
            detail: data.detail,
            content: data.content,
          })
        );
        break;
      case KEY.UPDATE_NAME_OF_FOLDER:
        // 2001 폴더 이름 수정에 대한 응답
        io.to(idMap.get(data.targetId).id).emit(
          KEY.UPDATE_NAME_OF_FOLDER,
          JSON.stringify({
            status: data.status,
            message: data.message,
            detail: data.detail,
            content: data.content,
          })
        );
        break;
      case KEY.DELETE_FOLDERS:
        // 2002 폴더 삭제에 대한 응답
        io.to(idMap.get(data.targetId).id).emit(
          KEY.DELETE_FOLDERS,
          JSON.stringify({
            status: data.status,
            message: data.message,
            detail: data.detail,
            content: data.content,
          })
        );
        break;
      case KEY.ADD_FOLDERS:
        // 2003 폴더 추가에 대한 응답
        io.to(idMap.get(data.targetId).id).emit(
          KEY.ADD_FOLDERS,
          JSON.stringify({
            status: data.status,
            message: data.message,
            detail: data.detail,
            content: data.content,
          })
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

    shareDevice = socketMap.get(socket);
  });
});

////////////// socket map CRUD //////////////////
function isJsonString(str) {
  try {
    var json = JSON.parse(str);
    return typeof json === 'object';
  } catch (e) {
    return false;
  }
}
let deleteSocket = (socket) => {
  if (idMap.get(socketMap.get(socket))) {
    idMap.delete(socketMap.get(socket));
  }
  if (socketMap.get(socket)) {
    if (shareDevice === socketMap.get(socket)) {
      shareDevice = undefined;
    }
    socketMap.delete(socket);
  } else {
    return;
  }
};
let registSocket = (socket) => {
  let uuid = uuidv4();
  if (!socketMap.get(socket)) {
    socketMap.set(socket, uuid);
  } else {
    return;
  }
  idMap.set(uuid, socket);
};
let checkSocket = (id) => {
  if (!idMap.get(id)) return false;
  return true;
};
let printSocket = (socket) => {
  console.log('---------print--------------');
  console.log('shareDevice : ' + shareDevice);
  console.log('socketMap size : ' + socketMap.size);
  console.log('idMap size : ' + idMap.size);
  console.log('----------------------------');
};
/////////////^^^^^^^^^^^^^^^^^^^^^^^//////////////

server.listen(9002, () => {
  console.log('웹-서버 socket연결');
});
andServer.listen(9003, () => {
  console.log('안드-서버 socket연결');
});

io.on('connection', (socket) => {
  console.log('Success Web Connect');
  registSocket(socket);
  socket.emit(
    KEY.CONNECT,
    JSON.stringify({
      namespace: KEY.CONNECT,
      status: 200,
      message4: 'CONNECT SUCCESS',
    })
  ); // 1010 연결 성공 응답
  printSocket();

  socket.on('disconnect', () => {
    deleteSocket(socket);
    printSocket();
  });
  socket.on(KEY.SET_SHARE_ID, () => {
    // 1020 공유 디바이스의 아이디를 설정한다
    shareDevice = socket.id;
    console.log(shareDevice);
  });
  socket.on(KEY.GET_COUNT_OF_CONNECTED_DEVICES, (data) => {
    // 1000 - 연결된 모든 디바이스의 개수를 출력 요청
  });
  socket.on(KEY.GET_LIST_OF_CONNECTED_DEVICES_NAME, (data) => {
    // 1002 - 연결된 디바이스의 이름들 출력 요청
  });
  socket.on(KEY.GET_TREE_OF_FOLDERS, (data) => {
    // 2000 - 폴더 구조를 출력 요청
    if (isJsonString(data)) {
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
        namespace: KEY.GET_TREE_OF_FOLDERS,
        targetId: socketMap.get(socket),
        path: data.path,
        name: data.name,
      }) + '\n'
    );
  });
  socket.on(KEY.RES_GET_TREE_OF_FOLDERS, (data) => {
    // 3000 - 폴더 구조를 출력 요청에 대한 응답(ex 폴더 트리 제공)
    if (isJsonString(data)) {
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
    console.log('RES_GET_TREE_OF_FOLDERS');
    console.log('data.targetId : ' + data.targetId);
    io.to(data.targetId).emit(KEY.RES_GET_TREE_OF_FOLDERS, data);
  });
  socket.on(KEY.UPDATE_NAME_OF_FOLDER, (data) => {
    // 2001 - 폴더 이름 변경
    if (isJsonString(data)) {
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
    if (!checkSocket(shareDevice)) {
      // 4000 - 공유 디바이스 연결이 되어있지 않음.
      io.to(socket.id).emit(
        KEY.NOT_SHARE_DEVICE,
        JSON.stringify({
          status: 204,
          message: 'NOT SHARE DEVICE',
        })
      );
      return;
    }
    data = JSON.parse(data);
    idMap.get(shareDevice).write(
      JSON.stringify({
        namespace: KEY.UPDATE_NAME_OF_FOLDER,
        targetId: socketMap.get(socket),
        path: data.path,
        name: data.name,
        newName: data.newName,
      }) + '\n'
    );
  });
  socket.on(KEY.RES_UPDATE_NAME_OF_FOLDER, (data) => {
    // 2001 - 폴더 이름 변경에 대한 응답(ex 폴더 트리 다시 제공)
  });
  socket.on(KEY.DELETE_FOLDERS, (data) => {
    // 2002 - 폴더 삭제
    if (isJsonString(data)) {
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
    if (!checkSocket(shareDevice)) {
      // 4000 - 공유 디바이스 연결이 되어있지 않음.
      io.to(socket.id).emit(
        KEY.NOT_SHARE_DEVICE,
        JSON.stringify({
          status: 204,
          message: 'NOT SHARE DEVICE',
        })
      );
      return;
    }
    data = JSON.parse(data);
    idMap.get(shareDevice).write(
      JSON.stringify({
        namespace: KEY.DELETE_FOLDERS,
        targetId: socketMap.get(socket),
        path: data.path,
        name: data.name,
      }) + '\n'
    );
  });
  socket.on(KEY.RES_DELETE_FOLDERS, (data) => {
    // 2002 - 폴더 삭제에 대한 응답(ex 폴더 트리 다시 제공)
  });
  socket.on(KEY.ADD_FOLDERS, (data) => {
    // 2003 - 폴더 추가
    if (isJsonString(data)) {
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
    if (!checkSocket(shareDevice)) {
      // 4000 - 공유 디바이스 연결이 되어있지 않음.
      io.to(socket.id).emit(
        KEY.NOT_SHARE_DEVICE,
        JSON.stringify({
          status: 204,
          message: 'NOT SHARE DEVICE',
        })
      );
      return;
    }
    data = JSON.parse(data);
    idMap.get(shareDevice).write(
      JSON.stringify({
        namespace: KEY.ADD_FOLDERS,
        targetId: socketMap.get(socket),
        path: data.path,
        name: data.name,
      }) + '\n'
    );
  });
  socket.on(KEY.RES_ADD_FOLDERS, (data) => {
    // 2003 - 폴더 추가에 대한 응답(ex 폴더 트리 다시 제공)
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
    // 7001 파일 전송
    // stream
    stream.on('data', function (data) {
      if (data) idMap.get(shareData).write(data);
    });
    stream.on('end', function () {
      console.log('끝');
    });

    //안드로이드와 새로운 TCP 연결 후, 전송 로직이 필요

    // console.log('----stream----');
    // console.log(stream);
    // idMap.get(shareData).write(stream);
    // stream.pipe();
  });

  // socket.on(KEY.SEND_FILE, (data) => {
  //   //console.log(data);
  //   // 7001 파일 전송
  //   if (!checkSocket(shareDevice)) {
  //     // 4000 - 공유 디바이스 연결이 되어있지 않음.
  //     io.to(socket.id).emit(
  //       KEY.NOT_SHARE_DEVICE,
  //       JSON.stringify({
  //         status: 204,
  //         message: 'NO SHARE DEVICE',
  //       })
  //     );
  //     return;
  //   }
  //   tmpfileSize += data.length;
  //   console.log(data.length);
  //   let percent = getFilePercent();
  //   console.log(percent);
  //   idMap.get(shareDevice).write(
  //     JSON.stringify({
  //       namespace: KEY.SEND_FILE,
  //       percent: percent,
  //     }) + '\n'
  //   );
  //   io.to(socket.id).emit(
  //     KEY.SEND_FILE,
  //     JSON.stringify({
  //       percent: percent,
  //     })
  //   );

  //   // 안드로이드와 새로운 TCP 연결 후, 전송 로직이 필요
  //   if (!data) return;
  //   idMap.get(shareData).write(data);
  // });
});

let getFilePercent = () => {
  let ans = Math.floor((tmpfileSize / size) * 100);
  //console.log(ans);
  return ans;
};
