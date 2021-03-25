package com.example.android.data.viewmodelimpl;

import android.app.Activity;
import android.os.Environment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.android.data.model.dto.Event;
import com.example.android.data.viewmodel.SendViewModel;
import com.example.android.ui.send.ShareFragment;
import com.example.android.ui.user.CheckEmailFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class SendViewModelImpl extends ViewModel implements SendViewModel {

    private WeakReference<Activity> mActivityRef;


    private MutableLiveData<String> folderPathLiveData = new MutableLiveData<>("");
    private MutableLiveData<Event<String>> switchFragment = new MutableLiveData<>(new Event<>(""));
    private MutableLiveData<Boolean> canShareLiveData = new MutableLiveData<>(false);

    private String mRoot = Environment.getExternalStorageDirectory().getAbsolutePath();


    @Override
    public void setParentContext(Activity parentContext) {
        this.mActivityRef = new WeakReference<>(parentContext);
    }
    @Override
    public void switchPage(String page){
        switchFragment.setValue(new Event<>(page));
    }
    @Override
    public void startShare(){
        ShareFragment shareFragment = ShareFragment.newInstance();
        shareFragment.show(mActivityRef.get().getFragmentManager(), "START_SHARE");
    }

    //prepare
    //선택 폴더 경로 삭제
    @Override
    public void deleteSelectedFolderPath(){
        folderPathLiveData.setValue("");
    }



    @Override
    public void setFolderPathLiveData(MutableLiveData<String> folderPath) {
        this.folderPathLiveData = folderPath;
    }
    @Override
    public MutableLiveData<String> getFolderPathLiveData() {
        return folderPathLiveData;
    }
    @Override
    public MutableLiveData<Event<String>> getSwitchFragment() {
        return switchFragment;
    }
    @Override
    public void setSwitchFragment(MutableLiveData<Event<String>> switchFragment) {
        this.switchFragment = switchFragment;
    }
    @Override
    public MutableLiveData<Boolean> getCanShareLiveData() {
        return canShareLiveData;
    }
    @Override
    public void setCanShareLiveData(MutableLiveData<Boolean> canShareLiveData) {
        this.canShareLiveData = canShareLiveData;
    }
}
