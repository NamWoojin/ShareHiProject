'use strict';
// import {swaggerUi, specs} from './swagger.js';

const express = require('express');
const app = express();

const server = require('http').createServer(app);
const io = require('socket.io')(server);

// swagger
// const { swaggerUi, specs } = require('./src/modules/swagger');
// 라우팅
const home = require('./src/routes');
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
//app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(specs));
//app.use('/api', home);
app.use('/api', home);

const KEY = require('./src/services/key/key.js');

server.listen(8081);
let idCount = 0;
let shareDevice;

io.on('connection', (socket) => {
  socket.emit(KEY.CONNECT, 'SUCCESS'); // 1010 연결 성공 응답
  console.log(socket.id);

  socket.on('disconnect', () => {});
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
  socket.on(KEY.GET_TREE_OF_FOLDERS, () => {
    // 2000 - 폴더 구조를 출력 요청
    io.to(shareDevice).emit(
      KEY.GET_TREE_OF_FOLDERS,
      JSON.stringify({
        // 2000 - 폴더 구조를 출력 요청 전달
        targetId: socket.id,
      })
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
    io.to(shareDevice).emit({
      targetId: socket.id,
    });
  });
  socket.on(KEY.RES_UPDATE_NAME_OF_FOLDER, (data) => {
    // 2001 - 폴더 이름 변경에 대한 응답(ex 폴더 트리 다시 제공)
  });
  socket.on(KEY.DELETE_FOLDERS, (data) => {
    // 2002 - 폴더 삭제
  });
  socket.on(KEY.RES_DELETE_FOLDERS, (data) => {
    // 2002 - 폴더 삭제에 대한 응답(ex 폴더 트리 다시 제공)
  });
  socket.on(KEY.ADD_FOLDERS, (data) => {
    // 2003 - 폴더 추가
  });
  socket.on(KEY.RES_ADD_FOLDERS, (data) => {
    // 2003 - 폴더 추가에 대한 응답(ex 폴더 트리 다시 제공)
  });
});

module.exports = app;
