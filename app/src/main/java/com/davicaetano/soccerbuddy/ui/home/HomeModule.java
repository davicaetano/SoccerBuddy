package com.davicaetano.soccerbuddy.ui.home;

import com.davicaetano.soccerbuddy.data.xmpp.ChatApi;
import com.davicaetano.soccerbuddy.ui.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {
    private HomeContract.View view;

    public HomeModule(HomeContract.View view) {
        this.view = view;
    }

    @Provides
    @ActivityScope
    HomeContract.View provideHomeActivity(){return view;}

    @Provides
    @ActivityScope
    HomeContract.Presenter provideHomePresenter(ChatApi chatApi){
        return new HomePresenter(chatApi, view);
    }
}
