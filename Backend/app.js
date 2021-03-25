'use strict';
// import {swaggerUi, specs} from './swagger.js';

const express = require('express');
const app = express();

// swagger
// const { swaggerUi, specs } = require('./src/modules/swagger');
// 라우팅
const home = require('./src/routes');
app.use(express.json());
app.use(express.urlencoded({ extended: false }));

const cors = require('cors');
app.use(cors());
//app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(specs));
//app.use('/api', home);
app.use('/api', home);

//////////////// start jae hyuk code /////////////////
//////////////////////////////////////////////////////

const server = require('http').createServer(app);
const io = require('socket.io')(server);
const net = require('net');
const HashMap = require('hashmap');
const { v4: uuidv4 } = require('uuid');

const KEY = require('./src/config/key/key.js');
const { client } = require('./src/config/redis/redis.emailAuth');

let shareDevice;
//////////////// socket map system /////////////
let socketMap = new HashMap(); // socket - id
let idMap = new HashMap(); // id - socket
///////^^^^^^^^^^^^^^^^^^^^^^^^^^^^/////////////

let andServer = net.createServer((socket) => {
  /////////// 초기 연결 /////////////////
  registSocket(socket);
  socket.write(
    JSON.stringify({
      namespace: KEY.CONNECT,
      status: 200,
      message: 'connect',
    }) + '\n'
  );
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
        // 2001 폴더 삭제에 대한 응답
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
      case KEY.RES_ADD_FOLDERS:
        // 2003 폴더 추가에 대한 응답
        io.to(idMap.get(data.targetId).id).emit(
          KEY.RES_ADD_FOLDERS,
          JSON.stringify({
            status: data.status,
            message: data.message,
            detail: data.detail,
            content: data.content,
          })
        );
        break;
      // 2001 폴더 이름을 변경한 후의 응답
    }
    //////////////////////////////////////////
    //////////////////////////////////////////
    //////////////////////////////////////////

    shareDevice = socketMap.get(socket);
  });
});

////////////// socket map CRUD //////////////////
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

server.listen(9000);
andServer.listen(9001);

io.on('connection', (socket) => {
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
    data = JSON.parse(data);
    console.log('RES_GET_TREE_OF_FOLDERS');
    console.log('data.targetId : ' + data.targetId);
    io.to(data.targetId).emit(KEY.RES_GET_TREE_OF_FOLDERS, data);
  });
  socket.on(KEY.UPDATE_NAME_OF_FOLDER, () => {
    // 2001 - 폴더 이름 변경
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
    idMap.get(shareDevice).write(
      JSON.stringify({
        namespace: KEY.UPDATE_NAME_OF_FOLDER,
        targetId: socketMap.get(socket),
        path: './example/folder',
        name: 'hello.txt',
        newName: 'hello2.txt',
      }) + '\n'
    );
  });
  socket.on(KEY.RES_UPDATE_NAME_OF_FOLDER, (data) => {
    // 2001 - 폴더 이름 변경에 대한 응답(ex 폴더 트리 다시 제공)
  });
  socket.on(KEY.DELETE_FOLDERS, (data) => {
    // 2002 - 폴더 삭제
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
    idMap.get(shareDevice).write(
      JSON.stringify({
        namespace: KEY.DELETE_FOLDERS,
        targetId: socketMap.get(socket),
        path: './example/folder',
        name: 'hello.txt',
      }) + '\n'
    );
  });
  socket.on(KEY.RES_DELETE_FOLDERS, (data) => {
    // 2002 - 폴더 삭제에 대한 응답(ex 폴더 트리 다시 제공)
  });
  socket.on(KEY.ADD_FOLDERS, (data) => {
    // 2003 - 폴더 추가
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
    idMap.get(shareDevice).write(
      JSON.stringify({
        namespace: KEY.ADD_FOLDERS,
        targetId: socketMap.get(socket),
        path: './example/folder',
        name: 'hello.txt',
      }) + '\n'
    );
  });
  socket.on(KEY.RES_ADD_FOLDERS, (data) => {
    // 2003 - 폴더 추가에 대한 응답(ex 폴더 트리 다시 제공)
  });
});

module.exports = app;
