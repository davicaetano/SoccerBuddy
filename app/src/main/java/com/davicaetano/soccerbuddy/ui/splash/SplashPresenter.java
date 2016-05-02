package com.davicaetano.soccerbuddy.ui.splash;

import com.davicaetano.soccerbuddy.data.user.User;
import com.davicaetano.soccerbuddy.data.user.UserManager;

import javax.inject.Inject;

public class SplashPresenter implements SplashContract.Presenter {
    private final String TAG = "SplashPresenter";

    @Inject
    SplashContract.View view;

    @Inject
    UserManager userManager;

    public SplashPresenter(SplashContract.View splashActivity, UserManager userManager){
        this.view = splashActivity;
        this.userManager = userManager;
    }

    @Override
    public boolean hasCachedUser() {
//        return (userManager.getUser() != null);
        return false;
    }

    @Override
    public User getCachedUser() {
        return userManager.getUser();
    }
}
