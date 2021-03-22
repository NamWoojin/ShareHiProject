var net = require('net');
var fs = require('fs');

let myProtocol = {
  roomId: 12104,
  role: 'sender',
  method: 'connect',
  data: '',
};

let myProtocol2 = {
  roomId: 12104,
  role: 'receiver',
  method: 'connect',
};

function getConnection(connName) {
  var client = net.connect({ port: 8888, host: 'localhost' }, function () {
    console.log(connName + ' Connected: ');
    console.log('   local = %s:%s', this.localAddress, this.localPort);
    console.log('   remote = %s:%s', this.remoteAddress, this.remotePort);
    //this.setTimeout(500);
    this.setEncoding('utf8');
    this.on('data', function (data) {
      console.log(connName + ' From Server: ' + data.toString());

      ///////////////////////////////////////////////////////////// sender 인식
      if (myProtocol.role === 'sender' && myProtocol.method === 'connect') {
        /////////////////////////////////////////////////////////// 데이터 전송 시작
        myProtocol.method = 'send';

        fs.readFile('./img/video-small.mp4', function (err, data) {
          console.log('---------------------------------');
          console.log('      SEND DATA TO SERVER');
          console.log(' DATA : ' + data.byteLength);
          console.log('---------------------------------');
          myProtocol.data = data;
          console.log(data);
          console.log(JSON.parse(JSON.stringify(myProtocol)));
          writeData(Dwarves, JSON.stringify(myProtocol));
        });

        /*
        const file = fs.createReadStream('./img/123123.txt');
        console.log('file : ' + file);
        let chunk;
        file.on('readable', () => {
          while (null !== (chunk = file.read(50000))) {
            myProtocol.data = chunk;
            writeData(Dwarves, JSON.stringify(myProtocol));
          }
        });
        */
      }
      //////////////////////////////////////////////////////////////////////////
      ////////////////////////////////////////////////////////////// 데이터 저장
      else if (myProtocol2.role === 'receiver' && myProtocol.method === 'send') {
        fs.appendFileSync('./saved-img/123123.txt', data, function (err) {
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
      }

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
}

////////////////////////////////////////////////////////Elves 연결 설정
var Elves = getConnection('Elves');

writeData(Elves, JSON.stringify(myProtocol2));
/////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////Dwarves 연결 설정
var Dwarves = getConnection('Dwarves');

writeData(Dwarves, JSON.stringify(myProtocol));
////////////////////////////////////////////////////////////////////////

/*
var fs = require('fs');
fs.readFile('./img/video-small.mp4', function (err, data) {
  console.log('---------------------------------');
  console.log('      SEND DATA TO SERVER');
  console.log(' DATA : ' + data.byteLength);
  console.log('---------------------------------');
  writeData(Dwarves, data);
});
*/

//var Elves = getConnection('Elves');
//var Hobbits = getConnection('Hobbits');
//writeData(Dwarves, file);
//writeData(Elves, 'More Arrows');
//writeData(Hobbits, 'More Pipe Weed');
