package com.davicaetano.soccerbuddy.data.xmpp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.davicaetano.soccerbuddy.data.user.User;

import java.util.ArrayList;

public class GroupModel implements Parcelable {


	public ArrayList<User> getUserDetailList() {
		return UserDetailList;
	}

	public void setUserDetailList(ArrayList<User> userDetailList) {
		UserDetailList = userDetailList;
	}

	ArrayList<User> UserDetailList= new ArrayList<>();

    String iGroupID, vGroupName,vGroupName_Portuguese, vGroupDesc,vGroupDesc_Portuguese, vTopicArn, total_users, is_joined;
    private GroupModel(Parcel source) {
        iGroupID  = source.readString();
        vGroupName = source.readString();
		vGroupName_Portuguese = source.readString();
        vGroupDesc = source.readString();
		vGroupDesc_Portuguese = source.readString();
        vTopicArn = source.readString();
        total_users = source.readString();
        is_joined = source.readString();
		source.readTypedList(UserDetailList, User.CREATOR);
    }


    public GroupModel(){

    }
	public String getiGroupID() {
		return iGroupID;
	}

	public void setiGroupID(String iGroupID) {
		this.iGroupID = iGroupID;
	}

	public String getvGroupName() {
		return vGroupName;
	}

	public void setvGroupName(String vGroupName) {
		this.vGroupName = vGroupName;
	}

	public String getvGroupDesc() {
		return vGroupDesc;
	}

	public void setvGroupDesc(String vGroupDesc) {
		this.vGroupDesc = vGroupDesc;
	}

	public String getvTopicArn() {
		return vTopicArn;
	}

	public void setvTopicArn(String vTopicArn) {
		this.vTopicArn = vTopicArn;
	}

	public String getTotal_users() {
		return total_users;
	}

	public void setTotal_users(String total_users) {
		this.total_users = total_users;
	}

	public String getIs_joined() {
		return is_joined;
	}

        public void setIs_joined(String is_joined) {
            this.is_joined = is_joined;
        }


	public String getvGroupName_Portuguese() {
		return vGroupName_Portuguese;
	}

	public void setvGroupName_Portuguese(String vGroupName_Portuguese) {
		this.vGroupName_Portuguese = vGroupName_Portuguese;
	}

	public String getvGroupDesc_Portuguese() {
		return vGroupDesc_Portuguese;
	}

	public void setvGroupDesc_Portuguese(String vGroupDesc_Portuguese) {
		this.vGroupDesc_Portuguese = vGroupDesc_Portuguese;
	}






    	@Override
    	public int describeContents() {
    		// TODO Auto-generated method stub
    		return 0;
    	}

    	@Override
    	public void writeToParcel(Parcel dest, int flags) {
    		  dest.writeString(iGroupID);
    		  dest.writeString(vGroupName);
			dest.writeString(vGroupName_Portuguese);
    		  dest.writeString(vGroupDesc);
			dest.writeString(vGroupDesc_Portuguese);
    		  dest.writeString(vTopicArn);
    		  dest.writeString(total_users);
    		  dest.writeString(is_joined);
			dest.writeTypedList(UserDetailList);

    	}


    	public static final Parcelable.Creator<GroupModel> CREATOR = new Parcelable.Creator<GroupModel>() {
    	    @Override
    	    public GroupModel[] newArray(int size) {
    	        return new GroupModel[size];
    	    }

    	    @Override
    	    public GroupModel createFromParcel(Parcel source) {
    	        return new GroupModel(source);
    	    }
    	};


}
