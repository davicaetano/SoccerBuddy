package com.davicaetano.soccerbuddy.data.xmpp;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.davicaetano.soccerbuddy.CustomApplication;
import com.davicaetano.soccerbuddy.data.database.DBHelper;
import com.davicaetano.soccerbuddy.data.xmpp.model.ChatMessage;
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
    public static final String USER_ID = "user";
    public static final String USER_PASSWORD = "password";
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
    @Inject XMPPApi xmppApi;

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
                int action = bundle.getInt(ACTION, -1);

                String user_id = intent.getStringExtra(USER_ID);
                String password = intent.getStringExtra(USER_PASSWORD);

                switch (action) {
                    case ACTION_REGISTER:
                        helper.register(user_id, password);
                        break;
                    case ACTION_LOGIN:
                        helper.login(user_id, password);
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
//                            helper.JoinGroup(messageData, Grouplist, Id, IsProfileChange);
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

    private void resendPendingMessageFromDatabase() {
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

    private XMPPConstants.XMPPCallbacks xmppCallbacks = new XMPPConstants.XMPPCallbacks() {
        @Override
        public void onConnectionFailed(Exception e) {
            Log.v(TAG, "onConnectionFailed");

            sendBroadcast((new Intent(ACTION_PERFORMED)).putExtra(ACTION, ACTION_FLAG_CONNECT_FAILED));
        }
        @Override
        public void onConnected(XMPPConnection connection) {
            Log.v(TAG, "onConnected");
            sendBroadcast((new Intent(ACTION_PERFORMED)).putExtra(ACTION, ACTION_FLAG_CONNECT_SUCCESS));
        }
        @Override
        public void onRegistrationFailed(Exception e) {
            Log.v(TAG, "onRegistrationFailed");
            sendBroadcast((new Intent(ACTION_PERFORMED)).putExtra(ACTION, ACTION_FLAG_REGISTER_FAILED));
        }
        @Override
        public void onRegistrationComplete() {
            Log.v(TAG, "onRegistrationComplete");
            sendBroadcast((new Intent(ACTION_PERFORMED)).putExtra(ACTION, ACTION_FLAG_REGISTER_SUCCESS));
        }
        @Override
        public void onLoginFailed(Exception e) {
            Log.v(TAG, "onLoginFailed",e);
            sendBroadcast((new Intent(ACTION_PERFORMED)).putExtra(ACTION, ACTION_FLAG_LOGIN_FAILED));
        }
        @Override
        public void onLogin() {
            Log.v(TAG, "onLogin");
            xmppApi.onLogin();
//            sendBroadcast((new Intent(ACTION_PERFORMED)).putExtra(ACTION, ACTION_FLAG_LOGIN_SUCCESS));
            resendPendingMessageFromDatabase();
        }
        @Override
        public void onLogout() {
            Log.v(TAG, "onLogout");
            sendBroadcast((new Intent(ACTION_PERFORMED)).putExtra(ACTION, ACTION_FLAG_LOGOUT));
            XMPPService.this.stopSelf();
        }
        @Override
        public void onMessageSent(ChatMessage chatMessage) {
            Log.v(TAG, "onMessageSent");
            if (TextUtils.isEmpty(chatMessage.group_id)) {
                sendBroadcast(new Intent(ACTION_PERFORMED).putExtra(ACTION, ACTION_FLAG_SEND_MESSAGE_SUCCESS)
                        .putExtra(DATA_MESSAGE, chatMessage));
            } else {
                sendBroadcast(new Intent(ACTION_GROUP_PERFORMED).putExtra(ACTION, ACTION_FLAG_SEND_MESSAGE_SUCCESS)
                        .putExtra(DATA_MESSAGE, chatMessage));
            }
        }

        @Override
        public void onMessageDelivered(String messageId, boolean IsGroup) {
            Log.v(TAG, "onMessageDeliver");
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
            Log.v(TAG, "onMessageSendingFailed");
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
        public void onMessageReceived(ChatMessage chatMessage) {
            Log.v(TAG, "onMessageRecieved");
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
        public void onGroupMessageReceived(ChatMessage messageData) {
            Log.v(TAG, "onGroupMessageRecieved");
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
            Log.v(TAG, "onGetUserPresence");
            Intent actionIntent = new Intent(ACTION_PERFORMED);
            actionIntent.putExtra(ACTION, ACTION_FLAG_GET_USER_PRESENCE);
            actionIntent.putExtra(DATA_USER_ID, userId);
            actionIntent.putExtra(DATA_USER_STATUS, status);

            sendBroadcast(actionIntent);
        }

        @Override
        public void onUserPresenceChange(String userId, String status) {
            Log.v(TAG, "onUserPresenceChange");
            Intent actionIntent = new Intent(ACTION_PERFORMED);
            actionIntent.putExtra(ACTION, ACTION_FLAG_GET_USER_PRESENCE);
            actionIntent.putExtra(DATA_USER_ID, userId);
            actionIntent.putExtra(DATA_USER_STATUS, status);

            sendBroadcast(actionIntent);
        }

        @Override
        public void onFriendListReceived(ArrayList<String> list) {
            Log.v(TAG, "onFriendListReceived");
            xmppApi.onListReceived(list);
        }
    };

}
