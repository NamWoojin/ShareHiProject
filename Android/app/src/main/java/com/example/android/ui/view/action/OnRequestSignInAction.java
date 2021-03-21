package com.example.android.ui.view.action;

public interface OnRequestSignInAction {
    void onRequestedSignIn(String email,String password);
    void onRequestedGoogleSignIn();
    void onRenderSignUp();
    void onMoveFindPassword();
}
