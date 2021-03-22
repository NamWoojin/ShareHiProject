/**
 * 예시 파일
 * module.exports = {
    getConnection: getConnection,
    sendData: sendData,
    recvMessageData: recvMessageData,
    setFileName: setFileName,
    sendBlob: sendBlob,
    getFilePercent: getFilePercent,
    setPath: setPath,
    recvBlob: recvBlob,
    
  };
 */
const { setPath, sendBlob, sendData } = require('./socket-web-client.js');
let client = require('./socket-web-client.js');

//연결 요청하기
client.getConnection();
console.log('connected');
// 메시지 한번 보내보기 : messag
client.sendData('message', 'hi~');
//데이터 한번 받아보기(서버에서 실행하기)
// call back 아직 안댐....
//전송할 파일 이름 선택하기
client.setFileName('./img/sample.mp4');
// 대용량 파일 전송해보기
//sendBlob('./img/sample.mp4');
sendData('hello', 'hello');
// 저장될 파일의 경로 설정하기
setPath('./img/', 'sample2', '.mp4');
//대용량 파일 받기
// call back 안댐...
