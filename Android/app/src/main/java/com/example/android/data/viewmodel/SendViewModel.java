package com.example.android.data.viewmodel;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.data.model.dto.Event;

import java.util.ArrayList;

public interface SendViewModel {
    void setParentContext(Activity parentContext);

    void switchPage(String page);
    void startShare();

    //prepare
    void deleteSelectedFolderPath();

    void setFolderPathLiveData(MutableLiveData<String> folderPath);
    MutableLiveData<String> getFolderPathLiveData();
    void setSwitchFragment(MutableLiveData<Event<String>> switchFragment);
    MutableLiveData<Event<String>> getSwitchFragment();
    void setCanShareLiveData(MutableLiveData<Boolean> canShareLiveData);
    MutableLiveData<Boolean> getCanShareLiveData();
}
