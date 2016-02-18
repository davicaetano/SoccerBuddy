package com.davicaetano.soccerbuddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final CustomApplication application;
    public AppModule(CustomApplication application){this.application = application;}

    @Provides
    @Singleton
    public CustomApplication application(){ return this.application;}

    @Provides
    @Singleton
    public Context applicationContext(){return this.application;}

    @Provides
    @Singleton
    public SharedPreferences sharedPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    public Gson provideGson(){
        return new Gson();
    }
}
