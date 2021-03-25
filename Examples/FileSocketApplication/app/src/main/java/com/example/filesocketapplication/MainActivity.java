package com.example.filesocketapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private final int WRITE_EXTERNAL_STORAGE = 1;

    Button directoryButton;
    Button fileButton;

    //루트 파일 경로
    private String mRoot = Environment.getExternalStorageDirectory().getAbsolutePath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        directoryButton = findViewById(R.id.send_directory_button);
        fileButton = findViewById(R.id.send_file_button);

        //디렉토리 전송
        directoryButton.setOnClickListener(v -> {
            JSONObject jsonObject = null;
            try {
                jsonObject = makeDirectoryJson(mRoot);
                Log.i("TAG", "onCreate: "+jsonObject.toString());
                DirectorySocket directorySocket = new DirectorySocket(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        //파일 전송
        fileButton.setOnClickListener(v -> {
            FileSocket fileSocket = new FileSocket();
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //파일 접근 권한 허용되지 않았을 때
            //권한 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
        } else {
            //권한 허용되어 있을 때

        }
    }

    //권한 승인 결과에 따라서 실행
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 승인

                } else {
                    exitProgram();
                }
                return;
            }

        }
    }

    //directory 정보 만들기
    private JSONObject makeDirectoryJson(String dirPath) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        File f = new File(dirPath);
        File[] files = f.listFiles();

        jsonObject.put("name",f.getName());
        jsonObject.put("path",f.getAbsolutePath());

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            JSONObject jo = new JSONObject();
            String fileName = file.getName();
            jo.put("name", fileName);
            jo.put("path", file.getAbsolutePath());
            if (file.isDirectory()) {
                jo.put("type","folder");
            } else {
                //확장자
                jo.put("type", fileName.substring(fileName.lastIndexOf(".")+1));
            }
            jsonArray.put(jo);
        }
        jsonObject.put("directory",jsonArray);

        return jsonObject;
    }

    //어플리케이션 종료
    private void exitProgram() {
        // 태스크를 백그라운드로 이동
        moveTaskToBack(true);

        if (Build.VERSION.SDK_INT >= 21) {
            // 액티비티 종료 + 태스크 리스트에서 지우기
            finishAndRemoveTask();
        } else {
            // 액티비티 종료
            finish();
        }
        System.exit(0);
    }
}