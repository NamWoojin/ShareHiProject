package com.example.android.viewimpl;

import android.content.Context;
import android.widget.Toast;

import com.example.android.view.ToastView;

public class ToastViewImpl implements ToastView {

    @Override
    public void render(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}