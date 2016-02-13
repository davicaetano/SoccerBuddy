package com.davicaetano.soccerbuddy;

import com.davicaetano.soccerbuddy.data.model.User;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {
    private User user;

    public UserModule(User user){this.user = user;}

    @Provides
    @UserScope
    User provideUser(){return this.user;}

}
