package com.davicaetano.soccerbuddy.ui.signin;

import com.davicaetano.soccerbuddy.ui.ActivityScope;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(
        modules = SignInModule.class
)
public interface SignInComponent {
    SignInActivity inject(SignInActivity signInActivity);
}
