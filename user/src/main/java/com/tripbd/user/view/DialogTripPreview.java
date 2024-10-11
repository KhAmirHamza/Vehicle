package com.tripbd.user.view;

import static com.tripbd.user.Constants.MAIN_URL;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonObject;
import com.tripbd.user.R;
import com.tripbd.user.model.Driver;
import com.tripbd.user.model.FCM;
import com.tripbd.user.model.Trip;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogTripPreview extends Dialog {
    private static final String TAG = "DialogTripPreview";

    TextView tv_car_details,tv_car_date_time,tv_loading_upazila_district,
            tv_loading_full_address,tv_unloading_upazila_district,
            tv_unloading_full_address,tv_description,tv_up_down_trip,
            tv_contain_animal,tv_fragile_product,tv_perishable_product,
            tv_labor_needed,tv_loading_nearAt,tv_unloading_nearAt, tv_length_alert, tv_weight_alert;

    MaterialButton btn_review;
    ProgressBar progressbar;
    List<String> tokens = new ArrayList<>();
    Activity activity;
    Trip trip;
    List<Driver> drivers = new ArrayList<>();


    public DialogTripPreview(@NonNull Activity context) {
        super(context, android.R.style.Theme_Holo_Light_Dialog);
        this.activity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_trip_review);
        tv_car_details =  findViewById(R.id.tv_car_details);
        tv_car_date_time =  findViewById(R.id.tv_car_date_time);
        tv_loading_upazila_district =  findViewById(R.id.tv_loading_upazila_district);
        tv_loading_full_address =  findViewById(R.id.tv_loading_full_address);
        tv_unloading_upazila_district =  findViewById(R.id.tv_unloading_upazila_district);
        tv_unloading_full_address =  findViewById(R.id.tv_unloading_full_address);
        tv_description =  findViewById(R.id.tv_description);
        tv_up_down_trip =  findViewById(R.id.tv_up_down_trip);
        tv_contain_animal =  findViewById(R.id.tv_contain_animal);
        tv_fragile_product =  findViewById(R.id.tv_fragile_product);
        tv_perishable_product =  findViewById(R.id.tv_perishable_product);
        tv_labor_needed =  findViewById(R.id.tv_labor_needed);
        tv_loading_nearAt =  findViewById(R.id.tv_loading_nearAt);
        tv_unloading_nearAt =  findViewById(R.id.tv_unloading_nearAt);
        tv_length_alert =  findViewById(R.id.tv_length_alert);
        tv_weight_alert =  findViewById(R.id.tv_weight_alert);

        progressbar =  findViewById(R.id.progressbar);

        btn_review =  findViewById(R.id.btn_review);

        tv_car_details.setText(getVehicleDetails(trip));
        tv_car_date_time.setText("( "+trip.getLoadingTime()+", "+trip.getLoadingDate()+" )");
        tv_loading_upazila_district.setText("Location: "+trip.getLoadingUpazilaThana());
        tv_loading_nearAt.setText("Place: "+trip.getLoadingArea());
        tv_loading_full_address.setText("Full Address: "+trip.getLoadingFullAddress() +"\nNa/O Nearby School/ Mosque/ other:"+trip.getLoadingLandmark());
        tv_unloading_upazila_district.setText("Location: "+trip.getUnloadingUpazilaThana());
        tv_unloading_nearAt.setText("Place: "+trip.getUnloadingArea());
        tv_unloading_full_address.setText("Full Address: "+trip.getUnloadingFullAddress() +"\nNa/O Nearby School/ Mosque/ other: "+trip.getUnloadingLandmark());
        tv_description.setText(trip.getDescription());
        tv_up_down_trip.setVisibility(trip.getUpDownTrip()==1? View.VISIBLE:View.GONE);
        tv_contain_animal.setVisibility(trip.getContainAnimal()==1? View.VISIBLE:View.GONE);
        tv_fragile_product.setVisibility(trip.getFragile()==1? View.VISIBLE:View.GONE);
        tv_perishable_product.setVisibility(trip.getPerishable()==1? View.VISIBLE:View.GONE);
        tv_labor_needed.setVisibility(trip.getLaborNeeded()==1? View.VISIBLE:View.GONE);
        tv_length_alert.setVisibility(trip.getLengthAlert()==1? View.VISIBLE:View.GONE);
        tv_weight_alert.setVisibility(trip.getWeightAlert()==1? View.VISIBLE:View.GONE);

        TextView tv_loading_header = findViewById(R.id.tv_loading_header);
        TextView tv_unloading_header = findViewById(R.id.tv_unloading_header);
        TextView tv_description_header = findViewById(R.id.tv_description_header);

        Toast.makeText(activity, trip.getVehicle().getType(), Toast.LENGTH_SHORT).show();

        if(
                trip.getVehicle().getType().equalsIgnoreCase("Pickup")||
                        trip.getVehicle().getType().equalsIgnoreCase("Truck")||
                        trip.getVehicle().getType().equalsIgnoreCase("Trailer")
        ){

            tv_loading_header.setText(R.string.informationonloadlocation);
            tv_unloading_header.setText(R.string.informationonunloadLocation);
            tv_description_header.setVisibility(View.VISIBLE);
        }else{

            tv_loading_header.setText(R.string.infoontripstartlocation);
            tv_unloading_header.setText(R.string.infoontripEndlocation);
            tv_description_header.setVisibility(View.GONE);
        }
       // Log.d(TAG, "onCreate: Driver size: "+drivers.size());

        getFilteredTokens(trip);
        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo... submit trip for bidding

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                trip.setStatus("Bidding");
                progressbar.setVisibility(View.VISIBLE);
                btn_review.setVisibility(View.INVISIBLE);
                db.collection("trip").add(trip).addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    //startActivity(new Intent(AddUpdateMoneyReceiveActivity.this, HomeActivity.class));
                    Toast.makeText(getContext(), "Trip added for bidding successfully", Toast.LENGTH_SHORT).show();
                    sendNotificationToDrivers();

                }).addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

            }
        });
    }

    String getVehicleDetails(Trip trip){
        if (trip.getVehicle()==null) return "...";
        else if (trip.getVehicle().getType().equalsIgnoreCase("Truck")||
                trip.getVehicle().getType().equalsIgnoreCase("Pickup")||
                trip.getVehicle().getType().equalsIgnoreCase("Trailer")){
            return trip.getVehicle().getType()+", "
                    +trip.getVehicle().getSize()+" ("
                    +trip.getVehicle().getVariety()+")";
        }else {
            return trip.getVehicle().getType()+", "
                    +trip.getVehicle().getSeat()+" Seats "+" ("
                    +trip.getVehicle().getVariety()+")";
        }
    }
    void sendNotificationToDrivers(){
       // Log.d(TAG, "sendNotificationToDrivers: called");
        FCM fcm = new FCM("New Order Published!",
                "New Trip has been published for your Area! Check it out now!", tokens);
        Toast.makeText(activity, "Tokens: "+tokens.size(), Toast.LENGTH_SHORT).show();
        ApiClient.getInstance(MAIN_URL).sendNotification(fcm).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                   //     Log.d(TAG, "onResponse: FCM Data is null");
                        return;
                    }
                   // Log.d(TAG, "onResponse: Successful");
                    String welcomeText = activity.getString(R.string.thank_you_for_publishing_a_bid_driver_will_tell_you_the_rate_wait_and_confirmed);
                    DialogWelcome dialogWelcome = new DialogWelcome(activity, welcomeText, new Intent(activity,MainActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    dialogWelcome.setCancelable(false);
                //Toast.makeText(activity, "Wait", Toast.LENGTH_LONG).show();
                    dialogWelcome.show();
                    progressbar.setVisibility(View.GONE);
                }else {
                    //Toast.makeText(activity, "ResponseError: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                  //  Log.d(TAG, "onResponse: ResponseError: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
                // Toast.makeText(OrderActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                //Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
               // Log.d(TAG, "onFailure: " + t.getMessage());
                //Log.d(OrderActivity.TAG, "onFailure: " + t.getCause().toString());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //getFilteredDriverFcmTokens();
    }

    public void setDrivers(List<Driver> drivers){
        this.drivers = drivers;
    }
    public List<Driver> getDrivers(){
        return drivers;
    }
    void getFilteredTokens(Trip trip){
        this.tokens.clear();
        String tripLoadingAddress = trip.getLoadingUpazilaThana().toLowerCase();
        String tripUnloadingAddress = trip.getUnloadingUpazilaThana().toLowerCase();
        //Log.d(TAG, "Trip Loading Address: "+tripLoadingAddress);
        //Log.d(TAG, "Trip Unloading Address: "+tripUnloadingAddress);

        for (int i = 0; i < drivers.size(); i++) {
            //if (drivers.get(i).getFcmArea().equalsIgnoreCase(trip.getLoadingUpazilaThana()))
            String driverLoadingFcmArea = drivers.get(i).getFcmLoadingArea();
            String driverUnloadingFcmArea = drivers.get(i).getFcmLoadingArea();
            //Log.d(TAG, "Driver Loading FCM Area: "+driverLoadingFcmArea);
            //Log.d(TAG, "Driver Unloading FCM Area: "+driverUnloadingFcmArea);
            if(!drivers.get(i).getFcmToken().isEmpty()){
                if(driverLoadingFcmArea!=null && driverUnloadingFcmArea != null){
                    if (tripLoadingAddress.contains(drivers.get(i).getFcmLoadingArea().toLowerCase()) &&
                                    tripUnloadingAddress.contains(drivers.get(i).getFcmUnloadingArea().toLowerCase())
                    ) tokens.add(drivers.get(i).getFcmToken());
                }else if(driverLoadingFcmArea!=null){
                    if (tripLoadingAddress.contains(drivers.get(i).getFcmLoadingArea().toLowerCase()))
                        tokens.add(drivers.get(i).getFcmToken());
                }else if(driverUnloadingFcmArea!=null){
                    if (tripUnloadingAddress.contains(drivers.get(i).getFcmUnloadingArea().toLowerCase()))
                        tokens.add(drivers.get(i).getFcmToken());
                }
            }


        }
        //Log.d(TAG, "getFilteredTokens: driverSize: "+drivers.size()+", fcmToken: "+tokens);


    }
    public void setTrip(Trip trip){
        this.trip = null;
        this.trip = trip;
    }
}
