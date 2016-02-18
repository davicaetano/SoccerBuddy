package com.davicaetano.soccerbuddy.ui.splash;

import com.davicaetano.soccerbuddy.data.user.UserManager;
import com.davicaetano.soccerbuddy.ui.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class SplashModule {
    private SplashContract.View view;

    public SplashModule(SplashContract.View view){
        this.view = view;
    }

    @Provides
    @ActivityScope
    SplashContract.View provideView(){return view;}

    @Provides
    @ActivityScope
    SplashContract.Presenter prov(SplashContract.View otherView, UserManager userManager){
        return new SplashPresenter(view, userManager );
    }
}
