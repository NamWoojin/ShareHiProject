package com.example.android.data.connection;

import com.example.android.data.model.LoginRepository;
import com.example.android.data.model.UserRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient{
    //local
    private static final String BASE_URL = "http://10.0.2.2:8080/api/";
    //public
//    private static final String BASE_URL = "https://j4f001.p.ssafy.io/api/";

    public static UserRepository getUserApiService(){
        return getInstance().create(UserRepository.class);
    }

    public static LoginRepository getLoginApiService(){
        return getInstance().create(LoginRepository.class);
    }


    private static Retrofit retrofit = null;

    public static Retrofit getInstance() {
        if (retrofit==null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            //RetrofitBuilder 생성
            Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson));

            // 통신 중 일어나는 로그를 인터셉트하는 Interceptor
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            //logInterceptor 등록
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.interceptors().add(logInterceptor);

            OkHttpClient client = builder.build();
            retrofitBuilder.client(client);

            retrofit = retrofitBuilder.build();
        }
        return retrofit;
    }
}
