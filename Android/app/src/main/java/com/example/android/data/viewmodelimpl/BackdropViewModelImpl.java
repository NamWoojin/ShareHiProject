package com.example.android.data.viewmodelimpl;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.example.android.data.viewmodel.BackdropViewModel;

import java.lang.ref.WeakReference;

/*
BackdropViewModelImpl : Backdrop에서의 데이터를 관리하는 ViewModel
 */
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

    //backdrop 여닫는 toggle
    @Override
    public void toggleBackdropMenu() {
        backdropMenuOpenLiveData.setValue(!backdropMenuOpenLiveData.getValue());
    }

    //화면 전환
    @Override
    public void changePage(String page) {
        if(backdropMenuOpenLiveData.getValue()) {
            pageLiveData.setValue(page);
            backdropMenuOpenLiveData.setValue(false);
        }
    }

    //LiveData getter & setter
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
