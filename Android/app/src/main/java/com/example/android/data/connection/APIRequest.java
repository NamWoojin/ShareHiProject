package com.example.android.data.connection;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
APIRequest : Retrofit 라이브러리를 이용한 Api 요청
 */
public class APIRequest {
    public static void request(Call<Object> object, Consumer<Response<Object>> callback, Consumer<Throwable> fail) {
        object.enqueue(new Callback<Object>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                callback.accept(response);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                fail.accept(t);
            }
        });
    }
}
