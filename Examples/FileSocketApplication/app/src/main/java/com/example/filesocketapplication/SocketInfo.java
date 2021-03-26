package com.example.filesocketapplication;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

public class SocketInfo {

	private static final String IP = "10.0.2.2";
	private static final int PORT = 9001;

	private Socket socket;
	BufferedReader in;
	PrintWriter out;
	Gson gson = new Gson();

	public void connect() {
		Log.d("myTag", "connection");
		try {
			socket = new Socket();
			SocketAddress socketAddress = new InetSocketAddress(IP, PORT);
			socket.connect(socketAddress, 8288);
			Log.d("myTag", "connection success");

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
					Log.d("myTag", "here");
					Log.d("myTag", data);
					int namespace = element.getAsJsonObject().get("namespace").getAsInt();
					switch (namespace) {
					case 1010: // 이 디바이스를 공유 디바이스로 설정

						JsonObject jobj = new JsonObject();
						jobj.addProperty("namespace", "1020");
						String json = gson.toJson(jobj);

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
