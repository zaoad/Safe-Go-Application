package com.example.safego.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.safego.R;
import com.example.safego.retrofit.API;
import com.example.safego.retrofit.RetrofitInstance;
import com.example.safego.utils.Commons;
import com.example.safego.utils.Constants;
import com.example.safego.utils.SharedPrefHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SosActivity extends AppCompatActivity {
    LinearLayout layout2,layout3,layout4,layout5,layout6;
    TextView number2,number3,number4,number5,number6;
    Button button1,button2,button3,button4,button5,button6;
    String n1="",n2="",n3="",n4="",n5="",n6="";
    SharedPrefHelper sharedPrefHelper;
    Button sendNotificationBtn;
    List<String> mobileNumberList;
    API api;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sos_activity);
        api= RetrofitInstance.getRetrofitInstance().create(API.class);
        layout2=findViewById(R.id.layout2);
        layout3=findViewById(R.id.layout3);
        layout4=findViewById(R.id.layout4);
        layout5=findViewById(R.id.layout5);
        layout6=findViewById(R.id.layout6);
        number2=findViewById(R.id.number2);
        number3=findViewById(R.id.number3);
        number4=findViewById(R.id.number4);
        number5=findViewById(R.id.number5);
        number6=findViewById(R.id.number6);
        button1=findViewById(R.id.button1);
        button2=findViewById(R.id.button2);
        button3=findViewById(R.id.button3);
        button4=findViewById(R.id.button4);
        button5=findViewById(R.id.button5);
        button6=findViewById(R.id.button6);
        sendNotificationBtn=findViewById(R.id.sendNotificationBtn);
        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
        n1=sharedPrefHelper.getStringFromSharedPref(Constants.FRIEND1);
        n2=sharedPrefHelper.getStringFromSharedPref(Constants.FRIEND2);
        n3=sharedPrefHelper.getStringFromSharedPref(Constants.FRIEND3);
        n4=sharedPrefHelper.getStringFromSharedPref(Constants.FRIEND4);
        n5=sharedPrefHelper.getStringFromSharedPref(Constants.FRIEND5);
        n6=sharedPrefHelper.getStringFromSharedPref(Constants.FRIEND6);
        mobileNumberList= new ArrayList<>();
        if(n1==null||n1=="")
        {
            n1="";
            layout2.setVisibility(View.INVISIBLE);
            button2.setVisibility(View.INVISIBLE);

        }
        else{
            mobileNumberList.add(n1);
        }
        if(n2==null||n2.equals(""))
        {
            n2="";
            layout3.setVisibility(View.INVISIBLE);
            button3.setVisibility(View.INVISIBLE);
        }
        else{
            mobileNumberList.add(n2);
        }
        if(n3==null||n3.equals(""))
        {
            n3="";
            layout4.setVisibility(View.INVISIBLE);
            button4.setVisibility(View.INVISIBLE);
        }
        else{
            mobileNumberList.add(n3);
        }
        if(n4==null||n4.equals(""))
        {
            n4="";
            layout5.setVisibility(View.INVISIBLE);
            button5.setVisibility(View.INVISIBLE);
        }
        else{
            mobileNumberList.add(n4);
        }
        if(n5==null||n5.equals(""))
        {
            n5="";
            layout6.setVisibility(View.INVISIBLE);
            button6.setVisibility(View.INVISIBLE);
        }
        else{
            mobileNumberList.add(n5);
        }
        number2.setText(n1);
        number3.setText(n2);
        number4.setText(n3);
        number5.setText(n4);
        number6.setText(n5);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(isPermissionGranted()) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" +"999"));
                        startActivity(callIntent);
                    }
                }
                catch (Exception e)
                {
                    Commons.showToast(getApplicationContext(),"Invalid Phone Number");
                    Log.e("Sos Activity",e.toString());
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(isPermissionGranted()) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" +n1));
                        startActivity(callIntent);
                    }
                }
                catch (Exception e)
                {
                    Commons.showToast(getApplicationContext(),"Invalid  Phone Number");
                    Log.e("SosActivity",e.toString());
                }
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(isPermissionGranted()) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" +n2));
                        startActivity(callIntent);
                    }
                }
                catch (Exception e)
                {
                    Commons.showToast(getApplicationContext(),"Invalid  Phone Number");
                    Log.e("SosActivity",e.toString());
                }
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(isPermissionGranted()) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" +n3));
                        startActivity(callIntent);
                    }
                }
                catch (Exception e)
                {
                    Commons.showToast(getApplicationContext(),"Invalid  Phone Number");
                    Log.e("SosActivity",e.toString());
                }
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(isPermissionGranted()) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" +n4));
                        startActivity(callIntent);
                    }
                }
                catch (Exception e)
                {
                    Commons.showToast(getApplicationContext(),"Invalid  Phone Number");
                    Log.e("SosActivity",e.toString());
                }
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(isPermissionGranted()) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" +n5));
                        startActivity(callIntent);
                    }
                }
                catch (Exception e)
                {
                    Commons.showToast(getApplicationContext(),"Invalid  Phone Number");
                    Log.e("SosActivity",e.toString());
                }
            }
        });
        sendNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });
    }

    private void sendNotification() {
        String phoneNumber=sharedPrefHelper.getStringFromSharedPref(Constants.MOBILE_NUMBER);
        String name=sharedPrefHelper.getStringFromSharedPref(Constants.NAME);
        String notificationStr="Your friend "+name+" Phone Number: "+ phoneNumber;
        for(String recieverNumber: mobileNumberList)
        {
            Call<Void> call=api.sendNotificationToFriends(recieverNumber,notificationStr);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });

        }


    }

    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }
}
