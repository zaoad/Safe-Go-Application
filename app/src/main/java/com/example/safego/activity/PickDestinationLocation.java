package com.example.safego.activity;

import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.safego.R;
import com.example.safego.utils.Commons;
import com.example.safego.utils.Constants;
import com.example.safego.utils.SharedPrefHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Locale;

public class PickDestinationLocation extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerDragListener {
    Button selectBtn;
    public double lat=0.0,longi=0.0;
    SharedPrefHelper sharedPrefHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pickup_location);
        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.showInMap);
        mapFragment.getMapAsync(this);
        selectBtn=findViewById(R.id.selectBtn);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lat!=0.0 && longi!=0.0)
                {
                    Intent intent = new Intent(getApplicationContext(), PickLocationSafeRoute.class);
                    sharedPrefHelper.saveDataToSharedPref(Constants.DESTINATION_LATITUDE, lat+"");
                    sharedPrefHelper.saveDataToSharedPref(Constants.DESTINATION_LONGITUDE, longi+"");
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        CameraPosition camera;
        camera = new CameraPosition.Builder()
                .target(new LatLng(23.8,90.4))
                .zoom(13)
                .bearing(0)
                .tilt(45)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
        googleMap.setOnMarkerDragListener(this);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(23.8,90.4)).draggable(true));
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDrag(Marker marker) {

            }
            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng newLocation = marker.getPosition();
                Commons.showToast(getApplicationContext(),newLocation.latitude+", "+newLocation.longitude);
                lat=newLocation.latitude;
                longi=newLocation.longitude;
                //                mLocation.setLatitude(newLocation.latitude);
//                mLocation.setLongitude(newLocation.longitude);
//                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), 15.0f));

            }
            @Override
            public void onMarkerDragStart(Marker marker) {}

        });
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Commons.showToast(getApplicationContext(),"marker");
        LatLng latLng = marker.getPosition();
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            android.location.Address address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
