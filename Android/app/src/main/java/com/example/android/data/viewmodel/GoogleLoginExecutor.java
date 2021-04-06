package com.example.android.data.viewmodel;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

/*
GoogleLoginExecutor : 구글로그인 수행
 */
public interface GoogleLoginExecutor {
    Intent getSignInIntent();
    GoogleSignInClient getGoogleSignInClient();
}
