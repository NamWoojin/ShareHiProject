'use strict';

var net = require('net');
var fs = require('fs');

//var outF = fs.createWriteStream('./img/video-small.mp4', { flags: 'w' });
//var count = 0;

var server = net.createServer(function (client) {
  console.log('---------------------------------');
  console.log('         NEW CONNECT');
  console.log('local : $s:$s', client.localAddress, client.localPort);
  console.log('remote : $s:$s', client.remoteAddress, client.remotePort);
  console.log('---------------------------------');

  client.on('data', function (data) {
    console.log(data.byteLength);
    console.log('---------------------------------');
    console.log('         RECV DATA');
    console.log('Bytes received : ' + client.bytesRead);
    console.log('---------------------------------');

    writeData(client, 'Bytes sent to server : ' + client.bytesRead);

    console.log('DATA BYTE : ' + data.byteLength);

    //console.log(count++);
    //outF.write(data);

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
  });

  client.on('drain', function () {
    console.log('꿑!');
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
