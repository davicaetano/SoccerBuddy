package com.davicaetano.soccerbuddy.ui.components;

import com.davicaetano.soccerbuddy.ui.ActivityScope;
import com.davicaetano.soccerbuddy.ui.HomeActivity;
import com.davicaetano.soccerbuddy.ui.modules.HomeActivityModule;

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
