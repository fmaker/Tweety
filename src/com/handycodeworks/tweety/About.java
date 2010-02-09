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

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

public class About extends ListActivity {
    
    ArrayList<HashMap<String, String>> list = 
	new ArrayList<HashMap<String,String>>();
    
    @Override
    public void onCreate(Bundle icicle) {
	super.onCreate(icicle);
	setContentView(R.layout.about);
	setTitle("About Tweety");
	addItems();
	SimpleAdapter notes = new SimpleAdapter(this, list,
		R.layout.list_item, 
		new String[] { "title", "description", "link" }, 
		new int[] { R.id.text1, 
			    R.id.text2, 
			    R.id.text3});
	setListAdapter(notes);
	getListView().setTextFilterEnabled(true);

    }

    private void addItems() {
	HashMap<String, String> item;

	item = new HashMap<String, String>();
	item.put("title", "Author");
	item.put("description", "Frank Maker, Handy Codeworks LLC");
	item.put("link","fmaker@handycodeworks.com");
	list.add(item);

	item = new HashMap<String, String>();
	item.put("title", "Icon");
	item.put("description", "Matt Hamm, Supereight Studio");
	item.put("link","matt@supereightstudio.com");
	list.add(item);

	item = new HashMap<String, String>();
	item.put("title", "Title Background");
	item.put("description", "Hugh Briss");
	item.put("link","http://www.hughbriss.com");
	list.add(item);

//	item = new HashMap<String, String>();
//	item.put("title", "Tweet Button");
//	item.put("description", "Jayson Lorenzen");
//	item.put("link","http://www.flickr.com/photos/jaysonlorenzen");
//	list.add(item);

	item = new HashMap<String, String>();
	item.put("title", "Carbon Fiber Background");
	item.put("description", "okiegeek");
	item.put("link","http://www.flickr.com/people/forceusr/");
	list.add(item);

	item = new HashMap<String, String>();
	item.put("title", "Location Icon");
	item.put("description", "Joseph Wain");
	item.put("link","http://glyphish.com/");
	list.add(item);
	

    }

}
