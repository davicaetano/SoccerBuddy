package com.davicaetano.soccerbuddy.data.signIn;

import android.content.Context;

import com.davicaetano.soccerbuddy.data.user.UserManager;
import com.davicaetano.soccerbuddy.ui.ActivityScope;
import com.davicaetano.soccerbuddy.ui.signin.SignInContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by davicaetano on 5/1/16.
 */
@Module
public class GoogleSignInModule {
    @Provides
    @ActivityScope
    GoogleSignIn provideGoogleSignIn(Context context, UserManager userManager, SignInContract.View view){
        return new GoogleSignIn(context, userManager, view);
    }
}
