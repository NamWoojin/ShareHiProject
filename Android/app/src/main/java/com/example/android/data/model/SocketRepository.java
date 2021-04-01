package com.example.android.data.model;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

import java.io.IOException;

public interface SocketRepository {
    void setParentContext(Activity parentContext);
    void startSocket(String path);
    void abruptSocketClosed();
    void stopSocket() throws IOException;
    JSONObject getFolderDirectory(String path);
    boolean changeFolderName(String path, String prevName, String newName);
    boolean deleteFolder(String path,String name);
    boolean createFolder(String path,String folderName);
    void setIsConnecting(MutableLiveData<Boolean> isConnecting);
    MutableLiveData<Boolean> getIsConnecting();
}