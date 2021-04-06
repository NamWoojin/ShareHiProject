package com.example.android.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.R;
import com.example.android.data.model.dto.Event;
import com.example.android.data.viewmodel.IntroViewModel;
import com.example.android.data.viewmodelimpl.IntroViewModelImpl;
import com.example.android.ui.user.LoginActivity;

/*
IntroActivity : 인트로 화면
 */
public class IntroActivity extends AppCompatActivity {

    private static final String TAG = "IntroActivity";

    private IntroViewModel mIntroViewModel;

    private BackPressHandler  backPressHandler = new BackPressHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_intro);

        mIntroViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(IntroViewModelImpl.class);
        mIntroViewModel.setParentContext(this);
        mIntroViewModel.getAdID();
        mIntroViewModel.getCheckAutoLoginLiveData().observe(this, this::setHandler);
    }


    //권한 허용에 대한 결과 전달
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        mIntroViewModel.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mIntroViewModel.onActivityResult(requestCode, resultCode,data);
    }

    //자동로그인 확인 결과에 따른 처리
    private void setHandler(Event<Boolean> event) {
        boolean canLogin = event.getContentIfNotHandled();
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent;
            //로그인 성공
            if (canLogin) {
                intent = new Intent(IntroActivity.this, MainActivity.class);
            }
            //로그인 실패
            else {
                intent = new Intent(IntroActivity.this, LoginActivity.class);
            }
            startActivity(intent);

            //화면 전환 animation 효과 적용
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            //인트로로 다시 돌아오지 못하도록 끝내기
            finish();
        }, 2000);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        backPressHandler.onBackPressed();
    }
}