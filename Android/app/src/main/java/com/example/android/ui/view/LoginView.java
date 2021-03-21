package com.example.android.ui.view;

import com.example.android.ui.view.action.OnRenderToastAction;
import com.example.android.ui.view.action.OnRequestSignInAction;

public interface LoginView {
    //사용할 ActionListener 초기화
    void setActionListener(ActionListener actionListener);
    interface ActionListener extends OnRequestSignInAction, OnRenderToastAction {
    }
}
