package com.example.safego.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.safego.MainActivity;
import com.example.safego.R;
import com.example.safego.dto.UserInfoDto;
import com.example.safego.domain.CrimeReport;
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

public class ReportCrime extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    Button pickUpLocationBtn;

    TextView textView;

    EditText timeEditTxt;

    EditText areaEditTxt;

    Button submit;

    EditText crimeDetails;

    String lat;

    String longi;

    String crimeDetailsStr;

    String timeStr;

    String victimAge;

    String victimSex;

    String areaStr;

    String phoneNumber;

    SharedPrefHelper sharedPrefHelper;

    Calendar myCalendar;

    API api;

    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_crime);
        api= RetrofitInstance.getRetrofitInstance().create(API.class);
        pickUpLocationBtn = findViewById(R.id.pickUpLocation);
        textView = findViewById(R.id.locationLatLong);
        submit = findViewById(R.id.submitBtn);
        timeEditTxt = findViewById(R.id.timeEditTxt);
        crimeDetails = findViewById(R.id.crimeDetails);
        areaEditTxt = findViewById(R.id.locationEditTxt);
        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
        lat = sharedPrefHelper.getStringFromSharedPref(Constants.LATITUDE);
        longi = sharedPrefHelper.getStringFromSharedPref(Constants.LONGITUDE);
        crimeDetailsStr = sharedPrefHelper.getStringFromSharedPref(Constants.CRIME_DETAILS);
        timeStr = sharedPrefHelper.getStringFromSharedPref(Constants.TIME);

        if(lat==null)
        {
            lat="";
        }
        if(longi==null)
        {
            longi="";
        }
        if(crimeDetailsStr==null)
        {
            crimeDetailsStr="";
        }
        if(timeStr==null)
        {
            timeStr="";
        }
        if(areaStr==null)
        {
            areaStr="";
        }
        crimeDetails.setText(crimeDetailsStr);
        timeEditTxt.setText(timeStr);
        areaEditTxt.setText(areaStr);
        if(!(lat.equals("0.0")||longi.equals("0.0")))
        {
            textView.setText("Latitude: "+lat+"Longitude: "+longi);
        }

        pickUpLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefHelper.saveDataToSharedPref(Constants.CRIME_DETAILS,crimeDetails.getText().toString());
                sharedPrefHelper.saveDataToSharedPref(Constants.TIME,timeEditTxt.getText().toString());
                Intent mySuperIntent = new Intent(getApplicationContext(), PickUplocation.class);
                startActivity(mySuperIntent);
            }
        });


        if(!(lat.equals("0.0")||longi.equals("0.0")))
        {
            textView.setText("Latitude: "+lat+"Longitude: "+longi);
        }



        timeEditTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ReportCrime.this, ReportCrime.this,year, month,day);
                datePickerDialog.show();
//                myCalendar = Calendar.getInstance();
//
//                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                          int dayOfMonth) {
//                        // TODO Auto-generated method stub
//                        myCalendar.set(Calendar.YEAR, year);
//                        myCalendar.set(Calendar.MONTH, monthOfYear);
//                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                        updateLabel();
//                    }
//
//                };
                /**
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ReportCrime.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String times= selectedHour+ ":" + selectedMinute;
                        datepicker();
                        String myFormat = "MM/dd/yy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        timeEditTxt.setText(sdf.format(myCalendar.getTime())+times);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                 **/
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApplicationUser();

            }
        });
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = day;
        myMonth = month;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(ReportCrime.this, ReportCrime.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;
        timeEditTxt.setText(myday+"-"+myMonth+"-"+myYear+" "+myHour+":"+myMinute);
    }
//    private void updateLabel() {
//        String myFormat = "dd/mm/yy"; //In which you need put here
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//        timeEditTxt.setText(sdf.format(myCalendar.getTime()));
//    }
//    private void datepicker()
//    {
//        myCalendar = Calendar.getInstance();
//
//        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                // TODO Auto-generated method stub
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                updateLabel();
//            }
//
//        };
//    }
    public void getApplicationUser()
    {
        phoneNumber=sharedPrefHelper.getStringFromSharedPref(Constants.MOBILE_NUMBER);
        Call<UserInfoDto> call=api.getApplicationUser(phoneNumber);
        call.enqueue(new Callback<UserInfoDto>() {
            @Override
            public void onResponse(Call<UserInfoDto> call, Response<UserInfoDto> response) {
                Object object= response.body();
                UserInfoDto userInfoDto=response.body();
                victimAge=userInfoDto.getAge();
                victimSex= userInfoDto. getSex();
                createCrimeObject();
            }
            @Override
            public void onFailure(Call<UserInfoDto> call, Throwable t) {
                Log.e("ReportCrime","failed");
            }
        });
    }
    public void addCrimeReport(CrimeReport crimeReport)
    {
        Call<String> call = api.addCrimeReport(crimeReport);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Object a=response.body();
                Log.d("SignUpActivity", response.toString());
                if(response.isSuccessful())
                {
                    Log.d("SignUpActivity", "successfully added");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("SignupActivity", "failed");

            }
        });
    }
    public void createCrimeObject()
    {
        crimeDetailsStr=crimeDetails.getText().toString();
        timeStr=timeEditTxt.getText().toString();
        areaStr=areaEditTxt.getText().toString();
        CrimeReport crimeReport=new CrimeReport();
        crimeReport.setArea(areaStr);
        crimeReport.setCrimeType(crimeDetailsStr);
        crimeReport.setLatitude(Double.parseDouble(lat));
        crimeReport.setLongitude(Double.parseDouble(longi));
        crimeReport.setVictimAge(victimAge);
        crimeReport.setPhoneNumber(phoneNumber);
        crimeReport.setVictimSex(victimSex);
        crimeReport.setTime(timeStr);
        addCrimeReport(crimeReport);
        Commons.showToast(getApplicationContext(),"crime report submitted");
        Intent intent=new Intent(getApplicationContext(),HomePageActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent mySuperIntent = new Intent(getApplicationContext(), HomePageActivity.class);
        startActivity(mySuperIntent);
        finish();
    }
}
