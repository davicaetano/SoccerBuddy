package com.davicaetano.soccerbuddy.data.xmpp;

public class XMPPConstants {
    public static final String PARAMS_TO = "to";
    public static final String PARAMS_MYJID = "myJid";
    public static final String PARAMS_ID = "id";
    public static final String PARAMS_DATE = "date";
    public static final String PARAMS_VNAME = "vName";

    public static final String PARAMS_USERID = "user_id";
    public static final String PARAMS_USERIMAGE = "user_image";
    public static final String PARAMS_ISFORIMAGE = "isForImage";

    public static final String PARAMS_TEXT = "text";

    public static final String PARAMS_ISRECEIVE = "IsDeliver";
    public static final String PARAMS_ISREAD = "IsRead";
    public static final String PARAMS_GROUPID = "GroupId";
    public static final String PARAMS_GROUPNAME = "GroupName";
    public static final String PARAMS_GROUPNAMEPOR = "GroupNamePor";


    public static String STATUS_TYPE_PENDING = "pending";
    public static String STATUS_TYPE_PROCESS = "process";
    public static String STATUS_TYPE_SENT = "sent";
    public static String STATUS_TYPE_DELIVER = "deliver";


    public static String USER_PRESENCE_ONLINE = "online";
    public static String USER_PRESENCE_OFFLINE = "offline";

    //CUSTOM XMPP MESSAGE OBJECT
    public static String MESSAGE_TAG="message";
    public static String MESSAGE_ID="message_id";
    public static String MESSAGE_TEXT="message_text";
    public static String MESSAGE_DATE="message_date";
    public static String SENDER_NAME="sender_name";
    public static String SENDER_ID="sender_id";
    public static String RECEIVER_ID="receiver_id";
    public static String GROUP_ID="group_id";
    public static String GROUP_NAME="group_name";
    public static String GROUP_NAME_POR="group_name_por";
    public static String GROUP_RECEIVER_ID="receiverIDs";


    //CUSTOM XMPP UPDATE PROFILE
    public static String PROFILE_USERID="user_id";
    public static String PROFILE_USERNAME="user_name";
    public static String PROFILE_USERIMAGE="user_image";
    public static String PROFILE_ISFORIMAGE="isForImage";
    ///
    public static String PREF_GROUPLIST = "GroupList";
    public static String EXTRA_RECEIVER_ID = "ReceiverId";
    public static String EXTRA_SENDER_ID = "SenderId";
    public static String EXTRA_CHAT_OBJECT = "ChatObject";


    public static String EXTRA_PROFILE_ID = "ProfileId"; // Its common for userId and ProfileId
    public static String EXTRA_FRIEND_PROFILE_PIC = "FriendProfilePic";
    public static String EXTRA_FRIEND_NAME = "FriendName";
    public static String EXTRA_MULTIPLE_NOTIFICATION = "MultipleNotification";
    public static String EXTRA_IS_NOTIFICATION = "IsNotification";

    public static String STR_SINGLE_CHAT = "SingleChat";
    public static String STR_GROUP_CHAT = "GroupChat";


}
