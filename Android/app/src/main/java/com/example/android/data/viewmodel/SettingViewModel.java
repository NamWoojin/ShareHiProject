package com.example.android.data.viewmodel;

import android.app.Activity;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;

import com.example.android.data.model.dto.Member;


public interface SettingViewModel{

    void setParentContext(Activity parentContext);
    void setGoogleLoginExecutor(GoogleLoginExecutor executor);
    void onRequestedSignOut();
    void onRequestedRevokeAccess();
    void getMemberInformation();

    void setMemberLiveData(MutableLiveData<Member> memberLiveData);
    MutableLiveData<Member> getMemberLiveData();
}
