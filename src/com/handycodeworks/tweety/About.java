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
	item.put("description", "Frank Maker");
	item.put("link","fmaker@handycodeworks.com");
	list.add(item);

	item = new HashMap<String, String>();
	item.put("title", "Company");
	item.put("description", "Handy Codeworks LLC");
	item.put("link","www.handycodeworks.com");
	list.add(item);

	item = new HashMap<String, String>();
	item.put("title", "Icon");
	item.put("description", "Matt Hamm: ");
	item.put("link","matt@supereightstudio.com");
	list.add(item);

    }

}
