package com.davicaetano.soccerbuddy;

import android.content.Context;
import android.content.SharedPreferences;

import com.davicaetano.soccerbuddy.ui.components.SplashActivityComponent;
import com.davicaetano.soccerbuddy.ui.modules.SplashActivityModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                AppModule.class
        }
)
public interface AppComponent {
    CustomApplication application();
    Context applicationContext();
    SharedPreferences sharedPreferences();

    //subcomponents.
    SplashActivityComponent plus(SplashActivityModule splashActivityModule);
    UserComponent plus(UserModule userModule);
}
