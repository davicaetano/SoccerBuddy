package com.davicaetano.soccerbuddy;


import com.davicaetano.soccerbuddy.ui.components.HomeActivityComponent;
import com.davicaetano.soccerbuddy.ui.modules.HomeActivityModule;

import dagger.Subcomponent;

@UserScope
@Subcomponent(
        modules = {
            UserModule.class
        }
)
public interface UserComponent {

    HomeActivityComponent plus(HomeActivityModule homeActivityModule);

}
