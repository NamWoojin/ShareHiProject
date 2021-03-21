package com.example.android.ui.view;

import com.example.android.ui.view.action.OnRenderToastAction;
import com.example.android.ui.view.action.OnRequestUserSettingAction;

public interface SettingView {
    //사용할 ActionListener 초기화
    void setActionListener(SettingView.ActionListener actionListener);
    interface ActionListener extends OnRequestUserSettingAction, OnRenderToastAction {
    }
}
