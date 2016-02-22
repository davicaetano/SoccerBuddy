package com.davicaetano.soccerbuddy.data.xmpp;


import android.content.Context;

import com.davicaetano.soccerbuddy.data.user.UserManager;

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

    @Provides
    @Singleton
    XMPPApi xmppapi(Context context){
        return new XMPPApi(context);
    }

    @Provides
    @Singleton
    ChatApi chatAPI(UserManager userManager, XMPPApi xmppApi){
        ChatApi chatApi = new ChatApi(userManager, xmppApi);
        xmppApi.injectChatApi(chatApi);
        return chatApi;
    }
}
