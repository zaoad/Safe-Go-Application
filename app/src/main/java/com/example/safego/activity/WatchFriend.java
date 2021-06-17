package com.example.safego.activity;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.safego.R;
import com.example.safego.utils.Commons;
import com.example.safego.utils.Constants;
import com.example.safego.utils.CurrentLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class WatchFriend extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap googleMap;
    private MarkerOptions markerOptions1;
    ImageView gMapButton;
    String friendLatitude;
    String friendLongitude;
    double latD;
    double longiD;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_friend);
        gMapButton = findViewById(R.id.gMapButton);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.showInMap);
        mapFragment.getMapAsync(this);
        //Commons.showToast(getApplicationContext(), CurrentLocation.CURR_LAT+"----"+CurrentLocation.CURR_LONGI);
        friendLatitude=getIntent().getStringExtra(Constants.LATITUDE);
        friendLongitude=getIntent().getStringExtra(Constants.LONGITUDE);
        String friendPhoneNumber=getIntent().getStringExtra(Constants.FRIEND_NUMBER);
        latD=Double.parseDouble(friendLatitude);
        longiD=Double.parseDouble(friendLongitude);
        if(latD==0.0||longiD==0.0)
        {
            Commons.showToast(getApplicationContext(),"Your friend location Can not be shown");
            return;
        }
        markerOptions1=new MarkerOptions().position(new LatLng(latD, longiD)).title("your friends "+friendPhoneNumber+ "location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        gMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String destination="http://maps.google.com/maps?&daddr="+ latD+","+longiD;
                Intent intend = new Intent(Intent.ACTION_VIEW, Uri.parse(destination));
                startActivity(intend);
            }
        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onMapReady(GoogleMap gmap) {
        googleMap = gmap;
//        try {
        googleMap.addMarker(markerOptions1);
//        }
//        catch(Exception e)
//        {
//            Toast.makeText(this, "please wait and check your GPS settings",Toast.LENGTH_SHORT).show();
//            Log.e("UpdateStatus",e.toString());
//            finish();
//        }
        CameraPosition camera;
        camera = new CameraPosition.Builder()
                .target(new LatLng(23.79,90.4))
                .zoom(15)
                .bearing(0)
                .tilt(45)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
    }

}

