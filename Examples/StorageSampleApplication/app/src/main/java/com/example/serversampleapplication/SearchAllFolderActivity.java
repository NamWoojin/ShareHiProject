package com.example.serversampleapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SearchAllFolderActivity extends AppCompatActivity {
    private String mFileName;
    private ListView lvFileControl;
    private Context mContext = this;

    private List<String> lItem = null;
    private List<String> lPath = null;
    private String mRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
    private TextView mPath;

    private JSONObject jsonObject = new JSONObject();
    private JSONObject currentJsonObject = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_all_folder);
        mPath = (TextView) findViewById(R.id.tvPath);
        lvFileControl = (ListView) findViewById(R.id.lvFileControl);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //권한 허용되지 않았으면
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        //파일 전체 읽기
        try {
            jsonObject.put("name", "root");
            jsonObject.put("path", mRoot);
            getAllDir(jsonObject, mRoot);


            File file = new File(mRoot); //path는 파일의 경로를 가리키는 문자열이다.
            File root = android.os.Environment.getExternalStorageDirectory();
            String path = root.getAbsolutePath() + "/hello.txt";
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file, true);
                fos.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
                // fos.flush(); // flush()를 사용해서 버퍼에 있는 내용을 즉시 쓸 수 있다.
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            makeItemList(jsonObject);
            Log.i("TAGG", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        lvFileControl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                File file = new File(lPath.get(position));
//                if (file.isDirectory()) {
//                    if (file.canRead())
//                        getDir(lPath.get(position));
//                    else {
//                        Toast.makeText(mContext, "No files in this folder.", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    mFileName = file.getName();
//                    Log.i("Test", "ext:" + mFileName.substring(mFileName.lastIndexOf('.') + 1, mFileName.length()));
//                }
//            }
//        });
    }

    private void getAllDir(JSONObject jsonObject, String dirPath) throws JSONException {
        //파일 목록 불러오기
        File f = new File(dirPath);
        File[] files = f.listFiles();

        JSONArray jsonArray = new JSONArray();

        if (!dirPath.equals(mRoot)) {
            JSONObject jo = new JSONObject();
            jo.put("name", "../");
            jo.put("path", f.getParent());
            jsonArray.put(jo);
        }

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            JSONObject jo = new JSONObject();
            jo.put("path", file.getAbsolutePath());
            if (file.isDirectory()) {
                jo.put("name", file.getName() + "/");
                getAllDir(jo, file.getAbsolutePath());
            } else {
                jo.put("name", file.getName());
            }
            jsonArray.put(jo);
        }
        jsonObject.put("folder", jsonArray);
    }

    private void makeItemList(JSONObject jsonObject) throws JSONException {
//        mPath.setText(jsonObject.toString());
        mPath.setText("Location: " + jsonObject.get("path"));
        lItem = new ArrayList<String>();

        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lItem);
        lvFileControl.setAdapter(fileList);
    }


//    private void getDir(String dirPath) {
//        mPath.setText("Location: " + dirPath);
//        lItem = new ArrayList<String>();
//        lPath = new ArrayList<String>();
//
//        File f = new File(dirPath);
//        File[] files = f.listFiles();
//
//        if (!dirPath.equals(mRoot)) {
//            //item.add(root); //to root.
//            //path.add(root);
//            lItem.add("../"); //to parent folder
//            lPath.add(f.getParent());
//        }
//
//        for (int i = 0; i < files.length; i++) {
//            File file = files[i];
//            lPath.add(file.getAbsolutePath());
//
//            if (file.isDirectory())
//                lItem.add(file.getName() + "/");
//            else
//                lItem.add(file.getName());
//        }
//        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lItem);
//        lvFileControl.setAdapter(fileList);
//    }
}