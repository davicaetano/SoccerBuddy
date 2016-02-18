package com.davicaetano.soccerbuddy.data.xmpp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class HomeModel implements Parcelable {

    public HomeModel(){

    }

    public String getiUserID() {
        return iUserID;
    }

    public void setiUserID(String iUserID) {
        this.iUserID = iUserID;
    }

    public String getvUserName() {
        return vUserName;
    }

    public void setvUserName(String vUserName) {
        this.vUserName = vUserName;
    }

    public String getvProfilePic() {
        return vProfilePic;
    }

    public void setvProfilePic(String vProfilePic) {
        this.vProfilePic = vProfilePic;
    }

    public String geteLocationShare() {
        return eLocationShare;
    }

    public void seteLocationShare(String eLocationShare) {
        this.eLocationShare = eLocationShare;
    }

    public double getdLat() {
        return dLat;
    }

    public void setdLat(double dLat) {
        this.dLat = dLat;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getdLong() {
        return dLong;
    }

    public void setdLong(double dLong) {
        this.dLong = dLong;
    }
    public String geteAnonymous() {
        return eAnonymous;
    }

    public void seteAnonymous(String eAnonymous) {
        this.eAnonymous = eAnonymous;
    }

    public String getOnline_status() {
        return online_status;
    }

    public void setOnline_status(String online_status) {
        this.online_status = online_status;
    }

    String iUserID, vUserName, vProfilePic, eLocationShare,eAnonymous,online_status;
    double dLat, dLong, distance;

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(iUserID);
        dest.writeString(vUserName);
        dest.writeString(vProfilePic);
        dest.writeString(eLocationShare);
        dest.writeString(eAnonymous);
        dest.writeString(online_status);
        dest.writeDouble(dLat);
        dest.writeDouble(dLong);
        dest.writeDouble(distance);

    }

    public HomeModel(Parcel in){
        iUserID=in.readString();
        vUserName=in.readString();
        vProfilePic=in.readString();
        eLocationShare=in.readString();
        eAnonymous=in.readString();
        online_status=in.readString();
        dLat=in.readDouble();
        dLong=in.readDouble();
        distance=in.readDouble();

    }

    public static final Parcelable.Creator<HomeModel> CREATOR = new Parcelable.Creator<HomeModel>() {
        @Override
        public HomeModel createFromParcel(Parcel source) {
            return new HomeModel(source);
        }

        @Override
        public HomeModel[] newArray(int size) {
            return new HomeModel[size];
        }
    };


}
