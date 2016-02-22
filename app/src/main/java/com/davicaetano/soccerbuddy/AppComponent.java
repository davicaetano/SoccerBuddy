package com.davicaetano.soccerbuddy;

import android.content.Context;
import android.content.SharedPreferences;

import com.davicaetano.soccerbuddy.data.user.UserManager;
import com.davicaetano.soccerbuddy.data.user.UserModule;
import com.davicaetano.soccerbuddy.data.xmpp.XMPPModule;
import com.davicaetano.soccerbuddy.data.xmpp.XMPPService;
import com.davicaetano.soccerbuddy.ui.home.HomeComponent;
import com.davicaetano.soccerbuddy.ui.home.HomeModule;
import com.davicaetano.soccerbuddy.ui.signin.SignInComponent;
import com.davicaetano.soccerbuddy.ui.signin.SignInModule;
import com.davicaetano.soccerbuddy.ui.splash.SplashComponent;
import com.davicaetano.soccerbuddy.ui.splash.SplashModule;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                AppModule.class,
                UserModule.class,
                XMPPModule.class
        }
)
public interface AppComponent {
    CustomApplication application();
    Context applicationContext();
    SharedPreferences sharedPreferences();
    Gson gson();
    UserManager userManager();

    XMPPService inject(XMPPService xmppService);

    //subcomponents.
    SplashComponent plus(SplashModule splashModule);
    SignInComponent plus(SignInModule signInModule);
    HomeComponent plus(HomeModule homeModule);
}
