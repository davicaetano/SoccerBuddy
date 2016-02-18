package com.davicaetano.soccerbuddy.ui.home;

import com.davicaetano.soccerbuddy.ui.ActivityScope;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(
        modules = {
                HomeActivityModule.class
        }
)
public interface HomeActivityComponent {

    HomeActivity inject(HomeActivity homeActivity);
}
