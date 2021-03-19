// 다음 작업 : express-generator를 활용한 구조화
// 지금은 일단 모듈화

let config = {
  host: 'localhost',
};

const net = require('net');

const http = require('http');
const fs = require('fs');
const socketio = require('socket.io');
const ss = require('socket.io-stream');
const express = require('express');
const { fileURLToPath } = require('url');
const app = express();

let server = http.createServer(app);
server.listen(8765, () => {
  console.log('Server Running at http://127.0.0.1:8765');
});

let io = socketio(server);

io.sockets.on('connection', (socket) => {
  socket.on('message', (data) => {
    console.log(data);
    writeData(socket, 'message', data);
  });
  socket.on('blob', (data) => {
    writeData(socket, 'blob', data);
  });
});

let writeData = function (socket, id, data) {
  socket.emit(id, data);
};

var andServer = net.createServer(function (client) {
  client.on('data', function (data) {
    console.log('------ writing to client: ---------');
    andWriteData(client, data);
    console.log(data);
    console.log('...........................');
  });

  client.on('end', function () {
    console.log('is end?');
    nfile.on('data', (data) => {
      console.log('-----------');
      andWriteData(client, data);
      console.log('...........................');
    });
  });

  client.on('error', function (err) {});

  client.on('timeout', function () {});

  client.on('drain', () => {
    console.log('hi drain~');
  });
});

andServer.listen(8888, function () {
  console.log('Server Running at http://127.0.0.1:8888');
  andServer.on('close', function () {});

  andServer.on('error', function (err) {});
});

var andWriteData = function (socket, data) {
  var success = !socket.write(data);
};
