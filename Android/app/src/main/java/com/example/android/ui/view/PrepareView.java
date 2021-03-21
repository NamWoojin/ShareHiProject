package com.example.android.ui.view;

import com.example.android.ui.view.action.OnRenderToastAction;

public interface PrepareView {
    //사용할 ActionListener 초기화
    void setActionListener(LoginView.ActionListener actionListener);
    interface ActionListener extends OnRenderToastAction {
    }
}
