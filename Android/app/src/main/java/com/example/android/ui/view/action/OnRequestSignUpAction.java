package com.example.android.ui.view.action;

public interface OnRequestSignUpAction {
    void onRequestedSignIn(String email, String password);

    void onRequestedSignUp(String name, String email, String password);

    void onRequestedGoogleSignIn();

    void onRenderCheckEmail(String email);
}
