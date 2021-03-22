'use strict';

var net = require('net');
let fs = require('fs');

var connectList = new Map();

var server = net.createServer(function (client) {
  console.log('---------------------------------');
  console.log('         NEW CONNECT');
  console.log('local : $s:$s', client.localAddress, client.localPort);
  console.log('remote : $s:$s', client.remoteAddress, client.remotePort);
  console.log('---------------------------------');

  client.on('data', function (data) {
    console.log('---------------------------------');
    console.log('         RECV DATA');
    console.log('Bytes received : ' + client.bytesRead);
    console.log('---------------------------------');

    let dataObj = JSON.parse(data.toString());
    //console.log(dataObj);
    console.log('len : ' + data.length);
    ////////////////////////////////////////////////////////////////// 연결 설정

    if (dataObj.method === 'connect') {
      if (connectList.get(dataObj.roomId) === undefined) {
        let connectObj = {};
        if (dataObj.role === 'sender') {
          connectObj.sender = client;
        } else {
          connectObj.receiver = client;
        }
        connectList.set(dataObj.roomId, connectObj);
      } else {
        let connectObj = connectList.get(dataObj.roomId);
        if (dataObj.role === 'sender') {
          connectObj.sender = client;
        } else {
          connectObj.receiver = client;
        }
        connectList.set(dataObj.roomId, connectObj);
        // 연결이 완료됨 - 완료되었다고 sender에게 전송
        writeData(connectList.get(dataObj.roomId).sender, 'hi');
      }
    }
    ///////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////// 데이터 전달
    else if (dataObj.method === 'send') {
      //writeData(connectList.get(dataObj.roomId).receiver, JSON.stringify(dataObj.data.data));
      writeData(connectList.get(dataObj.roomId).receiver, Buffer.from(dataObj.data, 'base64'));
    }
    ////////////////////////////////////////////////////////////////////////////

    /*
    fs.appendFileSync('./img/video-small.mp4', data, function (err) {
      if (err === null) {
        console.log('---------------------------------');
        console.log('          SUCCESS');
        console.log('---------------------------------');
      } else {
        console.log('---------------------------------');
        console.log('           FAIL');
        console.log('---------------------------------');
      }
    });
    */
  });

  client.on('end', function () {
    console.log('---------------------------------');
    console.log('      CLIENT DISCONNECTED');
    console.log('---------------------------------');

    server.getConnections(function (err, count) {
      console.log('Remaining Connections: ' + count);
    });
  });

  client.on('error', function (err) {
    console.log('---------------------------------');
    console.log('      SOCKET ERROR :' + JSON.stringify(err));
    console.log('---------------------------------');
  });

  client.on('timeout', function () {
    console.log('---------------------------------');
    console.log('      SOCKET TIMED OUT');
    console.log('---------------------------------');
  });
});

server.listen(8888, function () {
  console.log('---------------------------------');
  console.log('      SERVER LISTENING' + JSON.stringify(server.address()));
  console.log('---------------------------------');

  server.on('close', function () {
    console.log('---------------------------------');
    console.log('      SERVER TERMINATED');
    console.log('---------------------------------');
  });

  server.on('error', function (err) {
    console.log('---------------------------------');
    console.log('      SERVER ERROR : ' + JSON.stringify(err));
    console.log('---------------------------------');
  });
});

var writeData = function (socket, data) {
  var success = !socket.write(data);
};
