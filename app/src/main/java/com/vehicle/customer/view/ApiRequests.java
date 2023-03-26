package com.vehicle.customer.view;

import com.google.gson.JsonObject;
import com.vehicle.customer.model.FCM;

import java.util.List;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiRequests {
/*
    @POST("order")
    Call<Order> applyOrder(@Body Order order);

    @GET("medicine")
    Call<Medicine> getMedicine(@Query("id") String str);

    @GET("medicine")
    Call<List<Medicine>> getMedicines();

    @GET("order")
    Call<Order> getOrder(@Query("id") String str);

    @GET("order")
    Call<List<Order>> getOrders(@Query("user_phone_number") String str);

    @GET("order")
    Call<List<Order>> getOrdersByStatus(@Query("user_phone_number") String user_phone_number,
                                        @Query("status") String status);

    @PATCH("order/{id}")
    Call<Order> updateOrder(@Path("id") String id, @Body Order order);

    @GET("pharmacy")
    Call<List<Pharmacy>> getPharmacies();

    @GET("pharmacy")
    Call<List<Pharmacy>> getPharmacy(@Query("id") String str);

    @GET("user")
    Call<Profile> getUserProfileUsingEmail(@Query("email") String str);

    @GET("user")
    Call<Profile> getUserProfileUsingId(@Query("id") String str);

    @GET("user")
    Call<Profile> getUserProfileUsingPhoneNumber(@Query("phone_number") String phone_number);

    @POST("user")
    Call<Profile> setUpProfile(@Body Profile profile);

    @GET("auth")
    Call<Profile> signInUser(@Query("phone_number") String phone_number, @Query("password") String str2);
*/

    @POST("uploadImageToGenarateUrl")
    @Multipart
    Call<JsonObject> uploadImageToGenarateUrl(@Part MultipartBody.Part imageMultipartBody);

    @POST("fcm")
    Call<FCM> createFcmToken(@Body FCM fcm);
    @GET("fcm")
    Call<FCM> getFcmToken(@Query("id") String id);

    @PATCH("fcm/{id}")
    Call<FCM> updateFcmToken(@Path("id") String id, @Body FCM fcm);



    @POST("fcm/send")
    Call<JsonObject> sendNotification(@Body FCM fcm);
}


