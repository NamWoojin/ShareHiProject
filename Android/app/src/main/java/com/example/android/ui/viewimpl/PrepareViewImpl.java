package com.example.android.ui.viewimpl;

import android.view.View;

import androidx.lifecycle.LifecycleOwner;

import com.example.android.ui.view.LoginView;

public class PrepareViewImpl implements LoginView {

    private View mMainView;
    private ActionListener mActionListener;
    private LifecycleOwner mLifecycleOwner;

    public PrepareViewImpl(View view, LifecycleOwner lifecycleOwner) {
        mMainView = view;
        mLifecycleOwner = lifecycleOwner;

    }

    @Override
    public void setActionListener(ActionListener actionListener) {

    }
}
