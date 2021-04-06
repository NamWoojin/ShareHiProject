package com.example.android.data.viewmodelimpl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.example.android.R;
import com.example.android.data.connection.APIRequest;
import com.example.android.data.connection.RetrofitClient;
import com.example.android.data.model.dto.APIResponse;
import com.example.android.data.model.dto.LoginContent;
import com.example.android.data.model.dto.Member;
import com.example.android.data.model.dto.MemberPassword;
import com.example.android.data.viewmodel.GoogleLoginExecutor;
import com.example.android.data.viewmodel.SettingViewModel;
import com.example.android.ui.user.EditPasswordFragment;
import com.example.android.ui.user.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/*
SettingViewModelImpl : 계정 설정과 관련된 데이터를 관리하는 ViewModel
 */
public class SettingViewModelImpl extends ViewModel implements SettingViewModel {

    private static final int PICK_FROM_ALBUM = 2000;

    private static final String TAG = "SettingViewModelImpl";

    private WeakReference<Activity> mActivityRef;

    SharedPreferences memberInformation;

    private int mem_id;
    private String dev_id;

    //LiveData
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
        mem_id = memberInformation.getInt("mem_id", -1);
        dev_id = memberInformation.getString("dev_id", "");
        if (mem_id != -1) {
            getUser(mem_id);
        }
    }

    //사용자 정보 조회
    private void getUser(int mem_id) {
        APIRequest.request(RetrofitClient.getUserApiService().GetUser(mem_id), objectResponse -> {
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
                        memberNameLiveData.setValue(member.getMem_name());
                        memberEmailLiveData.setValue(member.getMem_email());
                        memberImgLiveData.setValue(member.getMem_image());
                        CircleImageView userImageView = (CircleImageView) mActivityRef.get().findViewById(R.id.fragment_setting_image_imageView);
                        Glide.with(mActivityRef.get()).load(member.getMem_image()).into(userImageView);
                        break;
                    case "FAIL":
                        //회원탈퇴 실패
                        Toast.makeText(mActivityRef.get(), R.string.toast_setting_user_delete_fail_message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, throwable -> {
            Toast.makeText(mActivityRef.get(), R.string.toast_connect_fail_message, Toast.LENGTH_SHORT).show();
        });
    }

    //이미지 변경하기
    @Override
    public void editImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        mActivityRef.get().startActivityForResult(intent, PICK_FROM_ALBUM);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_FROM_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                Uri photoUri = data.getData();

                Cursor cursor = null;
                try {
                    /*
                     *  Uri 스키마를
                     *  content:/// 에서 file:/// 로  변경한다.
                     */
                    String[] proj = {MediaStore.Images.Media.DATA};

                    assert photoUri != null;
                    cursor = mActivityRef.get().getContentResolver().query(photoUri, proj, null, null, null);

                    assert cursor != null;
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    cursor.moveToFirst();

                    File tempFile = new File(cursor.getString(column_index));
                    String filePath = getResizeFileImage(cursor.getString(column_index),4);
                    if(filePath != null){
                        tempFile = new File(filePath);
                    }
                    MultipartBody.Part file = changeImageToMultipart(tempFile);
                    RequestBody id = changeIntegerToRequestbody(mem_id);
                    onRequestChangeImage(id, file);
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }

        }
    }

    //이미지 용량 줄이기
    public String getResizeFileImage(String file_route, int size){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = size;
        Bitmap src = BitmapFactory.decodeFile(file_route, options);
        if(src == null){
            src = BitmapFactory.decodeFile(file_route);
        }
        int height = src.getHeight();
        int width = src.getWidth();
        Bitmap resized = Bitmap.createScaledBitmap(src, 256, height * 256/width, true);
        resized = rotateBitmap(resized, bitmapOrientation(file_route));
        return SaveBitmapToFileCache(resized);
    }

    //이미지 회전 값 구하기
    private int bitmapOrientation(String path){
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);
    }

    //이미지 회전값에 따라 원상복구
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    //캐시에 용량 줄인 이미지 저장
    private String SaveBitmapToFileCache(Bitmap bitmap){
        String path = mActivityRef.get().getCacheDir()+"/"+new Date()+".jpg";
        File fileCacheItem = new File(path);
        try {
            fileCacheItem.createNewFile();
            FileOutputStream out = new FileOutputStream(fileCacheItem);
            bitmap.compress(Bitmap.CompressFormat.JPEG,80,out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    //이미지 MultipartBody로 변환
    private MultipartBody.Part changeImageToMultipart(File file) {
        RequestBody rqFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));

        MultipartBody.Part result = MultipartBody.Part.createFormData("image", file.getName(), rqFile); // 키값, 파일이름, 데이터

        return result;
    }

    //int값 requestbody로 변환
    private RequestBody changeIntegerToRequestbody(int num) {
        return RequestBody.create(String.valueOf(num), MediaType.parse("text/plain"));
    }

    //프로필 이미지 변경 요청
    private void onRequestChangeImage(RequestBody id, MultipartBody.Part file) {
        APIRequest.request(RetrofitClient.getUserApiService().uploadImage(id, file), objectResponse -> {
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
                        getMemberInformation();
                        break;
                    case "FAIL":
                        Toast.makeText(mActivityRef.get(), R.string.toast_setting_change_image_fail_message, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, throwable -> {
            Toast.makeText(mActivityRef.get(), R.string.toast_connect_fail_message, Toast.LENGTH_SHORT).show();
        });
    }

    //비밀번호 변경 DialogFragment 열기
    @Override
    public void openEditPasswordDialog() {
        SharedPreferences loginInformation = mActivityRef.get().getSharedPreferences("login", Activity.MODE_PRIVATE);
        String login = loginInformation.getString("login", "none");
        if (login.equals("basic")) {
            currentPasswordLiveData.setValue("");
            newPasswordLiveData.setValue("");
            newCheckPasswordLiveData.setValue("");
            mEditPasswordFragment = EditPasswordFragment.newInstance();
            mEditPasswordFragment.show(mActivityRef.get().getFragmentManager(), "EDIT_PASSWORD");
        } else {
            Toast.makeText(mActivityRef.get(), R.string.fragment_setting_edit_password_cant_open_text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void editPassword() {
        checkCurrentPassword();
    }

    //현재 비밀번호 맞는지 확인
    private void checkCurrentPassword() {
        MemberPassword memberPassword = new MemberPassword();
        memberPassword.setMem_id(mem_id);
        memberPassword.setMem_password(currentPasswordLiveData.getValue());
        APIRequest.request(RetrofitClient.getUserApiService().CheckPassword(memberPassword), objectResponse -> {
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
                        updatePassword();
                        break;
                    case "FAIL":
                        Toast.makeText(mActivityRef.get(), R.string.fragment_setting_edit_password_current_fail_text, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, throwable -> {
            Toast.makeText(mActivityRef.get(), R.string.toast_connect_fail_message, Toast.LENGTH_SHORT).show();
        });
    }

    //새 비밀번호 변경
    private void updatePassword() {
        MemberPassword memberPassword = new MemberPassword();
        memberPassword.setMem_id(mem_id);
        memberPassword.setMem_password(newPasswordLiveData.getValue());
        APIRequest.request(RetrofitClient.getUserApiService().UpdatePassword(memberPassword), objectResponse -> {
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
                        Toast.makeText(mActivityRef.get(), R.string.fragment_setting_edit_password_current_success_text, Toast.LENGTH_SHORT).show();
                        mEditPasswordFragment.dismiss();
                        break;
                    case "FAIL":
                        Toast.makeText(mActivityRef.get(), R.string.fragment_setting_edit_password_current_fail_text, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, throwable -> {
            Toast.makeText(mActivityRef.get(), R.string.toast_connect_fail_message, Toast.LENGTH_SHORT).show();
        });
    }

    //비밀번호 변경 DialogFragment 닫기
    @Override
    public void closeEditPasswordDialog() {
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
                    APIRequest.request(RetrofitClient.getLoginApiService().Logout(mem_id, dev_id), objectResponse -> {
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
                                    //로그아웃 성공
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
                                    break;
                                case "FAIL":
                                    //회원탈퇴 실패
                                    Toast.makeText(mActivityRef.get(), R.string.toast_setting_user_delete_fail_message, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }, throwable -> {
                        Toast.makeText(mActivityRef.get(), R.string.toast_connect_fail_message, Toast.LENGTH_SHORT).show();
                    });

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

    //로그아웃/회원탈퇴 이후 로그인 Activity로 이동
    private void goLoginActivity() {
        Intent i = new Intent(mActivityRef.get(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mActivityRef.get().startActivity(i);
    }

    //LiveData getter & setter
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
