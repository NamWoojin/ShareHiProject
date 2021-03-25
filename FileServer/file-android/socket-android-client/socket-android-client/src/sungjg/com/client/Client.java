package sungjg.com.client;

public class Client {
	public static void main(String[] args) {
		//SocketClient socketClient = new SocketClient("localhost", 8888);
		
		//socketClient.sendMessage("hello server");
		
		SocketClientV1 client = new SocketClientV1();
		client.checkStart.start();
	}
}
