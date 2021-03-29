package sungjg.com.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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

import sugjg.com.dto.FileStat;

public class SocketInfo {

	private FileStat fs;

	private Socket socket;
	BufferedReader in;
	PrintWriter out;
	Gson gson = new Gson();
	String targetId;

	public SocketInfo(FileStat fs) {
		super();
		this.fs = fs;
	}

	public void connect() {
		try {
			socket = new Socket();
			System.out.println("ip,port : " + Client.IP + "," + Client.PORT);
			SocketAddress socketAddress = new InetSocketAddress(Client.IP, Client.PORT);
			socket.connect(socketAddress, 8288);

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

					JsonParser parser = new JsonParser();
					JsonElement element = parser.parse(data);
					//System.out.println(data);
					int namespace = element.getAsJsonObject().get("namespace").getAsInt();
					switch (namespace) {
					case 1010: // 이 디바이스를 공유 디바이스로 설정 + 동시에 공유 데이터 tcp 연결이 필요
						System.out.println("send 1020");
						JsonObject jobj = new JsonObject();
						jobj.addProperty("namespace", "1020");
						String json = gson.toJson(jobj);
						write(json);

						break;
					case 1030: // 파일 전송을 위한 TCP 연결이 완료되었다.
						jobj = new JsonObject();
						jobj.addProperty("namespace", "7000");
						jobj.addProperty("targetId", targetId);
						jobj.addProperty("tmpfileSize", fs.getTmpfileSize());
						jobj.addProperty("size", fs.getSize());
						jobj.addProperty("status", "200"); // or 403 FORBIDDEN
						jobj.addProperty("message", "OK");
						jobj.addProperty("detail", "");
						jobj.addProperty("content", "");
						json = gson.toJson(jobj);
						write(json);
						break;
					case 2000: // 폴더 디렉토리 출력

						/**
						 * TODO 폴더 이름 제공 로직!
						 */

						jobj = new JsonObject();
						jobj.addProperty("namespace", "2000");
						jobj.addProperty("targetId", element.getAsJsonObject().get("targetId").getAsString());

						jobj.addProperty("status", "200");
						jobj.addProperty("message", "OK");
						jobj.addProperty("detail", "");
						jobj.addProperty("content", "folder directory JSON object ");
						json = gson.toJson(jobj);
						write(json);
						break;
					case 2001: // 폴더 이름 수정

						/**
						 * TODO 폴더 이름 수정 로직!
						 */

						jobj = new JsonObject();
						jobj.addProperty("namespace", "2001");
						jobj.addProperty("targetId", element.getAsJsonObject().get("targetId").getAsString());

						jobj.addProperty("status", "200"); // or 403 FORBIDDEN
						jobj.addProperty("message", "OK");
						jobj.addProperty("detail", "");
						jobj.addProperty("content", "");
						json = gson.toJson(jobj);
						write(json);
						break;
					case 2002: // 폴더 삭제

						/**
						 * TODO 폴더 삭제 로직! (여러 개의 폴더, 파일 삭제가 요청을 올 수도 있음)
						 */

						jobj = new JsonObject();
						jobj.addProperty("namespace", "2002");
						jobj.addProperty("targetId", element.getAsJsonObject().get("targetId").getAsString());

						jobj.addProperty("status", "200"); // or 401 UNAUTHORIZED
						jobj.addProperty("message", "OK");
						jobj.addProperty("detail", "");
						jobj.addProperty("content", "");
						json = gson.toJson(jobj);
						write(json);
						break;
					case 2003: // 폴더 추가 (파일은 추가 못함(파일 추가 == 업로드)

						/**
						 * TODO 폴더 추가 로직!
						 */

						jobj = new JsonObject();
						jobj.addProperty("namespace", "2003");
						jobj.addProperty("targetId", element.getAsJsonObject().get("targetId").getAsString());
						jobj.addProperty("status", "200"); // or 403 FORBIDDEN
						jobj.addProperty("message", "OK");
						jobj.addProperty("detail", "");
						jobj.addProperty("content", "");
						json = gson.toJson(jobj);
						write(json);
						break;
					case 7000: // 파일 스텟 확인

						/**
						 * TODO 파일 스텟 로직
						 */
						int size = element.getAsJsonObject().get("size").getAsInt();
						String name = element.getAsJsonObject().get("name").getAsString();
						String path = element.getAsJsonObject().get("path").getAsString();
						String ext = element.getAsJsonObject().get("ext").getAsString();

						File file = new File(path + name + ext);
						// 1. 파일이 이미 있는지 확인한다
						if (file.length() == size) {
							System.out.println("이미 파일이 있습니다.");
							jobj = new JsonObject();
							jobj.addProperty("namespace", "7004");
							jobj.addProperty("targetId", element.getAsJsonObject().get("targetId").getAsString());
							jobj.addProperty("status", "400"); // or 403 FORBIDDEN
							jobj.addProperty("message", "BAD REQUEST");
							jobj.addProperty("detail", "");
							jobj.addProperty("content", "");
							json = gson.toJson(jobj);
							write(json);
							break;
						} else {

							file = new File(path + name + ext);
							long tmpfileSize = 0;
							if (file.exists()) {
								System.out.println("이어받기 로직을 수행합니다.");
								tmpfileSize = file.length();
								System.out.println("현재 청크 사이즈 : " + tmpfileSize);
							}

							fs = new FileStat(name, path, ext, size, tmpfileSize);
							// 새로운 TCP 연결 시도
							targetId = element.getAsJsonObject().get("targetId").getAsString();
							SocketData sd = new SocketData(fs);
							sd.connect();
						}
					case 7001: // 파일 전송

						/**
						 * TODO 퍼센트 로직
						 */
						break;

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};
}
