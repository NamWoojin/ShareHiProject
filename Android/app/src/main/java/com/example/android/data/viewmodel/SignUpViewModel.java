package com.example.android.data.viewmodel;

import android.app.Activity;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;

import com.example.android.data.model.dto.LoginDTO;
import com.example.android.data.model.dto.SignUpDTO;

public interface SignUpViewModel{
    //LiveData Getter & Setter
    MutableLiveData<String> getNameLiveData();
    void setNameLiveData(MutableLiveData<String> nameLiveData);
    MutableLiveData<String> getEmailLiveData();
    void setEmailLiveData(MutableLiveData<String> emailLiveData);
    MutableLiveData<String> getPasswordLiveData();
    void setPasswordLiveData(MutableLiveData<String> passwordLiveData);
    MutableLiveData<String> getCheckPasswordLiveData();
    void setCheckPasswordLiveData(MutableLiveData<String> checkPasswordLiveData);
    MutableLiveData<Boolean> getIsOKName();
    void setIsOKName(MutableLiveData<Boolean> isOKName);
    MutableLiveData<Boolean> getIsOKEmail();
    void setIsOKEmail(MutableLiveData<Boolean> isOKEmail);
    MutableLiveData<Boolean> getIsOKPassword();
    void setIsOKPassword(MutableLiveData<Boolean> isOKPassword);
    MutableLiveData<Boolean> getIsOKCheckPassword();
    void setIsOKCheckPassword(MutableLiveData<Boolean> isOKCheckPassword);
    MutableLiveData<Boolean> getIsOKCheckEmail();
    void setIsOKCheckEmail(MutableLiveData<Boolean> isOKCheckEmail);

    void setParentContext(Activity parentContext);
    void setGoogleLoginExecutor(GoogleLoginExecutor executor);
    int canSignUp();
    void onActivityResult(int requestCode, int resultCode, Intent data);
    void onRequestedGoogleSignIn();
    void onRenderCheckEmail();
    void onRequestedSignIn();
    void onRequestedSignUp();
}
