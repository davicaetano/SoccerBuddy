package com.davicaetano.soccerbuddy.data.xmpp;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.davicaetano.soccerbuddy.CustomApplication;
import com.davicaetano.soccerbuddy.data.database.DBHelper;
import com.davicaetano.soccerbuddy.data.user.UserManager;
import com.davicaetano.soccerbuddy.data.xmpp.model.ChatMessage;
import com.davicaetano.soccerbuddy.data.xmpp.model.ChatUser;
import com.davicaetano.soccerbuddy.data.xmpp.model.GroupModel;
import com.davicaetano.soccerbuddy.ui.chat.ChatActivity;
import com.davicaetano.soccerbuddy.ui.groupchat.GroupChatActivity;

import org.jivesoftware.smack.XMPPConnection;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by davicaetano on 2/18/16.
 */
public class XMPPService extends Service {

    private final String TAG = "XMPPService";

    public static final String ACTION_PERFORMED = "com.davicaetano.soccerbuddy.MessageService.ACTION_PERFORMED";
    public static final String ACTION_GROUP_PERFORMED = "com.davicaetano.soccerbuddy.MessageService.ACTION_GROUP_PERFORMED";
    public static final String ACTION_UNREADCOUNT = "com.davicaetano.soccerbuddy.MessageService.ACTION_UNREADCOUNT";

    public static final String ACTION = "action";
    public static final String ISFORIMAGE = "IsForImage";
    public static final String DATA_MESSAGE = "data_message";
    public static final String DATA_MESSAGE_ID = "data_message_id";
    public static final String DATA_USER_ID = "data_user_id";
    public static final String DATA_USER_STATUS = "data_user_status";

    public static final String DATA_LIST = "data_list";
    public static final String USER_ID = "user_id";
    public static final String ISSINGLEGROUP = "IsSingleGroup";
    public static final String ISPROFILECHANGE = "IsProfileChange";
    public static final String GROUPID = "GroupId";

    // SENDING ACTIONS
    public static final int ACTION_REGISTER = 0;
    public static final int ACTION_LOGIN = 1;
    public static final int ACTION_LOGOUT = 2;
    public static final int ACTION_SEND_MESSAGE = 4;
    public static final int ACTION_FRIEND_LIST = 5;
    public static final int ACTION_GET_USER_PRESENCE = 6;
    public static final int ACTION_SUBSCRIBE_USER = 7;
    public static final int ACTION_SEND_PRESENCE = 8;
    public static final int ACTION_CREATE_GROUP = 9;

    // RECEIVING ACTIONS
    public static final int ACTION_FLAG_REGISTER_SUCCESS = 0;
    public static final int ACTION_FLAG_REGISTER_FAILED = 1;
    public static final int ACTION_FLAG_LOGIN_SUCCESS = 2;
    public static final int ACTION_FLAG_LOGIN_FAILED = 3;
    public static final int ACTION_FLAG_LOGOUT = 4;
    public static final int ACTION_FLAG_SEND_MESSAGE_SUCCESS = 5;
    public static final int ACTION_FLAG_SEND_MESSAGE_FAILED = 6;
    public static final int ACTION_FLAG_CONNECT_SUCCESS = 7;
    public static final int ACTION_FLAG_CONNECT_FAILED = 8;
    public static final int ACTION_FLAG_MESSAGE_RECEIVED = 9;
    public static final int ACTION_FLAG_FRIEND_LIST_RECEIVED = 10;
    public static final int ACTION_FLAG_SEND_MESSAGE_DELIVERED = 11;
    public static final int ACTION_FLAG_GET_USER_PRESENCE = 12;
    public static final int ACTION_FLAG_SETVCARD = 13;

    @Inject XMPPHelper helper;
    @Inject UserManager sPreferenceManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((CustomApplication)getApplication()).getAppComponent().inject(this);
        helper.setXMPPCallbacks(xmppCallbacks);
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        helper.disconnect();
        super.onDestroy();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                int mMode = bundle.getInt(ACTION, -1);
//                User user = sPreferenceManager.getUser();
                ChatUser mUser = null;

                switch (mMode) {
                    case ACTION_REGISTER:
                        helper.register(mUser);
                        break;
                    case ACTION_LOGIN:
                        helper.login(mUser);
                        break;
                    case ACTION_LOGOUT:
                        helper.disconnect();
                        break;
                    case ACTION_SEND_MESSAGE:
                        boolean IsForImage = bundle.getBoolean(ISFORIMAGE, false);
                        ChatMessage chatMessage = bundle.getParcelable(DATA_MESSAGE);
                        String toUser = bundle.getString(DATA_LIST);
                        String groupId = bundle.getString(GROUPID);
                        helper.sendMessage(chatMessage, toUser, groupId, IsForImage);
                        Log.v(TAG, "XMPPService - onStart - ACTION_SEND_MESSAGE - toUser=" + toUser);
                        Log.v(TAG, "XMPPService - onStart - ACTION_SEND_MESSAGE - groupId=" + groupId);

                        break;

                    case ACTION_FRIEND_LIST:

                        helper.getFriendList();
                        break;
                    case ACTION_GET_USER_PRESENCE:
                        String userID = bundle.getString(DATA_USER_ID);
                        helper.getUserPresence(userID);
                        break;
                    case ACTION_SEND_PRESENCE:
                        helper.sendPresencePacket(bundle.getBoolean(DATA_USER_STATUS));
                        break;
                    case ACTION_SUBSCRIBE_USER:
                        helper.sendSubscribePacket(bundle.getString(DATA_USER_ID));
                        break;
                    case ACTION_CREATE_GROUP:
                        boolean IsSingle= bundle.getBoolean(ISSINGLEGROUP,false);
                        if(IsSingle){
                            String GroupId=bundle.getString(GROUPID);
                            String UserId = bundle.getString(USER_ID);
                            helper.JoinGroup(GroupId, UserId);
                        }else{
                            ArrayList<GroupModel> Grouplist = new ArrayList<>();
                            Grouplist = bundle.getParcelableArrayList(DATA_LIST);
                            String Id = bundle.getString(USER_ID);
                            boolean IsProfileChange=bundle.getBoolean(ISPROFILECHANGE);
                            ChatMessage messageData = bundle.getParcelable(DATA_MESSAGE);
                            helper.JoinGroup(messageData, Grouplist, Id, IsProfileChange);
                        }
                        break;
                    case ACTION_FLAG_SETVCARD:
                        break;
                }
            } else {
                Log.e(TAG,"onStartCommand - Intent without bundle.");
            }
        }
        return START_NOT_STICKY;
    }

    private XMPPCallbacks xmppCallbacks = new XMPPCallbacks() {

        @Override
        public void onRegistrationFailed(Exception e) {
            Log.e(TAG, "onRegistrationFailed", e);
            Intent actionIntent = new Intent(ACTION_PERFORMED);
            actionIntent.putExtra(ACTION, ACTION_FLAG_REGISTER_FAILED);
            sendBroadcast(actionIntent);
        }

        @Override
        public void onRegistrationComplete() {
            Log.e(TAG, "onRegistrationComplete");

            Intent actionIntent = new Intent(ACTION_PERFORMED);
            actionIntent.putExtra(ACTION, ACTION_FLAG_REGISTER_SUCCESS);
            sendBroadcast(actionIntent);
        }

        @Override
        public void onMessageSent(ChatMessage chatMessage) {
            Log.e(TAG, "onMessageSent - chatMessage = " + chatMessage.toString() );
            Intent actionIntent;
            if (TextUtils.isEmpty(chatMessage.group_id)) {
                actionIntent = new Intent(ACTION_PERFORMED);
            } else {
                actionIntent = new Intent(ACTION_GROUP_PERFORMED);
            }
            actionIntent.putExtra(ACTION, ACTION_FLAG_SEND_MESSAGE_SUCCESS);
            actionIntent.putExtra(DATA_MESSAGE, chatMessage);
            sendBroadcast(actionIntent);

        }

        @Override
        public void onMessageDeliver(String messageId, boolean IsGroup) {
            Log.e(TAG, "onMessageDeliver - messageId = " + messageId);

            Intent actionIntent;
            if (IsGroup) {
                actionIntent = new Intent(ACTION_GROUP_PERFORMED);
            } else {
                actionIntent = new Intent(ACTION_PERFORMED);
            }
            actionIntent.putExtra(ACTION, ACTION_FLAG_SEND_MESSAGE_DELIVERED);
            actionIntent.putExtra(DATA_MESSAGE_ID, messageId);
            sendBroadcast(actionIntent);

        }

        @Override
        public void onMessageSendingFailed(ChatMessage chatMessage, Exception e) {
            Log.e(TAG, "onMessageSendingFailed - chatMessage = " + chatMessage.toString(), e);

            Intent actionIntent;
            if (TextUtils.isEmpty(chatMessage.group_id)) {
                actionIntent = new Intent(ACTION_PERFORMED);
            } else {
                actionIntent = new Intent(ACTION_GROUP_PERFORMED);
            }
            actionIntent.putExtra(ACTION, ACTION_FLAG_SEND_MESSAGE_FAILED);
            actionIntent.putExtra(DATA_MESSAGE, chatMessage);
            sendBroadcast(actionIntent);

        }

        @Override
        public void onMessageRecieved(ChatMessage chatMessage) {
            Log.v(TAG, "onMessageRecieved - chatMessage = " + chatMessage.toString());

            if (ChatActivity.ISVISIBLE) {
                // IF APPLICATION IS OPEN THEN SEND BROADCAST SO USER CAN SHOW IT ON LISTVIEW.
                Intent actionIntent;
                if (TextUtils.isEmpty(chatMessage.group_id)) {
                    actionIntent = new Intent(ACTION_PERFORMED);
                } else {
                    actionIntent = new Intent(ACTION_GROUP_PERFORMED);
                }
                actionIntent.putExtra(ACTION, ACTION_FLAG_MESSAGE_RECEIVED);
                actionIntent.putExtra(DATA_MESSAGE, chatMessage);
                sendBroadcast(actionIntent);

            } else {
                ChatUtils.generateNotification(getApplicationContext(), chatMessage);

                //WHEN CHAT ACTIVITY IS NOT OPENED.
                String IsRead = "false";
                DBHelper.getInstance(getApplicationContext()).updateISReadStatus(chatMessage.msg_id, IsRead, true);
                Intent actionIntent = new Intent(ACTION_UNREADCOUNT);
                actionIntent.putExtra(XMPPConstants.EXTRA_RECEIVER_ID, chatMessage.receiver_id);
                actionIntent.putExtra(XMPPConstants.EXTRA_SENDER_ID, chatMessage.sender_id);
                sendBroadcast(actionIntent);

            }

        }

        @Override
        public void onGroupMessageRecieved(ChatMessage messageData) {
            Log.v(TAG, "onGroupMessageRecieved - chatMessage = " + messageData.toString());

            if (GroupChatActivity.ISVISIBLE) {
                // if application is open then send broadcast so user can show it on listview
                Intent actionIntent;
                actionIntent = new Intent(ACTION_GROUP_PERFORMED);
                actionIntent.putExtra(ACTION, ACTION_FLAG_MESSAGE_RECEIVED);
                actionIntent.putExtra(DATA_MESSAGE, messageData);
                sendBroadcast(actionIntent);

            } else {
                ChatUtils.generateNotification(getApplicationContext(), messageData);
            }

        }

        @Override
        public void onGetUserPresence(String userId, String status) {
            Log.v(TAG, "onGetUserPresence - userId = " + userId);
            Log.v(TAG, "onGetUserPresence - status = " + status);

            Intent actionIntent = new Intent(ACTION_PERFORMED);
            actionIntent.putExtra(ACTION, ACTION_FLAG_GET_USER_PRESENCE);
            actionIntent.putExtra(DATA_USER_ID, userId);
            actionIntent.putExtra(DATA_USER_STATUS, status);

            sendBroadcast(actionIntent);

        }

        @Override
        public void onUserPresenceChange(String userId, String status) {
            Log.v(TAG, "onUserPresenceChange - userId = " + userId);
            Log.v(TAG, "onUserPresenceChange - status = " + status);

            Intent actionIntent = new Intent(ACTION_PERFORMED);
            actionIntent.putExtra(ACTION, ACTION_FLAG_GET_USER_PRESENCE);
            actionIntent.putExtra(DATA_USER_ID, userId);
            actionIntent.putExtra(DATA_USER_STATUS, status);

            sendBroadcast(actionIntent);

        }

        @Override
        public void onLoginFailed(Exception e) {
            Log.e(TAG, "onLoginFailed", e);

            Intent actionIntent = new Intent(ACTION_PERFORMED);
            actionIntent.putExtra(ACTION, ACTION_FLAG_LOGIN_FAILED);
            sendBroadcast(actionIntent);

        }

        @Override
        public void onLogin(ChatUser chatUser) {
            Log.v(TAG, "onLogin - chatUser = " + chatUser);

            Intent actionIntent = new Intent(ACTION_PERFORMED);
            actionIntent.putExtra(ACTION, ACTION_FLAG_LOGIN_SUCCESS);
            sendBroadcast(actionIntent);

            resendPendingMessageFromDatabase();
        }

        @Override
        public void onConnected(XMPPConnection connection) {
            Log.v(TAG, "onConnected");

            Intent actionIntent = new Intent(ACTION_PERFORMED);
            actionIntent.putExtra(ACTION, ACTION_FLAG_CONNECT_SUCCESS);
            sendBroadcast(actionIntent);

        }

        @Override
        public void onConnectionFailed(Exception e) {
            Log.v(TAG, "onConnected", e);

            Intent actionIntent = new Intent(ACTION_PERFORMED);
            actionIntent.putExtra(ACTION, ACTION_FLAG_CONNECT_FAILED);
            sendBroadcast(actionIntent);

        }

        @Override
        public void onLogout() {
            Log.v(TAG, "onLogout");

            Intent actionIntent = new Intent(ACTION_PERFORMED);
            actionIntent.putExtra(ACTION, ACTION_FLAG_LOGOUT);
            sendBroadcast(actionIntent);

            XMPPService.this.stopSelf();
        }

        @Override
        public void onFriendListReceived(ArrayList<String> list) {
            Log.v(TAG, "onFriendListReceived - list.size() = " + list.size());

            final Intent actionIntent = new Intent(ACTION_PERFORMED);
            actionIntent.putExtra(ACTION, ACTION_FLAG_FRIEND_LIST_RECEIVED);
            actionIntent.putStringArrayListExtra(DATA_LIST, list);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    sendBroadcast(actionIntent);
                    Log.v(TAG, "onFriendListReceived - Running handler");
                }
            }, 100);
        }
    };

    private void resendPendingMessageFromDatabase() {
        Log.v(TAG, "resendPendingMessageFromDatabase");

        ArrayList<ChatMessage> listChatMessages = DBHelper.getInstance(getApplicationContext()).getAllPendingMessage();
        ChatUtils chatUtils = new ChatUtils(getApplicationContext());
        for (ChatMessage chatMessage : listChatMessages) {
            if(!TextUtils.isEmpty(chatMessage.isForImage)){
                if (chatMessage.isForImage.equalsIgnoreCase("YES")) {
                    chatUtils.sendMessage(chatMessage, chatMessage.receiver_id, chatMessage.group_id, true);
                } else {
                    chatUtils.sendMessage(chatMessage, chatMessage.receiver_id, chatMessage.group_id, false);
                }
            }else{
                chatMessage.isForImage="NO";
                chatUtils.sendMessage(chatMessage, chatMessage.receiver_id, chatMessage.group_id, false);

            }

        }
    }
}
