package com.example.android.data.injection;

import android.view.View;

import androidx.lifecycle.LifecycleOwner;

import com.example.android.ui.view.LoginView;
import com.example.android.ui.view.SignUpView;
import com.example.android.ui.view.ToastView;
import com.example.android.ui.viewimpl.LoginViewImpl;
import com.example.android.ui.viewimpl.SignUpViewImpl;
import com.example.android.ui.viewimpl.ToastViewImpl;

public class ViewInjection {
    private ViewInjection() {
        // No instance
    }

    public static SignUpView provideSignUpView(View view, LifecycleOwner lifecycleOwner) {
        return new SignUpViewImpl(view, lifecycleOwner);
    }

    public static LoginView provideLoginView(View view, LifecycleOwner lifecycleOwner) {
        return new LoginViewImpl(view, lifecycleOwner);
    }

    public static ToastView provideToastView() {
        return new ToastViewImpl();
    }

}
