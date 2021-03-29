package com.example.android.data.viewmodelimpl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.example.android.R;
import com.example.android.data.connection.APIRequest;
import com.example.android.data.connection.RetrofitClient;
import com.example.android.data.model.dto.APIResponse;
import com.example.android.data.model.dto.LoginContent;
import com.example.android.data.model.dto.Member;
import com.example.android.data.model.dto.User;
import com.example.android.data.viewmodel.GoogleLoginExecutor;
import com.example.android.data.viewmodel.SettingViewModel;
import com.example.android.ui.user.EditNameFragment;
import com.example.android.ui.user.EditPasswordFragment;
import com.example.android.ui.user.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.http.POST;

public class SettingViewModelImpl extends ViewModel implements SettingViewModel {

    private static final String TAG = "SettingViewModelImpl";

    private WeakReference<Activity> mActivityRef;

    SharedPreferences memberInformation;

    //LiveData
    private int mem_id;
    private MutableLiveData<String> memberNameLiveData = new MutableLiveData<>();
    private MutableLiveData<String> memberImgLiveData = new MutableLiveData<>();
    private MutableLiveData<String> memberEmailLiveData = new MutableLiveData<>();
    private MutableLiveData<String> currentPasswordLiveData = new MutableLiveData<>("");
    private MutableLiveData<String> newPasswordLiveData = new MutableLiveData<>("");
    private MutableLiveData<String> newCheckPasswordLiveData = new MutableLiveData<>("");
    private MutableLiveData<Boolean> isOKNewPassword = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> isOKNewCheckPassword = new MutableLiveData<>(false);

    private EditPasswordFragment mEditPasswordFragment;

    private GoogleLoginExecutor mGoogleLoginExecutor;

    @Override
    public void setParentContext(Activity parentContext) {
        mActivityRef = new WeakReference<>(parentContext);
    }

    @Override
    public void setGoogleLoginExecutor(GoogleLoginExecutor executor) {
        mGoogleLoginExecutor = executor;
    }

    //사용자 정보 얻기
    @Override
    public void getMemberInformation() {
        memberInformation = mActivityRef.get().getSharedPreferences("member", Activity.MODE_PRIVATE);
        mem_id = memberInformation.getInt("mem_id",-1);
        if (mem_id != -1) {
            getUser(mem_id);
        }
    }

    //사용자 정보 조회
    private void getUser(int mem_id) {
        APIRequest.request(RetrofitClient.getUserApiService().getUser(mem_id), objectResponse -> {
            Gson gson = new Gson();
            int code = objectResponse.code();
            String body = gson.toJson(objectResponse.body());

            if (code >= 500) {
                //서버 에러
                Toast.makeText(mActivityRef.get(), R.string.toast_server_fail_message, Toast.LENGTH_SHORT).show();
            } else if (code >= 400) {
                //클라이언트 에러
                Toast.makeText(mActivityRef.get(), R.string.toast_404_fail_message, Toast.LENGTH_SHORT).show();
            } else if (code >= 300) {

            } else if (code >= 200) {
                //성공
                Type listType = new TypeToken<APIResponse<LoginContent>>() {
                }.getType();
                APIResponse<LoginContent> res = gson.fromJson(body, listType);
                switch (res.getMessage()) {
                    case "SUCCESS":
                        Member member = res.getContent().getMember();
                        Log.i(TAG, "onRequestedSignIn: " + res.getContent().getMember().toString());
                        memberNameLiveData.setValue(member.getMem_name());
                        memberEmailLiveData.setValue(member.getMem_email());
                        memberImgLiveData.setValue(member.getMem_image());
                        CircleImageView userImageView = (CircleImageView)mActivityRef.get().findViewById(R.id.fragment_setting_image_imageView);
                        Glide.with(mActivityRef.get()).load(member.getMem_image()).into(userImageView);
                        break;
                    case "FAIL":
                        //회원탈퇴 실패
                        Toast.makeText(mActivityRef.get(), R.string.toast_setting_user_delete_fail_message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, throwable -> {
            Log.e(TAG, "onRequestedSignIn: " + throwable);
            Toast.makeText(mActivityRef.get(), R.string.toast_connect_fail_message, Toast.LENGTH_SHORT).show();
        });
    }

//    //이름 변경 dialog 열기
//    @Override
//    public void openEditNameDialog(){
//        editNameLiveData.setValue(memberNameLiveData.getValue());
//        mEditNameFragment = EditNameFragment.newInstance();
//        mEditNameFragment.show(mActivityRef.get().getFragmentManager(), "EDIT_NAME");
//    }
//
//    //이름 변경
//    @Override
//    public void editName(){
//        memberNameLiveData.setValue(editNameLiveData.getValue());
//        //이름 변경 api 추가
//        mEditNameFragment.dismiss();
//    }
//
//    //이름 변경 dialog 닫기
//    @Override
//    public void closeEditNameDialog(){
//        mEditNameFragment.dismiss();
//    }

    //이미지 변경하기
    @Override
    public void editImage(){

    }

    @Override
    public void openEditPasswordDialog(){
        currentPasswordLiveData.setValue("");
        newPasswordLiveData.setValue("");
        newCheckPasswordLiveData.setValue("");
        mEditPasswordFragment = EditPasswordFragment.newInstance();
        mEditPasswordFragment.show(mActivityRef.get().getFragmentManager(), "EDIT_PASSWORD");
    }

    @Override
    public void editPassword(){
        mEditPasswordFragment.dismiss();
    }

    @Override
    public void closeEditPasswordDialog(){
        mEditPasswordFragment.dismiss();
    }

    //로그아웃
    @Override
    public void onRequestedSignOut() {
        SharedPreferences loginInformation = mActivityRef.get().getSharedPreferences("login", Activity.MODE_PRIVATE);
        Boolean isBagicLogin = loginInformation.getBoolean("basicLogin", false);
        new AlertDialog.Builder(mActivityRef.get())
                .setTitle(R.string.fragment_setting_sign_out_button_text).setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("로그아웃", (dialog, whichButton) -> {
                    if (isBagicLogin) {
                        //기본 로그인인지 판단
                        removeUserInformation();
                        goLoginActivity();
                    } else {
                        //구글 로그인
                        mGoogleLoginExecutor.getGoogleSignInClient().signOut().addOnCompleteListener(mActivityRef.get(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                removeUserInformation();
                                goLoginActivity();
                            }
                        });
                    }
                })
                .setNegativeButton("취소", (dialog, whichButton) -> {

                })
                .show();

    }

    //회원탈퇴
    @Override
    public void onRequestedRevokeAccess() {
        SharedPreferences loginInformation = mActivityRef.get().getSharedPreferences("login", Activity.MODE_PRIVATE);
        Boolean isBagicLogin = loginInformation.getBoolean("basicLogin", false);
        new AlertDialog.Builder(mActivityRef.get())
                .setTitle(R.string.fragment_setting_user_delete_button_text).setMessage("정말 탈퇴하시겠습니까?")
                .setPositiveButton(R.string.fragment_setting_user_delete_button_text, (dialog, whichButton) -> {
                    APIRequest.request(RetrofitClient.getUserApiService().SignOut(mem_id), objectResponse -> {
                        Gson gson = new Gson();
                        int code = objectResponse.code();
                        String body = gson.toJson(objectResponse.body());

                        if (code >= 500) {
                            //서버 에러
                            Toast.makeText(mActivityRef.get(), R.string.toast_server_fail_message, Toast.LENGTH_SHORT).show();
                        } else if (code >= 400) {
                            //클라이언트 에러
                            Toast.makeText(mActivityRef.get(), R.string.toast_404_fail_message, Toast.LENGTH_SHORT).show();
                        } else if (code >= 300) {

                        } else if (code >= 200) {
                            //성공
                            Type listType = new TypeToken<APIResponse<Object>>() {
                            }.getType();
                            APIResponse<Object> res = gson.fromJson(body, listType);
                            switch (res.getMessage()) {
                                case "SUCCESS":
                                    //회원탈퇴 성공
                                    if (isBagicLogin) {
                                        //기본 로그인인지 판단
                                        removeUserInformation();
                                        goLoginActivity();
                                    } else {
                                        //구글 로그인
                                        mGoogleLoginExecutor.getGoogleSignInClient().revokeAccess().addOnCompleteListener(mActivityRef.get(), new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                removeUserInformation();
                                                goLoginActivity();
                                            }
                                        });
                                    }
                                    break;
                                case "FAIL":
                                    //회원탈퇴 실패
                                    Toast.makeText(mActivityRef.get(), R.string.toast_setting_user_delete_fail_message, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }, throwable -> {
                        Log.e(TAG, "onRequestedSignIn: " + throwable);
                        Toast.makeText(mActivityRef.get(), R.string.toast_connect_fail_message, Toast.LENGTH_SHORT).show();
                    });
                })
                .setNegativeButton("취소", (dialog, whichButton) -> {

                })
                .show();
    }

    //SharedPreferences에서 사용자 정보 삭제
    private void removeUserInformation() {
        //로그인 정보 삭제
        SharedPreferences loginInformation = mActivityRef.get().getSharedPreferences("login", Activity.MODE_PRIVATE);
        SharedPreferences.Editor loginEditor = loginInformation.edit();
        loginEditor.clear();
        loginEditor.apply();
        //사용자 정보 삭제
        SharedPreferences memberInformation = mActivityRef.get().getSharedPreferences("member", Activity.MODE_PRIVATE);
        SharedPreferences.Editor memberEditor = memberInformation.edit();
        memberEditor.clear();
        memberEditor.apply();
        //토큰 정보 삭제
        SharedPreferences tokenInformation = mActivityRef.get().getSharedPreferences("token", Activity.MODE_PRIVATE);
        SharedPreferences.Editor tokenEditor = tokenInformation.edit();
        tokenEditor.clear();
        tokenEditor.apply();
    }

    //로그인 액티비티로 이동
    private void goLoginActivity() {
        Intent i = new Intent(mActivityRef.get(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mActivityRef.get().startActivity(i);
    }

    //getter setter
    @Override
    public MutableLiveData<String> getMemberNameLiveData() {
        return memberNameLiveData;
    }
    @Override
    public void setMemberNameLiveData(MutableLiveData<String> memberNameLiveData) {
        this.memberNameLiveData = memberNameLiveData;
    }
    @Override
    public MutableLiveData<String> getMemberEmailLiveData() {
        return memberEmailLiveData;
    }
    @Override
    public void setMemberEmailLiveData(MutableLiveData<String> memberEmailLiveData) {
        this.memberEmailLiveData = memberEmailLiveData;
    }
    @Override
    public MutableLiveData<String> getCurrentPasswordLiveData() {
        return currentPasswordLiveData;
    }
    @Override
    public void setCurrentPasswordLiveData(MutableLiveData<String> currentPasswordLiveData) {
        this.currentPasswordLiveData = currentPasswordLiveData;
    }
    @Override
    public MutableLiveData<String> getNewPasswordLiveData() {
        return newPasswordLiveData;
    }
    @Override
    public void setNewPasswordLiveData(MutableLiveData<String> newPasswordLiveData) {
        this.newPasswordLiveData = newPasswordLiveData;
    }
    @Override
    public MutableLiveData<String> getNewCheckPasswordLiveData() {
        return newCheckPasswordLiveData;
    }
    @Override
    public void setNewCheckPasswordLiveData(MutableLiveData<String> newCheckPasswordLiveData) {
        this.newCheckPasswordLiveData = newCheckPasswordLiveData;
    }
    @Override
    public MutableLiveData<Boolean> getIsOKNewPassword() {
        return isOKNewPassword;
    }
    @Override
    public void setIsOKNewPassword(MutableLiveData<Boolean> isOKNewPassword) {
        this.isOKNewPassword = isOKNewPassword;
    }
    @Override
    public MutableLiveData<Boolean> getIsOKNewCheckPassword() {
        return isOKNewCheckPassword;
    }
    @Override
    public void setIsOKNewCheckPassword(MutableLiveData<Boolean> isOKNewCheckPassword) {
        this.isOKNewCheckPassword = isOKNewCheckPassword;
    }
}
