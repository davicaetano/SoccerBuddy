package com.davicaetano.soccerbuddy.data.user;

import android.content.Context;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {

    @Provides
    @Singleton
    UserManager provideUserManager(Context context, Gson gson){
        return new UserManager(context, gson);
    }

}
