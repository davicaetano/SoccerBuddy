package com.davicaetano.soccerbuddy.ui.home;

import com.davicaetano.soccerbuddy.ui.ActivityScope;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(
        modules = {
                HomeModule.class
        }
)
public interface HomeComponent {
    HomeActivity inject(HomeActivity homeActivity);
}
