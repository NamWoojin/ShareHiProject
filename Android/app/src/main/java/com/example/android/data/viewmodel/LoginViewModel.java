package com.example.android.data.viewmodel;

import android.app.Activity;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;

import com.example.android.data.model.dto.LoginDTO;
import com.example.android.data.model.dto.SignUpDTO;

public interface LoginViewModel {
    MutableLiveData<String> getEmailLivedata();
    void setEmailLivedata(MutableLiveData<String> emailLivedata);
    MutableLiveData<String> getPasswordLivedata();
    void setPasswordLivedata(MutableLiveData<String> passwordLivedata);
    void setParentContext(Activity parentContext);
    void setGoogleLoginExecutor(GoogleLoginExecutor executor);
    void onActivityResult(int requestCode, int resultCode, Intent data);
    void onRequestedSignIn();
    void onRequestedGoogleSignIn();
    void onRenderSignUp();
    void onMoveFindPassword();
}
