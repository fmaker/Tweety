/* ========================================================================
 * Copyright 2010, Handy Codeworks LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================================================================
 */
package com.handycodeworks.tweety;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import winterwell.jtwitter.OAuthSignpostClient;
import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Tweety extends Activity implements OnClickListener, OnKeyListener {
    // Class variables
    private static final String TAG = "Tweety";
    private SharedPreferences prefs;
    static LocationHelper sLocationHelper;
    private final static String TWITTER_OAUTH_KEY = "9Kf835SE0cuBjhSIZsxpQ";
    private final static String TWITTER_OAUTH_SECRET = "cXzb4WsDLIFIMGBKyNpxEL3Sdtww74xBtoxlEXXrt0";
    private final static String OAUTH_TOKEN = "OAUTH_TOKEN";
    private final static String OAUTH_TOKEN_SECRET = "OAUTH_TOKEN_SECRET";
    private final static int PIN_DIALOG = 0;
    private OAuthSignpostClient client;
    
    OAuthConsumer consumer = new DefaultOAuthConsumer(
    		TWITTER_OAUTH_KEY,
    		TWITTER_OAUTH_SECRET);

    OAuthProvider provider = new DefaultOAuthProvider(
            "http://twitter.com/oauth/request_token",
            "http://twitter.com/oauth/access_token",
            "http://twitter.com/oauth/authorize");


    // Instance variables
    private Twitter mTwitter;
    private String username, password;

    // UI Elements
    private Button tweetButton, locationButton, clearButton;
    private TextView textStatus, numChars;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.main);

	// Find views by id
	tweetButton = (Button) findViewById(R.id.UpdateButton);
	tweetButton.setOnClickListener(this);
	textStatus = (TextView) findViewById(R.id.TextStatus);
	textStatus.setOnKeyListener(this);
	numChars = (TextView) findViewById(R.id.NumChars);
	locationButton = (Button) findViewById(R.id.locationButton);
	locationButton.setOnClickListener(this);
	clearButton = (Button) findViewById(R.id.clearButton);
	clearButton.setOnClickListener(this);

	// Setup preferences
	prefs = PreferenceManager.getDefaultSharedPreferences(this);
	prefs.registerOnSharedPreferenceChangeListener(
		new OnSharedPreferenceChangeListener() {

		    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			    String key) {
			mTwitter = null;
		    }
		});

	// Start update service
	startService(new Intent(this,UpdateService.class));

	// Init location manager
	sLocationHelper = new LocationHelper(this);
	sLocationHelper.startUpdates();
    }

    private Twitter getTwitter(){
	if(mTwitter == null){
		
	    username = prefs.getString("username", "");


	    if(username == "" || username == null){
		Toast.makeText(getApplicationContext(), "Username not set!",
			Toast.LENGTH_SHORT).show();
	    }
	    
	    // See if we already have the token stored
	    SharedPreferences sharedPrefs = getSharedPreferences("Tweety", MODE_PRIVATE);
	    final String TOKEN = sharedPrefs.getString(OAUTH_TOKEN, null);
	    final String TOKEN_SECRET = sharedPrefs.getString(OAUTH_TOKEN_SECRET, null);
	    
	    if(TOKEN == null || TOKEN_SECRET == null){
	    	getOAuthPin();
	    	showDialog(PIN_DIALOG);
	    }
	    else{
	    	connectToTwitter(TOKEN, TOKEN_SECRET);
	    }
	}
	return mTwitter;
    }
    
    private void connectToTwitter(final String TOKEN, final String TOKEN_SECRET){
	    // Try to connect
	    client = new OAuthSignpostClient(TWITTER_OAUTH_KEY, TWITTER_OAUTH_SECRET, TOKEN, TOKEN_SECRET);
	    if(client.canAuthenticate())
	    	mTwitter = new Twitter(prefs.getString("username", ""),client);
    }
    
    private void gotOAuthPin(String pin){
    		SharedPreferences.Editor editor = getSharedPreferences("Tweety", MODE_PRIVATE).edit();
    		client.setAuthorizationCode(pin);
    		editor.putString(OAUTH_TOKEN, client.getAccessToken()[0]);
    		editor.putString(OAUTH_TOKEN_SECRET, client.getAccessToken()[1]);
    	    if (!editor.commit()) 
    	        throw new RuntimeException("Unable to save new token.");
    	    connectToTwitter(client.getAccessToken()[0],client.getAccessToken()[1]);
    }
    
    private void getOAuthPin(){

	    client = new OAuthSignpostClient(TWITTER_OAUTH_KEY, TWITTER_OAUTH_SECRET, "oob");
        Twitter jtwit = new Twitter(username, client);
        Intent authApp = new Intent(this, WebViewActivity.class);
        final String authURL = client.authorizeUrl().toString();
        authApp.putExtra("URL", authURL);
        
        Toast.makeText(this, "Remember PIN to authorize application", Toast.LENGTH_LONG).show();
    	startActivity(authApp);
        
    }
    
    @Override
	protected Dialog onCreateDialog(int id) {
    	switch(id){
    	case PIN_DIALOG:
            LayoutInflater factory = LayoutInflater.from(this);
            final View textEntryView = factory.inflate(R.layout.pin, null);
            final EditText pinText = (EditText) textEntryView.findViewById(R.id.pin_text);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Twitter OAuth PIN");
	        builder.setView(textEntryView);
	        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	if(pinText != null)
                		gotOAuthPin(pinText.getText().toString());
                }
            });
	        return builder.create();
    	}
    	
		return super.onCreateDialog(id);
	}

	public void onClick(View v) {

	String enteredText = textStatus.getText().toString();

	switch (v.getId()) {
	case R.id.clearButton:
	    textStatus.setText("");
	    break;
	case R.id.locationButton:
	    String newEntry = enteredText + sLocationHelper.getLocationString();
	    textStatus.setText(newEntry);
	    break;
	case R.id.UpdateButton:

	    // Status can't be empty
	    if (enteredText.length() == 0) {
		Toast.makeText(this, R.string.empty_status, Toast.LENGTH_SHORT)
			.show();
	    } else if (enteredText.length() < 0) {
		Toast.makeText(this, R.string.status_too_long,
			Toast.LENGTH_SHORT).show();
	    }
	    // Only process the button if status is not the same as the hint
	    else if (tweetButton.getId() == v.getId()) {
		try {
		    getTwitter().setStatus(textStatus.getText().toString());
		    Toast.makeText(this, R.string.status_posted,
			    Toast.LENGTH_SHORT).show();

		    // Show hint
		    textStatus.setText("");
		    // Refresh character label
		    updateCharacterCount();
		} catch (NullPointerException npe){
			Log.e(TAG, "Check username and password");
		    Toast.makeText(this, R.string.no_account,
				    Toast.LENGTH_SHORT).show();
		} catch (TwitterException te) {
		    Log.e(TAG, te.toString());
		    Toast.makeText(this, R.string.no_twitter,
			    Toast.LENGTH_SHORT).show();
		}

	    }
	    break;
	}
	// Update count of characters
	updateCharacterCount();
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
		case R.id.about:
        	    Intent ia = new Intent(Tweety.this, About.class);
        	    startActivity(ia);
        	    break;
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

    @Override
    protected void onStop() {
	sLocationHelper.stopUpdates();
	super.onStop();
    }

    private final int NUM_CHARS = 140;

    public boolean onKey(View v, int keyCode, KeyEvent event) {
	updateCharacterCount();
	return false;
    }

    private void updateCharacterCount() {
	int charsLeft = NUM_CHARS - textStatus.getText().length();
	if(charsLeft < 0){
	    tweetButton.setEnabled(false);
	    numChars.setTextColor(Color.RED);
	}
	else{
	    tweetButton.setEnabled(true);
	    numChars.setTextColor(Color.LTGRAY);
	}
	numChars.setText(String.valueOf(charsLeft));
    }

}