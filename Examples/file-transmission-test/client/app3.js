var net = require('net');
var fs = require('fs');

var count = 0;
var file = fs.createReadStream('./img/video-small.mp4', { flags: 'r' });
var nfile = fs.createWriteStream('./saved-img/video-small.mp4', { flags: 'w' });

function getConnection(connName) {
  var client = net.connect({ port: 8888, host: 'localhost' }, function () {
    this.on('data', function (data) {
      nfile.write(data);
    });
    this.on('end', function () {});
    this.on('error', function (err) {});
    this.on('timeout', function () {});
    this.on('close', function () {});
    this.on('drain', () => {
      console.log('hi drain~');
    });
  });
  return client;
}
function writeData(socket, data) {
  var success = !socket.write(data);

  if (!success) {
    (function (socket, data) {
      socket.once('drain', function () {
        writeData(socket, data);
      });
    })(socket, data);
  }
}

var Dwarves = getConnection('Dwarves');

//1. 파일 stream 연결

file.on('data', (data) => {
  console.log('------writing : ' + count++ + '-----');
  writeData(Dwarves, data);
  console.log('...........................');
});
