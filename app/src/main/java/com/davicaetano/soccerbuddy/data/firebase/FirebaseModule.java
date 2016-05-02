package com.davicaetano.soccerbuddy.data.firebase;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by davicaetano on 5/1/16.
 */
@Module
public class FirebaseModule {
    @Provides
    @Singleton
    FirebaseApi providesFirebaseApi(Context context){
        return new FirebaseApi(context);
    }
}
