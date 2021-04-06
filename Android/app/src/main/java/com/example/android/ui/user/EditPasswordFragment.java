package com.example.android.ui.user;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.R;
import com.example.android.data.viewmodel.SettingViewModel;
import com.example.android.data.viewmodelimpl.SettingViewModelImpl;
import com.example.android.databinding.FragmentUserEditPasswordBinding;
import com.example.android.ui.main.BackdropActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
EditPasswordFragment : 비밀번호 변경을 위한 DialogFragment
 */
public class EditPasswordFragment extends DialogFragment {

    private FragmentUserEditPasswordBinding binding;
    private SettingViewModel mSettingViewModel;

    private TextInputLayout newPasswordTextInputLayout;
    private TextInputLayout newCheckPasswordTextInputLayout;
    private Button changePasswordButton;

    public EditPasswordFragment() {
        // Required empty public constructor
    }

    public static EditPasswordFragment newInstance() {
        EditPasswordFragment fragment = new EditPasswordFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.fragment_user_edit_password);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_edit_password, container, false);
        View view = binding.getRoot();

        newPasswordTextInputLayout = view.findViewById(R.id.fragment_edit_password_new_text_input_layout);
        newCheckPasswordTextInputLayout = view.findViewById(R.id.fragment_edit_password_new_check_text_input_layout);
        changePasswordButton = view.findViewById(R.id.fragment_edit_password_ok_button);

        mSettingViewModel = new ViewModelProvider((BackdropActivity) getActivity()).get(SettingViewModelImpl.class);
        binding.setViewModel(mSettingViewModel);

        mSettingViewModel.getCurrentPasswordLiveData().observe((BackdropActivity)getActivity(), s -> canChangePassword());
        mSettingViewModel.getNewPasswordLiveData().observe((BackdropActivity)getActivity(), this::checkPasswordFormat);
        mSettingViewModel.getNewCheckPasswordLiveData().observe((BackdropActivity)getActivity(), this::checkCheckPasswordFormat);

        return view;
    }

    //비밀번호 체크
    private void checkPasswordFormat(String s) {
        String passwordFormat = "^(?=.*[$@$!%*#?&])[0-9A-Za-z$@$!%*#?&]{8,45}$";
        Pattern pattern = Pattern.compile(passwordFormat);
        Matcher matcher = pattern.matcher(s);
        if (s.length() > 0 && matcher.matches()) {
            //비밀번호 형식 통과
            newPasswordTextInputLayout.setError(null);
            mSettingViewModel.setIsOKNewPassword(new MutableLiveData<>(true));
        } else if (s.length() == 0) {
            //비밀번호 입력 X
            newPasswordTextInputLayout.setError(null);
            mSettingViewModel.setIsOKNewPassword(new MutableLiveData<>(false));
        } else {
            //비밀번호 형식 불일치
            newPasswordTextInputLayout.setError(getString(R.string.activity_signup_password_error));
            mSettingViewModel.setIsOKNewPassword(new MutableLiveData<>(false));
        }
        canChangePassword();
    }

    //비밀번호 확인 일치 체크
    private void checkCheckPasswordFormat(String s) {
        if (s.length() > 0) {
            if (mSettingViewModel.getNewPasswordLiveData().getValue().equals(s)) {
                //비밀번호 일치
                newCheckPasswordTextInputLayout.setError(null);
                mSettingViewModel.setIsOKNewCheckPassword(new MutableLiveData<>(true));
            } else {
                newCheckPasswordTextInputLayout.setError(getString(R.string.activity_signup_password_check_error));
                mSettingViewModel.setIsOKNewCheckPassword(new MutableLiveData<>(false));
            }
        } else {
            newCheckPasswordTextInputLayout.setError(null);
            mSettingViewModel.setIsOKNewCheckPassword(new MutableLiveData<>(false));
        }
        canChangePassword();
    }

    //입력에 따른 버튼 상태 처리
    private void canChangePassword() {
        if (mSettingViewModel.getCurrentPasswordLiveData().getValue().length() > 0 && mSettingViewModel.getIsOKNewPassword().getValue() &&
                mSettingViewModel.getIsOKNewCheckPassword().getValue()) {
            //회원가입 가능
            changePasswordButton.setEnabled(true);
            changePasswordButton.setBackgroundColor(Color.rgb(58, 197, 105));
        } else {
            //회원가입 불가
            changePasswordButton.setEnabled(false);
            changePasswordButton.setBackgroundColor(Color.rgb(218, 219, 219));
        }
    }
}