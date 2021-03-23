package com.example.android.data.injection;

import android.view.View;

import androidx.lifecycle.LifecycleOwner;

import com.example.android.data.view.LoginView;
import com.example.android.data.view.SignUpView;
import com.example.android.data.view.ToastView;
import com.example.android.data.viewimpl.LoginViewImpl;
import com.example.android.data.viewimpl.SignUpViewImpl;
import com.example.android.data.viewimpl.ToastViewImpl;

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
