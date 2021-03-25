const io = require('socket.io-client');

let config = {
  host: 'http://127.0.0.1:9000',
  uploadedSize: 0,
  uploadTotalSize: 0,
  savePath: './',
  saveName: 'sample',
  saveExt: '.mp4',
  saveFile: '',
  filename: '',
  downLoadSize: 0,
  downLoadTotalSize: 0,
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
    socket.emit(
      2001,
      JSON.stringify({
        path: 'root 또는 부모의 경로',
        name: '폴더의 이름',
        newName: '변경될 폴더의 이름',
      })
    );
  });
  socket.on(4000, (data) => {
    console.log('4000');
    console.log(data);
  });
  socket.on(2000, (data) => {
    console.log('2000');
    console.log(data);
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
