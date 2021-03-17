package com.example.android.injection;

import android.view.View;

import androidx.lifecycle.LifecycleOwner;

import com.example.android.view.LoginView;
import com.example.android.view.ToastView;
import com.example.android.viewimpl.LoginViewImpl;
import com.example.android.viewimpl.ToastViewImpl;

public class ViewInjection {
    private ViewInjection() {
        // No instance
    }

    public static LoginView provideLoginView(View view, LifecycleOwner lifecycleOwner) {
        return new LoginViewImpl(view, lifecycleOwner);
    }

    public static ToastView provideToastView() {
        return new ToastViewImpl();
    }

}
