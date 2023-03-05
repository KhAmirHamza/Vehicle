package com.vehicle.customer.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import com.squareup.picasso.Picasso;
import com.vehicle.customer.R;
import com.vehicle.customer.model.Trip;

public class DialogTripPreview extends Dialog {
    private static final String TAG = "DialogTripPreview";

    TextView tv_car_details,tv_car_date_time,tv_loading_upazila_district,
            tv_loading_full_address,tv_unloading_upazila_district,
            tv_unloading_full_address,tv_description,tv_up_down_trip,
            tv_contain_animal,tv_fragile_product,tv_perishable_product,
            tv_labor_needed;

    MaterialButton btn_review;
    ProgressBar progressbar;

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
                    //Toast.makeText(getContext(), "Trip added for bidding successfully", Toast.LENGTH_SHORT).show();

                    String welcomeText = "Thank you for submitting a bid. Drivers will tell you the rate. Wait and confirm.";

                    DialogWelcome dialogWelcome = new DialogWelcome(getContext(), welcomeText);
                    dialogWelcome.setCancelable(true);
                    dialogWelcome.show();

                    activity.startActivity(new Intent(activity,MainActivity.class));
                    activity.finish();
                    cancel();
                    progressbar.setVisibility(View.GONE);
                }).addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));




            }
        });
    }

/*    if (vehicle.getType().equalsIgnoreCase("Truck")||
            vehicle.getType().equalsIgnoreCase("Pickup")||
            vehicle.getType().equalsIgnoreCase("Trailer")){

        holder.txtv_vehicle_model.setText(vehicle.getModel());
        holder.txtv_vehicle_number.setText(vehicle.getMetro()+"-"+vehicle.getSerial()+"-"+vehicle.getNumber());
        holder.txtv_vehicle_year.setText(", Year: "+vehicle.getYear());
        holder.txtv_vehicle_description.setText(vehicle.getSize()+" ("+vehicle.getVariety()+")");
        if (vehicle.getVehicleImageUrl() != null) Picasso.get().load(vehicle.getVehicleImageUrl()).into(holder.imgv_car);

    }else {
        holder.txtv_vehicle_model.setText(vehicle.getModel());
        holder.txtv_vehicle_number.setText(vehicle.getMetro()+"-"+vehicle.getSerial()+"-"+vehicle.getNumber());
        holder.txtv_vehicle_year.setText(", Year: "+vehicle.getYear());
        holder.txtv_vehicle_description.setText(vehicle.getSeat()+" Seated"+" ("+vehicle.getVariety()+")");
        Toast.makeText(context, "Sit: "+vehicle.getSeat(), Toast.LENGTH_SHORT).show();
        if (vehicle.getVehicleImageUrl() != "") Picasso.get().load(vehicle.getVehicleImageUrl()).into(holder.imgv_car);

    }*/

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

}
