package com.example.safego.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.safego.R;
import com.example.safego.domain.AppToken;
import com.example.safego.retrofit.API;
import com.example.safego.retrofit.RetrofitInstance;
import com.example.safego.utils.Commons;
import com.example.safego.utils.Constants;
import com.example.safego.utils.SharedPrefHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogIn extends AppCompatActivity {
    private TextInputEditText phoneNumber;

    private TextInputEditText password;

    private Button logInBtn;

    private ProgressBar progressBar;

    private TextView signUp;

    private String phoneNumberStr;

    private String passwordStr;

    private String token;

    private API api;

    SharedPrefHelper sharedPrefHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        sharedPrefHelper=new SharedPrefHelper(getApplicationContext());
        api= RetrofitInstance.getRetrofitInstance().create(API.class);//?
        phoneNumber = findViewById(R.id.phoneNumberEditTxt);
        password = findViewById(R.id.passwordEditTxt);
        logInBtn = findViewById(R.id.logInBtn);
        progressBar = findViewById(R.id.vCodeProgressBar);
        signUp = findViewById(R.id.signUp);
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logInWithoutAuth();
                phoneNumberStr=phoneNumber.getText().toString();
                passwordStr=password.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"log in Activity",Toast.LENGTH_SHORT).show();
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                    return;
                                }
                                // Get new FCM registration token
                                token = task.getResult();
                                Log.e("LogInActivity",token);
                            }
                        });
                Authenticate(phoneNumberStr,passwordStr);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mySuperIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(mySuperIntent);
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"Signup activity",Toast.LENGTH_SHORT).show();
            }
        });
        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successful";
                        if (!task.isSuccessful()) {
                            msg = "failed";
                        }
                    }
                });

    }

    private boolean Authenticate(String phoneNumber, String passwordStr) {
        Call<String> call = api.Authenticate(phoneNumber,passwordStr);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Object newObj=response.body();
                if(response.body().equals("true"))
                {
                    setApptoken(phoneNumberStr,token);
                }
                else
                {
                    Commons.showToast(getApplicationContext(),"username or password incorrect");
                }
                Log.d("LogInActivity", response.toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
        return true;
    }

    public void setApptoken(final String phoneNumberStr, String token)
    {
        AppToken appToken= new AppToken();
        appToken.setPhoneNumber(phoneNumberStr);
        appToken.setToken(token);
        Call<String> call = api.addToken(appToken);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Object newObj=response.body();
                if(response.body().equals("true"))
                {
                    sharedPrefHelper.saveDataToSharedPref(Constants.MOBILE_NUMBER,phoneNumberStr);
                    sharedPrefHelper.saveDataToSharedPref(Constants.IS_LOG_IN,"1");
                    Intent mySuperIntent = new Intent(getApplicationContext(), HomePageActivity.class);
                    startActivity(mySuperIntent);
                    finish();
                }
                else{
                    Commons.showToast(getApplicationContext(),"username or password incorrect");
                }
                Log.d("LogInActivity",response.toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    public void logInWithoutAuth()
    {
        Intent mySuperIntent = new Intent(getApplicationContext(), HomePageActivity.class);
        startActivity(mySuperIntent);
        finish();
    }
}
