package com.davicaetano.soccerbuddy.ui.home;

import java.util.ArrayList;

/**
 * Created by davicaetano on 2/15/16.
 */
public interface HomeContract {
    interface View{
        void writeOnListItem(String line);

    }
    interface Presenter{
        void login();

        void disconnect();

        void getList();

        void onLogin();

        void onListReceived(ArrayList<String> list);
    }
}
