package com.example.android.data.viewmodel;

import android.app.Activity;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;

import com.example.android.data.model.entity.User;

public interface LoginViewModel {
    MutableLiveData<User> getuserLiveData();
    void setParentContext(Activity parentContext);
    void setGoogleLoginExecutor(GoogleLoginExecutor executor);
    void onActivityResult(int requestCode, int resultCode, Intent data);
    void onRequestedSignIn(User user);
    void onRequestedGoogleSignIn();
    void onRenderSignUp();
    void onMoveFindPassword();
}
