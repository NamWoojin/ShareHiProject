package com.example.android.data.view.action;

public interface OnRequestSignUpAction {
    void onRequestedSignIn(String email, String password);

    void onRequestedSignUp(String name, String email, String password);

    void onRequestedGoogleSignIn();

    void onRenderCheckEmail(String email);
}
