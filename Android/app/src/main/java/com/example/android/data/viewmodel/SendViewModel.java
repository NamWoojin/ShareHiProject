package com.example.android.data.viewmodel;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.data.model.SocketRepository;
import com.example.android.data.model.dto.Event;
import com.example.android.data.model.dto.Folder;
import com.example.android.ui.send.FolderRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public interface SendViewModel {
    void setParentContext(Activity parentContext);
    void setSocketRepository(SocketRepository repository,Activity parentContext);

    void switchPage(String page);

    //share
    void stopShare();

    //prepare
    void startShare();
    void deleteSelectedFolderPath();

    //folder
    void clickFolderList(int pos);
    FolderRecyclerAdapter getFolderRecyclerAdapter();
    List<Folder> getFolderItems();
    int getType(int pos);
    String getName(int pos);
    void choiceFolderPath();
    void createFolder();
    void createFolderFragmentOpen();
    void createFolderFragmentClose();

    void setFolderPathLiveData(MutableLiveData<String> folderPath);
    MutableLiveData<String> getFolderPathLiveData();
    void setCanShareLiveData(MutableLiveData<Boolean> canShareLiveData);
    MutableLiveData<Boolean> getCanShareLiveData();
    void setFolderTitleLiveData(MutableLiveData<String> folderTitleLiveData);
    MutableLiveData<String> getFolderTitleLiveData();
    void setSelectedPathLiveData(MutableLiveData<String> selectedPathLiveData);
    MutableLiveData<String> getSelectedPathLiveData();
    void setNewFolderNameLiveData(MutableLiveData<String> newFolderNameLiveData);
    MutableLiveData<String> getNewFolderNameLiveData();
}
