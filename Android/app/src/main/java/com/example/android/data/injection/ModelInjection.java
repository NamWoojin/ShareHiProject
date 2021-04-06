package com.example.android.data.injection;


import com.example.android.data.model.SocketRepository;
import com.example.android.data.modelImpl.SocketRepositoryImpl;

/*
ModelInjection : Repository의존성 주입
 */
public class ModelInjection {

    private ModelInjection() {
        // No instance
    }

    public static SocketRepository provideSocketRepository(){
        return new SocketRepositoryImpl();
    }
}
