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

server.listen(8081);

io.on('connection', (socket) => {
  socket.on('data', (data) => {
    console.log('hello data : ' + data);
  });
});

module.exports = app;
