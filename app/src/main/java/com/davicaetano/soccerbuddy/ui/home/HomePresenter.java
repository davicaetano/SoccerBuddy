package com.davicaetano.soccerbuddy.ui.home;

import com.davicaetano.soccerbuddy.data.xmpp.ChatApi;

import java.util.ArrayList;

import javax.inject.Inject;

public class HomePresenter implements HomeContract.Presenter{
    @Inject
    ChatApi chatApi;

    @Inject HomeContract.View view;

    @Inject
    public HomePresenter(ChatApi chatApi, HomeContract.View homeActivity) {
        this.chatApi = chatApi;
        this.view = homeActivity;
        chatApi.setPresenter(this);
    }

    //////Methods

    @Override
    public void login() {
        chatApi.login();
    }

    @Override
    public void disconnect() {
        chatApi.disconnect();
    }

    @Override
    public void getList() {
        chatApi.getList();
    }

    ///// Callbacks.


    @Override
    public void onLogin() {
        view.writeOnListItem("Logged in.");
    }

    @Override
    public void onListReceived(ArrayList<String> list) {
        for(String item:list) {
            view.writeOnListItem(item);
        }
    }

}
