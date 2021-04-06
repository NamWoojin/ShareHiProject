package com.example.android.data.viewmodel;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.android.data.model.dto.Event;

/*
IntroViewModel : 인트로에서의 동작을 수행하는 ViewModel
 */
public interface IntroViewModel {
    void getAdID();
    void setParentContext(Activity parentContext);
    void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults);
    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);

    void setCheckAutoLoginLiveData(MutableLiveData<Event<Boolean>> checkAutoLoginLiveData);
    MutableLiveData<Event<Boolean>> getCheckAutoLoginLiveData();
}
