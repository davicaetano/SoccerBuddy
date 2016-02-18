package com.davicaetano.soccerbuddy.data.user;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import javax.inject.Inject;

/**
 * Created by davicaetano on 2/16/16.
 */
public class UserManager {
    private final static String USER_PREF = "user_pref";
    private final static String USER_PREF_INFO = "user_info_gson";
    SharedPreferences pref;
    private User user;

    @Inject
    Context context;

    @Inject
    Gson gson;

    @Inject
    public UserManager(Context context, Gson gson){
        this.context = context;
        this.gson = gson;
        pref = context.getSharedPreferences(USER_PREF, context.MODE_PRIVATE);
        getUserFromPref();
    }

    private void getUserFromPref(){
        String userGson = pref.getString(USER_PREF_INFO,null);
        if(userGson == null){
            user = null;
        }else{
            user = gson.fromJson(userGson, User.class);
        }
    }

    public User getUser(){
        return user;
    }

    public void recordLocalUser(User user){
        this.user = user;
        String userGson = gson.toJson(user);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString(USER_PREF_INFO, userGson);
        prefEditor.commit();
    }
}
