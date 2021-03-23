package com.example.android.data.viewmodel;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public interface GoogleLoginExecutor {
    Intent getSignInIntent();
    GoogleSignInClient getGoogleSignInClient();
}
