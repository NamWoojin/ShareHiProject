package sungjg.com.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SocketInfo {

	private static final String IP = "localhost";
	private static final int PORT = 9001;

	private Socket socket;
	BufferedReader in;
	PrintWriter out;
	byte[] bytes;
	int size = 0;
	Gson gson = new Gson();

	boolean socketConnected = false;

	public void connect() {
		try {
			socket = new Socket();
			SocketAddress socketAddress = new InetSocketAddress(IP, PORT);
			socket.connect(socketAddress, 8288);
			
			System.out.println("------ 연결 성공 -------");
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			getIO.start();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void write(String data) {
		out.println(data);
		out.flush();
	}

	private Thread getIO = new Thread() {
		@Override
		public void run() {
			try {
				while (true) {
					String data = in.readLine();

					// 해석하기
					JsonParser parser = new JsonParser();
					JsonElement element = parser.parse(data);
					int id = element.getAsJsonObject().get("id").getAsInt();
					System.out.println("id : " + id);
					switch (id) {
					case 1010: // 이 디바이스를 공유 디바이스로 설정
						System.out.println("1010 : " + data);

						JsonObject jobj = new JsonObject();
						jobj.addProperty("namespace", "1020");
						jobj.addProperty("data", "hi");
						String json = gson.toJson(jobj);

						write(json);

						break;
					case 2000: // 폴더 디렉토리 출력
						System.out.println("2000 : " + data);

						/**
						 * TODO 폴더 이름 제공 로직!
						 */

						// 서버로 데이터 전송
						jobj = new JsonObject();
						jobj.addProperty("namespace", "2000");
						jobj.addProperty("targetId", element.getAsJsonObject().get("targetId").getAsString());
						jobj.addProperty("status", "200");
						jobj.addProperty("message", "OK");
						jobj.addProperty("data", "hi");
						json = gson.toJson(jobj);
						write(json);
						break;
					case 2001: // 폴더 이름 수정
						System.out.println("2001 : " + data);

						/**
						 * TODO 폴더 이름 수정 로직!
						 */

						// 서버로 데이터 전송
						jobj = new JsonObject();
						jobj.addProperty("namespace", "2001");
						jobj.addProperty("targetId", element.getAsJsonObject().get("targetId").getAsString());
						jobj.addProperty("status", "200"); // or 403 FORBIDDEN
						jobj.addProperty("message", "OK");
						json = gson.toJson(jobj);
						write(json);
						break;
					case 2002: // 폴더 삭제
						System.out.println("2002 : " + data);

						/**
						 * TODO 폴더 삭제 로직! (여러 개의 폴더, 파일 삭제가 요청을 올 수도 있음)
						 */

						// 서버로 데이터 전송
						jobj = new JsonObject();
						jobj.addProperty("namespace", "2002");
						jobj.addProperty("targetId", element.getAsJsonObject().get("targetId").getAsString());
						jobj.addProperty("status", "200"); // or 401 UNAUTHORIZED
						jobj.addProperty("message", "OK");
						json = gson.toJson(jobj);
						write(json);
						break;
					case 2003: // 폴더 추가 (파일은 추가 못함(파일 추가 == 업로드)
						System.out.println("2003 : " + data);

						/**
						 * TODO 폴더 추가 로직!
						 */

						// 서버로 데이터 전송
						jobj = new JsonObject();
						jobj.addProperty("namespace", "2003");
						jobj.addProperty("targetId", element.getAsJsonObject().get("targetId").getAsString());
						jobj.addProperty("status", "200"); // or 403 FORBIDDEN
						jobj.addProperty("message", "OK");
						json = gson.toJson(jobj);
						write(json);
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};
}
