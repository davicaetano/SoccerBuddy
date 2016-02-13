package com.davicaetano.soccerbuddy.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
    public static final int FACEBOOK = 1;
    public static final int GOOGLE = 2;
    private String email;
    private int loginMode;

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>(){
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


    public User(){
    }

    public User(Parcel source){
        this.email = source.readString();
        this.loginMode = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeInt(loginMode);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLoginMode() {
        return loginMode;
    }

    public void setLoginMode(int loginMode) {
        this.loginMode = loginMode;
    }
}
