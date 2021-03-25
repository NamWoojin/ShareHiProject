package com.example.android.data.viewmodelimpl;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.android.data.viewmodel.BackdropViewModel;

import java.lang.ref.WeakReference;

public class BackdropViewModelImpl extends ViewModel implements BackdropViewModel {

    private static final String TAG = "BackdropViewModelImpl";

    private WeakReference<Activity> mActivityRef;

    private MutableLiveData<String> pageLiveData = new MutableLiveData<>("");
    private MutableLiveData<Boolean> backdropMenuOpenLiveData = new MutableLiveData<>(false);

    public BackdropViewModelImpl(SavedStateHandle handle) {
        pageLiveData = handle.getLiveData("page");
    }

    @Override
    public void setParentContext(Activity parentContext) {
        this.mActivityRef = new WeakReference<>(parentContext);
    }

    @Override
    public void toggleBackdropMenu() {
        backdropMenuOpenLiveData.setValue(!backdropMenuOpenLiveData.getValue());
    }

    @Override
    public void changePage(String page) {
        pageLiveData.setValue(page);
        backdropMenuOpenLiveData.setValue(false);
    }

    @Override
    public MutableLiveData<String> getPageLiveData() {
        return pageLiveData;
    }

    @Override
    public void setPageLiveData(MutableLiveData<String> pageLiveData) {
        this.pageLiveData = pageLiveData;
    }

    @Override
    public MutableLiveData<Boolean> getBackdropMenuOpenLiveData() {
        return backdropMenuOpenLiveData;
    }

    @Override
    public void setBackdropMenuOpenLiveData(MutableLiveData<Boolean> backdropMenuOpenLiveData) {
        this.backdropMenuOpenLiveData = backdropMenuOpenLiveData;
    }
}
