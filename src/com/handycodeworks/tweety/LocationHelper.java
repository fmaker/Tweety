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

import java.io.PrintStream;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class LocationHelper implements LocationListener {
    private static final String TAG = "LocationHelper";
    private static final int MIN_UPDATE_MS = 60*1000;
    private static final int MIN_DISTANCE_M = 1000;
    
    Context context;
    static LocationManager sLocManager;
    Criteria mCriteria;
    String mBestProvider;
    Location location;

    public LocationHelper(Context context) {
	super();
	this.context = context;
	sLocManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
	mCriteria = new Criteria();
	mBestProvider = sLocManager.getBestProvider(mCriteria, true);
	updateLocation(mBestProvider);
    }
    
    String getLocationString(){
	
	String locString = "";
	if(location != null){
	    locString = "http://geopo.at/" + encode(location.getLatitude(),location.getLongitude(),3);
	}
	else{
	    String msg = (String) context.getString(R.string.location_unavailiable);
	    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	Log.d(TAG,"Location string is: "+locString);
	return locString;
    }
    
    public void startUpdates(){
	sLocManager.requestLocationUpdates(mBestProvider, MIN_UPDATE_MS, MIN_DISTANCE_M, this);
    }
    
    public void stopUpdates(){
	sLocManager.removeUpdates(this);
    }

    public void onLocationChanged(Location location) {
	this.location = location;
    }

    public void onProviderDisabled(String provider) {
	updateLocation(provider);
    }

    public void onProviderEnabled(String provider) {
	updateLocation(provider);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
	updateLocation(provider);
    }
    
    private void updateLocation(String provider){
	if(provider.equals(mBestProvider)){
	    mBestProvider = sLocManager.getBestProvider(mCriteria, true);
	    location = sLocManager.getLastKnownLocation(mBestProvider);
	}
    }
    
    /**
     * GeoPo Encode in Java.
     *
     * @param lat latitude
     * @param lng longnitude
     * @param scale scale of map
     * @return geopo code
     */
    public static String encode(double lat, double lng, int scale) {

        // 64characters (number + big and small letter + hyphen + underscore).
        final String chars =
                "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";

        StringBuffer geopo = new StringBuffer();

        lat = (lat + 90) / 180 * Math.pow(8, 10);
        lng = (lng + 180) / 360 * Math.pow(8, 10);

        for(int i = 0; i < scale; i++) {
            geopo.append(
                    chars.charAt((int)(Math.floor(lat / Math.pow(8, 9 - i) % 8)
                            + Math.floor(lng / Math.pow(8, 9 - i) % 8) * 8)));
        }

        return geopo.toString();

    }
    
}
