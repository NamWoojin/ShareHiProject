package com.example.android.data.viewmodel;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;

import com.example.android.data.model.dto.Event;

public interface IntroViewModel {
    void getPermissionAndLogin();
    void setParentContext(Activity parentContext);
    void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults);
    void setCheckAutoLoginLiveData(MutableLiveData<Event<Boolean>> checkAutoLoginLiveData);
    MutableLiveData<Event<Boolean>> getCheckAutoLoginLiveData();
}
