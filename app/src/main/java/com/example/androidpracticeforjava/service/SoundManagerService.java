package com.example.androidpracticeforjava.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SoundManagerService extends Service {
    public SoundManagerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
