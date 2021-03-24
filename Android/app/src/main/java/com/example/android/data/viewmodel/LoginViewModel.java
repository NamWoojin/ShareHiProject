package com.example.android.data.viewmodel;

import android.app.Activity;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;

import com.example.android.data.model.dto.Event;

public interface LoginViewModel {
    MutableLiveData<String> getEmailLivedata();
    void setEmailLivedata(MutableLiveData<String> emailLivedata);
    MutableLiveData<String> getPasswordLivedata();
    void setPasswordLivedata(MutableLiveData<String> passwordLivedata);
    MutableLiveData<Event<Boolean>> getLoginSuccessLiveData();
    void setLoginSuccessLiveData(MutableLiveData<Event<Boolean>> signUpLiveData);
    MutableLiveData<Boolean> getLoadingLiveData();
    void setLoadingLiveData(MutableLiveData<Boolean> loadingLiveData);
    void setParentContext(Activity parentContext);
    void setGoogleLoginExecutor(GoogleLoginExecutor executor);
    void onActivityResult(int requestCode, int resultCode, Intent data);
    void onRequestedSignIn();
    void onRequestedGoogleSignIn();
    void onRenderSignUp();
    void onMoveFindPassword();
}
