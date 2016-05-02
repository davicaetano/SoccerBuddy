package com.davicaetano.soccerbuddy;

import android.app.Application;
import android.content.Context;

import com.davicaetano.soccerbuddy.utils.Utils;


/**
 * Created by davicaetano on 1/17/16.
 */
public class CustomApplication extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent =  DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
        Utils.context = this;
    }

    public static CustomApplication get(Context context){
        return (CustomApplication) context.getApplicationContext();
    }

    public AppComponent getAppComponent(){return appComponent;}
}
