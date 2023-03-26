package com.vehicle.customer.view;

import static com.vehicle.customer.Constants.MAIN_URL;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.vehicle.customer.R;
import com.vehicle.customer.model.Driver;
import com.vehicle.customer.model.FCM;
import com.vehicle.customer.model.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogTripPreview extends Dialog {
    private static final String TAG = "DialogTripPreview";

    TextView tv_car_details,tv_car_date_time,tv_loading_upazila_district,
            tv_loading_full_address,tv_unloading_upazila_district,
            tv_unloading_full_address,tv_description,tv_up_down_trip,
            tv_contain_animal,tv_fragile_product,tv_perishable_product,
            tv_labor_needed;

    MaterialButton btn_review;
    ProgressBar progressbar;
    List<String> tokens = new ArrayList<>();
Activity activity;
    Trip trip;


    public DialogTripPreview(@NonNull Activity context, Trip trip) {
        super(context, android.R.style.Theme_Holo_Light_Dialog);
        this.activity = context;
        this.trip = trip;
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
        progressbar =  findViewById(R.id.progressbar);

        btn_review =  findViewById(R.id.btn_review);

        tv_car_details.setText(getVehicleDetails(trip));
        tv_car_date_time.setText("( "+trip.getLoadingTime()+", "+trip.getLoadingDate()+" )");
        tv_loading_upazila_district.setText("Area-->"+trip.getLoadingUpazilaThana());
        tv_loading_full_address.setText("Full Address-->"+trip.getLoadingFullAddress() +"\nLandMark-->"+trip.getLoadingLandmark());
        tv_unloading_upazila_district.setText("Area-->"+trip.getUnloadingUpazilaThana());
        tv_unloading_full_address.setText("Full Address-->"+trip.getUnloadingFullAddress() +"\nLandMark-->"+trip.getUnloadingLandmark());
        tv_description.setText(trip.getDescription());
        tv_up_down_trip.setVisibility(trip.getUpDownTrip()==1? View.VISIBLE:View.GONE);
        tv_contain_animal.setVisibility(trip.getContainAnimal()==1? View.VISIBLE:View.GONE);
        tv_fragile_product.setVisibility(trip.getFragile()==1? View.VISIBLE:View.GONE);
        tv_perishable_product.setVisibility(trip.getPerishable()==1? View.VISIBLE:View.GONE);
        tv_labor_needed.setVisibility(trip.getLaborNeeded()==1? View.VISIBLE:View.GONE);

        getFilteredDriverFcmTokens();
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
        FCM fcm = new FCM("New Order Published!",
                "New Trip has been published for your Area! Check it out now!", tokens);
        ApiClient.getInstance(MAIN_URL).sendNotification(fcm).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        Log.d(TAG, "onResponse: Order Data is null");
                        return;
                    }
                    Log.d(TAG, "onResponse: Successful");
                    String welcomeText = "Thank you for submitting a bid. Drivers will tell you the rate. Wait and confirm.";
                    DialogWelcome dialogWelcome = new DialogWelcome(activity, welcomeText, new Intent(activity,MainActivity.class));
                    dialogWelcome.setCancelable(false);
                    dialogWelcome.show();
                    progressbar.setVisibility(View.GONE);
                }else {
                    //Toast.makeText(OrderActivity.this, "ResponseError: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: ResponseError: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
                // Toast.makeText(OrderActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                Log.d(TAG, "onFailure: " + t.getMessage());
                //Log.d(OrderActivity.TAG, "onFailure: " + t.getCause().toString());
            }
        });
    }

    public void getFilteredDriverFcmTokens(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("driver").whereEqualTo("fcmArea", trip.getLoadingUpazilaThana())
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    Log.w(TAG, "Listen failed: " + error.getMessage());
                                    progressbar.setVisibility(View.GONE);
                                    return;
                                }
                                if (value == null || value.getDocuments().size() < 1) {
                                    progressbar.setVisibility(View.GONE);
                                    Toast.makeText(activity, "Registration First...", Toast.LENGTH_SHORT).show();
                                } else {
                                    for (QueryDocumentSnapshot doc : Objects.<QuerySnapshot>requireNonNull(value)) {
                                        Driver driver = doc.toObject(Driver.class);
                                        if (driver.getName() != null) {
                                            progressbar.setVisibility(View.GONE);
                                            tokens.add(driver.getFcmToken());
                                            Toast.makeText(activity, driver.getFcmToken(), Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(activity, "No Driver Found.1", Toast.LENGTH_SHORT).show();
                                            progressbar.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            }

                        });
        }
    }
