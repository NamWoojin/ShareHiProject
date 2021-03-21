package com.example.android.data.viewmodelimpl;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.android.data.view.SettingView;
import com.example.android.data.view.ToastView;
import com.example.android.data.viewmodel.GoogleLoginExecutor;
import com.example.android.data.viewmodel.SettingViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.lang.ref.WeakReference;

public class SettingViewModelImpl extends ViewModel implements SettingViewModel {

    private static final int REQ_CODE_SIGN_OUT = 1;
    private static final int REQ_CODE_REVOKE_ACCESS = 1;
    private WeakReference<Activity> mActivityRef;

    //View
    private ToastView mToastView;

    //LiveData
    private GoogleLoginExecutor mGoogleLoginExecutor;

    @Override
    public void setParentContext(Activity parentContext) {
        mActivityRef = new WeakReference<>(parentContext);
    }

    @Override
    public void setToastView(ToastView view) {
        mToastView = view;
    }

    @Override
    public void setSettingView(SettingView view) {
        view.setActionListener(this);
    }

    @Override
    public void setGoogleLoginExecutor(GoogleLoginExecutor executor) {
        mGoogleLoginExecutor = executor;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onRenderToast(String msg) {

    }

    @Override
    public void onRequestedSignOut() {
//        mGoogleLoginExecutor.getGoogleSignInClient().signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//            }
//        });
    }

    @Override
    public void onRequestedRevokeAccess() {

    }
}
