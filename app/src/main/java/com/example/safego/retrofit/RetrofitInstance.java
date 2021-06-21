package com.example.safego.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit=null;
    //emulator
//    private static final String BASE_URL="http://10.0.2.2:8080/";
    
    //device
//    private static final String BASE_URL="http://192.168.43.253:8181/";

    //server
//    private static final String BASE_URL="http://192.168.0.104:8181/";
//    server 2
//    private static final String BASE_URL="https://prod.quixx.xyz/api/quixx/v1/";
    //    private static final String BASE_URL="https://api-new.quixx.xyz/api/quixx/v1/";
    //test
//    private static final String BASE_URL="https://jsonplaceholder.typicode.com/";
    //digital occean servere
    private static final String BASE_URL="http://139.59.66.206:8181/";
    public static Retrofit getRetrofitInstance(){
        if(retrofit==null){
            retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
