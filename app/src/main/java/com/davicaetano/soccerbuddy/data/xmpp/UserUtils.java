package com.davicaetano.soccerbuddy.data.xmpp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.davicaetano.soccerbuddy.data.xmpp.model.ChatUser;

import org.json.JSONException;
import org.json.JSONObject;

public class UserUtils {
	public final String TAG = getClass().getSimpleName();
	private Context mContext;
	private SharedPreferences mPreferences;
	private SharedPreferences.Editor mEditor;

	public static final String SPREF_USER_ID = "user_id";
	public static final String SPREF_USER_NAME = "user_name";
	public static final String SPREF_USER_PASSWORD = "user_password";

	public static final String SPREF_USER_OBJECT = "userFullJsonObject";

	private UserUtils(Context mContext) {
		this.mContext = mContext;
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
		mEditor = mPreferences.edit();
	}

	private static UserUtils userUtils;

	public static UserUtils getInstance(Context mContext) {

		if (userUtils != null) {
			return userUtils;
		} else {
			userUtils = new UserUtils(mContext);
			return userUtils;
		}
	}

	public ChatUser getCachedUser() {
		String userRawData = getRawData();
		Log.i(TAG, "raw Data: " + userRawData);
		if (userRawData != null) {
			try {
				JSONObject jsonObject = new JSONObject(userRawData);
				ChatUser chatUser = new ChatUser();
				chatUser.setUserId(jsonObject.optString(SPREF_USER_ID));
				chatUser.setUserName(jsonObject.optString(SPREF_USER_NAME));
				chatUser.setPassword(jsonObject.optString(SPREF_USER_PASSWORD));

				return chatUser;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public String getGroupListData() {
		return mPreferences.getString(XMPPConstants.PREF_GROUPLIST, null);
	}


	private String getRawData() {
		return mPreferences.getString(SPREF_USER_OBJECT, null);
	}

}
