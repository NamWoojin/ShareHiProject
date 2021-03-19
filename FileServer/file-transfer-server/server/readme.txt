<<사용법 및 현재 진행된 정도>>

현재 된 것 :
1. socket.io를 이용한 web - server socket
2. 모듈화 (단, call back을 어떻게 만드는지 몰라서 서버로부터 파일을 
            받는 경우는 메서드는 있지만 모듈화는 안댐)
3. 일단, 메시지를 보내면 클라이언트한테 도로 돌려줌
4. 일단, 대용량 파일을 보내면 클라이언트한테 도로 돌려줌
5. client.js : 샘플파일
6. socket-web-client.js : 리팩터링해야할 로직 파일
7. 보내는 클라이언트는 파일을 몇퍼센트 보냈는지 알 수 있다.

아직 안된 것 :
1. android - server socket
2. call back 함수 모듈화
3. 이어받기
4. 멀티 파일 전송(한 사람이 여러 파일 받기)
5. 멀티 파일 전송(여러 사람이 파일 받기)
6. 코드 리팩터링(express를 사용?(http서버인가?), 주석)

클라이언트 웹 소켓 사용법

1. 파일을 원하는 경로에 추가
2. 임포트하기
3. API
    1. 파일 전송 서버와 연결하기
        getConnection();
        예시 : 
        getConnection() 실행
        => 서버로 성공적으로 연결되었습니다 : http://localhost:8765
    2. 서버로 JSON이나 message 보내기
        sendData('message', JSON or data)
        예시 :
        sendData('message', {id : 1});
        => 서버로 {id : 1} 데이터를 전송하였습니다.
    3. 서버로 전송할 파일명 설정하기
        setFileName(file name(경로, 확장자 포함));
        예시 :
        setFileName('./img/sample.mp4');
    4. 서버로 stream blob 보내기
        sendBlob(fileName);
        예시 :
        sendBlob(fileName);
        => 서버로 파일을 전송하였습니다.
    5. 파일 전송 퍼센트를 출력하는 call back
        getFilePercent();
        ////////아직 콜백함수 어떻게 만드는지 몰겠습니다...
        //현재는 sendBlob을 호출하면 퍼센트를 console에 출력합니다.
    6. 서버에서 JSON이나 message를 받으면 실행되는 call back
        recvMessageData(data);
        예시 :
        let data = recvMessageData();
        console.log(data);
        => hi~
    7. 서버에서 받을 파일의 로컬 경로 설정하기
        setPath(path, name, ext);
        예시 :
        setPath('./img/', 'sample', '.mp4');
    8. 서버에서 파일 받아서 파일에 저장
        recvBlob(data);
        예시 :
        recvBlob(data);