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
			SocketAddress socketAddress = new InetSocketAddress(Client.IP, Client.PORT);
			/**
			 * 서버와 소켓 연결 시도하는 코드
			 */
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
					System.out.println(data);
					JsonElement element = parser.parse(data);
					JsonObject jobj = new JsonObject();
					String json = "";
					int namespace = element.getAsJsonObject().get("namespace").getAsInt();
					switch (namespace) {
					/**
					 * Android
					 * 1010 
					 * 응답에 성공했을 때 수행되는 코드 설명 : 서버와 성공적으로 응답한 경우 수행된다 
					 * data : {"namespace":1010,"status":200,"message":"OK"}
					 */
					
					case 1010:
						/**
						 * LOGIC
						 */
						jobj = new JsonObject();
						jobj.addProperty("namespace", "1020");
						json = gson.toJson(jobj);
						write(json);
						break;
						
					/**
					 * Android 
					 * 1050 
					 * 설명 : 내 디바이스를 제외한 현재 공유 중인 모든 디바이스의 정보를 클라이언트에게 제공한다
					 * 메시지 :{"devices":[{"id":"c69ad27e-48ff-4849-b9ed-568a6935e794","name":"c69ad27e-48ff-4849-b9ed-568a6935e794"}]}
					 */
					case 1050:
						/**
						 * LOGIC
						 */
						break;
						
					/**
					* Android 
					* 1060 
					* 설명 : 내 디바이스를 제외한 현재 공유 중이 '아닌' 모든 디바이스의 정보를 클라이언트에게 제공한다
					* 메시지 :{"namespace":1060,"devices":[{"id":"c45f6a42-259c-4f48-a469-18f26d89a561","name":"c45f6a42-259c-4f48-a469-18f26d89a561"}]}
					*/
					case 1060:
						/**
						 * LOGIC
						 */
						break;
						
					/**
					 * Android
					 * 1070
					 * 설명 : 공유된 특정 디바이스에 나의 디바이스를 연결한다.
					 * 메시지 :{"namespace":1010,"status":200,"message":"OK"}
					 */
					case 1070:
						/**
						 * LOGIC
						 */
						jobj = new JsonObject();
						jobj.addProperty("namespace", "1070");
						jobj.addProperty("path", "./");
						jobj.addProperty("targetId", element.getAsJsonObject().get("targetId").getAsString());
						json = gson.toJson(jobj);
						write(json);
						break;
						
						
					/**
					 * Android
					 * 4001
					 * 설명 : JSON 파싱이 실패할 경우 부른다
					 * 메시지 :{"namespace":4001,"status":400,"message":"BAD REQUEST"}
					*/
					case 4001:
						/**
						 * LOGIC
						 */
						break;
					case 7200: // 파일 전송을 위한 TCP 연결이 완료되었다.
						System.out.println("fs size : " + fs.getSize());
						if(fs.getSize() == 0) break;
						jobj = new JsonObject();
						jobj.addProperty("namespace", "7100");
						jobj.addProperty("tmpfileSize", fs.getTmpfileSize());
						jobj.addProperty("size", fs.getSize());
						json = gson.toJson(jobj);
						write(json);
						break;
						
					 /**
					   * Android
					   * 2100
					   * 설명 : 폴더 디렉토리 구조를 공유 디바이스에게 요청한다.
					   * 메시지 : {"namespace":2100,"targetId":"9ebe9cf8-61aa-43ee-bcfc-66005e81f287","path":"./"}
					  */
					case 2100:
						/**
						 * LOGIC
						 */
						jobj = new JsonObject();
						jobj.addProperty("namespace", "2100");
						jobj.addProperty("targetId", element.getAsJsonObject().get("targetId").getAsString());
						jobj.addProperty("data", "./");
						json = gson.toJson(jobj);
						write(json);
						break;
					/**
					* Android
					* 2101
					* 설명 : 폴더를 수정한다
					* 메시지 : 
					*/
					case 2101:

						/**
						 * LOGIC
						 */
						break;
						
						/**
						* Android
						* 2102
						* 설명 : 폴더를 삭제한다
						* 메시지 : 
						*/
					case 2102:

						/**
						 * LOGIC
						 */
						break;
						
						/**
						* Android
						* 2101
						* 설명 : 폴더를 추가한다
						* 메시지 : 
						*/
					case 2103:

						/**
						 * LOGIC
						 */
					case 7100: // 파일 스텟 확인

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
							System.out.println("파일 이름 : " + file.getName());
							System.out.println("이미 파일이 있습니다.");
							fs = new FileStat(name, path, ext, 0, 0);
							jobj = new JsonObject();
							jobj.addProperty("namespace", "7004");
							jobj.addProperty("targetId", element.getAsJsonObject().get("targetId").getAsString());
							jobj.addProperty("status", "400"); // or 403 FORBIDDEN
							jobj.addProperty("message", "BAD REQUEST");
							jobj.addProperty("detail", "");
							jobj.addProperty("content", "이미 파일이 있습니다.");
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
						}
						SocketData sd = new SocketData(fs);
						sd.connect();
					case 7001: // 파일 전송
						
						/**
						 * TODO 퍼센트 로직
						 */
						break;
						
					/**
					* Android
					* 8100
					* 설명 : 폴더를 추가한다
					* 메시지 : 
					*/
					case 8100:
						String name2 = element.getAsJsonObject().get("name").getAsString();
						String path2 = element.getAsJsonObject().get("path").getAsString();
						String ext2 = element.getAsJsonObject().get("ext").getAsString();
						File file2 = new File(path2 + name2 + ext2);
						fs = new FileStat(name2, path2, ext2, file2.length(), 0);
						targetId = element.getAsJsonObject().get("targetId").getAsString();
						
						jobj = new JsonObject(); 
						jobj.addProperty("namespace", "8100");
						jobj.addProperty("size", file2.length());
						jobj.addProperty("targetId", targetId);
						json = gson.toJson(jobj); 
						write(json);
						break;
					case 8200:
						SocketDownload sdown = new SocketDownload(fs);
						sdown.connect();
						break;
					}
				}
				/**
				 * 1020 
				 * 해당 디바이스를 공유 디바이스로 설정하기 위한 코드 
				 * 설명 : 1020으로 서버에 전달하면 서버는 이 디바이스를 공유할 수 있도록 설정한다. 
				 * 메시지 : {"namespace":1010,"status":200,"message":"OK"}
				 */
				/*
				 * jobj = new JsonObject(); 
				 * jobj.addProperty("namespace", "1020"); 
				 * json = gson.toJson(jobj); 
				 * write(json);
				 */
				
				/**
				 * Android 
				 * 1050 
				 * 설명 : 내 디바이스를 제외한 현재 공유 중인 모든 디바이스의 정보를 클라이언트에게 제공한다
				 * 메시지 :{"devices":[{"id":"c69ad27e-48ff-4849-b9ed-568a6935e794","name":"c69ad27e-48ff-4849-b9ed-568a6935e794"}]}
				 */
				/*
				 * jobj = new JsonObject(); 
				 * jobj.addProperty("namespace", "1050"); 
				 * json = gson.toJson(jobj); 
				 * write(json);
				 */
				
				/**
				* Android 
				* 1060 
				* 설명 : 내 디바이스를 제외한 현재 공유 중이 '아닌' 모든 디바이스의 정보를 클라이언트에게 제공한다
				* 메시지 :{"namespace":1060,"devices":[{"id":"c45f6a42-259c-4f48-a469-18f26d89a561","name":"c45f6a42-259c-4f48-a469-18f26d89a561"}]}
				*/
				/*
				 * jobj = new JsonObject(); 
				 * jobj.addProperty("namespace", "1060"); 
				 * json = gson.toJson(jobj); 
				 * write(json);
				 */
				
				/**
				 * Android
				 * 1070
				 * 설명 : 공유된 특정 디바이스에 나의 디바이스를 연결한다.
				 * 메시지 :{"namespace":1010,"status":200,"message":"OK"}
				 */
				/*
				 * jobj = new JsonObject(); 
				 * jobj.addProperty("namespace", "1070"); 
				 * jobj.addProperty("id", "device id");
				 * json = gson.toJson(jobj); 
				 * write(json);
				 */
				
				 /**
				   * Android
				   * 2100
				   * 설명 : 폴더 디렉토리 구조를 공유 디바이스에게 요청한다.
				   * 메시지 :
				  */
				/*
				jobj = new JsonObject();
				jobj.addProperty("namespace", "2100");
				jobj.addProperty("targetId", element.getAsJsonObject().get("targetId").getAsString());
				jobj.addProperty("content", "folder directory JSON object ");
				json = gson.toJson(jobj);
				write(json);
				*/
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};
}
