package com.example.android.ui.viewimpl;

import android.content.Context;
import android.widget.Toast;

import com.example.android.ui.view.ToastView;

public class ToastViewImpl implements ToastView {

    @Override
    public void render(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}