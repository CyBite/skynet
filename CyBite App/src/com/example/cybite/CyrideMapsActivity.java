package com.example.cybite;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CyrideMapsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cyride_maps);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cyride_maps, menu);
		return true;
	}

}
