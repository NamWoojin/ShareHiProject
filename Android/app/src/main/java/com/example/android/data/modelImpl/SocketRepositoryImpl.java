package com.example.android.data.modelImpl;

import android.Manifest;
import android.app.Activity;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.example.android.data.connection.SocketDownload;
import com.example.android.data.connection.SocketInfo;
import com.example.android.data.connection.dto.FileStat;
import com.example.android.data.model.SocketRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;

/*
SocketRepositoryImpl : 소켓통신과 요청에 따른 연산을 수행하는 SocketRepository의 구현체
 */
public class SocketRepositoryImpl implements SocketRepository {

    private final int PERMISSIONS_REQUEST_CODE = 1;

    private String rootPath;
    private String name;

    private WeakReference<Activity> mActivityRef;

    private MutableLiveData<String> isConnecting = new MutableLiveData<>();

    private SocketInfo socketInfo;


    @Override
    public void setParentContext(Activity parentContext) {
        this.mActivityRef = new WeakReference<>(parentContext);
    }

    //Socket 시작
    @Override
    public void startSocket(String path, String name) {
        rootPath = path;
        this.name = name;
        socketInfo = new SocketInfo(this);
        SocketStartThread sst = new SocketStartThread();
        sst.start();
    }

    //소켓 연결 시도
    class SocketStartThread extends Thread {
        @Override
        public void run() {
            super.run();
            socketInfo.connect(name);
        }
    }

    //소켓 연결 결과 처리
    @Override
    public void successSocketConnection() {
        mActivityRef.get().runOnUiThread(() -> {
            isConnecting.setValue("successConnect");
        });
    }

    @Override
    public void failSocketConnection() {
        mActivityRef.get().runOnUiThread(() -> {
            isConnecting.setValue("failConnect");
        });
    }

    @Override
    public void successSocketClosed() {
        mActivityRef.get().runOnUiThread(() -> {
            isConnecting.setValue("successClose");
        });
    }

    @Override
    public void failSocketClosed() {
        mActivityRef.get().runOnUiThread(() -> {
            isConnecting.setValue("failClose");
        });
    }

    @Override
    public void stopSocket() {
        socketInfo.disConnect();
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }


    //폴더 디렉토리 JSONObject
    @Override
    public JSONObject getFolderDirectory(String path) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            File f = new File(path);
            File[] files = f.listFiles();

            jsonObject.put("name", f.getName());
            jsonObject.put("path", f.getAbsolutePath());

            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                JSONObject jo = new JSONObject();
                String fileName = file.getName();
                jo.put("name", fileName);
                jo.put("path", file.getAbsolutePath());
                if (file.isDirectory()) {
                    jo.put("type", "folder");
                } else {
                    //확장자
                    jo.put("type", fileName.substring(fileName.lastIndexOf(".") + 1));
                }
                jsonArray.put(jo);
            }
            jsonObject.put("directory", jsonArray);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    //파일명 변경
    @Override
    public boolean changeFolderName(String path, String prevName, String newName) {
        File filePre = new File(path, prevName);
        File fileNow = new File(path, newName);
        return filePre.renameTo(fileNow);
    }

    //폴더 삭제
    @Override
    public boolean deleteFolder(String path, String name) {
        return deleteFolderwithChild(path + "/" + name);
    }

    //폴더의 하위 파일 전부 삭제
    private boolean deleteFolderwithChild(String path) {
        boolean result = true;
        File dir = new File(path);
        File[] childFileList = dir.listFiles();
        if (dir.exists()) {
            if (childFileList != null) {
                for (File childFile : childFileList) {
                    if (childFile.isDirectory()) {
                        deleteFolderwithChild(childFile.getAbsolutePath()); //하위 디렉토리
                    } else {
                        childFile.delete(); //하위 파일
                    }
                }
            }
            result = dir.delete();
        }
        return result;
    }

    //폴더 생성
    @Override
    public boolean createFolder(String path, String folderName) {
        boolean result = false;
        boolean shouldProviceRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(mActivityRef.get(), Manifest.permission.READ_EXTERNAL_STORAGE);//사용자가 이전에 거절한적이 있어도 true 반환

        if (shouldProviceRationale) {
            //앱에 필요한 권한이 없어서 권한 요청
            ActivityCompat.requestPermissions(mActivityRef.get(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
        } else {
            //권한있을때.
            //오레오부터 꼭 권한체크내에서 파일 만들어줘야함
            File dir = new File(path + "/" + folderName);
            if (!dir.exists()) {
                result = dir.mkdir();
            }
        }
        return result;
    }

    //안드로이드 파일 다운로드 시작
    @Override
    public boolean getSocketFile(FileStat fs) {
        boolean shouldProviceRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(mActivityRef.get(), Manifest.permission.READ_EXTERNAL_STORAGE);//사용자가 이전에 거절한적이 있어도 true 반환
        Log.i("TAG", "getFile: " + shouldProviceRationale);
        if (shouldProviceRationale) {
            return false;
        } else {
            SocketDownload sd = new SocketDownload(fs);
            sd.connect(mActivityRef.get());
            return true;
        }
    }

    //LiveData getter & setter
    @Override
    public MutableLiveData<String> getIsConnecting() {
        return isConnecting;
    }

    @Override
    public void setIsConnecting(MutableLiveData<String> isConnecting) {
        this.isConnecting = isConnecting;
    }
}
