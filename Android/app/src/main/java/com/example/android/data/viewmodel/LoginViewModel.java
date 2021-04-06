package com.example.android.data.viewmodel;

import android.app.Activity;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;

/*
LoginViewModel : 로그인과 관련된 데이터를 관리하는 ViewModel
 */
public interface LoginViewModel {
    void setParentContext(Activity parentContext);
    void getAdID();
    void setGoogleLoginExecutor(GoogleLoginExecutor executor);
    void onActivityResult(int requestCode, int resultCode, Intent data);
    void onRequestedSignIn();
    void onRequestedGoogleSignIn();
    void onRenderSignUp();
    void onMoveFindPassword();

    MutableLiveData<String> getEmailLivedata();
    void setEmailLivedata(MutableLiveData<String> emailLivedata);
    MutableLiveData<String> getPasswordLivedata();
    void setPasswordLivedata(MutableLiveData<String> passwordLivedata);
    MutableLiveData<Boolean> getLoadingLiveData();
    void setLoadingLiveData(MutableLiveData<Boolean> loadingLiveData);
}
