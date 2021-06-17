package com.example.safego.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.safego.R;
import com.example.safego.Service.LocationService;
import com.example.safego.Service.LocationTrack;
import com.example.safego.domain.ReceiveLocation;
import com.example.safego.dto.CrimeReportDto;
import com.example.safego.dto.ReceiveLocationDto;
import com.example.safego.retrofit.API;
import com.example.safego.retrofit.RetrofitInstance;
import com.example.safego.utils.Commons;
import com.example.safego.utils.Constants;
import com.example.safego.utils.CurrentLocation;
import com.example.safego.utils.SharedPrefHelper;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class HomePageActivity extends AppCompatActivity {


    private TextView sosTxtView;

    private TextView findSafeRouteTxtView;

    private TextView TrackMeTxtView;

    private TextView reportCrimeTxtView;

    private TextView manageFriedListTxtView;

    private TextView crimeStatisticsTxtView;

    private TextView watchFriendTxtView;

    private String longitude,latitude;

    List<CrimeReportDto> crimeReportDtos;

    SharedPrefHelper sharedPrefHelper;

    String is_online="0";
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    boolean isFriendInDanger=false;
    double friendLatitude,friendLongitude;
    String friendPhoneNumber;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    API api;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);
        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        isPermissionGranted();
        setContentView(R.layout.home_page_activity);
        api= RetrofitInstance.getRetrofitInstance().create(API.class);
        sosTxtView=findViewById(R.id.sos);
        findSafeRouteTxtView=findViewById(R.id.findSafeRoute);
        TrackMeTxtView=findViewById(R.id.trackMe);
        reportCrimeTxtView=findViewById(R.id.reportCrime);
        manageFriedListTxtView=findViewById(R.id.manageFriendList);
        crimeStatisticsTxtView=findViewById(R.id.crimeStatistics);
        watchFriendTxtView = findViewById(R.id.watch_friend);
        getFriendLocation();
        sosTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sosTxtView.setBackground(getResources().getDrawable(R.drawable.button_background_yellow));
                Commons.showToast(getApplicationContext(),"location sent to the close friends");
//                locationServiceOn();
//                Commons.showToast(getApplicationContext(),"current latitude"+CurrentLocation.CURR_LAT+" current Longitude"+CurrentLocation.CURR_LONGI);
                double lat=0,longi=0;
                locationTrack = new LocationTrack(HomePageActivity.this);

                if (locationTrack.canGetLocation()) {
                    longi = locationTrack.getLongitude();
                    lat = locationTrack.getLatitude();

                    Toast.makeText(getApplicationContext(), "Longitude:" + CurrentLocation.CURR_LAT + "\nLatitude:" + CurrentLocation.CURR_LONGI, Toast.LENGTH_SHORT).show();
                } else {
                    locationTrack.showSettingsAlert();
                }
                if(lat==0.0 || longi==0.0)
                {
                    Toast.makeText(getApplicationContext(),"please wait or turn on gps",Toast.LENGTH_SHORT).show();
                }
                latitude = Double.toString(lat);
                longitude = Double.toString(longi);
                Intent intent=new Intent(getApplicationContext(),SosActivity.class);
                startActivity(intent);
                //Commons.showToast(getApplicationContext(),latitude+" "+longitude);
//                sosTxtView.setBackground(getResources().getDrawable(R.drawable.button_background_blue));
            }
        });

        findSafeRouteTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Commons.showToast(getApplicationContext(),"find safest route button click");
            }
        });

        TrackMeTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Commons.showToast(getApplicationContext(),"track me button click");
                if(CurrentLocation.CURR_LONGI.equals("")||CurrentLocation.CURR_LAT.equals("") ||
                        Double.parseDouble(CurrentLocation.CURR_LAT)==0||Double.parseDouble(CurrentLocation.CURR_LONGI)==0)
                {
                    is_online=sharedPrefHelper.getStringFromSharedPref(Constants.IS_ONLINE);
                    if(is_online==null || is_online.equals("0")) {
                        locationServiceOn();
                    }
                    else{
                        locationServiceStop();
                        locationServiceOn();
                    }
                }
                else {
                    Intent mySuperIntent = new Intent(getApplicationContext(), MapActivity.class);
                    startActivity(mySuperIntent);
                }
            }
        });
        watchFriendTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFriendInDanger) {
                    Intent mySuperIntent = new Intent(getApplicationContext(), WatchFriend.class);
                    mySuperIntent.putExtra(Constants.LATITUDE,friendLatitude);
                    mySuperIntent.putExtra(Constants.LONGITUDE,friendLongitude);
                    mySuperIntent.putExtra(Constants.FRIEND_NUMBER,friendPhoneNumber);
                    startActivity(mySuperIntent);
                }
            }
        });
        reportCrimeTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefHelper.saveDataToSharedPref(Constants.LATITUDE,"0.0");
                sharedPrefHelper.saveDataToSharedPref(Constants.LONGITUDE,"0.0");
                sharedPrefHelper.saveDataToSharedPref(Constants.CRIME_DETAILS,"");
                sharedPrefHelper.saveDataToSharedPref(Constants.TIME,"");
                Intent intent = new Intent(getApplicationContext(), ReportCrime.class);
                startActivity(intent);
            }
        });

        manageFriedListTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Commons.showToast(getApplicationContext(),"manage friend list button click");
                Intent mySuperIntent = new Intent(getApplicationContext(), ManageFriendList.class);
                startActivity(mySuperIntent);
            }
        });

        crimeStatisticsTxtView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent mySuperIntent = new Intent(getApplicationContext(), CrimeStatisticsMapActivity.class);
//                mySuperIntent.putExtra(Constants.CRIME_REPORTS, (Serializable) crimeReportDtos);
                startActivity(mySuperIntent);
//                getCrimeReports();
//                Commons.showToast(getApplicationContext(),"crime statistics button click");

            }
        });

    }

    private void getFriendLocation() {
        String phoneNumber=sharedPrefHelper.getStringFromSharedPref(Constants.MOBILE_NUMBER);
        Call<ReceiveLocationDto> call=api.getReceiveLocationByPhoneNumber(phoneNumber);
        call.enqueue(new Callback<ReceiveLocationDto>() {
            @Override
            public void onResponse(Call<ReceiveLocationDto> call, Response<ReceiveLocationDto> response) {
                Object newObj= response.body();
                ReceiveLocationDto receiveLocationDto=response.body();
                if(receiveLocationDto == null ||receiveLocationDto.getSenderPhoneNumber().equals("None"))
                {
                   isFriendInDanger=false;
                }
                else{
                    friendPhoneNumber=receiveLocationDto.getSenderPhoneNumber();
                    friendLatitude=receiveLocationDto.getLatitude();
                    friendLongitude=receiveLocationDto.getLongitude();
                    isFriendInDanger=true;
                    watchFriendTxtView.setText("watch your friends 1");
                }
            }

            @Override
            public void onFailure(Call<ReceiveLocationDto> call, Throwable t) {

            }
        });
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("TAG", "Permission is granted");
                return true;
            } else {

                Log.e("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.e("TAG", "Permission is granted");
            return true;
        }
    }
    public void locationServiceOn()
    {
        Intent intent = new Intent(getApplicationContext(), LocationService.class);
        intent.putExtra(Constants.NOTIFICATION_TITLE, Constants.SAFE_GO);
        intent.setAction(Constants.START_FORGROUND_ACTION);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            HomePageActivity.this.startForegroundService(intent);
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
            HomePageActivity.this.startForegroundService(intent);
        }else{
            startService(intent);
        }
    }
    private void getCrimeReports() {
        Call<List<CrimeReportDto>> call= api.getAllCrimeReports();
        call.enqueue(new Callback<List<CrimeReportDto>>() {
            @Override
            public void onResponse(Call<List<CrimeReportDto>> call, Response<List<CrimeReportDto>> response) {
                crimeReportDtos= response.body();
                Log.e("crime stat activity","success");
                Intent mySuperIntent = new Intent(getApplicationContext(), CrimeStatisticsMapActivity.class);
                mySuperIntent.putExtra(Constants.CRIME_REPORTS, (Serializable) crimeReportDtos);
                startActivity(mySuperIntent);
//                addGoogleMarkers();
            }

            @Override
            public void onFailure(Call<List<CrimeReportDto>> call, Throwable t) {
                Log.e("crime stat activity","failure");
            }
        });
    }
    public void addGoogleMarkers()
    {

        for(CrimeReportDto crimeReportDto : crimeReportDtos)
        {
            if(!(crimeReportDto.getLatitude()==0||crimeReportDto.getLongitude()==0))
            {
                double latitude=crimeReportDto.getLatitude();
                double longitude = crimeReportDto.getLongitude();
                String details =crimeReportDto.getCrimeType();

//                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(latitude, longitude)).title(details).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                markerOptionsList.add(markerOption);
//                googleMap.addMarker(markerOption);

            }
        }


    }
}
