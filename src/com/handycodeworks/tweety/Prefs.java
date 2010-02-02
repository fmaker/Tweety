package com.handycodeworks.tweety;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Prefs extends PreferenceActivity{
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	
	// Inflate preferences from XML file
	addPreferencesFromResource(R.xml.prefs);
    }

}
