package com.example.android.injection;

import android.app.Activity;

import com.example.android.viewmodel.GoogleLoginExecutor;
import com.example.android.viewmodelimpl.GoogleLoginExecutorImpl;

public class ViewModelInjection {
    private ViewModelInjection() {
        // No instance
    }

    public static GoogleLoginExecutor provideLoginUsecaseExecutor(Activity activity) {
        return new GoogleLoginExecutorImpl(activity);
    }
}
