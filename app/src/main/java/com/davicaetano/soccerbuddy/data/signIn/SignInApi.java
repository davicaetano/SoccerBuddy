package com.davicaetano.soccerbuddy.data.signIn;

import com.davicaetano.soccerbuddy.ui.signin.SignInContract;

/**
 * Created by davicaetano on 5/1/16.
 */
public interface SignInApi {
    void clearToken();
    void getToken();
    void setPresenter(SignInContract.Presenter presenter);
}
