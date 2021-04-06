package com.example.android.data.viewmodel;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;

/*
BackdropViewModel : Backdrop에서의 데이터를 관리하는 ViewModel
 */
public interface BackdropViewModel {
    void setParentContext(Activity parentContext);

    void toggleBackdropMenu();
    void changePage(String page);

    MutableLiveData<String> getPageLiveData();
    void setPageLiveData(MutableLiveData<String> pageLiveData);
    MutableLiveData<Boolean> getBackdropMenuOpenLiveData();
    void setBackdropMenuOpenLiveData(MutableLiveData<Boolean> backdropMenuOpenLiveData);
}
