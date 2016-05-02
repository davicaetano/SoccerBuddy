package com.davicaetano.soccerbuddy.ui.signin;

import android.content.Intent;

import com.google.android.gms.common.api.Scope;

/**
 * Created by davicaetano on 2/15/16.
 */
public interface SignInContract {
    interface View {

        void changeProgressDialog(boolean on);

        void showMessage(String s);
    }

    interface Presenter {
        void onClickGoogleSignIn();

        void onActivityResult(int requestCode, int resultCode, Intent data);

//        SignInContract.View getView();

        void tokenCallback(String token);

        Scope[] getScopeArray();
    }
}
