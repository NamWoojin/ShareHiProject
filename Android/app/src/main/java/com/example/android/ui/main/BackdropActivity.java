package com.example.android.ui.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.R;
import com.example.android.data.injection.ModelInjection;
import com.example.android.data.injection.ViewModelInjection;
import com.example.android.data.modelImpl.SocketRepositoryImpl;
import com.example.android.data.viewmodel.BackdropViewModel;
import com.example.android.data.viewmodel.SendViewModel;
import com.example.android.data.viewmodel.SettingViewModel;
import com.example.android.data.viewmodel.SignUpViewModel;
import com.example.android.data.viewmodelimpl.BackdropViewModelImpl;
import com.example.android.data.viewmodelimpl.SendViewModelImpl;
import com.example.android.data.viewmodelimpl.SettingViewModelImpl;
import com.example.android.databinding.ActivityMainBackdropBinding;
import com.example.android.ui.send.PrepareFragment;
import com.example.android.ui.user.SettingFragment;

/*
BackdropActivity : 파일 전송, 계정 설정 페이지에서의 바탕. 상단 Backdrop 구현
 */
public class BackdropActivity extends AppCompatActivity {

    private static final String TAG = "BACKDROP_ACTIVITY";

    private BackdropViewModel mBackdropViewModel;
    private SendViewModel mSendViewModel;
    private SettingViewModel mSettingViewModel;

    private View frameLayout;
    private View actionBarDetail;

    public FragmentManager fragmentManager;
    private Fragment fragment;

    private ActivityMainBackdropBinding binding;

    private LoadingFragment loadingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_backdrop);

        //BackdropViewModel 요청
        mBackdropViewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(getApplication(), this, getIntent().getExtras())).get(BackdropViewModelImpl.class);
        mBackdropViewModel.setParentContext(this);

        //바인딩 객체 설정
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_backdrop);
        binding.setLifecycleOwner(this);
        binding.setViewModel(mBackdropViewModel);

        //SendViewModel 요청
        mSendViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(SendViewModelImpl.class);
        mSendViewModel.setParentContext(this);
        mSendViewModel.setSocketRepository(ModelInjection.provideSocketRepository(),this);   //의존성 주입
        mSendViewModel.getLoadingLiveData().observe(this,this::doLoading);

        //SettingViewModel 요청
        mSettingViewModel = new ViewModelProvider(this,new ViewModelProvider.NewInstanceFactory()).get(SettingViewModelImpl.class);
        mSettingViewModel.setParentContext(this);
        injectViewModel(mSettingViewModel);

        //페이지 이동에 따른 fragment, UI제어
        mBackdropViewModel.getPageLiveData().observe(this, this::changePage);
        mBackdropViewModel.getBackdropMenuOpenLiveData().observe(this, aBoolean -> {
            backdropMenuToggle();
        });

        frameLayout = (View) findViewById(R.id.activity_backdrop_fragment);
        actionBarDetail = (View) findViewById(R.id.activity_backdrop_action_bar_detail);
        fragmentManager = getSupportFragmentManager();
        fragment = fragmentManager.findFragmentById(R.id.activity_backdrop_fragment);

        //backdrop 초기 값 설정
        loadingFragment = LoadingFragment.newInstance();

    }

    //viewModel에 GoogleLoginExecutor의존성 주입
    private void injectViewModel(SettingViewModel viewModel) {
        viewModel.setGoogleLoginExecutor(ViewModelInjection.provideGoogleLoginExecutor(this));
    }
    
    //fragment 페이지
    public void changePage(String page) {
        switch (page) {
            case "send":
                //첫 진입 시 fragment == null
                //현재 페이지가 PrepageFragment면 실행X
                if (fragment == null || !(fragment instanceof PrepareFragment)) {
                    clearFragmentTransactionStack();
                    replaceFragment(PrepareFragment.newInstance(), false);
                    backdropMenuToggle();
                }
                break;
            case "user":
                //첫 진입 시 fragment == null
                //현재 페이지가 SettingFragment면 실행X
                if (fragment == null || !(fragment instanceof SettingFragment)) {
                    clearFragmentTransactionStack();
                    replaceFragment(SettingFragment.newInstance(), false);
                    backdropMenuToggle();
                }
                break;
        }
    }

    //fragment stack 비우기
    private void clearFragmentTransactionStack() {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    //backdrop 메뉴 여닫기, menu 아이콘 설정
    private void backdropMenuToggle() {
        if (mBackdropViewModel.getBackdropMenuOpenLiveData().getValue()) {
            frameLayout.animate().translationY(actionBarDetail.getHeight());
        } else {
            frameLayout.animate().translationY(0);
        }
    }

    //프래그먼트 화면이동
    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_backdrop_fragment, fragment);
        if (addToBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() >= 1) {
            fm.popBackStack();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSettingViewModel.onActivityResult(requestCode, resultCode, data);
    }

    //로딩 표시
    private void doLoading(Boolean b){
        if(b){
            if(!loadingFragment.isAdded()) {
                loadingFragment.show(getFragmentManager(), "loading");
            }
        }else{
            loadingFragment.dismiss();
        }
    }
}