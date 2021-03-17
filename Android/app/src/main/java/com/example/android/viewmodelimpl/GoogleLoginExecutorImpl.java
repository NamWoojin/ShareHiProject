package com.example.android.viewmodelimpl;

import android.app.Activity;
import android.content.Intent;

import com.example.android.viewmodel.GoogleLoginExecutor;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class GoogleLoginExecutorImpl implements GoogleLoginExecutor {

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    public GoogleLoginExecutorImpl(Activity activity) {
        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        // [END build_client]
    }

    @Override
    public Intent getSignInIntent() {
        return mGoogleSignInClient.getSignInIntent();
    }


}
