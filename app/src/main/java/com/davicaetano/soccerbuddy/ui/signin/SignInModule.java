package com.davicaetano.soccerbuddy.ui.signin;

import com.davicaetano.soccerbuddy.data.firebase.FirebaseApi;
import com.davicaetano.soccerbuddy.data.signIn.GoogleSignIn;
import com.davicaetano.soccerbuddy.data.user.UserManager;
import com.davicaetano.soccerbuddy.ui.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class SignInModule {
    private SignInContract.View view;

    public SignInModule(SignInContract.View view){
        this.view = view;
    }

    @ActivityScope
    @Provides
    SignInContract.View provideSignInActivity(){
        return view;
    }

    @ActivityScope
    @Provides
    SignInContract.Presenter provideSignInPresenter(UserManager userManager, GoogleSignIn googleSignIn, FirebaseApi firebaseApi){
        return new SignInPresenter(view, userManager, googleSignIn, firebaseApi);
    }

}
