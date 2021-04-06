package com.example.android.data.viewmodel;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

/*
SettingViewModel : 계정 설정과 관련된 데이터를 관리하는 ViewModel
 */
public interface SettingViewModel{

    void setParentContext(Activity parentContext);
    void setGoogleLoginExecutor(GoogleLoginExecutor executor);
    void onRequestedSignOut();
    void onRequestedRevokeAccess();
    void getMemberInformation();

    void editImage();
    void openEditPasswordDialog();
    void editPassword();
    void closeEditPasswordDialog();

    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);

    void setMemberNameLiveData(MutableLiveData<String> memberNameLiveData);
    MutableLiveData<String> getMemberNameLiveData();
    void setMemberEmailLiveData(MutableLiveData<String> memberEmailLiveData);
    MutableLiveData<String> getMemberEmailLiveData();
    void setCurrentPasswordLiveData(MutableLiveData<String> currentPasswordLiveData);
    MutableLiveData<String> getCurrentPasswordLiveData();
    void setNewPasswordLiveData(MutableLiveData<String> newPasswordLiveData);
    MutableLiveData<String> getNewPasswordLiveData();
    void setNewCheckPasswordLiveData(MutableLiveData<String> newCheckPasswordLiveData);
    MutableLiveData<String> getNewCheckPasswordLiveData();
    void setIsOKNewCheckPassword(MutableLiveData<Boolean> isOKNewCheckPassword);
    MutableLiveData<Boolean> getIsOKNewCheckPassword();
    void setIsOKNewPassword(MutableLiveData<Boolean> isOKNewPassword);
    MutableLiveData<Boolean> getIsOKNewPassword();
}

