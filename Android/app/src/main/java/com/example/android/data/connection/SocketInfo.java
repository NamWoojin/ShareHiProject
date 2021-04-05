package com.example.android.data.connection;

import android.content.Context;
import android.util.Log;

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

import com.example.android.data.connection.dto.FileStat;
import com.example.android.data.model.SocketRepository;
import com.example.android.notification.DownloadNotification;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;


public class SocketInfo {
    public static final String IP = "j4f001.p.ssafy.io";
    //    public static final String IP = "10.0.2.2";
//    public static final String IP = "192.168.35.127";
    public static final int PORT = 9003;

    private FileStat fs;

    private SocketRepository socketRepository;
    private Context context;
    private Socket socket;
    private String adID;
    private int CHUNK_SIZE = 1024;
    private boolean closeSocketByUser;
    private boolean threadRunning;
    private DownloadNotification downloadNotification;
    BufferedReader in;
    PrintWriter out;
    Gson gson = new Gson();

    public SocketInfo(SocketRepository socketRepository, Context context) {
        this.socketRepository = socketRepository;
        this.context = context;
    }

    public void connect(String adID) {
        Log.d("myTag", "connection");
        this.adID = adID;
        try {
            socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(IP, PORT);
            socket.connect(socketAddress, 8288);
            Log.d("myTag", "connection success");

            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            threadRunning = true;
            getIO.start();
            socketRepository.successSocketConnection();
            closeSocketByUser = false;
        } catch (Exception e) {
            e.printStackTrace();
            socketRepository.failSocketConnection();
        }
    }

    public void disConnect() {
        //중지 요청 보내기
        //getIO스레드에서 중지 요청 승인하면 답장
        try {
            closeSocketByUser = true;
            socket.close();
            threadRunning = false;
            socketRepository.successSocketClosed();
        } catch (IOException e) {
            e.printStackTrace();
            socketRepository.failSocketClosed();
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
                while (threadRunning) {
                    String data = in.readLine();

                    if (data == null) {
                        Log.i("TAG", "run: null");
                        throw new IOException();
//                        continue;
                    }
                    if(socket.isClosed()){
                        Log.i("TAG", "run: 소켓 끝");
                        break;
                    }

                    Log.d("myTagReceive", data);

                    JSONObject jsonObject = new JSONObject(data);
                    int namespace = jsonObject.getInt("namespace");

                    JsonObject jobj;
                    String json;
//                    JsonParser parser = new JsonParser();
//                    JsonElement element = parser.parse(data);
//                    int namespace = element.getAsJsonObject().get("namespace").getAsInt();
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
                            jobj.addProperty("adId", adID);
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
                         * 설명 : 현재 공유하고 있는 root path를 전송한다
                         * 메시지 :{"namespace":1010,"path":path value}
                         */
                        case 1070:
                            /**
                             * LOGIC
                             */
                            String rootPath = socketRepository.getRootPath();

                            jobj = new JsonObject();
                            jobj.addProperty("namespace", "1070");
                            jobj.addProperty("targetId", jsonObject.getString("targetId"));
                            jobj.addProperty("path", rootPath);
                            json = gson.toJson(jobj);
                            Log.i("myTag", "1070: " + json);
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

                        /**
                         * Android
                         * 2100
                         * 설명 : 폴더 디렉토리 구조를 공유 디바이스에게 요청한다.
                         * 메시지 : {"namespace":2100,"targetId":"9ebe9cf8-61aa-43ee-bcfc-66005e81f287","path":"./"}
                         */
                        case 2100:
                            /**
                             * TODO 폴더 이름 제공 로직!
                             */
                            String returnJSONObjectString = socketRepository.getFolderDirectory(jsonObject.getString("path")).toString();
                            Log.i("myTag", "2100: " + returnJSONObjectString.length());
                            int chunkCount = (returnJSONObjectString.length() / CHUNK_SIZE) + (returnJSONObjectString.length() % CHUNK_SIZE > 0 ? 1 : 0);
                            jobj = new JsonObject();
                            jobj.addProperty("namespace", "2100");
                            jobj.addProperty("targetId", jsonObject.getString("targetId"));
                            jobj.addProperty("path", jsonObject.getString("path"));
                            jobj.addProperty("chunkCount", chunkCount);
                            json = gson.toJson(jobj);
                            write(json);

                            Log.i("myTagSend", "2100: " + json);
                            break;

                        case 2150:
                            //chunkCount 보내야 할 개수
                            // pathDataChunkCount
                            String path = jsonObject.getString("path");
                            String targetId = jsonObject.getString("targetId");
                            chunkCount = jsonObject.getInt("chunkCount");
                            int num = jsonObject.getInt("pathDataChunkCount");
                            if (num < 0 || num >= chunkCount) {
                                break;
                            }
                            returnJSONObjectString = socketRepository.getFolderDirectory(path).toString();
                            int length = returnJSONObjectString.length();
                            int start = num * CHUNK_SIZE;
                            int next = Math.min(length, CHUNK_SIZE * (num + 1));
                            Log.i("TAG", "run: " + returnJSONObjectString.length() + " " + start + " " + next);
                            jobj = new JsonObject();
                            jobj.addProperty("namespace", "2150");
                            jobj.addProperty("targetId", targetId);
                            jobj.addProperty("path", path);
                            jobj.addProperty("chunkCount", chunkCount);
                            jobj.addProperty("pathDataChunkCount", num);
                            jobj.addProperty("data", returnJSONObjectString.substring(start, next));
                            json = gson.toJson(jobj);

                            Log.i("myTag", "2150: " + json);
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
                             * TODO 폴더 이름 수정 로직!
                             */
                            String newName = jsonObject.getString("newName");
                            path = jsonObject.getString("path");
                            String name = jsonObject.getString("name");
                            boolean result2101 = socketRepository.changeFolderName(path, name, newName);
                            Log.i("TAG", "run: 2101 return "+result2101);
//                            returnJSONObjectString = socketRepository.getFolderDirectory(jsonObject.getString("path")).toString();
//                            Log.i("myTag", "2100: " + returnJSONObjectString.length());
//                            chunkCount = (returnJSONObjectString.length() / CHUNK_SIZE) + (returnJSONObjectString.length() % CHUNK_SIZE > 0 ? 1 : 0);
//                            jobj = new JsonObject();
//                            jobj.addProperty("namespace", "2100");
//                            jobj.addProperty("targetId", jsonObject.getString("targetId"));
//                            jobj.addProperty("path", jsonObject.getString("path"));
//                            jobj.addProperty("chunkCount", chunkCount);
//                            json = gson.toJson(jobj);
//                            write(json);


//                            jobj = new JsonObject();
//                            jobj.addProperty("namespace", "2101");
//                            jobj.addProperty("targetId", jsonObject.getString("targetId"));
//                            jobj.addProperty("detail", "");
//                            jobj.addProperty("content", "");
//                            if (result2001) {
//                                jobj.addProperty("status", "200"); // or 403 FORBIDDEN
//                                jobj.addProperty("message", "OK");
//                            } else {
//                                jobj.addProperty("status", "403");
//                                jobj.addProperty("message", "FORBIDDEN");
//                            }
//                            json = gson.toJson(jobj);
//                            write(json);

                            break;

                        /**
                         * Android
                         * 2102
                         * 설명 : 폴더를 삭제한다
                         * 메시지 :
                         */
                        case 2102:

                            /**
                             * TODO 폴더 삭제 로직! (여러 개의 폴더, 파일 삭제가 요청을 올 수도 있음)
                             */

                            boolean result2102 = socketRepository.deleteFolder(jsonObject.getString("path"), jsonObject.getString("name"));
                            Log.i("TAG", "run: 2102 return "+result2102);
//                            jobj = new JsonObject();
//                            jobj.addProperty("namespace", "2102");
//                            jobj.addProperty("targetId", jsonObject.getString("targetId"));
//                            jobj.addProperty("detail", "");
//                            jobj.addProperty("content", "");
//                            if (result2002) {
//                                jobj.addProperty("status", "200"); // or 401 UNAUTHORIZED
//                                jobj.addProperty("message", "OK");
//                            } else {
//                                jobj.addProperty("status", "401");
//                                jobj.addProperty("message", "UNAUTHORIZED");
//                            }
//
//                            json = gson.toJson(jobj);
//                            write(json);

                            break;

                        /**
                         * Android
                         * 2103
                         * 설명 : 폴더를 추가한다
                         * 메시지 :
                         */
                        case 2103:

                            /**
                             * TODO 폴더 추가 로직!
                             */
                            ;
                            boolean result2103 = socketRepository.createFolder(jsonObject.getString("path"), jsonObject.getString("name"));
                            Log.i("TAG", "run: 2103 return "+result2103);
//                            jobj = new JsonObject();
//                            jobj.addProperty("namespace", "2103");
//                            jobj.addProperty("targetId", jsonObject.getString("targetId"));
//                            jobj.addProperty("detail", "");
//                            jobj.addProperty("content", "");
//                            if (result2003) {
//                                jobj.addProperty("status", "200"); // or 403 FORBIDDEN
//                                jobj.addProperty("message", "OK");
//                            } else {
//                                jobj.addProperty("status", "403");
//                                jobj.addProperty("message", "FORBIDDEN");
//                            }
//                            json = gson.toJson(jobj);
//                            write(json);
                            break;

                        case 7100: // 파일 스텟 확인

                            /**
                             * TODO 파일 스텟 로직
                             */

                            int size = jsonObject.getInt("size");
                            String ext = jsonObject.getString("ext");
                            String path7100 = jsonObject.getString("path");
                            String name7100 = jsonObject.getString("name");

                            File file = new File(path7100 + "/" + name7100 + ext);

                            // 1. 파일이 이미 있는지 확인한다
                            if (file.length() == size) {
                                System.out.println("이미 파일이 있습니다.");
                                fs = new FileStat(name7100, path7100, ext, 0, 0);
                                jobj = new JsonObject();
                                jobj.addProperty("namespace", "7004");
                                jobj.addProperty("targetId", jsonObject.getString("targetId"));
                                jobj.addProperty("status", "400"); // or 403 FORBIDDEN
                                jobj.addProperty("message", "BAD REQUEST");
                                jobj.addProperty("detail", "");
                                jobj.addProperty("content", "이미 파일이 있습니다.");
                                json = gson.toJson(jobj);
                                write(json);
                                break;
                            } else {

                                long tmpfileSize = 0;
                                if (file.exists()) {
                                    System.out.println("이어받기 로직을 수행합니다.");
                                    tmpfileSize = file.length();
                                    System.out.println("현재 청크 사이즈 : " + tmpfileSize);
                                }

                                fs = new FileStat(name7100, path7100, ext, size, tmpfileSize);
                                // 새로운 TCP 연결 시도
                                socketRepository.getSocketFile(fs);
                                downloadNotification = new DownloadNotification(context,name7100,path7100);
                            }
                            break;

                        case 7001: // 파일 전송

                            /**
                             * TODO 퍼센트 로직
                             */

                            int percentage = jsonObject.getInt("percent");
                            downloadNotification.startNotification(percentage);
//                            Log.i("myTag", "percentage: " + percentage);
                            break;


                        case 7200: // 파일 전송을 위한 TCP 연결이 완료되었다.
                            System.out.println("fs size : " + fs.getSize());
                            if (fs.getSize() == 0) break;
                            jobj = new JsonObject();
                            jobj.addProperty("namespace", "7100");
                            jobj.addProperty("tmpfileSize", fs.getTmpfileSize());
                            jobj.addProperty("size", fs.getSize());
                            json = gson.toJson(jobj);
                            write(json);
                            break;
                        /**
                         * Android
                         * 8100
                         * 설명 : 안드로이드가 파일을 업로드한다.
                         * 메시지 :
                         */
                        case 8100:
                            String name2 = jsonObject.getString("name");
                            String path2 = jsonObject.getString("path");
                            String ext2 = jsonObject.getString("ext");
                            File file2 = new File(path2 + "/" + name2 + ext2);
                            fs = new FileStat(name2, path2, ext2, file2.length(), 0);
                            targetId = jsonObject.getString("targetId");

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

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                //사용자의 의도에 의한 종료가 아닐 경우
                if (!closeSocketByUser)
                    socketRepository.failSocketConnection();

                Log.i("TAG", "run: socketStop");
            }

        }
    };

}
