package com.example.android.data.viewmodelimpl;

import android.app.Activity;
import android.os.Environment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.data.viewmodel.SendViewModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class SendViewModelImpl extends ViewModel implements SendViewModel {

    private WeakReference<Activity> mActivityRef;

    private MutableLiveData<String> folderPathLiveData = new MutableLiveData<>("");

    private String mRoot = Environment.getExternalStorageDirectory().getAbsolutePath();


    @Override
    public void setParentContext(Activity parentContext) {
        this.mActivityRef = new WeakReference<>(parentContext);
    }

    @Override
    public void setFolderPathLiveData(MutableLiveData<String> folderPath) {
        this.folderPathLiveData = folderPath;
    }

    @Override
    public MutableLiveData<String> getFolderPathLiveData() {
        return folderPathLiveData;
    }


}
