package com.davicaetano.soccerbuddy.data.xmpp;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.davicaetano.soccerbuddy.data.database.DBHelper;
import com.davicaetano.soccerbuddy.data.xmpp.model.ChatMessage;
import com.davicaetano.soccerbuddy.data.xmpp.model.GroupModel;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import static com.davicaetano.soccerbuddy.utils.Utils.IsInternetConnected;


/**
 * Created by davicaetano on 1/21/16.
 */
public class XMPPHelper {
    private final String TAG = this.getClass().getName();
    public static String CONFERENCE_NAME = "@conference.52.53.212.42";
    public static int PORT = 5222;
    public static String HOST = "52.53.212.42";

    Context context;

    XMPPTCPConnectionConfiguration.Builder configBuilder;

    private XMPPTCPConnection connection;
    private ChatManager mChatManager;
    private Roster mRoster;
    private XMPPConstants.XMPPCallbacks xmppCallBacks;
    private enum PENDING_ACTION {
        NONE, REGISTER, LOGIN
    }
    private PENDING_ACTION mPendingAction = PENDING_ACTION.NONE;

    public XMPPHelper(Context context) {
        this.context = context;
        configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder.setUsernameAndPassword("davicaetano", "abc123");
        configBuilder.setServiceName("Smack");
        configBuilder.setHost(HOST);
        configBuilder.setPort(PORT);
        configBuilder.setConnectTimeout(10000);
        configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
        try{
            TLSUtils.acceptAllCertificates(configBuilder);
        }catch (NoSuchAlgorithmException e) {
        }catch (KeyManagementException e) {
        }
        configBuilder.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }

    public void setXMPPCallbacks(XMPPConstants.XMPPCallbacks xmppCallBacks) {
        this.xmppCallBacks = xmppCallBacks;
    }

    // Register

    public void register(String user, String password) {
            new RegistrationTask().execute(new String[]{user, password});
    }

    private class RegistrationTask extends AsyncTask<String, Void, Boolean> {
        private Exception exception;
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                if(connection == null)
                    connection = new XMPPTCPConnection(configBuilder.build());
                if(!connection.isConnected())
                    connection.connect();
                AccountManager.getInstance(connection).createAccount(params[0], params[1], null);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                exception = e;
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result)
                xmppCallBacks.onRegistrationComplete();
            else
                xmppCallBacks.onRegistrationFailed(exception);
        }
    }

    //LOGIN

    public void login(String user, String password) {
        new LoginTask().execute(new String[]{user, password});
    }
    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        private Exception exception;
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                if(connection == null) connection = new XMPPTCPConnection(configBuilder.build());
                if(!connection.isConnected()) connection.connect();
                if(connection.isAuthenticated()) return true;
                connection.login();
                return true;
            } catch (Exception e) {
                exception = e;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                xmppCallBacks.onLogin();
//                sendPresencePacket(true);
//                listenForInCommingPacket();

//                //Rejoined the groups after login successful.
//                String groupsData=sPreferenceManager.getGroupListData();
//                fetchJoinedGroupList(groupsData, null, mChatUser.getEmail(), false);
            } else {
                xmppCallBacks.onLoginFailed(exception);
            }
        }
    }



    //Disconnect

    public void disconnect() {
        if(connection!= null && connection.isConnected())
            new DisconnectionTask().execute();
    }

    private class DisconnectionTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Presence presence = new Presence(Presence.Type.unavailable);
                connection.disconnect(presence);
            } catch (SmackException.NotConnectedException e) {}
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            xmppCallBacks.onLogout();
        }
    }


    ///////

    public void sendPresencePacket(boolean isAvailable) {
        try {
            Stanza presence;
            if (isAvailable) {
                presence = new Presence(Presence.Type.available);
            } else {
                presence = new Presence(Presence.Type.unavailable);
            }
            connection.sendStanza(presence);
        } catch (Exception e) {
        }
    }


    private void fetchJoinedGroupList(String groupsData, ChatMessage message, String UserId, boolean IsProfileChange) {
        ArrayList<GroupModel> Grouplist = new ArrayList<>();
        try{
            JSONArray arr= new JSONArray(groupsData);
            for (int i = 0; i < arr.length() ; i++) {
                GroupModel model = new GroupModel();
                JSONObject obj= arr.getJSONObject(i);
                if (obj.getString("is_joined").equalsIgnoreCase("1")) {
                    model.setiGroupID(obj.getString("iGroupID"));
                    model.setIs_joined("1");
                    Grouplist.add(model);
                }
            }
            JoinGroup(message, Grouplist, UserId, IsProfileChange);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void JoinGroup(String groupID, String UserId) {
        try {

            MultiUserChat muc = MultiUserChatManager.getInstanceFor(connection)
                    .getMultiUserChat(groupID + CONFERENCE_NAME);
            if (!muc.isJoined()) {
                if (TextUtils.isEmpty(UserId)) {
                    muc.join("regular user");
                } else {
                    DiscussionHistory history = new DiscussionHistory();
                    history.setMaxStanzas(0);
                    muc.join(connection.getUser(), null, history, SmackConfiguration.getDefaultPacketReplyTimeout());
                }
            }
        } catch (Exception e) {
            Log.e(TAG,"JoinGroup",e);
        }
    }

    public void JoinGroup(ChatMessage messageData, ArrayList<GroupModel> mlist, String UserId, boolean IsProfileChange) {
        if (IsInternetConnected(context)) {
            try {
                if (connection != null && connection.isConnected()) {
                    if (connection.isAuthenticated()) {
                        new JoinGroupTask(messageData, mlist, UserId, IsProfileChange).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        mPendingAction = PENDING_ACTION.LOGIN;
//                        login(mChatUser);
                    }

                } else {
                    mPendingAction = PENDING_ACTION.NONE;
//                    connect();
                }

            } catch (Exception e) {
                Log.e(TAG,"",e);
            }
        }
    }

    private class JoinGroupTask extends AsyncTask<Void, Void, Boolean> {

        private ArrayList<GroupModel> mlist;
        private String UserId;
        private boolean IsProfileChange;
        private ChatMessage messageData;

        public JoinGroupTask(ChatMessage messageData, ArrayList<GroupModel> mlist, String UserId, boolean IsProfileChange) {
            this.mlist = mlist;
            this.UserId = UserId;
            this.IsProfileChange = IsProfileChange;
            this.messageData = messageData;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (connection == null) {
                    return false;
                } else {

                    List<String> rooms = MultiUserChatManager.getInstanceFor(connection)
                            .getJoinedRooms(connection.getUser());
                    List<String> temp = new ArrayList<>();
                    if (rooms.size() > 0) {
                        for (int i = 0; i < rooms.size(); i++) {
                            String[] parts = rooms.get(i).trim().split("@");
                            temp.add(parts[0]);
                        }
                    }
                    for (int i = 0; i < mlist.size(); i++) {
                        MultiUserChat muc = null;

                        String groupID = mlist.get(i).getiGroupID().replace(" ", "");
                        muc = MultiUserChatManager.getInstanceFor(connection)
                                .getMultiUserChat(groupID + CONFERENCE_NAME);
                        if (mlist.get(i).getIs_joined().equalsIgnoreCase("1")) {

                            if (!temp.contains(mlist.get(i).getiGroupID())) {
                                if (!muc.isJoined()) {
                                    if (!TextUtils.isEmpty(UserId)) {
                                     /* Date TimeStamp=  XMPPConstants.getLastSavedTime(context);
                                        DiscussionHistory history = new DiscussionHistory();
                                        if(TimeStamp!=null){
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        // User2 joins the room requesting to receive the messages of the last 2 seconds.
                                            history.setSince(TimeStamp);
                                        }else{
                                            history.setMaxStanzas(0);
                                        }*/

//                                        int sec =XMPPConstants.getLastSavedTime(context);
//                                        Log.e(TAG,"Final Second="+sec);
                                        DiscussionHistory history = new DiscussionHistory();
                                        history.setMaxStanzas(0);
                                        muc.join(connection.getUser(), null, history, SmackConfiguration.getDefaultPacketReplyTimeout());
                                    }else{
                                        Log.e(TAG,"UserId null");
                                    }
                                }
                            }
                            ///profile update message
                            if (IsProfileChange) {
                                messageData.group_id = mlist.get(i).getiGroupID();
                                xmppSendGroupMessage(messageData, mlist.get(i).getiGroupID(), true);
                            }
                        } else {
                            // IF NOT JOIN THEN LEAVE FROM THE ROOM
                            if (temp.contains(mlist.get(i).getiGroupID())) {
                                leaveChatRoom(muc, mlist.get(i).getiGroupID());
                            }
                        }

                    }
                    return true;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Log.v(TAG, "JoinGroupTask - onPostExecute - Succeed");
            } else {
                Log.v(TAG, "JoinGroupTask - onPostExecute - Failed");
            }
        }
    }


    public void leaveChatRoom(MultiUserChat muc, String UserId) {
        Log.v(TAG, "leaveChatRoom");
        if (muc != null) {
            try {
                Presence leavePresence = new Presence(Presence.Type.unavailable);
                leavePresence.setTo(UserId + CONFERENCE_NAME + "/" + connection.getUser());
                connection.sendPacket(leavePresence);
                muc.leave();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            } catch (Exception e) {

            }
        }

    }

    //Send presence packet
    public void sendSubscribePacket(String userId) {
        // SENDING SUBSCRIBE PACKET
        try {
            if (mRoster == null)
                mRoster = Roster.getInstanceFor(connection);

            if (mRoster != null) {
                Collection<RosterEntry> entries = mRoster.getEntries();
                boolean isFound = false;
                for (RosterEntry entry : entries) {
                    Presence presence = mRoster.getPresence(entry.getUser());
                    if (presence.getFrom().split("/")[0].equalsIgnoreCase(userId)) {
                        isFound = true;
                        break;
                    }
                }

                if (isFound == false) {
                    Stanza subscribe = new Presence(Presence.Type.subscribe);
                    subscribe.setTo(userId);
                    // subscribe.setMode(Presence.Mode.available);
                    connection.sendStanza(subscribe);
                }
            }
        } catch (Exception e) {
            if (e instanceof SmackException.ConnectionException) {
            }
        }
    }



    private void listenForInCommingPacket() {
        connection.addAsyncStanzaListener(mChatListener, MessageTypeFilter.CHAT);
        connection.addAsyncStanzaListener(mPresenceListener, StanzaTypeFilter.PRESENCE);
        connection.addAsyncStanzaListener(mgroupChatListener, MessageTypeFilter.GROUPCHAT);
    }


    private class SendMessageTask extends AsyncTask<Void, Void, Boolean> {

        private ChatMessage messageData;
        private String toUser;
        private String GroupId;
        private Exception exception;
        private boolean IsForImage;

        public SendMessageTask(ChatMessage messageData, String toUser, String GroupId, boolean IsForImage) {
            this.messageData = messageData;
            this.toUser = toUser;
            this.GroupId = GroupId;
            this.IsForImage = IsForImage;
            Log.e(TAG, "XMPPHelper toUser=" + toUser);
            Log.e(TAG, "XMPPHelper groupId=" + GroupId);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (IsForImage) {
                    xmppSendGroupMessage(messageData, GroupId, IsForImage);

                } else {
                    if (TextUtils.isEmpty(GroupId)) {
                        xmppSendMessage(messageData, toUser);
                    } else {
                        xmppSendGroupMessage(messageData, GroupId, IsForImage);
                    }
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                exception = e;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (xmppCallBacks != null && result) {
                xmppCallBacks.onMessageSent(messageData);
                messageData.status = XMPPConstants.STATUS_TYPE_SENT;
                DBHelper.getInstance(context).update(messageData);
            } else {
                xmppCallBacks.onMessageSendingFailed(messageData, exception);
            }
        }
    }

    public void sendMessage(ChatMessage messageData, String toUser, String GroupId, boolean IsForImage) {

        if (IsInternetConnected(context)) {
            try {
                if (connection != null && connection.isConnected()) {

                    if (connection.isAuthenticated()) {
                        new SendMessageTask(messageData, toUser, GroupId, IsForImage).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        mPendingAction = PENDING_ACTION.LOGIN;
//                        login(mChatUser);
                    }

                } else {
                    mPendingAction = PENDING_ACTION.LOGIN;
//                    connect();
                }

            } catch (Exception e) {
                if (xmppCallBacks != null)
                    xmppCallBacks.onMessageSendingFailed(messageData, e);
                e.printStackTrace();
            }
        } else {
            if (xmppCallBacks != null)
                xmppCallBacks.onMessageSendingFailed(messageData, new Exception("Network unavailable"));
        }

    }


    private void xmppSendMessage(ChatMessage messageData, String userId) throws Exception {
        try {
            JSONObject obj = new JSONObject();
            obj.put(XMPPConstants.MESSAGE_ID, messageData.msg_id);
            obj.put(XMPPConstants.MESSAGE_TEXT, ChatUtils.encodeToBase64String(messageData.msg_text));
            obj.put(XMPPConstants.MESSAGE_DATE, messageData.msg_date);
            obj.put(XMPPConstants.SENDER_NAME, messageData.vname);
            obj.put(XMPPConstants.PROFILE_USERIMAGE, messageData.user_image);
            obj.put(XMPPConstants.SENDER_ID, messageData.sender_id);
            obj.put(XMPPConstants.RECEIVER_ID, messageData.receiver_id);
            obj.put(XMPPConstants.GROUP_ID, messageData.group_id);
            JSONObject jobj = new JSONObject();
            jobj.put(XMPPConstants.MESSAGE_TAG, obj);

            Message msg = new Message();
            msg.setBody(jobj.toString());
            msg.setPacketID(messageData.msg_id);

            DeliveryReceiptManager.addDeliveryReceiptRequest(msg);
            if (!userId.contains("@"))
                userId = userId + "@" + HOST;
            Chat mChat = ChatManager.getInstanceFor(connection).createChat(userId, null);
            mChat.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void xmppSendGroupMessage(ChatMessage messageData, String userId, boolean IsForImage) throws Exception {
        try {
            JSONObject jobj = new JSONObject();
            if (IsForImage) {
                Log.e(TAG, "XMPPHelper PROFILE UPDATE");
                JSONObject obj = new JSONObject();
                obj.put(XMPPConstants.PROFILE_USERID, messageData.user_id);
                obj.put(XMPPConstants.PROFILE_USERNAME, messageData.vname);
                obj.put(XMPPConstants.PROFILE_USERIMAGE, messageData.user_image);
                obj.put(XMPPConstants.PROFILE_ISFORIMAGE, messageData.isForImage);
                obj.put(XMPPConstants.GROUP_ID, messageData.group_id);
                jobj.put(XMPPConstants.MESSAGE_TAG, obj);

            } else {
                Log.e(TAG, "XMPPHelper xmppSendGroupMessage");
                JSONObject obj = new JSONObject();
                obj.put(XMPPConstants.MESSAGE_ID, messageData.msg_id);
                obj.put(XMPPConstants.SENDER_ID, messageData.sender_id);
                obj.put(XMPPConstants.RECEIVER_ID, messageData.receiver_id);
                obj.put(XMPPConstants.GROUP_ID, messageData.group_id);
                obj.put(XMPPConstants.GROUP_RECEIVER_ID, messageData.user_id);
                obj.put(XMPPConstants.MESSAGE_TEXT, ChatUtils.encodeToBase64String(messageData.msg_text));
                obj.put(XMPPConstants.MESSAGE_DATE, messageData.msg_date);
                obj.put(XMPPConstants.SENDER_NAME, messageData.vname);
                obj.put(XMPPConstants.GROUP_NAME, messageData.group_name);
                obj.put(XMPPConstants.GROUP_NAME_POR, ChatUtils.encodeToBase64String(messageData.group_name_por));
                jobj.put(XMPPConstants.MESSAGE_TAG, obj);

            }
            Message msg = new Message(messageData.group_id + CONFERENCE_NAME, Message.Type.groupchat);
            msg.setBody(jobj.toString());
            if (!IsForImage) {
                msg.setPacketID(messageData.msg_id);
            }
            DeliveryReceiptManager.addDeliveryReceiptRequest(msg);

            MultiUserChat mMultiUserChat = MultiUserChatManager.getInstanceFor(connection)
                    .getMultiUserChat(messageData.group_id + CONFERENCE_NAME);
            mMultiUserChat.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // ==========GET FRIEND LIST===========

    public void getFriendList() {

        if (IsInternetConnected(context)) {
            try {
                if (connection != null && connection.isConnected()) {

                    if (connection.isAuthenticated()) {
                        new GetFriendListTask().execute();
                    } else {
                        mPendingAction = PENDING_ACTION.LOGIN;
//                        login(mChatUser);
                    }

                } else {
                    mPendingAction = PENDING_ACTION.NONE;
//                    connect();
                }

            } catch (Exception e) {
                if (xmppCallBacks != null)
                    e.printStackTrace();
            }
        } else {
            if (xmppCallBacks != null) {
            }
        }

    }

    private class GetFriendListTask extends AsyncTask<Void, Void, Boolean> {
        private Exception exception;
        private ArrayList<String> friendList;

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                friendList = xmppgetFriendList();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                exception = e;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (xmppCallBacks != null && result) {
                xmppCallBacks.onFriendListReceived(friendList);
            } else {

            }
        }
    }

    private ArrayList<String> xmppgetFriendList() throws Exception {
        try {
            ArrayList<String> friendList = new ArrayList<String>();
            Roster roster = Roster.getInstanceFor(connection);
            Collection<RosterEntry> entries = roster.getEntries();
            for (RosterEntry entry : entries) {
                System.out.println(entry);
                friendList.add(entry.getUser());
            }
            return friendList;
        } catch (Exception e) {
            return null;
        }
    }

    // ==========HELPERS===========

    private ChatMessage parceRecievedPacket(Message message) {
        Log.v(TAG, "XMPPHelper - parceRecievedPacket: " + message.toString());

        if (message.getBody() != null && !message.getBody().isEmpty()) {
            try {
                Log.v(TAG, "Message received=" + message.toString());
                Log.v(TAG, "XMPPHelper parceRecievedPacket");
                String rawData = message.getBody();
                JSONObject obj = new JSONObject(rawData);
                if (obj.has(XMPPConstants.MESSAGE_TAG)) {
                    JSONObject jobj = obj.getJSONObject(XMPPConstants.MESSAGE_TAG);
                    ChatMessage chatMessage = new ChatMessage();
                    if (TextUtils.isEmpty(jobj.getString(XMPPConstants.GROUP_ID))) { //SINGLE CHAT
                        boolean flag = DBHelper.getInstance(context).IsMessageExists(jobj.getString(XMPPConstants.MESSAGE_ID));
                        if (!flag) {
                            chatMessage.receiver_id = jobj.getString(XMPPConstants.RECEIVER_ID);
                            chatMessage.group_id = jobj.getString(XMPPConstants.GROUP_ID); //it will blank in case of one to one chat.
                            chatMessage.msg_id = jobj.getString(XMPPConstants.MESSAGE_ID);
                            chatMessage.msg_text = ChatUtils.decodeFromBase64String(jobj.getString(XMPPConstants.MESSAGE_TEXT));
                            chatMessage.msg_date = jobj.getString(XMPPConstants.MESSAGE_DATE);
                            chatMessage.vname = jobj.getString(XMPPConstants.SENDER_NAME);
                            chatMessage.user_image = jobj.getString(XMPPConstants.PROFILE_USERIMAGE);
                            chatMessage.sender_id = jobj.getString(XMPPConstants.SENDER_ID);
                            chatMessage.isdeliver = "false";
                            chatMessage.isread = "false";

//                            if(!mChatUser.getEmail().equalsIgnoreCase(chatMessage.sender_id)){
//                                DBHelper.getInstance(context).add(chatMessage);
//                            }

                            return chatMessage;
                        }

                    } else {//UPDATE PROFILE
                        if (jobj.has(XMPPConstants.PROFILE_ISFORIMAGE)) {
                            if (!TextUtils.isEmpty(jobj.getString(XMPPConstants.PROFILE_ISFORIMAGE))
                                    && jobj.getString(XMPPConstants.PROFILE_ISFORIMAGE).equalsIgnoreCase("YES")) {
                                chatMessage.user_id = jobj.getString(XMPPConstants.PROFILE_USERID);
                                chatMessage.vname = jobj.getString(XMPPConstants.PROFILE_USERNAME);
                                chatMessage.user_image = jobj.getString(XMPPConstants.PROFILE_USERIMAGE);
                                chatMessage.isForImage = "YES";
                                boolean IsExist = DBHelper.getInstance(context).IsProfilePictureExists(jobj.getString(XMPPConstants.PROFILE_USERID));
                                if (IsExist) {
                                    DBHelper.getInstance(context).updateProfile(chatMessage);
                                } else {
                                    DBHelper.getInstance(context).addUserDetail(jobj.getString(XMPPConstants.PROFILE_USERID),
                                            jobj.getString(XMPPConstants.PROFILE_USERIMAGE));
                                }

                                return chatMessage;
                            }
                        } else {//GROUP CHAT
                            boolean flag = DBHelper.getInstance(context).IsMessageExists(jobj.getString(XMPPConstants.MESSAGE_ID));
                            if (!flag) {
                                chatMessage.receiver_id = jobj.getString(XMPPConstants.RECEIVER_ID);
                                chatMessage.group_id = jobj.getString(XMPPConstants.GROUP_ID);
                                chatMessage.msg_id = jobj.getString(XMPPConstants.MESSAGE_ID);
                                chatMessage.user_id = jobj.getString(XMPPConstants.GROUP_RECEIVER_ID);
                                chatMessage.msg_text = ChatUtils.decodeFromBase64String(jobj.getString(XMPPConstants.MESSAGE_TEXT));
                                chatMessage.msg_date = jobj.getString(XMPPConstants.MESSAGE_DATE);
                                chatMessage.vname = jobj.getString(XMPPConstants.SENDER_NAME);
                                chatMessage.sender_id = jobj.getString(XMPPConstants.SENDER_ID);
                                chatMessage.group_name = jobj.getString(XMPPConstants.GROUP_NAME);
                                chatMessage.group_name_por = jobj.getString(XMPPConstants.GROUP_NAME_POR);
                                chatMessage.group_name_por = ChatUtils.decodeFromBase64String(jobj.getString(XMPPConstants.GROUP_NAME_POR));
                                chatMessage.isdeliver = "false";
                                chatMessage.isread = "false";
                                DBHelper.getInstance(context).add(chatMessage);
                                return chatMessage;
                            }
                        }
                    }
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private StanzaListener mChatListener = new StanzaListener() {
        public void processPacket(Stanza stanza) {
            Message message = (Message) stanza;
            ChatMessage messageData = parceRecievedPacket(message);
            if (xmppCallBacks != null && messageData != null) {
                if (TextUtils.isEmpty(messageData.isForImage)) {
                    xmppCallBacks.onMessageReceived(messageData);
                }
            }
        }
    };

    private StanzaListener mgroupChatListener = new StanzaListener() {
        @Override
        public void processPacket(Stanza stanza) {
            Message message = (Message) stanza;
            if(message!=null){
                ChatMessage messageData = parceRecievedPacket(message);
                if(messageData!=null){
                    try {
                        JSONObject obj = new JSONObject(message.getBody());
                        if (obj.has(XMPPConstants.MESSAGE_TAG)) {
                            if (xmppCallBacks != null && messageData != null)
                                if (TextUtils.isEmpty(messageData.isForImage)) {
                                    xmppCallBacks.onGroupMessageReceived(messageData);
                                }
                            Log.v(TAG, "XMPPHelper - mgroupChatListener");
                            Log.v(TAG, "mgroupChatListener");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    };

    StanzaListener mPresenceListener = new StanzaListener() {

        @Override
        public void processPacket(Stanza stanza) {
            Presence presence = (Presence) stanza;
            if (presence.getType() == Presence.Type.subscribe) {
                sendSubscribePacket(presence.getFrom());
            }
        }
    };


    private ReceiptReceivedListener mReceiptReceived = new ReceiptReceivedListener() {
        @Override
        public void onReceiptReceived(String fromJid, String toJid, String receiptId, Stanza receipt) {
            Log.v(TAG, "ReceiptReceivedListener - receiptId: " + receiptId);
            Log.v(TAG, "ReceiptReceivedListener - fromJid: " + fromJid);
            Log.v(TAG, "ReceiptReceivedListener - toJid: " + toJid);
            if (xmppCallBacks != null)
                if (fromJid.contains("conference")) {//for group
                    xmppCallBacks.onMessageDelivered(receiptId, true);
                } else {//single chat
                    xmppCallBacks.onMessageDelivered(receiptId, false);
                }

            //BUT I AM CONSIDER STATUS NOT IS DELIEVER
            DBHelper.getInstance(context).updateStatus(receiptId, XMPPConstants.STATUS_TYPE_DELIVER);
            DBHelper.getInstance(context).updateIsDeliever(receiptId, "true");
        }
    };


    public void getUserPresence(String userId) {
        if (mRoster != null) {

            mRoster = Roster.getInstanceFor(connection);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }

            Presence availability = mRoster.getPresence(userId);
            if (xmppCallBacks != null)
                xmppCallBacks.onGetUserPresence(userId, retrieveState_mode(availability.getMode(), availability.isAvailable()));
        }
    }

    private String retrieveState_mode(Presence.Mode userMode, boolean isOnline) {
        if (userMode == Presence.Mode.chat) {
            return XMPPConstants.USER_PRESENCE_ONLINE;
        }
        return XMPPConstants.USER_PRESENCE_OFFLINE;
    }

    private RosterListener mRosterListener = new RosterListener() {

        @Override
        public void presenceChanged(Presence presence) {
            if (mRoster != null) {
                String user = presence.getFrom();
                Presence bestPresence = mRoster.getPresence(user);
                Log.v(TAG, "presenceChanged");
                Log.v(TAG, "User: " + user);
                Log.v(TAG, "Presence: " + bestPresence);
                Log.v(TAG, "State" + presence.toString());
                if (xmppCallBacks != null)
                    xmppCallBacks.onUserPresenceChange(user.split("/")[0], retrieveState_mode(presence.getMode(), presence.isAvailable()));
            }
        }

        @Override
        public void entriesUpdated(Collection<String> arg0) {
        }

        @Override
        public void entriesDeleted(Collection<String> arg0) {
        }

        @Override
        public void entriesAdded(Collection<String> arg0) {
        }
    };

}
