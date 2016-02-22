package com.davicaetano.soccerbuddy.ui.signin;

import com.davicaetano.soccerbuddy.data.user.User;
import com.davicaetano.soccerbuddy.data.user.UserManager;

import javax.inject.Inject;

/**
 * Created by davicaetano on 2/15/16.
 */
public class SignInPresenter implements SignInContract.Presenter{
    private static final String TAG = "SignInPresenter";

    @Inject
    SignInContract.View view;

    @Inject
    UserManager userManager;

    @Inject
    public SignInPresenter(SignInContract.View view, UserManager userManager){
        this.view = view;
        this.userManager = userManager;
    }

    @Override
    public void recordUser(User user) {
        userManager.recordLocalUser(user);
    }
}
