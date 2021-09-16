package com.example.safego.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.safego.domain.UserInfo;
import com.example.safego.retrofit.API;
import com.example.safego.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Commons {
    public static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
    public static API getApi()
    {
        API api= RetrofitInstance.getRetrofitInstance().create(API.class);
        return api;
    }
}
