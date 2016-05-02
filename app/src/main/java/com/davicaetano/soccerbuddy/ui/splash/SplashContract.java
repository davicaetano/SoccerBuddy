package com.davicaetano.soccerbuddy.ui.splash;

import com.davicaetano.soccerbuddy.data.user.User;

/**
 * Created by davicaetano on 2/15/16.
 */
public interface SplashContract {
    interface View {
        void progressBar(boolean turnOn);
        void goesToSignIn();
        void goesToHome();
    }

    interface Presenter{
        boolean hasCachedUser();
        User getCachedUser();
    }
}
