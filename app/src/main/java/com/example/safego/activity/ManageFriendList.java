package com.example.safego.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.safego.R;
import com.example.safego.utils.Commons;
import com.example.safego.utils.Constants;
import com.example.safego.utils.SharedPrefHelper;

public class ManageFriendList extends AppCompatActivity {
    EditText number1, number2, number3, number4, number5, number6;
    SharedPrefHelper sharedPrefHelper;
    String n1="",n2="",n3="",n4="",n5="",n6="";
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
        setContentView(R.layout.manage_friend_list);
        number1=findViewById(R.id.numberOneEditTxt);
        number2=findViewById(R.id.numberTwoEditTxt);
        number3=findViewById(R.id.numberThreeEditTxt);
        number4=findViewById(R.id.numberFourEditTxt);
        number5=findViewById(R.id.numberFiveEditTxt);
        number6=findViewById(R.id.numberSixEditTxt);
        submit = findViewById(R.id.submitBtn);
        n1=sharedPrefHelper.getStringFromSharedPref(Constants.FRIEND1);
        n2=sharedPrefHelper.getStringFromSharedPref(Constants.FRIEND2);
        n3=sharedPrefHelper.getStringFromSharedPref(Constants.FRIEND3);
        n4=sharedPrefHelper.getStringFromSharedPref(Constants.FRIEND4);
        n5=sharedPrefHelper.getStringFromSharedPref(Constants.FRIEND5);
        n6=sharedPrefHelper.getStringFromSharedPref(Constants.FRIEND6);
        if(n1==null)
        {
            n1="";
        }
        if(n2==null)
        {
            n2="";
        }
        if(n3==null)
        {
            n3="";
        }
        if(n4==null)
        {
            n4="";
        }
        if(n5==null)
        {
            n5="";
        }
        if(n6==null)
        {
            n6="";
        }
        number1.setText(n1);
        number2.setText(n2);
        number3.setText(n3);
        number4.setText(n4);
        number5.setText(n5);
        number6.setText(n6);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefHelper.saveDataToSharedPref(Constants.FRIEND1,number1.getText().toString());
                sharedPrefHelper.saveDataToSharedPref(Constants.FRIEND2,number2.getText().toString());
                sharedPrefHelper.saveDataToSharedPref(Constants.FRIEND3,number3.getText().toString());
                sharedPrefHelper.saveDataToSharedPref(Constants.FRIEND4,number4.getText().toString());
                sharedPrefHelper.saveDataToSharedPref(Constants.FRIEND5,number5.getText().toString());
                sharedPrefHelper.saveDataToSharedPref(Constants.FRIEND6,number6.getText().toString());
                Commons.showToast(getApplicationContext(),"added number to close friend list");
                Intent mySuperIntent = new Intent(getApplicationContext(), HomePageActivity.class);
                startActivity(mySuperIntent);
                finish();
            }
        });

    }
}
