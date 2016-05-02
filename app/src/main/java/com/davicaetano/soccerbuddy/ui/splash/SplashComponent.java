package com.davicaetano.soccerbuddy.ui.splash;

import com.davicaetano.soccerbuddy.ui.ActivityScope;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(
        modules = SplashModule.class
)
public interface SplashComponent {
    SplashActivity inject(SplashActivity view);
}
