package com.davicaetano.soccerbuddy.data.xmpp;

import com.davicaetano.soccerbuddy.data.user.UserManager;
import com.davicaetano.soccerbuddy.ui.home.HomeContract;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by davicaetano on 2/19/16.
 */
public class ChatApi {
    private HomeContract.Presenter presenter;

    @Inject UserManager userManager;

    @Inject XMPPApi xmppApi;

    @Inject
    public ChatApi(UserManager userManager, XMPPApi xmppApi) {
        this.userManager = userManager;
        this.xmppApi = xmppApi;
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

    public void onLogin() {
        presenter.onLogin();
    }

    public void onListReceived(ArrayList<String> list) {
        presenter.onListReceived(list);
    }

    public void setPresenter(HomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

}
