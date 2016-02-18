package com.davicaetano.soccerbuddy.data.xmpp.model;

public class NotificationReceivedModel {
    String vUserName,vProfilePic,iSenderID;

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

    public String getiSenderID() {
        return iSenderID;
    }

    public void setiSenderID(String iSenderID) {
        this.iSenderID = iSenderID;
    }
}
