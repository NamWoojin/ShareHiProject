var net = require('net');
var fs = require('fs');

function getConnection(connName) {
  var client = net.connect({ port: 8888, host: 'localhost' }, function () {
    console.log(connName + ' Connected: ');
    console.log('   local = %s:%s', this.localAddress, this.localPort);
    console.log('   remote = %s:%s', this.remoteAddress, this.remotePort);
    //this.setTimeout(500);
    this.setEncoding('utf8');
    this.on('data', function (data) {
      console.log(connName + ' From Server: ');
      fs.appendFileSync('./saved-img/video-small.mp4', data, function (err) {
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
      //this.end();
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

fs.readFile('./img/video-small.mp4', function (err, data) {
  console.log('---------------------------------');
  console.log('      SEND DATA TO SERVER');
  console.log(' DATA : ' + data.byteLength);
  console.log('---------------------------------');
  writeData(Dwarves, data);
});

/*
var inStream = fs.createReadStream('./img/video-small.mp4');
inStream.pipe(Dwarves);
*/

//var Elves = getConnection('Elves');
//var Hobbits = getConnection('Hobbits');
//writeData(Dwarves, file);
//writeData(Elves, 'More Arrows');
//writeData(Hobbits, 'More Pipe Weed');
