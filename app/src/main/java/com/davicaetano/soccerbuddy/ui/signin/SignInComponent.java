package com.davicaetano.soccerbuddy.ui.signin;

import com.davicaetano.soccerbuddy.data.signIn.GoogleSignIn;
import com.davicaetano.soccerbuddy.data.signIn.GoogleSignInModule;
import com.davicaetano.soccerbuddy.ui.ActivityScope;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(
        modules = {
                SignInModule.class,
                GoogleSignInModule.class
        }
)
public interface SignInComponent {
    GoogleSignIn googleSignIn();

    SignInActivity inject(SignInActivity signInActivity);
}
