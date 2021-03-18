package com.example.android.data.view;

import com.example.android.data.view.action.OnRenderToastAction;
import com.example.android.data.view.action.OnRequestSignUpAction;

public interface SignUpView {
    //사용할 ActionListener 초기화
    void setActionListener(SignUpView.ActionListener actionListener);
    interface ActionListener extends OnRequestSignUpAction, OnRenderToastAction {
    }
}
