package com.davicaetano.soccerbuddy.data.xmpp;

import android.content.BroadcastReceiver;

import com.davicaetano.soccerbuddy.data.user.UserManager;
import com.davicaetano.soccerbuddy.ui.home.HomeBroadcast;

import javax.inject.Inject;

/**
 * Created by davicaetano on 2/19/16.
 */
public class ChatApi {
    @Inject UserManager userManager;

    @Inject
    XMPPApi xmppApi;

    BroadcastReceiver broadcastReceiver;

    public ChatApi(UserManager userManager, XMPPApi xmppApi) {
        this.userManager = userManager;
        this.xmppApi = xmppApi;
        broadcastReceiver = new HomeBroadcast()
    }

    public void getList() {
        xmppApi.getList();
    }

    public void login(){
        xmppApi.login(userManager.getUser().getEmail(), userManager.getUser().getPassword());
    }

    public void disconnect() {
        xmppApi.disconnect();
    }
}
