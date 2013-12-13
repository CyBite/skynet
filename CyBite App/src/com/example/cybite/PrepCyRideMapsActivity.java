package com.example.cybite;


import java.util.ArrayList;

import com.DatabaseAPI.Restaurant;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

public class PrepCyRideMapsActivity extends Activity {

// Checkboxes for Routes 1 Red to 24 Silver
int[] checkboxes = {R.id.checkBox1, R.id.checkBox2, R.id.checkBox3, R.id.checkBox4, R.id.checkBox5, R.id.checkBox6,
					R.id.checkBox7, R.id.checkBox8,R.id.checkBox9, R.id.checkBox10, R.id.checkBox11, R.id.checkBox12, 
					R.id.checkBox13, R.id.checkBox14, R.id.checkBox15, R.id.checkBox16, R.id.checkBox17, R.id.checkBox18};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// Create instance
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prep_cyridemaps);
        initializeCyRideSpinner();
        
        // Create View Times Button and Event
        Button viewTimesButton = (Button) findViewById(R.id.ViewTimes);
        viewTimesButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.cyride.com/index.aspx?page=897"));
				startActivity(browserIntent);
			} });
        
        // Create Submit Button and forwarding of information
        Button submitButton = (Button) findViewById(R.id.ShowRoutes);
        submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// On submit, only add those checkboxes that are checked unless they are 19 (View All Routes)
				// or 20 (View Closest Route)
				ArrayList<String> checkedBoxes = new ArrayList<String>();
				if(((CheckBox) findViewById(R.id.checkBox20)).isChecked()) {
					checkedBoxes.add(((CheckBox)findViewById(R.id.checkBox20)).getText().toString());
				}
				else {
				for(int i = 0; i < checkboxes.length; i++)
				{
					CheckBox box = (CheckBox) findViewById(checkboxes[i]);
					if((box.isChecked() && box.isEnabled()) || ((CheckBox) findViewById(R.id.checkBox19)).isChecked())
					{
						checkedBoxes.add(box.getText().toString());
					}				
				}
				}
				 Intent intent = new Intent(PrepCyRideMapsActivity.this, CyRideMapsActivity.class);
				 intent.putExtra("restaurant", (Restaurant) getIntent().getExtras().get("restaurant"));
				 intent.putExtra("CheckedBoxes", checkedBoxes);
				 intent.putExtra("routeSpinner", (String) ((Spinner)findViewById(R.id.routeSpinner)).getSelectedItem());
				 PrepCyRideMapsActivity.this.startActivity(intent);
			}
		});
        
        // When this checkbox is checked, disable all others, so you can only view closest route(s)
        ((CheckBox) findViewById(R.id.checkBox19)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {	
    		@Override
    		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    			for(int i = 0; i < checkboxes.length; i++)
    			{
    				if(isChecked) {
    					((CheckBox) findViewById(checkboxes[i])).setEnabled(false);
    				} else {
    					((CheckBox) findViewById(checkboxes[i])).setEnabled(true);
    				}
    			}
    			if(isChecked) {
    				((CheckBox) findViewById(R.id.checkBox20)).setEnabled(false);
    				((Spinner) findViewById(R.id.routeSpinner)).setEnabled(false);
    				((TextView) findViewById(R.id.textView7)).setEnabled(false);
    			} else {
    				((CheckBox) findViewById(R.id.checkBox20)).setEnabled(true);
    				((Spinner) findViewById(R.id.routeSpinner)).setEnabled(true);
    				((TextView) findViewById(R.id.textView7)).setEnabled(true);
    			}
    		}
    	});
        
        // When this checkbox is checked, disable all others since all routes will be displayed to the user
        ((CheckBox) findViewById(R.id.checkBox20)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    		@Override
    		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    			for(int i = 0; i < checkboxes.length; i++)
    			{
    				if(isChecked) {
    					((CheckBox) findViewById(checkboxes[i])).setEnabled(false);
    				} else {
    					((CheckBox) findViewById(checkboxes[i])).setEnabled(true);
    				}
    			}
    			if(isChecked) {
    				((CheckBox) findViewById(R.id.checkBox19)).setEnabled(false);
    			} else {
    				((CheckBox) findViewById(R.id.checkBox19)).setEnabled(true);
    			}
    		}
    	});
    }
    
    // Creates a spinner that displays 1 to 5 for number of closest routes to display
    public void initializeCyRideSpinner() {
    	Spinner routeSpinner = (Spinner) findViewById(R.id.routeSpinner);
    	ArrayList<String> routeList = new ArrayList<String>();
    	for(int i = 1; i <= 5; i++) {
    		routeList.add(String.valueOf(i));
    	}
    	ArrayAdapter<String> routeSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, routeList);
    	routeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		routeSpinner.setAdapter(routeSpinnerAdapter);
    }
}
