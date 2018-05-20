package com.arctouch.codechallenge;

import android.app.Application;

public class MainApplication extends Application {

    private static MainApplication INSTANCE;

    public void onCreate(){
        super.onCreate();

        if(INSTANCE == null){
            INSTANCE = this;
        }
    }

    public static MainApplication getInstance(){
        return INSTANCE;
    }

}
