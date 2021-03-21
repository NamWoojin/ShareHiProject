package com.example.android.data.connection;

import com.example.android.data.model.UserRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient{
    //local
    private static final String BASE_URL = "http://10.0.2.2:9999/";
    //public
    //private static final String BASE_URL = "http://j4f001.p.ssafy.io/";

    public static UserRepository getUserApiService(){
        return getInstance().create(UserRepository.class);
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
