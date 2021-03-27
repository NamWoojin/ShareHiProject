package sungjg.com.client;

import sugjg.com.dto.FileStat;

public class Client {
	public static void main(String[] args) {

		FileStat fs = new FileStat();
		
		SocketInfo s = new SocketInfo(fs);
		
		s.connect();
		
		
		//s.write("something..."); // 이 메소드로 서버에 메시지를 보낸다 (JSON 형식 맞춰서 보내야 함)
	}
}
