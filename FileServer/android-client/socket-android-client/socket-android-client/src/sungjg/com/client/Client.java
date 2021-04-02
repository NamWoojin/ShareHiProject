package sungjg.com.client;

import sugjg.com.dto.FileStat;

public class Client {

//	public static final String IP = "j4f001.p.ssafy.io";
//	public static final int PORT = 9003;

	 public static final String IP = "localhost";
	 public static final int PORT = 9003;

	public static void main(String[] args) {
		FileStat fs = new FileStat();
		SocketInfo s = new SocketInfo(fs);
		System.out.println("소켓 연결됨");
		s.connect();
	}
}
