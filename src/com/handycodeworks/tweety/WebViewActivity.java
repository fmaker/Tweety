package com.handycodeworks.tweety;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends Activity {
	public final String URL = "URL";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView wv = new WebView(this);
		setContentView(wv);
		final String page = getIntent().getStringExtra(URL);
		wv.loadUrl(page);
	}
}
