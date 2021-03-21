package com.example.android.data.view.action;

public interface OnRequestSignInAction {
    void onRequestedSignIn(String email,String password);
    void onRequestedGoogleSignIn();
    void onRenderSignUp();
    void onMoveFindPassword();
}
