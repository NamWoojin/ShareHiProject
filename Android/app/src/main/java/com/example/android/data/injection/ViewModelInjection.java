package com.example.android.data.injection;

import android.app.Activity;

import com.example.android.data.viewmodel.GoogleLoginExecutor;
import com.example.android.data.viewmodelimpl.GoogleLoginExecutorImpl;

/*
ViewModelInjection : ViewModel 의존성 주입
 */
public class ViewModelInjection {
    private ViewModelInjection() {
        // No instance
    }

    public static GoogleLoginExecutor provideGoogleLoginExecutor(Activity activity) {
        return new GoogleLoginExecutorImpl(activity);
    }
}
