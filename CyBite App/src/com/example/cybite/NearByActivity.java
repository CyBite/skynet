package com.example.cybite;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.DatabaseAPI.DatabaseAPI;
import com.DatabaseAPI.Restaurant;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NearByActivity extends FragmentActivity {

	GoogleMap googleMap;
	ArrayList<Restaurant> restaurants;
	
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_near_by);
	    // Getting Google Play availability status
	    int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

	    // Showing status
	    if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available

	        int requestCode = 10;
	        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
	        dialog.show();

	    }else { // Google Play Services are available

	        // Getting reference to the SupportMapFragment of activity_main.xml
	        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

	        // Getting GoogleMap object from the fragment
	        googleMap = fm.getMap();

	        // Enabling MyLocation Layer of Google Map
	        googleMap.setMyLocationEnabled(true);

	        // Getting LocationManager object from System Service LOCATION_SERVICE
	        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

	        // Creating a criteria object to retrieve provider
	        Criteria criteria = new Criteria();

	        // Getting the name of the best provider
	        String provider = locationManager.getBestProvider(criteria, true);

	        // Getting Current Location
	        Location location = locationManager.getLastKnownLocation(provider);

	        LocationListener locationListener = new LocationListener() {
	          public void onLocationChanged(Location location) {
	          // redraw the marker when get location update.
	        	  createMap(location);
	        }

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}};

	        if(location!=null){
	           //PLACE THE INITIAL MARKER              
	        	createMap(location);
	        }
	        //locationManager.requestLocationUpdates(provider, 20000, 0, locationListener);        
	    }
	}

	private void createMap(final Location location)
	{
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run() {
				DatabaseAPI db = new DatabaseAPI();
				restaurants = db.getRestaurants();
				db.disconnect();
				drawMarkersAndMove(location, restaurants);
			}});
		thread.start();
	}
	
	
	private void drawMarkersAndMove(final Location location, final ArrayList<Restaurant> rests)
	{
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable(){

			@Override
			public void run() {
				LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
				for(int i = 0; i < rests.size(); i++)
				{
					googleMap.addMarker(new MarkerOptions()
				 	.position(new LatLng(rests.get(i).getLat(), rests.get(i).getLongitude()))
				 	.snippet(rests.get(i).getName() + "\na " + rests.get(i).getGenre() + " restaurant")
				 	.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
				    .title(rests.get(i).getName()));
					googleMap.setOnMarkerClickListener(new OnMarkerClickListener()
		                {

							@Override
							public boolean onMarkerClick(Marker marker)
							{
								System.out.println("Starting");
								for(int i = 0; i < restaurants.size(); i++)
								{
									System.out.println("Looping");
									if(restaurants.get(i).getName().equals(marker.getTitle()))
									{
										Intent intent = new Intent(NearByActivity.this, RestaurantInfoActivity.class);
										intent.putExtra("restaurant", restaurants.get(i));
										startActivity(intent);
									}
								}
								return false;
							}
							
		                });
				}
				//Move camera to that location
				CameraUpdate center = CameraUpdateFactory.newLatLng(currentPosition);
				googleMap.moveCamera(center);
				//Zoom in to location
				CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
				googleMap.animateCamera(zoom);
			} 

		});	
	}
	
	
}