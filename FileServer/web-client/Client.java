package sungjg.com.client;

public class Client {
	public static void main(String[] args) {

		SocketInfo s = new SocketInfo();
		
		s.connect();
		
		//s.write("something..."); // 이 메소드로 서버에 메시지를 보낸다 (JSON 형식 맞춰서 보내야 함)
	}
}
