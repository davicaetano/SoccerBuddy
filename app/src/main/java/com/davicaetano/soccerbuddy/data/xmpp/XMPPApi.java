package com.davicaetano.soccerbuddy.data.xmpp;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by davicaetano on 2/19/16.
 */
public class XMPPApi{
    @Inject
    Context context;

    @Inject
    ChatApi chatApi;

    @Inject
    public XMPPApi(Context context) {
        this.context = context;
    }

    @Inject
    public void injectChatApi(ChatApi chatApi){
        this.chatApi = chatApi;
    }
       
    public void register(String user, String password){
        Intent XMPPIntent = new Intent(context,XMPPService.class);
        XMPPIntent.putExtra(XMPPService.ACTION, XMPPService.ACTION_REGISTER);
        XMPPIntent.putExtra(XMPPService.USER_ID, user);
        XMPPIntent.putExtra(XMPPService.USER_PASSWORD, password);
        context.startService(XMPPIntent);
    }

    public void login(String user, String password){
        Intent XMPPIntent = new Intent(context,XMPPService.class);
        XMPPIntent.putExtra(XMPPService.ACTION, XMPPService.ACTION_LOGIN);
        XMPPIntent.putExtra(XMPPService.USER_ID, user);
        XMPPIntent.putExtra(XMPPService.USER_PASSWORD, password);
        context.startService(XMPPIntent);
    }

    public void disconnect() {
        Intent XMPPIntent = new Intent(context,XMPPService.class);
        XMPPIntent.putExtra(XMPPService.ACTION, XMPPService.ACTION_LOGOUT);
        context.startService(XMPPIntent);
    }

    public void getList() {
        Intent XMPPIntent = new Intent(context,XMPPService.class);
        XMPPIntent.putExtra(XMPPService.ACTION, XMPPService.ACTION_FRIEND_LIST);
        context.startService(XMPPIntent);
    }

    public void onLogin() {
        chatApi.onLogin();
    }

    public void onListReceived(ArrayList<String> list){
        chatApi.onListReceived(list);
    }
}
