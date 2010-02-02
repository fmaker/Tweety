package com.handycodeworks.tweety;

import winterwell.jtwitter.Twitter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
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
    private Twitter twitter;
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
	username = prefs.getString("username", USERNAME);
	password = prefs.getString("password", PASSWORD);
	twitter = new Twitter(username, password);

	// Set application name for twitter
	twitter.setSource(TAG);

    }

    public void onClick(View v) {
	String enteredText = textStatus.getText().toString();

	// Status can't be empty
	if (enteredText.length() <= 0) {
	    Toast.makeText(this, R.string.empty_status, Toast.LENGTH_SHORT)
		    .show();
	}
	// Only process the button if status is not the same as the hint
	else if (updateButton.getId() == v.getId()) {
	    twitter.setStatus(textStatus.getText().toString());
	    Toast.makeText(this, R.string.status_posted, Toast.LENGTH_SHORT)
		    .show();

	    // Show hint
	    textStatus.setText(null);

	    // Refresh character label
	    updateCharacterCount();
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
	    Intent i = new Intent(Tweety.this, Prefs.class);
	    startActivity(i);
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
	numChars.setText(String.valueOf(charsLeft));
    }

}