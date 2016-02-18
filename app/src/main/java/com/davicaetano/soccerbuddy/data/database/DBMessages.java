package com.davicaetano.soccerbuddy.data.database;


public class DBMessages {

    public static final String TABLE_NAME = "messages";
    public static final String USER_TABLE_NAME = "userDetail";
    public static final String FRIEND_TABLE_NAME = "friendDetail";
    public static final String LOGIN_TABLE = "LoginDetail";

    public static final String _ID = "_id";
    public static final String MSG_ID = "msg_id";
    public static final String SENDER_ID = "sender_id"; // myid
    public static final String VNAME = "vName"; // my name
    public static final String RECEIVER_ID = "receiver_id"; // destination id
    public static final String MSG_DATE = "msg_date"; // date when i click on send
    public static final String MSG_TEXT = "msg_text"; // message or base 64 string
    public static final String STATUS = "status"; // pending, process, done
    public static final String ISDELIVER = "isDeliver";
    public static final String ISREAD = "isRead";
    public static final String GROUPID = "groupId";


    public static final String USER_ID = "user_id";
    public static final String USER_IMAGE = "user_image";
    public static final String USER_NAME = "user_name";
    public static final String USER_PASSWORD = "user_pass";
    public static final String URL = "url";



    protected static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MSG_ID + " text, " +
            SENDER_ID + " text, " +
            VNAME + " text, " +
            RECEIVER_ID + " text, " +
            MSG_DATE + " text, " +
            MSG_TEXT + " text, " +
            STATUS + " text, " +
            ISDELIVER + " text, " +
            ISREAD + " text, " +
            GROUPID + " text " +
            " ) ";

    protected static final String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE_NAME + " ( " +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            GROUPID + " text, " +
            USER_ID + " text, " +
            USER_NAME + " text, " +
            USER_IMAGE + " text " +
            " ) ";

    protected static final String CREATE_FRIEND_TABLE = "CREATE TABLE " + FRIEND_TABLE_NAME + " ( " +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            USER_ID + " text, " +
            USER_IMAGE + " text " +
            " ) ";


    protected static final String CREATE_LOGIN_TABLE = "CREATE TABLE " + LOGIN_TABLE + " ( " +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            USER_NAME + " text, " +
            USER_PASSWORD + " text " +
            " ) ";
}



