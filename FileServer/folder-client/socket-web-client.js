// 다음 작업 : express-generator를 활용한 구조화? express는 뭘까 http기술 아님?
// 지금은 일단 모듈화 서비스 제공

const fs = require('fs');
//const { send } = require('process');
const io = require('socket.io-client');
//const ss = require('socket.io-stream');

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
  socket = io.connect(config.host, {
    reconnectionDelayMax: 1000,
    auth: {
      token: '123',
    },
    query: {
      'my-key': 'my-value',
    },
  });
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
    socket.emit(4000);
  });
  socket.on(4000, (data) => {
    console.log('4000');
    console.log(data);
  });
  socket.on(2000, (data) => {
    console.log('new folder tree');
    console.log(data);
  });

  socket.on('connect', () => {
    if (socket.connected) console.log('서버로 성공적으로 연결되었습니다 : ' + config.host);
    else console.log('서버의 연결이 끊겼습니다 : ' + config.host);
  });

  socket.on('message', recvMessageData);
  //socket.on('blob', recvBlob);
};

let sendData = (id, data) => {
  socket.emit(id, data);
  console.log('서버로 데이터를 전송하였습니다.');
};

let recvMessageData = (data) => {
  console.log('서버에서 데이터를 받아 리턴합니다');
  console.log(data);
  return data;
};

getConnection();

////////////////////////////////
////////////////////////////////
////////////////////////////////

let setFileName = (filename) => {
  config.filename = filename;
  console.log('보낼 파일의 이름을 변경합니다');
};
//setFileName('./img/sample.mp4');

let sendBlob = (fileName) => {
  setFileName(fileName);
  let file = fs.createReadStream(config.filename, { flags: 'r' });
  var stats = fs.statSync(config.filename);
  config.uploadTotalSize = stats.size;
  config.uploadedSize = 0;
  console.log('서버로 대용량 파일을 전송합니다');
  file.on('data', (data) => {
    config.uploadedSize += data.length;
    sendData('blob', data);
    getFilePercent();
  });
};

let getFilePercent = () => {
  let ans = Math.floor((config.uploadedSize / config.uploadTotalSize) * 100);
  console.log(ans);
  return ans;
};
//sendBlob('./img/sample.mp4');

let setPath = (path, name, ext) => {
  config.savePath = path;
  config.saveName = name;
  config.saveExt = ext;
  config.saveFile = fs.createWriteStream(config.savePath + config.saveName + config.saveExt, {
    flags: 'w',
  });
  console.log('받을 파일의 경로를 수정했습니다');
};

let recvBlob = (data) => {
  config.saveFile.write(data);
};

//setPath('./save/', 'sample', '.mp4');
module.exports = {
  socket: socket,
  getConnection: getConnection,
  sendData: sendData,
  recvMessageData: recvMessageData,
  setFileName: setFileName,
  sendBlob: sendBlob,
  getFilePercent: getFilePercent,
  setPath: setPath,
  recvBlob: recvBlob,
};
