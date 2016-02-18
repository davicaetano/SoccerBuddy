package com.davicaetano.soccerbuddy.ui.home;

import com.davicaetano.soccerbuddy.ui.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeActivityModule {
    private HomeActivity homeActivity;

    public HomeActivityModule(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    @Provides
    @ActivityScope
    HomeActivity provideHomeActivity(){return homeActivity;}

    @Provides
    @ActivityScope
    HomeActivityPresenter provideHomeActivityPresenter(){
        return new HomeActivityPresenter(homeActivity);
    }
}
