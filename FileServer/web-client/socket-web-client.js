const io = require('socket.io-client');
const fs = require('fs');

let config = {
  host: 'http://localhost:9002',
  // host: 'http://j4f001.p.ssafy.io:9002',
  filename: './img/sample.txt',
};

/**
 * 서버와의 소켓 연결
 */
let socket = io.connect(config.host, { transports: ['websocket'] });

/**
 * Web
 * 1010
 * 설명 : 서버와 첫 연결 후 수행되는 콜백 함수 예제
 * 메시지 : {"namespace":1010,"status":200,"message":"OK"}
 */
let flag = 0;
socket.on(1010, (data) => {
  console.log(1010);
  console.log(data);
  if (flag === 0) {
    flag = 1;
    socket.emit(1050);
  }
});

/**
 * Web
 * 1050
 * 설명 : 서버에게 내 디바이스를 제외한 현재 공유중인 모든 디바이스의 정보를 받아온다.
 * 메시지 :{"devices":[{"id":"c69ad27e-48ff-4849-b9ed-568a6935e794","name":"c69ad27e-48ff-4849-b9ed-568a6935e794"}]}
 */
let flag2 = 0;
socket.on(1050, (data) => {
  console.log(1050);
  console.log(data);
  data = JSON.parse(data);
  if (flag2 === 0) {
    flag2 = 1;
    socket.emit(1070, JSON.stringify(data.devices[0]));
  }
});

/**
 * Web
 * 1060
 * 설명 : 내 디바이스를 제외한 현재 공유 중이 '아닌' 모든 디바이스의 정보를 클라이언트에게 제공한다
 * 메시지 :{"namespace":1060,"devices":[{"id":"c45f6a42-259c-4f48-a469-18f26d89a561","name":"c45f6a42-259c-4f48-a469-18f26d89a561"}]}
 */
socket.on(1060, (data) => {
  console.log(1060);
  console.log(data);
});

/**
 * Web
 * 1070
 * 설명 : 공유된 특정 디바이스에 나의 디바이스를 연결한다.
 * 메시지 :{"namespace":1010,"status":200,"message":"OK"}
 */
socket.on(1070, (data) => {
  console.log(1070);
  console.log(data);
  data = JSON.parse(data);
  //파일을 보낸다
  // let stats = fs.statSync(config.filename);

  // let fileStat = {
  //   path: data.path,
  //   name: 'sample',
  //   ext: '.txt',
  //   size: stats.size,
  // };
  // console.log(fileStat);
  //socket.emit(7000, JSON.stringify(fileStat));

  //자! 파일 다운로드를 시작해 보자!
  socket.emit(
    8000,
    JSON.stringify({
      path: './',
      name: 'sample-for-download',
      ext: '.mp4',
    })
  );
});

/**
 * Web
 * 7000
 * 설명 : 데이터를 주고받았고, 전송해야 한다
 */
socket.on(7000, (data) => {
  console.log(7000);
  console.log(data);
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

/**
 * Web
 * 2000
 * 설명 : 폴더 디렉토리 구조를 받아서 클라이언트에게 제공한다
 * 메시지 :
 */
socket.on(2000, (data) => {
  console.log(2000);
  console.log(data);
});
socket.on(8000, (data) => {
  console.log(8000);
  console.log(data);
});

/**
 * Web
 * 1050
 * 설명 : 현재 공유중인 모든 디바이스의 정보를 가져온다.
 * 메시지 : {"devices":[{"id":"c69ad27e-48ff-4849-b9ed-568a6935e794","name":"c69ad27e-48ff-4849-b9ed-568a6935e794"}]}
 */
// socket.emit(1050);
/**
 * Web
 * 1060
 * 설명 : 내 디바이스를 제외한 현재 공유 중이 '아닌' 모든 디바이스의 정보를 클라이언트에게 제공한다
 * 메시지 :{"namespace":1060,"devices":[{"id":"c45f6a42-259c-4f48-a469-18f26d89a561","name":"c45f6a42-259c-4f48-a469-18f26d89a561"}]}
 */
// socket.emit(1060);

/**
 * Web
 * 1070
 * 설명 : 공유된 특정 디바이스에 나의 디바이스를 연결한다.
 * 메시지 :{"namespace":1010,"status":200,"message":"OK"}
 */
// data = JSON.parse(data);
// socket.emit(1070, JSON.stringify(data.devices[0]));

/**
 * Web
 * 2001
 * 설명 : 폴더 이름 수정을 요청한다
 * 메시지 :
 */
// socket.emit(
//   2001,
//   JSON.stringify({
//     path: 'path',
//     newPath: 'newPath',
//   })
// );

/**
 * Web
 * 2002
 * 설명 : 폴더 삭제를 요청한다
 * 메시지 :
 */
// socket.emit(
//   2002,
//   JSON.stringify({
//     path: 'path',
//   })
// );

/**
 * Web
 * 2003
 * 설명 : 폴더 추가를 요청한다
 * 메시지 :
 */
// socket.emit(
//   2003,
//   JSON.stringify({
//     path: 'path',
//     newFolder: 'newFolder',
//   })
// );

/*
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
*/
/**
 * 파일 전송 1단계. 정보 제공
 */
/*
     file.on('data', (data) => {
       config.uploadedSize += data.length;
       */
/*
    let stats = fs.statSync(config.filename);

    let fileStat = {
      path: './',
      name: 'sample',
      ext: '.mp4',
      size: stats.size,
    };
    console.log(fileStat);
*/
/**
 * 먼저 파일 정보를 보낸다 - 7000
 */
/*
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
*/
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

// getConnection();
