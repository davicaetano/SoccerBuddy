package com.davicaetano.soccerbuddy.data.xmpp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.davicaetano.soccerbuddy.R;
import com.davicaetano.soccerbuddy.data.xmpp.model.ChatMessage;
import com.davicaetano.soccerbuddy.ui.groupchat.GroupChatActivity;
import com.davicaetano.soccerbuddy.ui.home.HomeActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ChatUtils {

    private static final String TAG = "ChatUtils";
    Intent xmppService;
    Context activity;
    public static int MESSAGE_NOTIFICATION_ID = 1;
    public static int MESSAGE_GROUPNOTIFICATION_ID = 3;
    public static Map<String, ArrayList<String>> mapGroupMessages = new HashMap<String, ArrayList<String>>();
    public static ArrayList<String> privateGroupMessages = new ArrayList<String>();
    public static Map<String, ArrayList<String>> mapMessages = new HashMap<String, ArrayList<String>>();
    public static ArrayList<String> privateMessages = new ArrayList<String>();


    public ChatUtils(Context context) {
        xmppService = new Intent(context, XMPPService.class);
        this.activity = context;
        this.activity.startService(xmppService);
    }

    public void sendMessage(ChatMessage chatMessage, String toUser, String GroupId, boolean IsForImage) {
        Bundle bnd = new Bundle();
        bnd.putInt(XMPPService.ACTION, XMPPService.ACTION_SEND_MESSAGE);
        bnd.putParcelable(XMPPService.DATA_MESSAGE, chatMessage);
        bnd.putBoolean(XMPPService.ISFORIMAGE, IsForImage);
        bnd.putString(XMPPService.DATA_LIST, toUser);
        bnd.putString(XMPPService.GROUPID, GroupId);
        xmppService.putExtras(bnd);
        activity.startService(xmppService);
    }


    public static void generateNotification(Context context, ChatMessage messageData) {
        // TODO Auto-generated method stub

        Log.i(TAG,
                "================Inside generateNotification Method==============================");
        Uri soundUri = null;
        soundUri = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (TextUtils.isEmpty(messageData.group_id)) { // SINGLE CHAT NOTIFICATION CONFIG
            if (mapMessages == null) {
                mapMessages = new HashMap<>();
            }

            if (privateMessages == null) {
                privateMessages = new ArrayList<>();
            }
            privateMessages.add(messageData.vname + ":" + " " + messageData.msg_text);
            mapMessages.put(messageData.sender_id, privateMessages);


        } else {
            if (mapGroupMessages == null) {
                mapGroupMessages = new HashMap<>();
            }

            if (privateGroupMessages == null) {
                privateGroupMessages = new ArrayList<>();
            }
            privateGroupMessages.add(messageData.vname + "@" + messageData.group_name + ":" + " " + messageData.msg_text);
            mapGroupMessages.put(messageData.group_id, privateGroupMessages);

        }
        NotificationCompat.Builder builder;
        if (TextUtils.isEmpty(messageData.group_id)) {
            builder = createMessageNotificationBuilder(
                    context, messageData);
        } else {
            builder = createGroupMessageNotificationBuilder(
                    context, messageData);
        }


        builder.setDefaults(Notification.DEFAULT_LIGHTS
                | Notification.DEFAULT_VIBRATE);
        builder.setSound(soundUri);

        //Look up the notification manager service.
        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        //Pass the notification to the NotificationManager.
        if (TextUtils.isEmpty(messageData.group_id)) {
            nm.notify(MESSAGE_NOTIFICATION_ID, builder.build());
        } else {
            nm.notify(MESSAGE_GROUPNOTIFICATION_ID, builder.build());
        }
    }

    private static NotificationCompat.Builder createMessageNotificationBuilder(
            Context context, ChatMessage messageData) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);
        builder.setContentTitle(getActiveMessageCount() > 1 ? "Messages"
                : "Message");
        if (getActiveMessageCount() > 1) {
            builder.setContentText(privateMessages.size()
                    + " messages from " + mapMessages.size()
                    + " conversations.");
        } else {
            builder.setContentText(privateMessages.get(0));
        }

        builder.setSmallIcon(R.drawable.icon); // stat_notify_chat taken from Android SDK (API level 17)
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon);
        builder.setLargeIcon(largeIcon);
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());

        if (mapMessages.size() > 1) {
            if (Build.VERSION.SDK_INT >= 19) // KITKAT
            {
                PendingIntent pi = createNotificationMessageActivityPendingIntent(context, messageData.group_id);
                if (pi != null) {
                    pi.cancel();
                }
            }

            PendingIntent msgPendingIntent = createNotificationMessageActivityPendingIntent(context, messageData.group_id);
            builder.setContentIntent(msgPendingIntent);
        } else {
            if (Build.VERSION.SDK_INT >= 19) // KITKAT
            {

                PendingIntent pi = createChatActivityPendingIntent(context,
                        messageData);
                if (pi != null) {
                    pi.cancel();
                }
            }

            PendingIntent msgPendingIntent = createChatActivityPendingIntent(
                    context, messageData);

            builder.setContentIntent(msgPendingIntent);
        }

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        if ((mapMessages != null) && (mapMessages.size() > 0)) {
            for (int i = privateMessages.size() - 1; i >= 0; i--) {
                inboxStyle.addLine(privateMessages.get(i));
            }
        }

        if ((mapMessages != null) && (getActiveMessageCount() >= 1)) {
            if (getActiveMessageCount() > 1)
                inboxStyle.setSummaryText(privateMessages.size()
                        + " messages from " + mapMessages.size()
                        + " conversations.");
            builder.setStyle(inboxStyle);
        }
        return builder;
    }


    private static NotificationCompat.Builder createGroupMessageNotificationBuilder(
            Context context, ChatMessage messageData) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);
        builder.setContentTitle(getActiveGroupMessageCount() > 1 ? "Group Messages"
                : "Group Message");
        if (getActiveGroupMessageCount() > 1) {
            builder.setContentText(privateGroupMessages.size()
                    + " messages from " + mapGroupMessages.size()
                    + " conversations.");
        } else {
            builder.setContentText(privateGroupMessages.get(0));
        }

        builder.setSmallIcon(R.drawable.icon); // stat_notify_chat taken from Android SDK (API level 17)

        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon);
        builder.setLargeIcon(largeIcon);
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());

        if (mapGroupMessages.size() > 1) {
            if (Build.VERSION.SDK_INT >= 19) // KITKAT
            {
                PendingIntent pi = createGroupNotificationMessageActivityPendingIntent(context, messageData.group_id);
                if (pi != null) {
                    pi.cancel();
                }
            }

            PendingIntent msgPendingIntent = createGroupNotificationMessageActivityPendingIntent(context, messageData.group_id);
            builder.setContentIntent(msgPendingIntent);
        } else {
            if (Build.VERSION.SDK_INT >= 19) // KITKAT
            {
                PendingIntent pi = createChatActivityPendingIntent(context,
                        messageData);
                if (pi != null) {
                    pi.cancel();
                }
            }
            PendingIntent msgPendingIntent = createChatActivityPendingIntent(
                    context, messageData);

            builder.setContentIntent(msgPendingIntent);
        }


        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        if ((mapGroupMessages != null) && (mapGroupMessages.size() > 0)) {
            for (int i = privateGroupMessages.size() - 1; i >= 0; i--) {
                inboxStyle.addLine(privateGroupMessages.get(i));
            }
        }

        if ((mapGroupMessages != null) && (getActiveGroupMessageCount() >= 1)) {
            if (getActiveGroupMessageCount() > 1)
                inboxStyle.setSummaryText(privateGroupMessages.size()
                        + " messages from " + mapGroupMessages.size()
                        + " conversations.");
            builder.setStyle(inboxStyle);
        }
        return builder;
    }


    private static int getActiveMessageCount() {
        int cnt = 0;
        if (privateMessages != null) {
            cnt = privateMessages.size();
        }
        return cnt;
    }

    private static int getActiveGroupMessageCount() {
        int cnt = 0;
        if (privateGroupMessages != null) {
            cnt = privateGroupMessages.size();
        }
        return cnt;
    }

    private static PendingIntent createChatActivityPendingIntent(
            Context context, ChatMessage messageData) {
        //Create an intent to start DalChatActivity.
        Intent msgIntent = null;
        if (TextUtils.isEmpty(messageData.group_id)) {
            msgIntent = new Intent(context, GroupChatActivity.class);
            msgIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            msgIntent.putExtra(XMPPConstants.EXTRA_PROFILE_ID, messageData.sender_id);
            msgIntent.putExtra(XMPPConstants.EXTRA_FRIEND_NAME, messageData.vname);
            msgIntent.putExtra(XMPPConstants.EXTRA_FRIEND_PROFILE_PIC, messageData.user_image);
            msgIntent.putExtra(XMPPConstants.EXTRA_IS_NOTIFICATION, true);
        } else {
            msgIntent = new Intent(context, GroupChatActivity.class);
            msgIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            msgIntent.putExtra(XMPPConstants.EXTRA_IS_NOTIFICATION, true);
            msgIntent.putExtra(XMPPConstants.EXTRA_CHAT_OBJECT, messageData);

        }
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        //Add the back stack to the stack builder. This method also adds flags
        //that start the stack in a fresh task.
        if (TextUtils.isEmpty(messageData.group_id)) {
            stackBuilder.addParentStack(GroupChatActivity.class);
        } else {
            stackBuilder.addParentStack(GroupChatActivity.class);
        }

        //Add the Intent that starts the Activity from the notification.
        stackBuilder.addNextIntent(msgIntent);

        //Get a PendingIntent containing the entire back stack.
        PendingIntent msgPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        return msgPendingIntent;
    }

    private static PendingIntent createNotificationMessageActivityPendingIntent(
            Context context, String groupId) {
        //Start DalMainActivity in the message threads position.
        Intent msgIntent = new Intent(context, HomeActivity.class);
        msgIntent.putExtra(XMPPConstants.EXTRA_MULTIPLE_NOTIFICATION, XMPPConstants.STR_SINGLE_CHAT);
        msgIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        android.support.v4.app.TaskStackBuilder stackBuilder = android.support.v4.app.TaskStackBuilder.create(context);
        //Add the back stack to the stack builder. This method also adds flags
        //that start the stack in a fresh task.
        stackBuilder.addParentStack(HomeActivity.class);
        //Add the Intent that starts the Activity from the notification.
        stackBuilder.addNextIntent(msgIntent);

        //Get a PendingIntent containing the entire back stack.
        PendingIntent msgPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        return msgPendingIntent;
    }

    //IT WILL OPEN GROUPLIST SCREEN.
    private static PendingIntent createGroupNotificationMessageActivityPendingIntent(
            Context context, String groupId) {
        //Start DalMainActivity in the message threads position.
        Intent msgIntent = new Intent(context, HomeActivity.class);
        msgIntent.putExtra(XMPPConstants.EXTRA_MULTIPLE_NOTIFICATION, XMPPConstants.STR_GROUP_CHAT);
        msgIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //        msgIntent.putExtra(DalMainActivity.KEY_PAGE_INDEX, DalMainActivity.MESSAGES_POSITION);x

        android.support.v4.app.TaskStackBuilder stackBuilder = android.support.v4.app.TaskStackBuilder.create(context);
        //Add the back stack to the stack builder. This method also adds flags
        //that start the stack in a fresh task.
        stackBuilder.addParentStack(HomeActivity.class);
        //Add the Intent that starts the Activity from the notification.
        stackBuilder.addNextIntent(msgIntent);

        //Get a PendingIntent containing the entire back stack.
        PendingIntent msgPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        return msgPendingIntent;
    }


    public static <K extends Comparable, V extends Comparable> Map<K, V> sortByKeys(Map<K, V> map) {
        List<K> keys = new LinkedList<K>(map.keySet());
        Collections.sort(keys);

        Map<K, V> sortedMap = new LinkedHashMap<K, V>();
        for (K key : keys) {
            sortedMap.put(key, map.get(key));
        }

        return sortedMap;
    }

    public static String encodeToBase64String(String text) {
        String str = Base64.encodeToString(text.getBytes(), Base64.NO_WRAP);
        str = str.replace("+", "%2B");
        return str;
    }

    public static String decodeFromBase64String(String text) {
        text = text.replace("%2B", "+");
        String str = new String(Base64.decode(text, Base64.NO_WRAP)).toString();
        return str;
    }
}