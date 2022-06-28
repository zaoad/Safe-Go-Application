package com.example.safego.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.safego.R;
import com.example.safego.domain.CrimeReport;
import com.example.safego.dto.UserInfoDto;
import com.example.safego.retrofit.API;
import com.example.safego.retrofit.RetrofitInstance;
import com.example.safego.utils.Commons;
import com.example.safego.utils.Constants;
import com.example.safego.utils.SharedPrefHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickLocationSafeRoute extends AppCompatActivity {

    Button pickSourceButton;

    Button pickDestinationButton;

    TextView sourceLatLongTxt;

    TextView destinationLatLongTxt;

    EditText timeEditTxt;

    EditText areaEditTxt;

    Button findSafeBtn;

    String sourceLat;

    String sourceLongi;

    String destinationLat;

    String destinationLongi;

    SharedPrefHelper sharedPrefHelper;

    Calendar myCalendar;

    API api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_location_find_map);
        api= RetrofitInstance.getRetrofitInstance().create(API.class);
        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
        pickSourceButton = findViewById(R.id.sourceLocationBtn);
        pickDestinationButton = findViewById(R.id.destinationLocationBtn);
        sourceLatLongTxt = findViewById(R.id.sourceLatLong);
        destinationLatLongTxt = findViewById(R.id.destinationLatLong);
        findSafeBtn = findViewById(R.id.findSafeBtn);
        sourceLat = sharedPrefHelper.getStringFromSharedPref(Constants.SOURCE_LATITUDE);
        sourceLongi = sharedPrefHelper.getStringFromSharedPref(Constants.SOURCE_LONGITUDE);
        destinationLat = sharedPrefHelper.getStringFromSharedPref(Constants.DESTINATION_LATITUDE);
        destinationLongi = sharedPrefHelper.getStringFromSharedPref(Constants.DESTINATION_LONGITUDE);

        if(sourceLat==null)
        {
            sourceLat="";
        }
        if(sourceLongi==null)
        {
            sourceLongi="";
        }
        if(destinationLat==null)
        {
            destinationLat="";
        }
        if(destinationLongi==null)
        {
            destinationLongi="";
        }

        if(!(sourceLat.equals("0.0")||sourceLongi.equals("0.0")))
        {
            sourceLatLongTxt.setText("Latitude: "+sourceLat+"Longitude: "+sourceLongi);
        }
        if(!(destinationLat.equals("0.0")||destinationLongi.equals("0.0")))
        {
            destinationLatLongTxt.setText("Latitude: "+destinationLat+"Longitude: "+destinationLongi);
        }

        pickSourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mySuperIntent = new Intent(getApplicationContext(), PickSourcelocation.class);
                startActivity(mySuperIntent);
            }
        });
        pickDestinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mySuperIntent = new Intent(getApplicationContext(), PickDestinationLocation.class);
                startActivity(mySuperIntent);
            }
        });

        findSafeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sourceLat.equals("0.0")||sourceLongi.equals("0.0")|| destinationLat.equals("0.0")||destinationLongi.equals("0.0") )
                {
                    Commons.showToast(getApplicationContext(),"Source or destination not valid");
                }
                else{
                    Intent mySuperIntent = new Intent(getApplicationContext(), SafeGoMap.class);
                    startActivity(mySuperIntent);
                    finish();
                }

            }
        });
    }

    public void onBackPressed() {
        Intent mySuperIntent = new Intent(getApplicationContext(), HomePageActivity.class);
        startActivity(mySuperIntent);
        finish();
    }
}
