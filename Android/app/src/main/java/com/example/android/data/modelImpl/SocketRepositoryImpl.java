package com.example.android.data.modelImpl;

import android.Manifest;
import android.app.Activity;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.android.data.connection.SocketInfo;
import com.example.android.data.model.SocketRepository;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class SocketRepositoryImpl implements SocketRepository {

    private final int PERMISSIONS_REQUEST_CODE = 1;

    private WeakReference<Activity> mActivityRef;

    private SocketInfo socketInfo;

    //Socket 시작
    @Override
    public void startSocket(String path) {
        socketInfo = new SocketInfo(this);
        SocketStartThread sst = new SocketStartThread();
        sst.start();
    }

    class SocketStartThread extends Thread{
        @Override
        public void run() {
            super.run();
            //Ad ID 발급
            AdvertisingIdClient.Info idInfo = null;
            try {
                idInfo = AdvertisingIdClient.getAdvertisingIdInfo(mActivityRef.get().getApplicationContext());
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try{
                String advertId = idInfo.getId();
                Log.i("TAG", "run: "+advertId);
                //Socket 실행
                socketInfo.connect(advertId);

            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stopSocket() throws IOException {
        socketInfo.disConnect();
    }

    @Override
    public void setParentContext(Activity parentContext) {
        this.mActivityRef = new WeakReference<>(parentContext);
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
    public boolean deleteFolder(String path,String name) {
        return deleteFolderwithChild(path+"/"+name);
    }

    private boolean deleteFolderwithChild(String path){
        boolean result = true;
        File dir = new File(path);
        File[] childFileList = dir.listFiles();
        if (dir.exists()) {
            for (File childFile : childFileList) {
                if (childFile.isDirectory()) {
                    deleteFolderwithChild(childFile.getAbsolutePath()); //하위 디렉토리
                } else {
                    childFile.delete(); //하위 파일
                }
            }
            result = dir.delete();
        }
        return result;
    }

    //폴더 생성
    @Override
    public boolean createFolder(String path,String folderName) {
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
            File dir = new File(path +"/"+ folderName);
            if (!dir.exists()) {
                result = dir.mkdir();
            }
        }
        return result;
    }
}
