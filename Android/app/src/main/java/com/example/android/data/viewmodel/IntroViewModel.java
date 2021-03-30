package com.example.android.data.viewmodel;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.android.data.model.dto.Event;

public interface IntroViewModel {
    void getAdID();
    void setParentContext(Activity parentContext);
    void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults);
    void setCheckAutoLoginLiveData(MutableLiveData<Event<Boolean>> checkAutoLoginLiveData);
    MutableLiveData<Event<Boolean>> getCheckAutoLoginLiveData();
}
