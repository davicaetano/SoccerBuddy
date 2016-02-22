package com.davicaetano.soccerbuddy.data.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.davicaetano.soccerbuddy.utils.Utils;

public class User implements Parcelable{

    public static final int FACEBOOK = 1;
    public static final int GOOGLE = 2;

    private String email;
    private String name;
    private String pictureUrl;
    private String ID;
    private int loginMode;

    private boolean isRegistered = false;

    //Parceable stuff
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
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeInt(loginMode);
        dest.writeString(name);
        dest.writeString(pictureUrl);
        dest.writeByte((byte)(isRegistered?1:0));
    }
    public User(Parcel source){
        this.email = source.readString();
        this.loginMode = source.readInt();
        this.name = source.readString();
        this.pictureUrl = source.readString();
        this.isRegistered = source.readByte() != 0;
    }


    public User(){
    }

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return Utils.password(email);}

    public int getLoginMode() {return loginMode;}
    public void setLoginMode(int loginMode) {this.loginMode = loginMode;}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean isRegistered() {return isRegistered;}
    public void setIsRegistered(boolean isRegistered) {this.isRegistered = isRegistered;}

}
