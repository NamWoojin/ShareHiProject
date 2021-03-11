package com.example.android.user;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.R;
import com.google.android.gms.common.SignInButton;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        SignInButton signInButton = (SignInButton) findViewById(R.id.activity_signup_google_button);
        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText("Google 이메일로 가입하기");
    }
}