package com.davicaetano.soccerbuddy.ui.components;

import com.davicaetano.soccerbuddy.ui.ActivityScope;
import com.davicaetano.soccerbuddy.ui.SplashActivity;
import com.davicaetano.soccerbuddy.ui.modules.SplashActivityModule;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(
        modules = SplashActivityModule.class
)
public interface SplashActivityComponent {

    SplashActivity inject(SplashActivity splashActivity);

}
