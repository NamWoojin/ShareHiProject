package com.example.android.ui.view;

import com.example.android.ui.view.action.OnRenderToastAction;
import com.example.android.ui.view.action.OnRequestSignUpAction;

public interface SignUpView {
    //사용할 ActionListener 초기화
    void setActionListener(SignUpView.ActionListener actionListener);
    interface ActionListener extends OnRequestSignUpAction, OnRenderToastAction {
    }
}
