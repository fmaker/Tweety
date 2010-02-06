package com.handycodeworks.tweety;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver{
    private static final String TAG = "BootReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
	context.startService(new Intent(context,UpdateService.class));
    }

}
