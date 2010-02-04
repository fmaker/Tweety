package com.handycodeworks.tweety;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Timeline extends Activity{
    private static final String TAG = "Timeline";
    
    private ListView mListTimeLine;
    private DatabaseOpenHelper mHelper;	
    private SQLiteDatabase mDb;
    private Cursor c;
    private SimpleCursorAdapter mAdapter;
    private BroadcastReceiver mReceiver;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.timeline);
	
	// Find views by id
	mListTimeLine = (ListView) findViewById(R.id.listTimeLine);
	
	// Initialize database
	mHelper = new DatabaseOpenHelper(this);
	mDb = mHelper.getReadableDatabase();
	
	// Get data
	c = mDb.query(DatabaseOpenHelper.TABLE, null, null, null, null, null, 
		DatabaseOpenHelper.C_CREATED_AT + " DESC");
	mAdapter = new TimelineAdapter(this, c);
	mListTimeLine.setAdapter(mAdapter);
	
		
    }
    
    @Override
    protected void onStart() {
	super.onStart();
	
	mReceiver = new BroadcastReceiver() {
	    
	    @Override
	    public void onReceive(Context context, Intent intent) {
		c.requery();
	    }
	};

	// Register for new tweets broadcast intent
	Log.d(TAG,"Registering broadcast receiver");
	registerReceiver(mReceiver, new IntentFilter(UpdateService.ACTION_NEW_TWEETS));
    }

    @Override
    protected void onStop() {
	Log.d(TAG,"Un-registering broadcast receiver");
	unregisterReceiver(mReceiver);
	super.onStop();
    }

}
