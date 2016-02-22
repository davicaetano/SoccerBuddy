package com.davicaetano.soccerbuddy.data.xmpp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserUtils {
	public final String TAG = getClass().getSimpleName();
	private Context mContext;
	private SharedPreferences mPreferences;
	private SharedPreferences.Editor mEditor;

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


	public String getGroupListData() {
		return mPreferences.getString(XMPPConstants.PREF_GROUPLIST, null);
	}

}
