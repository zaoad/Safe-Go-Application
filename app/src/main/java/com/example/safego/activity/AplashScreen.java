package com.example.safego.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.safego.R;
import com.example.safego.domain.Post;
import com.example.safego.retrofit.API;
import com.example.safego.retrofit.RetrofitInstance;
import com.example.safego.utils.Constants;
import com.example.safego.utils.SharedPrefHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.safego.utils.Constants.SPLASH_TIME;

public class AplashScreen extends AppCompatActivity {
    private API api;
    SharedPrefHelper sharedPrefHelper;
    private String is_log_in="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api= RetrofitInstance.getRetrofitInstance().create(API.class);
//        Call<List<Post>> call = api.getPosts();
//        call.enqueue(new Callback<List<Post>>() {
//            @Override
//            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
//                if(!response.isSuccessful()) {
//                    Log.d("Code: " ,response.code()+"");
//                    return;
//                }
//                List<Post> posts = response.body();
//                for (Post post : posts) {
//                    Log.e("posts",post.toString());
//                }
//            }
//            @Override
//            public void onFailure(Call<List<Post>> call, Throwable t) {
//                int a=1;
//            }
//        });
        setContentView(R.layout.activity_splash_screen);
        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
        is_log_in=sharedPrefHelper.getStringFromSharedPref(Constants.IS_LOG_IN);
        //is_log_in="1";
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
        if(is_log_in==null || is_log_in.equals("0"))
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mySuperIntent = new Intent(getApplicationContext(), LogIn.class);
                    startActivity(mySuperIntent);
                    finish();
                }
            }, SPLASH_TIME);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mySuperIntent = new Intent(getApplicationContext(), HomePageActivity.class);
                    startActivity(mySuperIntent);
                    finish();
                }
            }, SPLASH_TIME);
        }


    }
}
