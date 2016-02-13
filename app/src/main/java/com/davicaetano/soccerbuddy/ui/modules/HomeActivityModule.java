package com.davicaetano.soccerbuddy.ui.modules;

import com.davicaetano.soccerbuddy.ui.ActivityScope;
import com.davicaetano.soccerbuddy.ui.HomeActivity;
import com.davicaetano.soccerbuddy.ui.presenter.HomeActivityPresenter;

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
