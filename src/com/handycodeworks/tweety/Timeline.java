package com.handycodeworks.tweety;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	startManagingCursor(c);
	mAdapter = new TimelineAdapter(this, c);
	mListTimeLine.setAdapter(mAdapter);
	
		
    }

    @Override
    protected void onResume() {
	super.onResume();
	
	NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	nm.cancel(UpdateService.NOTIFICATION_ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.menu, menu);
	
	// Enable Tweet and disable Timeline
	MenuItem m = menu.findItem(R.id.menuTweet);
	m.setVisible(true);
	
	m = menu.findItem(R.id.menuTimeline);
	m.setVisible(false);
	
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

	switch (item.getItemId()) {
		case R.id.about:
        	    Intent ia = new Intent(Timeline.this, About.class);
        	    startActivity(ia);
        	    break;
        	case R.id.menuPrefs:
        	    Intent ip = new Intent(Timeline.this, Prefs.class);
        	    startActivity(ip);
        	    break;
        	case R.id.menuTweet:
        	    Intent it = new Intent(Timeline.this, Tweety.class);
        	    startActivity(it);
        	    break;
	}
	return true;
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
