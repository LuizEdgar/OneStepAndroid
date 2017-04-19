package com.lutzed.servoluntario;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by luizfreitas on 18/04/2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        Stetho.initializeWithDefaults(this);
    }
}
