package com.handycodeworks.tweety;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TimelineAdapter extends SimpleCursorAdapter {

    private static final String[] from = { DatabaseOpenHelper.C_CREATED_AT,
	    DatabaseOpenHelper.C_USER, DatabaseOpenHelper.C_TEXT };
    private static final int[] to = { R.id.rowTime, R.id.rowName,
	    R.id.rowStatus };

    public TimelineAdapter(Context context, Cursor c) {
	super(context, R.layout.row, c, from, to);
    }

    /* 
     * This is where data is mapped to its view
     */
    @Override
    public void bindView(View row, Context context, Cursor c) {
	super.bindView(row, context, c);
	long createdTime = c.getLong(c.getColumnIndex(DatabaseOpenHelper.C_CREATED_AT));
	String user = c.getString(c.getColumnIndex(DatabaseOpenHelper.C_USER));
	String text = c.getString(c.getColumnIndex(DatabaseOpenHelper.C_TEXT));
	
	// Find view for each item
	TextView textCreatedTime = (TextView) row.findViewById(R.id.rowTime);
	TextView textUser = (TextView) row.findViewById(R.id.rowName);
	TextView textText = (TextView) row.findViewById(R.id.rowStatus);
	
	// Convert time
	String timeSince = DateUtils.getRelativeTimeSpanString(createdTime).toString();
	textCreatedTime.setText(timeSince);
	textUser.setText(user);
	textText.setText(text);
    }
    

}
