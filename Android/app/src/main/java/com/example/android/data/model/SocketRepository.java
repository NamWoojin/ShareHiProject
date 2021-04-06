package com.example.android.data.model;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;

import com.example.android.data.connection.dto.FileStat;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public interface SocketRepository {
    void setParentContext(Activity parentContext);
    void startSocket(String path, String name);
    String getRootPath();
    void successSocketConnection();
    void failSocketConnection();
    void successSocketClosed();
    void failSocketClosed();
    void stopSocket();
    boolean getSocketFile(FileStat fs);
    JSONObject getFolderDirectory(String path);
    boolean changeFolderName(String path, String prevName, String newName);
    boolean deleteFolder(String path,String name);
    boolean createFolder(String path,String folderName);
    void setIsConnecting(MutableLiveData<String> isConnecting);
    MutableLiveData<String> getIsConnecting();
}
