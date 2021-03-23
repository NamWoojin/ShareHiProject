package com.example.android.data.viewmodel;

import android.app.Activity;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;

import com.example.android.data.model.dto.Event;

public interface SignUpViewModel{

    void setParentContext(Activity parentContext);
    void setGoogleLoginExecutor(GoogleLoginExecutor executor);

    //Event LiveData
    MutableLiveData<Event<Boolean>> getClickOKLiveData();
    MutableLiveData<Event<Boolean>> getClickCancelLiveData();
    void setClickOKLiveData(MutableLiveData<Event<Boolean>> clickOKLiveData);
    void setClickCancelLiveData(MutableLiveData<Event<Boolean>> clickCancelLiveData);
    MutableLiveData<Event<Integer>> getGoCheckEmailLiveData();
    MutableLiveData<Event<Boolean>> getLoginSuccessLiveData();
    void setGoCheckEmailLiveData(MutableLiveData<Event<Integer>> goCheckEmailLiveData);
    void setLoginSuccessLiveData(MutableLiveData<Event<Boolean>> loginSuccessLiveData);

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
    void onRenderCheckEmail();
    void onRequestedSignUp();
    void onRequestedSignIn();
    void onRequestedGoogleSignIn();

    //이메일 인증
    MutableLiveData<String> getCheckEmailLiveData();
    void setCheckEmailLiveData(MutableLiveData<String> checkEmailLiveData);
    MutableLiveData<String> getInfoLiveData();
    void setInfoLiveData(MutableLiveData<String> infoLiveData);
    MutableLiveData<String> getTimeLiveData();
    void setTimeLiveData(MutableLiveData<String> timeLiveData);
    void checkEmailAuth();
    void closeEmailAuth();


    void onActivityResult(int requestCode, int resultCode, Intent data);
}
