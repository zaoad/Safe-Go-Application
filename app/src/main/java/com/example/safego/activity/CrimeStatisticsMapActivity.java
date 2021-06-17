package com.example.safego.activity;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.safego.R;
import com.example.safego.dto.CrimeReportDto;
import com.example.safego.retrofit.API;
import com.example.safego.retrofit.RetrofitInstance;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrimeStatisticsMapActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap googleMap;
    private MarkerOptions markerOptions1, markerOptions2, markerOptions3,markerOptions4,markerOptions5, markerOptions6, markerOptions7,markerOptions8,
    markerOptions9, markerOptions10, markerOptions11,markerOptions12, markerOptions13, markerOptions14,markerOptions15,
            markerOptions16, markerOptions17, markerOptions18,markerOptions19;
    API api;
    private List<MarkerOptions> markerOptionsList;

    MapFragment mapFragment;

    List<CrimeReportDto> crimeReportDtos;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crime_statistics_activity);
        api= RetrofitInstance.getRetrofitInstance().create(API.class);
        markerOptionsList=new ArrayList<>();
        getCrimeReports();
//        crimeReportDtos= (List<CrimeReportDto>) getIntent().getSerializableExtra(Constants.CRIME_DETAILS);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.showInMap);
        mapFragment.getMapAsync(this);

//        addGoogleMarkers();

        //Commons.showToast(getApplicationContext(), CurrentLocation.CURR_LAT+"----"+CurrentLocation.CURR_LONGI);
        markerOptions1=new MarkerOptions().position(new LatLng(23.79, 90.413)).title("01.03.2021: a man robbed in atm  boot").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//        markerOptions2=new MarkerOptions().position(new LatLng(23.60, 90.435)).title("05.04.2021: mobile snitch from car").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//        markerOptions3=new MarkerOptions().position(new LatLng(23.805, 90.427)).title("07.01.2020: local gang conflict").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
//        markerOptions4=new MarkerOptions().position(new LatLng(23.832, 90.351)).title("01.03.2021: harass an woman").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
//        markerOptions5=new MarkerOptions().position(new LatLng(23.799, 90.431)).title("01.03.2021: a man robbed in atm  boot").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//        markerOptions6=new MarkerOptions().position(new LatLng(23.778, 90.456)).title("05.04.2021: mobile snitch from car").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//        markerOptions7=new MarkerOptions().position(new LatLng(23.811, 90.4363)).title("07.01.2020: local gang conflict").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
//        markerOptions8=new MarkerOptions().position(new LatLng(23.836, 90.38353)).title("01.03.2021: harass an woman").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
//        markerOptions9=new MarkerOptions().position(new LatLng(23.735, 90.40585)).title("01.03.2021: a man robbed in atm  boot").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//        markerOptions10=new MarkerOptions().position(new LatLng(23.632, 90.438)).title("05.04.2021: mobile snitch from car").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//        markerOptions11=new MarkerOptions().position(new LatLng(23.805, 90.444)).title("07.01.2020: car accident").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
//        markerOptions12=new MarkerOptions().position(new LatLng(23.801, 90.391)).title("01.03.2021: fire ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
//        markerOptions13=new MarkerOptions().position(new LatLng(23.74, 90.422)).title("01.03.2021: harrass").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//        markerOptions14=new MarkerOptions().position(new LatLng(23.76, 90.412342)).title("05.04.2021: bag snitch from rickshaw").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//        markerOptions15=new MarkerOptions().position(new LatLng(23.85, 90.4645)).title("07.01.2020: fire in day").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
//        markerOptions16=new MarkerOptions().position(new LatLng(23.84, 90.3588)).title("01.03.2021: murder").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
//        markerOptions17=new MarkerOptions().position(new LatLng(23.77, 90.3834)).title("01.03.2021: rape a girl").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//        markerOptions18=new MarkerOptions().position(new LatLng(23.81, 90.44756)).title("05.04.2021: bus harras").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//        markerOptions19=new MarkerOptions().position(new LatLng(23.68, 90.4204)).title("07.01.2020: murder in road").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

    }

    private void getCrimeReports() {
        Call<List<CrimeReportDto>> call= api.getAllCrimeReports();
        call.enqueue(new Callback<List<CrimeReportDto>>() {
            @Override
            public void onResponse(Call<List<CrimeReportDto>> call, Response<List<CrimeReportDto>> response) {
                crimeReportDtos= response.body();
                Log.e("crime stat activity","success");
//                addGoogleMarkers();
            }

            @Override
            public void onFailure(Call<List<CrimeReportDto>> call, Throwable t) {
                Log.e("crime stat activity","failure");
            }
        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onMapReady(GoogleMap gmap) {
        googleMap = gmap;
        for(CrimeReportDto crimeReportDto : crimeReportDtos)
        {
            if(!(crimeReportDto.getLatitude()==0||crimeReportDto.getLongitude()==0))
            {
                double latitude=crimeReportDto.getLatitude();
                double longitude = crimeReportDto.getLongitude();

                String details =crimeReportDto.getCrimeType();
                String time= crimeReportDto.getTime();
                if(details==null)
                {
                    details=" asdf";
                }
                if(time==null)
                {
                    time="";
                }
                markerOptions1=new MarkerOptions().position(new LatLng(latitude, longitude)).title(details+" at "+time).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//
//                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(latitude, longitude)).title(details).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                markerOptionsList.add(markerOption);
                googleMap.addMarker(markerOptions1);

            }
        }
//        addGoogleMarkers();
//        try {
//        googleMap.addMarker(markerOptions1);
//        googleMap.addMarker(markerOptions2);
//        googleMap.addMarker(markerOptions3);
//        googleMap.addMarker(markerOptions4);
//        googleMap.addMarker(markerOptions5);
//        googleMap.addMarker(markerOptions6);
//        googleMap.addMarker(markerOptions7);
//        googleMap.addMarker(markerOptions8);
//        googleMap.addMarker(markerOptions9);
//        googleMap.addMarker(markerOptions10);
//        googleMap.addMarker(markerOptions11);
//        googleMap.addMarker(markerOptions12);
//        googleMap.addMarker(markerOptions13);
//        googleMap.addMarker(markerOptions14);
//        googleMap.addMarker(markerOptions15);
//        googleMap.addMarker(markerOptions16);
//        googleMap.addMarker(markerOptions17);
//        googleMap.addMarker(markerOptions18);
//        googleMap.addMarker(markerOptions19);

//        }
//        catch(Exception e)
//        {
//            Toast.makeText(this, "please wait and check your GPS settings",Toast.LENGTH_SHORT).show();
//            Log.e("UpdateStatus",e.toString());
//            finish();
//        }
        CameraPosition camera;
        camera = new CameraPosition.Builder()
                .target(new LatLng(23.8,90.4))
                .zoom(13)
                .bearing(0)
                .tilt(45)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));
    }
    public void addGoogleMarkers()
    {

//        markerOptions1=new MarkerOptions().position(new LatLng(23.8, 90.4)).title("sonamoni").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//
//                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(latitude, longitude)).title(details).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                markerOptionsList.add(markerOption);
//        googleMap.addMarker(markerOptions1);
//        onMapReady(googleMap);

    }

}

