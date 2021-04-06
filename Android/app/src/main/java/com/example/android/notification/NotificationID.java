package com.example.android.notification;

import java.util.concurrent.atomic.AtomicInteger;

/*
NotificationID : notification id 발급
 */
public class NotificationID {
    private final static AtomicInteger c = new AtomicInteger(0);

    public static int getID() {
        return c.incrementAndGet();
    }
}