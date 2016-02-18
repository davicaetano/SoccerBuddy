package com.davicaetano.soccerbuddy.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.davicaetano.soccerbuddy.data.xmpp.XMPPConstants;
import com.davicaetano.soccerbuddy.data.xmpp.model.ChatMessage;

import java.util.ArrayList;

public class DBHelper {

    private static DBHelper dbHelper;
    private SQLiteDatabase db;

    private DBHelper(Context context) {
        db = new SQLHelper(context).getWritableDatabase();
    }

    public static DBHelper getInstance(Context context) {
        if (dbHelper == null)
            dbHelper = new DBHelper(context);

        return dbHelper;
    }

    // ============MESSAGES============
    public ArrayList<ChatMessage> getAllRecords(String senderId, String receiverId, String groupId) {
        String sql = "";
        if (TextUtils.isEmpty(groupId)) {
            sql = "SELECT * FROM messages WHERE sender_id='" + senderId + "' AND receiver_id='" + receiverId + "' OR sender_id='" + receiverId + "' AND receiver_id='" + senderId + "' ";
        } else {
            sql = "SELECT * FROM messages WHERE groupId='" + groupId + "' ";
        }
        Cursor cur = db.rawQuery(sql, null);
        ArrayList<ChatMessage> listChatMessage = new ArrayList<>();
        if (cur != null && cur.moveToFirst()) {
            do {
                ChatMessage msg = new ChatMessage();
                msg._id = cur.getString(cur.getColumnIndex(DBMessages._ID));
                msg.msg_date = cur.getString(cur.getColumnIndex(DBMessages.MSG_DATE));
                msg.msg_id = cur.getString(cur.getColumnIndex(DBMessages.MSG_ID));
                msg.sender_id = cur.getString(cur.getColumnIndex(DBMessages.SENDER_ID));
                msg.status = cur.getString(cur.getColumnIndex(DBMessages.STATUS));
                msg.msg_text = cur.getString(cur.getColumnIndex(DBMessages.MSG_TEXT));
                msg.receiver_id=ProfilePicture(msg.sender_id);
                msg.vname = cur.getString(cur.getColumnIndex(DBMessages.VNAME));
                msg.isdeliver = cur.getString(cur.getColumnIndex(DBMessages.ISDELIVER));
                msg.isread = cur.getString(cur.getColumnIndex(DBMessages.ISREAD));
                msg.group_id = cur.getString(cur.getColumnIndex(DBMessages.GROUPID));
                listChatMessage.add(msg);
            } while (cur.moveToNext());
        }
        return listChatMessage;
    }


    public String ProfilePicture(String senderId){
        String sql = "";
        sql = "SELECT * FROM " + DBMessages.USER_TABLE_NAME + " WHERE user_id='" + senderId + "' ";
        Cursor cur = db.rawQuery(sql, null);
        if (cur != null && cur.moveToFirst()) {
            return cur.getString(cur.getColumnIndex(DBMessages.USER_IMAGE));
        }

        return "";
    }

    public void add(ChatMessage msg) {
        ContentValues values = new ContentValues();
        values.put(DBMessages.MSG_ID, msg.msg_id);
        values.put(DBMessages.MSG_DATE, msg.msg_date);
        values.put(DBMessages.SENDER_ID, msg.sender_id);
        values.put(DBMessages.MSG_TEXT, msg.msg_text);
        values.put(DBMessages.RECEIVER_ID, msg.receiver_id);
        values.put(DBMessages.VNAME, msg.vname);
        values.put(DBMessages.ISDELIVER, msg.isdeliver);
        values.put(DBMessages.ISREAD, msg.isread);
        values.put(DBMessages.GROUPID, msg.group_id);
        values.put(DBMessages.STATUS, msg.status);
        long result = db.insert(DBMessages.TABLE_NAME, null, values);

    }

    public void addUserDetail(String userId, String url) {
        ContentValues values = new ContentValues();
        values.put(DBMessages.GROUPID, "");
        values.put(DBMessages.USER_ID, userId);
        values.put(DBMessages.USER_NAME, "");
        values.put(DBMessages.USER_IMAGE, url);
        int affectedRows = db.update(DBMessages.USER_TABLE_NAME, values, DBMessages.USER_ID + "='" + userId + "' COLLATE NOCASE", null);
        if (affectedRows <= 0) {
            long result = db.insert(DBMessages.USER_TABLE_NAME, null, values);
        } else {
        }


    }


    /*STORE USER CREDENTIAL*/
    public void addLoginDetail(String user_name, String user_pass) {
        ContentValues values = new ContentValues();
        values.put(DBMessages.USER_NAME, user_name);
        values.put(DBMessages.USER_PASSWORD, user_pass);
        long result = db.insert(DBMessages.LOGIN_TABLE, null, values);
    }

    public void updateLoginDetail(String user_name,String user_pass){
        ContentValues values = new ContentValues();
        values.put(DBMessages.USER_NAME, user_name);
        values.put(DBMessages.USER_PASSWORD, user_pass);
        db.update(DBMessages.LOGIN_TABLE, values, DBMessages._ID + "='" + 1 + "' COLLATE NOCASE", null);
    }

    public void addFriendDetail(String userId, String url) {
        ContentValues values = new ContentValues();
        values.put(DBMessages.USER_ID, userId);
        values.put(DBMessages.USER_IMAGE, url);
        long result = db.insert(DBMessages.FRIEND_TABLE_NAME, null, values);
    }

    public void update(ChatMessage msg) {
        ContentValues values = new ContentValues();
        values.put(DBMessages.MSG_ID, msg.msg_id);
        values.put(DBMessages.MSG_DATE, msg.msg_date);
        values.put(DBMessages.SENDER_ID, msg.sender_id);
        values.put(DBMessages.MSG_TEXT, msg.msg_text);
        values.put(DBMessages.RECEIVER_ID, msg.receiver_id);
        values.put(DBMessages.VNAME, msg.vname);
        values.put(DBMessages.ISDELIVER, msg.isdeliver);
        values.put(DBMessages.ISREAD, msg.isread);
        values.put(DBMessages.GROUPID, msg.group_id);
        values.put(DBMessages.STATUS, msg.status);


        if (!TextUtils.isEmpty(msg._id)) {
            db.update(DBMessages.TABLE_NAME, values, DBMessages._ID + "='" + msg._id + "' COLLATE NOCASE", null);
        } else {
            db.update(DBMessages.TABLE_NAME, values, DBMessages.MSG_ID + "='" + msg.msg_id + "' COLLATE NOCASE", null);
        }
    }

    public void updateStatus(String id, String status) {
        ContentValues values = new ContentValues();
        values.put(DBMessages.STATUS, status);
        db.update(DBMessages.TABLE_NAME, values, DBMessages.MSG_ID + "='" + id + "' COLLATE NOCASE", null);
    }


    public void updateProfile(ChatMessage msg) {
        ContentValues values = new ContentValues();
        values.put(DBMessages.USER_IMAGE, msg.user_image);
        db.update(DBMessages.USER_TABLE_NAME, values, DBMessages.USER_ID + "='" + msg.user_id + "' COLLATE NOCASE", null);
        Log.e(">>>>>>>>>>", "update profile picture");
    }


    public String getLoginDetail(String FIELD){
        String url = "";
        String sql = "SELECT " + FIELD + " FROM " + DBMessages.LOGIN_TABLE;
        Cursor cur = db.rawQuery(sql, null);
        if (cur != null && cur.moveToFirst()) {
            url = cur.getString(0);
            return url;
        }
        return "";
    }

    public ArrayList<ChatMessage> getAllPendingMessage() {

        String sql = "SELECT * FROM " + DBMessages.TABLE_NAME + " WHERE " +
                DBMessages.STATUS + " = '" + XMPPConstants.STATUS_TYPE_PENDING + "' COLLATE NOCASE " + " OR " + DBMessages.STATUS + " = '" + XMPPConstants.STATUS_TYPE_PROCESS + "' COLLATE NOCASE " + " ORDER BY " + DBMessages._ID;

        Cursor cur = db.rawQuery(sql, null);

        ArrayList<ChatMessage> listChatMessage = new ArrayList<>();
        if (cur != null && cur.moveToFirst()) {

            do {

                ChatMessage msg = new ChatMessage();
                msg._id = cur.getString(cur.getColumnIndex(DBMessages._ID));
                msg.msg_date = cur.getString(cur.getColumnIndex(DBMessages.MSG_DATE));
                msg.msg_id = cur.getString(cur.getColumnIndex(DBMessages.MSG_ID));
                msg.sender_id = cur.getString(cur.getColumnIndex(DBMessages.SENDER_ID));
                msg.status = cur.getString(cur.getColumnIndex(DBMessages.STATUS));
                msg.msg_text = cur.getString(cur.getColumnIndex(DBMessages.MSG_TEXT));
                msg.receiver_id = cur.getString(cur.getColumnIndex(DBMessages.RECEIVER_ID));
                msg.vname = cur.getString(cur.getColumnIndex(DBMessages.VNAME));
                msg.isdeliver = cur.getString(cur.getColumnIndex(DBMessages.ISDELIVER));
                msg.isread = cur.getString(cur.getColumnIndex(DBMessages.ISREAD));
                msg.group_id = cur.getString(cur.getColumnIndex(DBMessages.GROUPID));

                listChatMessage.add(msg);
            } while (cur.moveToNext());

        }
        return listChatMessage;
    }

    public void updateFriendPic(String user_id, String url) {
        ContentValues values = new ContentValues();
        values.put(DBMessages.USER_IMAGE, url);
        db.update(DBMessages.FRIEND_TABLE_NAME, values, DBMessages.USER_ID + "='" + user_id + "' COLLATE NOCASE", null);
        db.update(DBMessages.USER_TABLE_NAME, values, DBMessages.USER_ID + "='" + user_id + "' COLLATE NOCASE", null);
    }

    public void deleteDatabase(String tableName) {
        // TODO Auto-generated method stub
        String sql = "DELETE FROM " + tableName;
        db.execSQL(sql);
    }


    public void updateISReadStatus(String id, String status, boolean flag) {
        ContentValues values = new ContentValues();
        values.put(DBMessages.ISREAD, status);
        if (flag) {
            db.update(DBMessages.TABLE_NAME, values, DBMessages.MSG_ID + "='" + id + "' COLLATE NOCASE", null);
        } else {
            db.update(DBMessages.TABLE_NAME, values, "sender_id='" + id + "' COLLATE NOCASE", null);
        }

    }

    public void updateIsDeliever(String id, String status) {
        ContentValues values = new ContentValues();
        values.put(DBMessages.ISDELIVER, status);
        db.update(DBMessages.TABLE_NAME, values, DBMessages.MSG_ID + "='" + id + "' COLLATE NOCASE", null);
    }


    public String getUnReadCount(String user_id, String status, String groupId) {
        String sql = "";
        sql = "SELECT count(*) FROM " + DBMessages.TABLE_NAME + " WHERE sender_id='" + user_id + "' AND isRead='" + status + "' AND groupId='" + groupId + "' COLLATE NOCASE ";
        String count;
        Cursor cur = db.rawQuery(sql, null);
        if (cur != null && cur.moveToFirst()) {
            count = cur.getString(0);
            return count;
        }
        return "";
    }

    public boolean IsMessageExists(String msg_id) {
        String sql = "SELECT * FROM " + DBMessages.TABLE_NAME + " WHERE msg_id='" + msg_id + "' COLLATE NOCASE ";

        Cursor cur = db.rawQuery(sql, null);
        if (cur != null && cur.moveToFirst()) {
            if (cur.getCount() > 0) {
                return true;
            }

        }
        return false;
    }

    public boolean IsLoginExists() {
        String sql = "SELECT * FROM " + DBMessages.LOGIN_TABLE;
        Cursor cur = db.rawQuery(sql, null);
        if (cur != null && cur.moveToFirst()) {
            if (cur.getCount() > 0) {
                return true;
            }

        }
        return false;
    }

    public boolean IsProfilePictureExists(String user_id) {
        String sql = "SELECT * FROM " + DBMessages.USER_TABLE_NAME + " WHERE "+ DBMessages.USER_ID + "='" + user_id + "' COLLATE NOCASE ";

        Cursor cur = db.rawQuery(sql, null);
        if (cur != null && cur.moveToFirst()) {
            if (cur.getCount() > 0) {
                return true;
            }

        }
        return false;
    }

}
