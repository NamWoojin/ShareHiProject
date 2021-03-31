package com.example.android.data.injection;


import com.example.android.data.model.SocketRepository;
import com.example.android.data.modelImpl.SocketRepositoryImpl;

/**
 * @author firefly2.kim
 * @since 19. 8. 25
 */
public class ModelInjection {

    private ModelInjection() {
        // No instance
    }

    public static SocketRepository provideSocketRepository(){
        return new SocketRepositoryImpl();
    }
}
