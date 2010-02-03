package com.handycodeworks.tweety;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.provider.BaseColumns;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper{
    static final String TAG = "DatabaseOpenHelper";
    static final String DATABASE_NAME = "timeline.sqlite";
    static final int DATABASE_VERSION = 6;
    static final String TABLE = "timeline";
    static final String C_ID = BaseColumns._ID;
    static final String C_CREATED_AT ="created_at";
    static final String C_TEXT ="status";
    static final String C_USER ="user";
    

    public DatabaseOpenHelper(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
	Twitter.Status.class.getMethods();
	String sql = String.format(
		"CREATE table %s (" +
		"%s integer NOT NULL primary key," +
		"%s timestamp, %s TEXT, %s TEXT)", TABLE, C_ID, C_CREATED_AT, C_TEXT, C_USER);
	Log.d(TAG, "Created SQLite database: " + sql);
	db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	db.execSQL("DROP TABLE IF EXISTS "+TABLE+";");
	this.onCreate(db);
    }
    
    public static ContentValues statusToContentValues(Status status){
	ContentValues cv = new ContentValues();
	cv.put(C_ID, status.id);
	cv.put(C_CREATED_AT, status.createdAt.getTime());
	cv.put(C_TEXT, status.text);
	cv.put(C_USER, status.user.screenName);
	return cv;
    }
}
