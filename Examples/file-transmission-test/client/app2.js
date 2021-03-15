var net = require('net');
function getConnection(connName) {
  var client = net.connect({ port: 8888, host: 'localhost' }, function () {
    console.log(connName + ' Connected: ');
    console.log('   local = %s:%s', this.localAddress, this.localPort);
    console.log('   remote = %s:%s', this.remoteAddress, this.remotePort);
    //this.setTimeout(500);
    this.setEncoding('utf8');
    this.on('data', function (data) {
      console.log(connName + ' From Server: ' + data.toString());
      this.end();
    });
    this.on('end', function () {
      console.log(connName + ' Client disconnected');
    });
    this.on('error', function (err) {
      console.log('Socket Error: ', JSON.stringify(err));
    });
    this.on('timeout', function () {
      console.log('Socket Timed Out');
    });
    this.on('close', function () {
      console.log('Socket Closed');
    });
  });
  return client;
}
function writeData(socket, data) {
  var success = !socket.write(data);
  /*
  if (!success) {
    (function (socket, data) {
      socket.once('drain', function () {
        writeData(socket, data);
      });
    })(socket, data);
  }
  */
}

var Dwarves = getConnection('Dwarves');
var fs = require('fs');
fs.readFile('./img/video-small.mp4', function (err, data) {
  console.log('---------------------------------');
  console.log('      SEND DATA TO SERVER');
  console.log(' DATA : ' + data.byteLength);
  console.log('---------------------------------');
  writeData(Dwarves, data);
});

/*
var fs = require('fs');
var Dwarves = getConnection('Dwarves');

var count = 0;

const file = fs.createReadStream('./img/video-small.mp4');
let chunk;
file.on('readable', () => {
  while (null !== (chunk = file.read(10000))) {
    console.log(count++);
    writeData(Dwarves, chunk);
  }
});
file.on('end', () => {
  console.log('end');
});
*/

//var Elves = getConnection('Elves');
//var Hobbits = getConnection('Hobbits');
//writeData(Dwarves, file);
//writeData(Elves, 'More Arrows');
//writeData(Hobbits, 'More Pipe Weed');
