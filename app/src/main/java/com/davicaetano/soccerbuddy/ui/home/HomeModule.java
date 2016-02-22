package com.davicaetano.soccerbuddy.ui.home;

import com.davicaetano.soccerbuddy.ui.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {
    private HomeActivity homeActivity;

    public HomeModule(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    @Provides
    @ActivityScope
    HomeActivity provideHomeActivity(){return homeActivity;}

    @Provides
    @ActivityScope
    HomePresenter provideHomeActivityPresenter(){
        return new HomePresenter(homeActivity);
    }
}
