package sungjg.com.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class SocketClient {

	private Socket socket;
	private PrintWriter out;
	private InputFile inputFile;
	private BufferedReader in;
	
	public SocketClient(String host, int port) {
		getConnection(host, port);
		init();
	}
	
	public SocketClient() {
		init();
	}
	
	public void getConnection(String host, int port) {
		Socket socket;
		try {
			socket = new Socket(host,port);
			this.socket = socket;
			System.out.println("서버 연결 성공 - => "+host+ ":" + port);
		} catch (Exception e) {
			System.out.println("서버 연결 실패 - => "+host+ ":" + port + " at SocketClient Constructor");
			e.printStackTrace();
		}
	}
	
	private void init() {
		this.inputFile = new InputFile();
		
		try {
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			
			Thread inputThread = new Thread() {
				
				@Override
				public void run() {
					System.out.println("hi~");
					while(true) {
						String x = getMessage();
						System.out.println(x);
					}
				}
			};
			
			inputThread.start();
		} catch (IOException e) {
			System.out.println("초기화 실패");
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String message) {
		byte[] data = message.getBytes(); //getBytes() 메서드를 사용 해 문자열을 Byte로 바꿔준다 output.write(data);
		out.println(data);
	}
	
	private void setInputFilePath(String path, String name, String ext) {
		inputFile.setPath(path);
		inputFile.setName(name);
		inputFile.setExt(ext);
	}
	
	
	private String getMessage() {
		
		String message = "";
		try {
			message = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return message;
	}
}