package com.example.android.data.model;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;

import com.example.android.data.connection.dto.FileStat;

import org.json.JSONObject;

/*
SocketRepository : 소켓통신과 요청에 따른 연산을 수행하는 Repository
 */
public interface SocketRepository {
    void setParentContext(Activity parentContext);
    void startSocket(String path, String name);

    void successSocketConnection();
    void failSocketConnection();
    void successSocketClosed();
    void failSocketClosed();

    void stopSocket();

    String getRootPath();
    boolean getSocketFile(FileStat fs);
    JSONObject getFolderDirectory(String path);
    boolean changeFolderName(String path, String prevName, String newName);
    boolean deleteFolder(String path,String name);
    boolean createFolder(String path,String folderName);

    void setIsConnecting(MutableLiveData<String> isConnecting);
    MutableLiveData<String> getIsConnecting();
}
