package com.davicaetano.soccerbuddy.ui.modules;

import android.content.Context;

import com.davicaetano.soccerbuddy.ui.ActivityScope;
import com.davicaetano.soccerbuddy.ui.SplashActivity;
import com.davicaetano.soccerbuddy.ui.presenter.SplashActivityPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class SplashActivityModule {
    private SplashActivity splashActivity;

    public SplashActivityModule(SplashActivity splashActivity){
        this.splashActivity = splashActivity;
    }

    @Provides
    @ActivityScope
    SplashActivity provideSplashActivity(){return splashActivity;}

    @Provides
    @ActivityScope
    SplashActivityPresenter provideSplashActivityPresenter(SplashActivity splashActivity, Context context){
        return new SplashActivityPresenter(splashActivity, context);
    }

}
