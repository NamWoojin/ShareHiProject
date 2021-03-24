package com.example.android.data.viewmodel;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;

public interface BackdropViewModel {
    void setParentContext(Activity parentContext);

    void setPageLiveData(MutableLiveData<String> pageLiveData);
    void toggleBackdropMenu();
    void changePage(String page);

    MutableLiveData<String> getPageLiveData();
    MutableLiveData<Boolean> getBackdropMenuOpenLiveData();
    void setBackdropMenuOpenLiveData(MutableLiveData<Boolean> backdropMenuOpenLiveData);
}
