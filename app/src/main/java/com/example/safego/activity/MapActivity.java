package com.example.safego.activity;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.safego.R;
import com.example.safego.Service.LocationService;
import com.example.safego.utils.Commons;
import com.example.safego.utils.Constants;
import com.example.safego.utils.CurrentLocation;
import com.example.safego.utils.SharedPrefHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap googleMap;
    private MarkerOptions pickupLocation, deliveryLocation, currentLocation;
    Button trackingLocation;
    SharedPrefHelper sharedPrefHelper;
    String is_online="0";
    ImageView gMapButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
        trackingLocation =findViewById(R.id.trackingButton);
        gMapButton = findViewById(R.id.gMapButton);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.showInMap);
        mapFragment.getMapAsync(this);
        Commons.showToast(getApplicationContext(),CurrentLocation.CURR_LAT+"----"+CurrentLocation.CURR_LONGI);
        setButtonText();
        currentLocation=new MarkerOptions().position(new LatLng(Double.parseDouble(CurrentLocation.CURR_LAT), Double.parseDouble(CurrentLocation.CURR_LONGI))).title("Your Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        trackingLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trackingLocation.getText().equals(Constants.START_TRACKING))
                {
                    trackingLocation.setText(Constants.LOCATION_SENDING);
                    sharedPrefHelper.saveDataToSharedPref(Constants.IS_ONLINE,"1");
                    trackingLocation.setBackground(getResources().getDrawable(R.drawable.button_background_yellow));
                    Commons.showToast(getApplicationContext(),"location sending to the friends");
                    Log.e("LocationUpdate","active");
                    locationServiceOn();
                }
                else
                {
                    Log.e("LocationUpdate","inactive");
                    locationServiceStop();
                    trackingLocation.setText(Constants.START_TRACKING);
                    trackingLocation.setBackground(getResources().getDrawable(R.drawable.button_background_blue));
                    sharedPrefHelper.saveDataToSharedPref(Constants.IS_ONLINE,"0");
                    Commons.showToast(getApplicationContext(),"location sending off");
                }
            }
        });
        gMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String destination="http://maps.google.com/maps?&daddr="+CurrentLocation.CURR_LAT+","+CurrentLocation.CURR_LONGI;
                    Intent intend = new Intent(Intent.ACTION_VIEW, Uri.parse(destination));
                    startActivity(intend);
            }
        });
    }

    private void setButtonText() {

        is_online=sharedPrefHelper.getStringFromSharedPref(Constants.IS_ONLINE);
        if(is_online==null || is_online.equals("0"))
        {
            Log.e("LocationUpdate","inactive");
            locationServiceStop();
            trackingLocation.setText(Constants.START_TRACKING);
            trackingLocation.setBackground(getResources().getDrawable(R.drawable.button_background_blue));
        }
        else
        {
            trackingLocation.setText(Constants.LOCATION_SENDING);
            trackingLocation.setBackground(getResources().getDrawable(R.drawable.button_background_yellow));
            Log.e("LocationUpdate","active");
            locationServiceOn();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onMapReady(GoogleMap gmap) {
        googleMap = gmap;
//        try {
            googleMap.addMarker(currentLocation);
//        }
//        catch(Exception e)
//        {
//            Toast.makeText(this, "please wait and check your GPS settings",Toast.LENGTH_SHORT).show();
//            Log.e("UpdateStatus",e.toString());
//            finish();
//        }
        CameraPosition camera;
        camera = new CameraPosition.Builder()
                .target(new LatLng(Double.parseDouble(CurrentLocation.CURR_LAT), Double.parseDouble(CurrentLocation.CURR_LONGI)))
                .zoom(15)
                .bearing(0)
                .tilt(45)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
    }
    public void locationServiceOn()
    {
        Intent intent = new Intent(getApplicationContext(), LocationService.class);
        intent.putExtra(Constants.NOTIFICATION_TITLE, Constants.SAFE_GO);
        intent.setAction(Constants.START_FORGROUND_ACTION);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            MapActivity.this.startForegroundService(intent);
        }else{
            startService(intent);
        }
    }
    public void locationServiceStop()
    {
        Intent intent = new Intent(getApplicationContext(), LocationService.class);
        intent.putExtra(Constants.NOTIFICATION_TITLE, Constants.SAFE_GO);
        intent.setAction(Constants.STOP_FORGROUND_ACTION);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            MapActivity.this.startForegroundService(intent);
        }else{
            startService(intent);
        }
    }

}
