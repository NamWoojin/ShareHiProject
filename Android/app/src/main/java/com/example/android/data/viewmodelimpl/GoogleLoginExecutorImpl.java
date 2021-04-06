package com.example.android.data.viewmodelimpl;

import android.app.Activity;
import android.content.Intent;

import com.example.android.data.viewmodel.GoogleLoginExecutor;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

/*
GoogleLoginExecutorImpl : 구글로그인 수행
 */
public class GoogleLoginExecutorImpl implements GoogleLoginExecutor {

    private static final String TAG = "GoogleLoginExecutorImpl";

    private GoogleSignInClient mGoogleSignInClient;

    public GoogleLoginExecutorImpl(Activity activity) {

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    //mGoogleSignInClient 반환
    @Override
    public GoogleSignInClient getGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    //SignInIntent 반환
    @Override
    public Intent getSignInIntent() {
        return mGoogleSignInClient.getSignInIntent();
    }


}
