import io from 'socket.io-client';

let config = {
  host: 'http://j4f001.p.ssafy.io:9002',
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
  socket.on('connect', () => {
    if (socket.connected) console.log('서버로 성공적으로 연결되었습니다 : ' + config.host);
    else console.log('서버의 연결이 끊겼습니다 : ' + config.host);
  });
}

export {
  test
}