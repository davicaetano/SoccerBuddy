package com.davicaetano.soccerbuddy.data.xmpp;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class XMPPModule {

    @Provides
    @Singleton
    XMPPHelper xmppHelper(Context context){
        return new XMPPHelper(context);
    }
}
