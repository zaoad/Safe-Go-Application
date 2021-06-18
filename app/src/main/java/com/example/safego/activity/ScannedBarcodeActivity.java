package com.example.safego.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.safego.R;
import com.example.safego.retrofit.API;
import com.example.safego.retrofit.RetrofitInstance;
import com.example.safego.utils.Commons;
import com.example.safego.utils.Constants;
import com.example.safego.utils.CurrentLocation;
import com.example.safego.utils.SharedPrefHelper;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScannedBarcodeActivity extends AppCompatActivity {

    public static String barcodeId;
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
//    String intentData = "user ID:4 delivery man ID:266 deliveryIds:358";
    String intentData ="";
    boolean isEmail = false;
    private String userId;
    private String deliveryManId;
    SharedPrefHelper sharedPrefHelper;
    Button button;
    API api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);
        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
        api= RetrofitInstance.getRetrofitInstance().create(API.class);
        initViews();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);        api= RetrofitInstance.getRetrofitInstance().create(API.class);

        button = findViewById(R.id.btnAction);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(intentData.equals(""))
                {
                    Commons.showToast(getApplicationContext(),"No barcode found");
                }
                else{
                    sendBusInfo();
                }
            }
        });

    }

    private void sendBusInfo() {
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
        String name=sharedPrefHelper.getStringFromSharedPref(Constants.NAME);
        String notificationStr="Your friend "+name+" Phone Number: "+ phoneNumber+"Bus: "+intentData+ "location "+CurrentLocation.CURR_LAT+" ,"+CurrentLocation.CURR_LONGI;
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

    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
//        if(!barcodeDetector.isOperational()){
//            Toast.makeText(getApplicationContext(), "Sorry, Couldn't setup the detector", Toast.LENGTH_LONG).show();
//            this.finish();
//        }
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;
//                                goToDeliveryActivity();
//                                txtBarcodeValue.setText(intentData);
                                isEmail = true;
//                                btnAction.setText("ADD CONTENT TO THE MAIL");
                            } else {
                                isEmail = false;
//                                btnAction.setText("LAUNCH URL");
                                intentData = barcodes.valueAt(0).displayValue;
//                                txtBarcodeValue.setText(intentData);
//                                goToDeliveryActivity();
                            }
                        }
                    });

                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

}