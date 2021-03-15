package com.example.android.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.R;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.textfield.TextInputLayout;

public class SignupActivity extends AppCompatActivity {

    private final int CHECK_EMAIL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        SignInButton signInButton = (SignInButton) findViewById(R.id.activity_signup_google_button);
        TextView textView = (TextView) signInButton.getChildAt(0);
        textView.setText("Google 이메일로 가입하기");

        TextInputLayout emailTextInputLayout = (TextInputLayout)findViewById(R.id.activity_signup_email_text_input_layout);
        Button emailCheckButton  =(Button)findViewById(R.id.activity_signup_email_check_image_view);
        emailCheckButton.setOnClickListener(v -> {
            Log.i("TfffAG", "onCreate: "+emailTextInputLayout.getEditText().getText().toString());
            Intent intent = new Intent(this, CheckEmailActivity.class);
            intent.putExtra("email",emailTextInputLayout.getEditText().getText().toString());
            startActivityForResult(intent, CHECK_EMAIL);
        });
    }

    //CheckEmailActivity 종료 시 불러오는 메소드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHECK_EMAIL) {
            //이메일 인증
            if (resultCode == RESULT_OK) {
                //이메일 인증 성공
                Log.i("TTTT","확인");
            } else {
                //이메일 인증 실패
                Log.i("TTTT","취소");
            }
        }
    }
}