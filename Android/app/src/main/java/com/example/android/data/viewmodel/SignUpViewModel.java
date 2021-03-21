package com.example.android.data.viewmodel;

import android.app.Activity;
import android.content.Intent;

public interface SignUpViewModel{
    void setParentContext(Activity parentContext);
    void setGoogleLoginExecutor(GoogleLoginExecutor executor);
    void onActivityResult(int requestCode, int resultCode, Intent data);
    void onRequestedGoogleSignIn();
    void onRenderCheckEmail(String email);
    void onRequestedSignIn(String email, String password);
    public void onRequestedSignUp(String name, String email, String password);
}
