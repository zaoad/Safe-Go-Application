package com.example.safego.Service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.safego.R;
import com.example.safego.activity.HomePageActivity;
import com.example.safego.domain.ReceiveLocation;
import com.example.safego.domain.SendingLocation;
import com.example.safego.retrofit.API;
import com.example.safego.retrofit.RetrofitInstance;
import com.example.safego.utils.Commons;
import com.example.safego.utils.Constants;
import com.example.safego.utils.CurrentLocation;
import com.example.safego.utils.SharedPrefHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationService  extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private String is_online="0";
    API api;
    SharedPrefHelper sharedPrefHelper;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPrefHelper=new SharedPrefHelper(getApplicationContext());
        api= RetrofitInstance.getRetrofitInstance().create(API.class);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= 26) {
            createNotification(intent);
        }
        if(intent.getAction().equals(Constants.START_FORGROUND_ACTION)) {
            if(mFusedLocationClient==null) {
                requestLocationUpdate();
                Log.i("LocationUpadateService", "Recieved start foreground intent");
            }
        }
        else if(intent.getAction().equals(Constants.STOP_FORGROUND_ACTION)){
            if(mFusedLocationClient != null)
            {
                mFusedLocationClient.removeLocationUpdates(locationCallback);
            }
            stopForeground(true);
            stopSelf();
            Log.i("LocationUpadateService","Recieved stop foreground intent");
        }
        return START_NOT_STICKY;
    }

    private void createNotification(Intent intent) {
        String input = intent.getStringExtra(Constants.NOTIFICATION_TITLE);
        createNotificationChannel();
        Intent notificationIntent = new Intent(getApplicationContext(), HomePageActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("User location updating")
                .setContentText(input)
                .setSmallIcon(R.drawable.safegologo)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
    }


    public void requestLocationUpdate() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(Constants.FASTEST_INTERVAL_TIME);
        locationRequest.setInterval(Constants.INTERVAL_TIME);
        locationCallback=new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                String lat = ""+locationResult.getLastLocation().getLatitude();
                String longi = ""+locationResult.getLastLocation().getLongitude();
                CurrentLocation.CURR_LAT= lat;
                CurrentLocation.CURR_LONGI = longi;
                locationSendServer(Double.parseDouble(lat),Double.parseDouble(longi));
                Commons.showToast(getApplicationContext(),"latitude: "+lat+" Longitude"+longi);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("testing");
            return;
        }
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback , getMainLooper());
    }
    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID, "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
    public void locationSendServer(double latitude, double longitude)
    {

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
        SendingLocation sendingLocation=new SendingLocation();
        sendingLocation.setSenderPhoneNumber(phoneNumber);
        sendingLocation.setLatitude(latitude);
        sendingLocation.setLongitude(longitude);
        Call<Void> call = api.addSendingLocation(sendingLocation);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
        for(String receiveLocationStr : mobileNumberList) {
            ReceiveLocation receiveLocation= new ReceiveLocation();

            receiveLocation.setSenderPhoneNumber(phoneNumber);
            receiveLocation.setLatitude(latitude);
            receiveLocation.setLongitude(longitude);
            receiveLocation.setReceiverPhoneNumber(receiveLocationStr);
            Call<Void> call1 = api.addReceiveLocation(receiveLocation);
            call1.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }
    }

}
