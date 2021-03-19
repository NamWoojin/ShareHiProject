package com.example.android.data.connection;

import android.app.Application;

import com.example.android.data.model.LoginRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient{
    private static final String BASE_URL = "http://10.0.2.2:9999/";
//    private static final String BASE_URL = "http://j4f001.p.ssafy.io/";

    public static LoginRepository getLoginApiService(){
        return getInstance().create(LoginRepository.class);
    }
    private static Retrofit retrofit = null;

    public static Retrofit getInstance() {
        if (retrofit==null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

}
