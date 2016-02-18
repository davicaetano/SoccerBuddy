package com.davicaetano.soccerbuddy.data.xmpp.model;

public class NotificationSentModel {

    String iFriendID,iReceiverID,vUserName,vProfilePic,eRequestStatus;

    public String getiReceiverID() {
        return iReceiverID;
    }

    public String getiFriendID() {
        return iFriendID;
    }

    public void setiFriendID(String iFriendID) {
        this.iFriendID = iFriendID;
    }

    public void setiReceiverID(String iReceiverID) {
        this.iReceiverID = iReceiverID;
    }

    public String geteRequestStatus() {
        return eRequestStatus;
    }

    public void seteRequestStatus(String eRequestStatus) {
        this.eRequestStatus = eRequestStatus;
    }

    public String getvProfilePic() {
        return vProfilePic;
    }

    public void setvProfilePic(String vProfilePic) {
        this.vProfilePic = vProfilePic;
    }

    public String getvUserName() {
        return vUserName;
    }

    public void setvUserName(String vUserName) {
        this.vUserName = vUserName;
    }
}
