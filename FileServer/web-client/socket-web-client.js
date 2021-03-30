const io = require('socket.io-client');

/**
 *  FOR TEST - ENV : LOCALHOST
 *  로컬 파일 시스템을 이용하여 파일 전송을 테스트합니다
 *  다음에 서버와 연결이 원활히 된다면, 브라우저에서 파일을 로드할 수 있도록 재구성합니다.
 */
const fs = require('fs');

let config = {
  host: 'http://localhost:9002',
  filename: './img/sample.mp4',
};

let socket = '';

let getConnection = function () {
  console.log('connecting...');
  socket = io.connect(config.host, {});
  socket.on(2003, (data) => {
    console.log('2003');
    console.log(data);
  });
  socket.on(2002, (data) => {
    console.log('2002');
    console.log(data);
  });
  socket.on(2001, (data) => {
    console.log('2001');
    console.log(data);
  });
  socket.on(1010, (data) => {
    console.log('1010');

    /**
     * 파일 전송 1단계. 정보 제공
     */
    /*
     file.on('data', (data) => {
       config.uploadedSize += data.length;
       */

    let stats = fs.statSync(config.filename);

    let fileStat = {
      path: './',
      name: 'sample',
      ext: '.mp4',
      size: stats.size,
    };
    console.log(fileStat);

    /**
     * 먼저 파일 정보를 보낸다 - 7000
     */
    socket.emit(7000, JSON.stringify(fileStat));
  });
  socket.on(4000, (data) => {
    console.log('4000');
    console.log(data);
  });
  socket.on(2000, (data) => {
    console.log('2000');
    console.log(data);
  });
  socket.on(4001, (data) => {
    console.log('4001');
    console.log(data);
  });
  socket.on(7000, (data) => {
    console.log('7000');
    //데이터를 보내야 한다
    data = JSON.parse(data);
    let file = fs.createReadStream(config.filename, { flags: 'r', start: data.tmpfileSize }); // default : 64 * 1024
    file.on('data', (data) => {
      socket.emit(7001, data);
    });
  });
  socket.on(7001, (data) => {
    console.log('7001');
    data = JSON.parse(data);
    console.log('progress bar : ' + data.percent);
  });

  socket.on('connect', () => {
    if (socket.connected) console.log('서버로 성공적으로 연결되었습니다 : ' + config.host);
    else console.log('서버의 연결이 끊겼습니다 : ' + config.host);
  });
};

// socket.emit(
//   2000,
//   JSON.stringify({
//     path: 'root 또는 부모의 경로',
//     name: '폴더의 이름',
//   })
// );
// socket.emit(
//   2001,
//   JSON.stringify({
//     path: 'root 또는 부모의 경로',
//     name: '폴더의 이름',
//     newName: '변경될 폴더의 이름',
//   })
// );
// socket.emit(
//   2002,
//   JSON.stringify({
//     path: 'root 또는 부모의 경로',
//     name: '폴더의 이름',
//   })
// );
// socket.emit(
//   2003,
//   JSON.stringify({
//     path: 'root 또는 부모의 경로',
//     name: '폴더의 이름',
//   })
// );

getConnection();
