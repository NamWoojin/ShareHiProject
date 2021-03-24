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

io.on('connection', (socket) => {
  socket.on(KEY.GET_COUNT_OF_CONNECTED_DEVICES, (data) => {
    // 1000 - 연결된 모든 디바이스의 개수를 출력
  });
  socket.on(KEY.GET_LIST_OF_CONNECTED_DEVICES_NAME, (data) => {
    // 1002 - 연결된 디바이스의 이름들 출력
  });
  socket.on(KEY.GET_TREE_OF_FOLDERS, (data) => {
    // 2000 - 폴더 구조를 툴력
  });
  socket.on(KEY.UPDATE_NAME_OF_FOLDER, (data) => {
    // 2001 - 폴더 이름 변경
  });
  socket.on(KEY.DELETE_FOLDERS, (data) => {
    // 2002 - 폴더 삭제
  });
  socket.on(KEY.ADD_FOLDERS, (data) => {
    // 2003 - 폴더 추가
  });
});

module.exports = app;
