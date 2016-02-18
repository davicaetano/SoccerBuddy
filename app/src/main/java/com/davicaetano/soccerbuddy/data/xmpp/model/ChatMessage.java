package com.davicaetano.soccerbuddy.data.xmpp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatMessage implements Parcelable {

	public String _id; //  DEFAULT ID WHICH WILL AUTO GENERATE.
	public String msg_id; // ID OF THE MESSAGE
	public String sender_id;// ID OF THE  PERSON WHO WILL SEND MESSAGE.
	public String vname; // NAME OF PERSON WHO SEND MESSAGE
	public String receiver_id; //ID OF THE PERSON WHO WILL RECEIVE THE MESSAGE. // IN CASE OF GROUP CHAT RECEIVER ID WILL STORE  USER PROFILE PATH.
	public String msg_date; // MESSAGE DATE
	public String msg_text; // MESSAGE TEXT
    public String isdeliver; // BOOLEAN TO TRACE WHETHER MESSAGE DELIVER OR NOT
    public String isread; // BOOLEAN TO CHECK WHETHER SENT MESSAGE IS READ OR NOT
    public String group_id; // ID OF THE GROUP
	public String group_name; // NAME OF THE GROUP
	public String group_name_por; // NAME OF THE GROUP in por
    public String status;
	public String user_id;
	public String user_image; //IT USE WHILE USER PROFILE UPDATE & IN A SINGLE CHAT OBJECT.
	public String isForImage;

	public ChatMessage() {

	}

	ChatMessage(Parcel source) {
		// TODO Auto-generated constructor stub
		readFromParcel(source);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

		dest.writeString(_id);
		dest.writeString(msg_id);
		dest.writeString(sender_id);
		dest.writeString(vname);
		dest.writeString(receiver_id);
		dest.writeString(msg_date);
		dest.writeString(msg_text);
        dest.writeString(status);
        dest.writeString(isdeliver);
        dest.writeString(isread);
        dest.writeString(group_id);
		dest.writeString(group_name);
		dest.writeString(group_name_por);
		dest.writeString(user_id);
		dest.writeString(user_image);
		dest.writeString(isForImage);



	}

	private void readFromParcel(Parcel parcel) {
		_id = parcel.readString();
        msg_id = parcel.readString();
        sender_id = parcel.readString();
		vname = parcel.readString();
        receiver_id = parcel.readString();
        msg_date = parcel.readString();
        msg_text = parcel.readString();
        status = parcel.readString();
        isdeliver= parcel.readString();
        isread=parcel.readString();
        group_id= parcel.readString();
		group_name= parcel.readString();
		group_name_por= parcel.readString();
		user_id= parcel.readString();
		user_image= parcel.readString();
		isForImage= parcel.readString();
	}

	// Method to recreate a Question from a Parcel
	public static Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {

		@Override
		public ChatMessage createFromParcel(Parcel source) {
			return new ChatMessage(source);
		}

		@Override
		public ChatMessage[] newArray(int size) {
			return new ChatMessage[size];
		}

	};

	@Override
	public int describeContents() {
		return 0;
	}

}
