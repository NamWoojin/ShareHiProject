package com.example.android.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CheckEmailActivity extends AppCompatActivity {

    TextView infoTextView;
    TextView timeTextView;
    TextInputEditText checkTextInputEditText;
    Button cancelButton;
    Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_check_email);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        infoTextView = (TextView)findViewById(R.id.activity_check_email_info_textView);
        timeTextView = (TextView)findViewById(R.id.activity_check_email_time_textView);
        checkTextInputEditText = (TextInputEditText)findViewById(R.id.activity_check_email_text_input_edit_text);
        cancelButton = (Button)findViewById(R.id.activity_check_email_cancel_button);
        okButton = (Button)findViewById(R.id.activity_check_email_ok_button);

        //안내정보
        infoTextView.setText(email + "으로\n인증번호가 발송되었습니다.");

        //남은 시간
        CountDownTimer countDownTimer = new CountDownTimer(180000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int)(millisUntilFinished / 60000);
                int seconds =(int)(millisUntilFinished % 60000 / 1000);
                timeTextView.setText(minutes+"분 "+seconds+"초");
            }

            @Override
            public void onFinish() {
                returnCANCEL();
            }
        };
        countDownTimer.start();

        checkTextInputEditText.addTextChangedListener(checkInputWatcher);
        Log.i("TAG", "onCreate: "+checkTextInputEditText.getText().toString().length());
        canOK(checkTextInputEditText.getText().toString());

        //취소 버튼 클릭 시
        cancelButton.setOnClickListener(v -> {
            returnCANCEL();
        });
        //확인 버튼 클릭 시
        okButton.setOnClickListener(v -> {
            returnOK();
        });
    }

    TextWatcher checkInputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            canOK(s.toString());
        }
    };

    private void canOK(String text){
        if (text.length() > 0) {
            okButton.setClickable(true);
            okButton.setBackgroundColor(Color.rgb(58,197,105));
        } else {
            okButton.setClickable(false);
            okButton.setBackgroundColor(Color.rgb(218,219,219));
        }
    }


    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 클릭
        returnCANCEL();
        return;
    }
    
    private void returnOK(){
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);

        //액티비티(팝업) 닫기
        finish();
    }
    
    private void returnCANCEL(){
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);

        //액티비티(팝업) 닫기
        finish();
    }
}