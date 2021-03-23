package com.example.android.data.view;

import com.example.android.data.view.action.OnRenderToastAction;
import com.example.android.data.view.action.OnRequestSignInAction;

public interface LoginView {
    //사용할 ActionListener 초기화
    void setActionListener(ActionListener actionListener);
    interface ActionListener extends OnRequestSignInAction,OnRenderToastAction {
    }
}
