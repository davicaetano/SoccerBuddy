package com.davicaetano.soccerbuddy.data.xmpp.model;

public class FriendModel {
	String iUserID,vUserName,eAnonymous,vProfilePic,is_block,is_abuse,iFriendID,ISMOREAVAILABLE;
    private String Unread;
	private String online_status;


	public String getOnline_status() {
		return online_status;
	}

	public void setOnline_status(String online_status) {
		this.online_status = online_status;
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

	public String getIs_block() {
		return is_block;
	}

	public void setIs_block(String is_block) {
		this.is_block = is_block;
	}

	public String getIs_abuse() {
		return is_abuse;
	}

	public void setIs_abuse(String is_abuse) {
		this.is_abuse = is_abuse;
	}

	public String getiFriendID() {
		return iFriendID;
	}

	public void setiFriendID(String iFriendID) {
		this.iFriendID = iFriendID;
	}


    public String getISMOREAVAILABLE() {
        return ISMOREAVAILABLE;
    }

    public void setISMOREAVAILABLE(String ISMOREAVAILABLE) {
        this.ISMOREAVAILABLE = ISMOREAVAILABLE;
    }

    public String geteAnonymous() {
        return eAnonymous;
    }

    public void seteAnonymous(String eAnonymous) {
        this.eAnonymous = eAnonymous;
    }

    public String getUnread() {
        return Unread;
    }

    public void setUnread(String unread) {
        Unread = unread;
    }
}
