package com.example.android.data.view;

import com.example.android.data.view.action.OnRenderToastAction;
import com.example.android.data.view.action.OnRequestSignInAction;
import com.example.android.data.view.action.OnRequestUserSettingAction;

public interface SettingView {
    //사용할 ActionListener 초기화
    void setActionListener(SettingView.ActionListener actionListener);
    interface ActionListener extends OnRequestUserSettingAction, OnRenderToastAction {
    }
}
