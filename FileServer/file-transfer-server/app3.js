'use strict';

var net = require('net');
var fs = require('fs');
var file = fs.createWriteStream('./img/video-small.mp4', { flags: 'w' });
var nfile = fs.createReadStream('./img/video-small.mp4', { flags: 'r' });
var count = 0;

var server = net.createServer(function (client) {
  client.on('data', function (data) {
    console.log('------ writing to client: ' + count++ + '---------');
    writeData(client, data);
    console.log(data.length);
    console.log('...........................');
  });

  client.on('end', function () {
    console.log('is end?');
    nfile.on('data', (data) => {
      console.log('------!!!!!!!!!!!!!!!! : ' + count++ + '-----');
      writeData(client, data);
      console.log('...........................');
    });
  });

  client.on('error', function (err) {});

  client.on('timeout', function () {});

  client.on('drain', () => {
    console.log('hi drain~');
  });
});

server.listen(8888, function () {
  server.on('close', function () {});

  server.on('error', function (err) {});
});

var writeData = function (socket, data) {
  var success = !socket.write(data);
};
