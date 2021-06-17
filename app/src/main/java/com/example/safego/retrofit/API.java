package com.example.safego.retrofit;

import com.example.safego.domain.ReceiveLocation;
import com.example.safego.domain.SendingLocation;
import com.example.safego.dto.CrimeReportDto;
import com.example.safego.dto.ReceiveLocationDto;
import com.example.safego.dto.UserInfoDto;
import com.example.safego.domain.AppToken;
import com.example.safego.domain.CrimeReport;
import com.example.safego.domain.Post;
import com.example.safego.domain.SimpleApiResponse;
import com.example.safego.domain.UserAuth;
import com.example.safego.domain.UserInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface API {
    @POST("users")
    Call<String>addApplicationUser(@Body UserInfo userInfo);

    @POST("userauths")
    Call<SimpleApiResponse>addUserAuth(@Body UserAuth userAuth);

    @GET("login/{phoneNumber}/{password}")
    Call<String> Authenticate(@Path("phoneNumber") String phoneNumber, @Path("password") String password);

    @POST("tokens")
    Call<String> addToken(@Body AppToken token);

    @GET("users/{phoneNumber}")
    Call<UserInfoDto> getApplicationUser(@Path("phoneNumber") String phoneNumber);

    @POST("crimereports")
    Call<String>addCrimeReport(@Body CrimeReport crimeReport);

    @GET("crimereports")
    Call<List<CrimeReportDto>> getAllCrimeReports();

    @POST("/notification/topic/{receiverPhoneNumber}/{fullNotificationString}")
    Call<Void> sendNotificationToFriends(@Path("receiverPhoneNumber") String receiverPhoneNumber,
                                                    @Path("fullNotificationString")
                                                            String fullNotificationString ) ;
    @POST("sendinglocations")
    Call<Void> addSendingLocation(@Body SendingLocation sendingLocation);

    @POST("receivinglocations")
    Call<Void> addReceiveLocation(@Body ReceiveLocation receiveLocation);

    @GET("receivinglocations/{phoneNumber}")
    Call<ReceiveLocationDto> getReceiveLocationByPhoneNumber(@Path("phoneNumber") String phoneNumber);

    @GET("posts")
    Call<List<Post>> getPosts();
}
