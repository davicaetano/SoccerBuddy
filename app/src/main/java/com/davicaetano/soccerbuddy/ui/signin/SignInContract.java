package com.davicaetano.soccerbuddy.ui.signin;

import com.davicaetano.soccerbuddy.data.user.User;

/**
 * Created by davicaetano on 2/15/16.
 */
public interface SignInContract {
    interface View {

    }

    interface Presenter {
        void recordUser(User user);
    }
}
