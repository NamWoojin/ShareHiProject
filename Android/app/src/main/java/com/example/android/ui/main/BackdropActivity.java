package com.example.android.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.R;
import com.example.android.data.viewmodel.LoginViewModel;
import com.example.android.data.viewmodel.SendViewModel;
import com.example.android.ui.send.PrepareFragment;
import com.example.android.ui.user.SettingFragment;

public class BackdropActivity extends AppCompatActivity{

    private static final String TAG = "BACKDROP_ACTIVITY";

    private SendViewModel mSendViewModel;

    private TextView actionBarTextView;
    private View frameLayout;
    private View actionBarDetail;
    private ImageView actionBarMenu;
    private ConstraintLayout sendLayout;
    private ConstraintLayout userLayout;

    public FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_backdrop);

        sendLayout = (ConstraintLayout) findViewById(R.id.activity_backdrop_go_send_layout);
        userLayout = (ConstraintLayout) findViewById(R.id.activity_backdrop_go_user_layout);
        actionBarTextView = (TextView) findViewById(R.id.activity_backdrop_action_bar_text);
        frameLayout = (View) findViewById(R.id.activity_backdrop_fragment);
        actionBarDetail = (View) findViewById(R.id.activity_backdrop_action_bar_detail);
        fragmentManager = getSupportFragmentManager();

        //요청에 따른 fragment 페이지 설정
        Intent intent = getIntent();
        String page = intent.getStringExtra("page");
        changePage(page);
        setUIbyFragment(page);

        //상단바 상세메뉴 여닫기 이벤트 추가
        actionBarMenu = (ImageView) findViewById(R.id.activity_backdrop_action_bar_menu);
        actionBarMenu.setOnClickListener(v -> backdropMenuToggle());

        //backdrop 전송하기 메뉴 클릭 시
        sendLayout.setOnClickListener(v -> {
            changePage("send");
            setUIbyFragment("send");
            backdropMenuToggle();
        });

        //backdrop 계정설정 메뉴 클릭 시
        userLayout.setOnClickListener(v -> {
            changePage("user");
            setUIbyFragment("user");
            backdropMenuToggle();
        });
    }

    //페이지 이동
    public void changePage(String page) {
        //addToBackStack - true : 뒤로가기 적용
        //addToBackStack - false : 뒤로가기 미적용
        Fragment fragment = fragmentManager.findFragmentById(R.id.activity_backdrop_fragment);

        switch (page) {
            case "send":
                //첫 진입 시 fragment == null
                //현재 페이지가 PrepageFragment면 실행X
                if (fragment == null || !(fragment instanceof PrepareFragment)) {
                    replaceFragment(PrepareFragment.newInstance());
                }
                break;
            case "user":
                //첫 진입 시 fragment == null
                //현재 페이지가 SettingFragment면 실행X
                if (fragment == null || !(fragment instanceof SettingFragment)) {
                    replaceFragment(SettingFragment.newInstance());
                }
                break;
        }

    }

    //fragment 변화에 따른 backdrop 제어
    private void setUIbyFragment(String page) {
        switch (page) {
            case "send":
                actionBarTextView.setText("공유하기");
                setMenuLayoutUI(sendLayout, true);
                setMenuLayoutUI(userLayout, false);
                break;
            case "user":
                actionBarTextView.setText("계정설정");
                setMenuLayoutUI(sendLayout, false);
                setMenuLayoutUI(userLayout, true);
                break;
        }
    }

    //layout 디자인 변경
    private void setMenuLayoutUI(ConstraintLayout layout, boolean clicked) {
        if (clicked) {
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.backdrop_detaillayout_radius));
        } else {
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.backdrop_detaillayout_none));
        }
    }

    //backdrop 메뉴 여닫기, menu 아이콘 설정
    private void backdropMenuToggle() {
        if (frameLayout.isEnabled()) {
            frameLayout.animate().translationY(actionBarDetail.getHeight());
            actionBarMenu.setImageResource(R.drawable.ic_menu_close_24);
        } else {
            frameLayout.animate().translationY(0);
            actionBarMenu.setImageResource(R.drawable.ic_menu_white_24);
        }
        frameLayout.setEnabled(!frameLayout.isEnabled());
    }

    //프래그먼트 화면이동
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_backdrop_fragment, fragment);
        fragmentTransaction.commit();
    }

}