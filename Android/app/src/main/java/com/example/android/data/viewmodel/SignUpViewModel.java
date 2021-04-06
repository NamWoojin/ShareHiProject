package com.example.android.data.viewmodel;

import android.app.Activity;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;

/*
SignUpViewModel : 회원가입과 관련된 데이터를 관리하는 ViewModel
 */
public interface SignUpViewModel{

    void setParentContext(Activity parentContext);
    void setGoogleLoginExecutor(GoogleLoginExecutor executor);
    void getAdID();

    MutableLiveData<Boolean> getLoadingLiveData();
    void setLoadingLiveData(MutableLiveData<Boolean> loadingLiveData);

    //회원가입
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

    int canSignUp();
    void onRequestedSignUp();
    void onRequestedSignIn();
    void onRequestedGoogleSignIn();
    void checkEmailDuplicate();

    //이메일 인증
    MutableLiveData<String> getCheckEmailLiveData();
    void setCheckEmailLiveData(MutableLiveData<String> checkEmailLiveData);
    void checkEmailAuth();
    void closeEmailAuth();


    void onActivityResult(int requestCode, int resultCode, Intent data);

}
