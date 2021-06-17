package com.example.safego.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.safego.R;
import com.example.safego.domain.SimpleApiResponse;
import com.example.safego.domain.UserAuth;
import com.example.safego.domain.UserInfo;
import com.example.safego.retrofit.API;
import com.example.safego.retrofit.RetrofitInstance;
import com.example.safego.utils.Commons;
import com.example.safego.utils.Constants;
import com.example.safego.utils.SharedPrefHelper;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private API api=null;

    private TextInputEditText name;

    private TextInputEditText age;

    private TextInputEditText password;

    private TextInputEditText repassword;

    private TextInputEditText phoneNumber;

    private Spinner isManOrWomanSpinner;

    private ProgressBar progressBar;

    private Button signUpBtn;

    private String[] isManOrWomanString= {"Select", "Man", "Woman"};

    private String nameStr;

    private String ageStr;

    private String phoneNumberStr;

    private String sexStr;

    private String passwordStr;
    SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sharedPrefHelper=new SharedPrefHelper(getApplicationContext());
        api= RetrofitInstance.getRetrofitInstance().create(API.class);//?
        name = findViewById(R.id.nameEditTxt);
        age = findViewById(R.id.ageEditTxt);
        phoneNumber=findViewById(R.id.phoneNumberEditTxt);
        password = findViewById(R.id.passwordEditTxt);
        repassword = findViewById(R.id.repasswordEditTxt);
        isManOrWomanSpinner = findViewById(R.id.SpinnerManWoman);
        progressBar = findViewById(R.id.vCodeProgressBar);
        signUpBtn = findViewById(R.id.signUpBtn);
        ArrayAdapter<String> adapterIsManWoman = new ArrayAdapter<String>(SignUpActivity.this,
                android.R.layout.simple_spinner_item, isManOrWomanString);

        adapterIsManWoman.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        isManOrWomanSpinner.setAdapter(adapterIsManWoman);
        isManOrWomanSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameStr=name.getText().toString();
                phoneNumberStr=phoneNumber.getText().toString();
                ageStr=age.getText().toString();
                sexStr=isManOrWomanSpinner.getSelectedItem().toString();
                if(isValidInput()) {
                    progressBar.setVisibility(View.VISIBLE);
                    UserInfo userInfo = new UserInfo();
                    userInfo.setName(nameStr);
                    userInfo.setAge(ageStr);
                    userInfo.setPhoneNumber(phoneNumberStr);
                    userInfo.setSex(sexStr);
                    Call<String> call = api.addApplicationUser(userInfo);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Object a=response.body();
                            Log.d("SignUpActivity", response.toString());
                            if(response.isSuccessful())
                            {
                                setUser();
                                Log.d("SignUpActivity", "success");

                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("SignupActivity", "failed");

                        }
                    });

//                    call.enqueue(new Callback<SimpleApiResponse>() {
//                        @Override
//                        public void onResponse(Call<SimpleApiResponse> call, Response<SimpleApiResponse> response) {
//                            Object a=response.body();
//                            if(response.isSuccessful()) {
//                                SimpleApiResponse simpleApiResponse = response.body();
//                                if (simpleApiResponse.getData().equals("true")) {
//                                    Log.d("SignUpActivity", response.toString());
//                                    setUser();
//                                    //Commons.showToast(getApplicationContext(), "Done");
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<SimpleApiResponse> call, Throwable t) {
//                            Log.d("SignupActivity", "failed");
//                        }
//                    });
                    Toast.makeText(getApplicationContext(),"Signup",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setUser() {
        UserAuth userAuth=new UserAuth();
        passwordStr = password.getText().toString();
        userAuth.setPhoneNumber(phoneNumberStr);
        userAuth.setPassword(passwordStr);
        Call<SimpleApiResponse> call = api.addUserAuth(userAuth);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                Object res=response.body();
//                Log.e("kabjab","kabjab");
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
        call.enqueue(new Callback<SimpleApiResponse>() {
            @Override
            public void onResponse(Call<SimpleApiResponse> call, Response<SimpleApiResponse> response) {
                Object newobj=response.body();
                if(response.isSuccessful()) {
                    SimpleApiResponse simpleApiResponse = response.body();
                    if (simpleApiResponse.getData().equals("true")) {
                        Log.d("SignUpActivity", response.toString());
                        Commons.showToast(getApplicationContext(), "SignUp Successful");
                        sharedPrefHelper.saveDataToSharedPref(Constants.NAME,nameStr);
                        Intent intent=new Intent(getApplicationContext(),LogIn.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleApiResponse> call, Throwable t) {
                Log.d("SignupActivity", "failed");
            }
        });

    }

    private boolean isValidInput() {
        if(nameStr.equals(""))
        {
            Commons.showToast(getApplicationContext(),"name can't be empty");
            return false;
        }
        if(phoneNumberStr.equals(""))
        {
            Commons.showToast(getApplicationContext(),"phone Number can't be empty");
            return false;
        }
        if(ageStr.equals(""))
        {
            Commons.showToast(getApplicationContext(),"Age can't be empty");
            return false;
        }
        if(sexStr.equals("Select")||sexStr.equals(""))
        {
            Commons.showToast(getApplicationContext(),"Select gender");
            return false;
        }
        return  true;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
