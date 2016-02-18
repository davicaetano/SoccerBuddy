package com.davicaetano.soccerbuddy.data.xmpp;

import com.davicaetano.soccerbuddy.data.xmpp.model.ChatMessage;
import com.davicaetano.soccerbuddy.data.xmpp.model.ChatUser;

import org.jivesoftware.smack.XMPPConnection;

import java.util.ArrayList;

public interface XMPPCallbacks {
	 void onRegistrationComplete();

	 void onRegistrationFailed(Exception e);

	 void onLogin(ChatUser ChatnaUser);

	 void onLoginFailed(Exception e);

	 void onLogout();

	 void onMessageRecieved(ChatMessage messageData);

	 void onGroupMessageRecieved(ChatMessage messageData);

	 void onMessageSent(ChatMessage message);
	
	 void onMessageDeliver(String message, boolean IsGroup);

	 void onMessageSendingFailed(ChatMessage message, Exception e);

	 void onConnected(XMPPConnection connection);

	 void onConnectionFailed(Exception e);
	
	 void onFriendListReceived(ArrayList<String> list);
	
	 void onGetUserPresence(String userId, String status);
	
	 void onUserPresenceChange(String userId, String status);
}
