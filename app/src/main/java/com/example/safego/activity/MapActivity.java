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
import com.example.safego.domain.ActiveUser;
import com.example.safego.domain.ReceiveLocation;
import com.example.safego.domain.SendingLocation;
import com.example.safego.retrofit.API;
import com.example.safego.retrofit.RetrofitInstance;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap googleMap;
    private MarkerOptions pickupLocation, deliveryLocation, currentLocation;
    Button trackingLocation;
    SharedPrefHelper sharedPrefHelper;
    String is_online="0";
    ImageView gMapButton;
    API api;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        api= RetrofitInstance.getRetrofitInstance().create(API.class);
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
                    removeActiveUser();
                    addActiveUser();
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
                    removeActiveUser();
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

    private void addActiveUser() {
        String n1,n2,n3,n4,n5;
        n1=sharedPrefHelper.getStringFromSharedPref(Constants.FRIEND1);
        n2=sharedPrefHelper.getStringFromSharedPref(Constants.FRIEND2);
        n3=sharedPrefHelper.getStringFromSharedPref(Constants.FRIEND3);
        n4=sharedPrefHelper.getStringFromSharedPref(Constants.FRIEND4);
        n5=sharedPrefHelper.getStringFromSharedPref(Constants.FRIEND5);
        List<String> mobileNumberList= new ArrayList<>();
        if(n1==null||n1=="")
        {
            n1="";

        }
        else{
            mobileNumberList.add(n1);
        }
        if(n2==null||n2=="")
        {
            n2="";
        }
        else{
            mobileNumberList.add(n2);
        }
        if(n3==null||n3=="")
        {
            n3="";
        }
        else{
            mobileNumberList.add(n3);
        }
        if(n4==null||n4=="")
        {
            n4="";
        }
        else{
            mobileNumberList.add(n4);
        }
        if(n5==null||n5=="")
        {
            n5="";
        }
        else{
            mobileNumberList.add(n5);
        }
        String phoneNumber=sharedPrefHelper.getStringFromSharedPref(Constants.MOBILE_NUMBER);
        for(String receiveLocationStr : mobileNumberList) {
            ActiveUser activeUser=new ActiveUser();
            activeUser.setSenderPhoneNumber(phoneNumber);
            activeUser.setReceiverPhoneNumber(receiveLocationStr);
            activeUser.setActive(true);
            Call<String> call = api.addActiveUser(activeUser);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.e("MapActive","successful");
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("MapActive",t.toString());
                }
            });

        }
    }

    private void removeActiveUser() {
        String phoneNumber=sharedPrefHelper.getStringFromSharedPref(Constants.MOBILE_NUMBER);
        Call<Void> call= api.deleteActiveUser(phoneNumber);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("MapActive","successful");

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("MapActive",t.toString());
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
    @Override
    public void onBackPressed() {
        Intent mySuperIntent = new Intent(getApplicationContext(), HomePageActivity.class);
        startActivity(mySuperIntent);
        finish();
    }

}
