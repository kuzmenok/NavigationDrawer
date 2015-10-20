package com.example.sok.navigationdrawer.app;

import android.app.Application;

import com.example.sok.navigationdrawer.database.HelperFactory;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HelperFactory.setHelper(getApplicationContext());
    }
    @Override
    public void onTerminate() {
        HelperFactory.releaseHelper();
        super.onTerminate();
    }
}
