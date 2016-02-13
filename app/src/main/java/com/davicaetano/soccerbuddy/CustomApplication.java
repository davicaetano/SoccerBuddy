package com.davicaetano.soccerbuddy;

import android.app.Application;
import android.content.Context;

import com.davicaetano.soccerbuddy.data.model.User;


/**
 * Created by davicaetano on 1/17/16.
 */
public class CustomApplication extends Application {
    private AppComponent appComponent;
    private UserComponent userComponent;



    @Override
    public void onCreate() {
        super.onCreate();
        appComponent =  DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static CustomApplication get(Context context){
        return (CustomApplication) context.getApplicationContext();
    }

    public UserComponent createUserComponent(User user){
        userComponent = appComponent.plus(new UserModule(user));
        return userComponent;
    }

    public AppComponent getAppComponent(){return appComponent;}

    public UserComponent getUserComponent() {
        return userComponent;
    }
}
