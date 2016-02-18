package com.davicaetano.soccerbuddy.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLHelper extends SQLiteOpenHelper {

	private final String TAG = "SQLHelper";

	private static final String DATABASE_NAME = "msg.db";
	private static final int DATABASE_VERSION= 1;

	public SQLHelper(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DBMessages.CREATE_TABLE);
		db.execSQL(DBMessages.CREATE_USER_TABLE);
		db.execSQL(DBMessages.CREATE_FRIEND_TABLE);
		db.execSQL(DBMessages.CREATE_LOGIN_TABLE);
		Log.e(TAG, "Database Created");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
