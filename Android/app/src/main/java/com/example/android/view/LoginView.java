package com.example.android.view;

import com.example.android.view.action.OnNotifySignInSuccessAction;
import com.example.android.view.action.OnRenderToastAction;
import com.example.android.view.action.OnRequestedSignInAction;

public interface LoginView {
    //사용할 ActionListener 초기화
    void setActionListener(ActionListener actionListener);
    interface ActionListener extends OnNotifySignInSuccessAction, OnRequestedSignInAction,
            OnRenderToastAction {
    }
}
