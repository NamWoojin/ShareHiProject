import io from 'socket.io-client';

let config = {
  host: 'ws://172.30.1.17:9000',
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
let config1 = {
  host: 'wss://172.30.1.17:9000',
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

function test() {
  console.log('testing...')
  let socket = io.connect(config.host, { transports: ['websocket'] });
  let socket1 = io.connect(config1.host, { transports: ['websocket'] });
  console.log('socket http',socket)
  console.log('socket https',socket1)
  socket.on('connect', () => {
    if (socket.connected) console.log('서버로 성공적으로 연결되었습니다 : ' + config.host);
    else console.log('서버의 연결이 끊겼습니다 : ' + config.host);
  });
}

export {
  test
}