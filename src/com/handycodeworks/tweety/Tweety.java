package com.handycodeworks.tweety;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Tweety extends Activity implements OnClickListener, OnKeyListener {
    // Class variables
    private static final String TAG = "Tweety";
    private static final String USERNAME = "mcsgtest";
    private static final String PASSWORD = "pD98^LGy6m";
    private SharedPreferences prefs;

    // Instance variables
    private Twitter mTwitter;
    private String username, password;

    // UI Elements
    private Button updateButton;
    private TextView textStatus, numChars;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);

	// Find views by id
	updateButton = (Button) findViewById(R.id.UpdateButton);
	updateButton.setOnClickListener(this);
	textStatus = (TextView) findViewById(R.id.TextStatus);
	textStatus.setOnKeyListener(this);
	numChars = (TextView) findViewById(R.id.NumChars);

	// Setup preferences
	prefs = PreferenceManager.getDefaultSharedPreferences(this);
	prefs.registerOnSharedPreferenceChangeListener(
		new OnSharedPreferenceChangeListener() {
		    
		    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			    String key) {
			mTwitter = null;
		    }
		});
    }

    @Override
    protected void onResume() {
	super.onResume();
	
	// Start update service
	startService(new Intent(this,UpdateService.class));
	
	
    }

    private Twitter getTwitter(){
	if(mTwitter == null){
	    username = prefs.getString("username", USERNAME);
	    password = prefs.getString("password", PASSWORD);
	    mTwitter = new Twitter(username, password);
	    mTwitter.setSource(TAG);
	}
	return mTwitter;
    }
    
    @Override
    protected void onStop() {
	stopService(new Intent(this,UpdateService.class));
	super.onStop();	
    }

    public void onClick(View v) {
	String enteredText = textStatus.getText().toString();

	// Status can't be empty
	if (enteredText.length() == 0) {
	    Toast.makeText(this, R.string.empty_status, Toast.LENGTH_SHORT).show();
	}
	else if(enteredText.length() < 0){
	    Toast.makeText(this, R.string.status_too_long, Toast.LENGTH_SHORT).show();
	}
	// Only process the button if status is not the same as the hint
	else if (updateButton.getId() == v.getId()) {
	    try {
		getTwitter().setStatus(textStatus.getText().toString());
		Toast.makeText(this, R.string.status_posted,Toast.LENGTH_SHORT).show();

		// Show hint
		textStatus.setText(null);

		// Refresh character label
		updateCharacterCount();
	    }
	    catch(TwitterException te){
		Log.e(TAG,te.toString());
		Toast.makeText(this, R.string.no_twitter, Toast.LENGTH_SHORT).show();
	    }
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.menu, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

	switch (item.getItemId()) {
        	case R.id.menuPrefs:
        	    Intent ip = new Intent(Tweety.this, Prefs.class);
        	    startActivity(ip);
        	    break;
        	case R.id.menuTimeline:
        	    Intent it = new Intent(Tweety.this, Timeline.class);
        	    startActivity(it);
        	    break;
	}
	return true;
    }

    private final int NUM_CHARS = 140;

    public boolean onKey(View v, int keyCode, KeyEvent event) {
	updateCharacterCount();
	return false;
    }

    private void updateCharacterCount() {
	int charsLeft = NUM_CHARS - textStatus.getText().length();
	if(charsLeft < 0){
	    updateButton.setEnabled(false);
	    numChars.setTextColor(Color.RED);
	}
	else{
	    updateButton.setEnabled(true);
	    numChars.setTextColor(Color.LTGRAY);
	}
	numChars.setText(String.valueOf(charsLeft));
    }

}