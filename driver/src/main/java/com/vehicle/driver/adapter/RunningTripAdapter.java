package com.vehicle.driver.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vehicle.driver.Constants;
import com.vehicle.driver.R;
import com.vehicle.driver.model.Trip;
import com.vehicle.driver.view.BiddingDialog;
import com.vehicle.driver.view.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RunningTripAdapter extends RecyclerView.Adapter<RunningTripAdapter.MyViewHolder> {

    Activity context;
    List<Trip> trips;
    private static final String TAG = "TripAdapter";
    RecyclerView recyclerView;
    public OnClickListener onClickListener;
    String driverPhoneNumber;
    View view;

    public RunningTripAdapter(Activity context, String driverPhoneNumber) {
        this.context = context;
        this.driverPhoneNumber = driverPhoneNumber;
        this.trips = new ArrayList<>();
    }
    public void setData(List<Trip> trips, RecyclerView recyclerView ){

        this.trips = null;
        this.trips = trips;
        this.recyclerView = recyclerView;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RunningTripAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.iv_running_trip, parent,
                    false));
    }
    boolean isBidPlaced( int position){
        boolean bidPlaced = false;
        if (trips.get(position).getBids()!=null && trips.get(position).getBids().size()>0){
            for (Trip.Bid bid : trips.get(position).getBids()) {
                if (bid.getDriver().getPhoneNumber().equalsIgnoreCase(driverPhoneNumber)) {
                    bidPlaced = true;
                    break;
                }
            }
            return  bidPlaced;
        }else{
            return false;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RunningTripAdapter.MyViewHolder holder, int position) {
        Trip trip = trips.get(position);
        String details = getVehicleDetails(trip);
        holder.tv_car_details.setText(details==null?"...": details);
        holder.tv_loading_date_time.setText("( "+trip.getLoadingTime()+", "+trip.getLoadingDate()+" )");
        holder.txtv_trip_id.setText(""+trip.getId());
        long rMills = trip.getCreatedAtMills()+1000* Constants.BIDDING_TIME_MINUTES *60;
        long cMills = System.currentTimeMillis();

        holder.tv_trip_status.setText(trip.getStatus());
        holder.tv_trip_status.setSelected(true);


        holder.tv_loading_upazila_district.setText("Area: "+trip.getLoadingUpazilaThana());
        holder.tv_loading_full_address.setText("Full Address: "+trip.getLoadingFullAddress() +"\nNa/O Nearby School/ Mosque/ other: "+trip.getLoadingLandmark());
        holder.tv_unloading_upazila_district.setText("Area: "+trip.getUnloadingUpazilaThana());
        holder.tv_unloading_full_address.setText("Full Address: "+trip.getUnloadingFullAddress() +"\nNa/O Nearby School/ Mosque/ other: "+trip.getUnloadingLandmark());
        holder.tv_description.setText(trip.getDescription().isEmpty()?"":trip.getDescription());
        holder.tv_up_down_trip.setVisibility(trip.getUpDownTrip()==1? View.VISIBLE:View.GONE);
        holder.tv_contain_animal.setVisibility(trip.getContainAnimal()==1? View.VISIBLE:View.GONE);
        holder.tv_fragile_product.setVisibility(trip.getFragile()==1? View.VISIBLE:View.GONE);
        holder.tv_perishable_product.setVisibility(trip.getPerishable()==1? View.VISIBLE:View.GONE);
        holder.tv_labor_needed.setVisibility(trip.getLaborNeeded()==1? View.VISIBLE:View.GONE);
        holder.tv_lengthAlert.setVisibility(trip.getLengthAlert()==1? View.VISIBLE:View.GONE);
        holder.tv_weightAlert.setVisibility(trip.getWeightAlert()==1? View.VISIBLE:View.GONE);
        holder.tv_loading_PersonName.setText(trip.getCustomer().getName());
        holder.tv_loading_PersonMobile.setText(trip.getCustomer().getPhoneNumber());
        holder.tv_loading_AlterPersonMobile.setText(trip.getLoadingAlternative_person_number());
        holder.tv_unloading_PersonName.setText(trip.getUnloading_personName());
        holder.tv_unloading_PersonMobile.setText(trip.getUnloading_mobileNumber());
        holder.tv_trip_price.setText(trip.getRentalPrice()+" Tk");

    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        MaterialButton btn_complete_trip;
        RelativeLayout lay_trip_details;
        MaterialCardView cv_trip_header;
        TextView tv_car_details,tv_loading_date_time,tv_loading_upazila_district,
                tv_loading_full_address,tv_unloading_upazila_district,
                tv_unloading_full_address,tv_description,tv_up_down_trip,
                tv_contain_animal,tv_fragile_product,tv_perishable_product,
                tv_labor_needed, txtv_trip_id, tv_trip_status, tv_loading_PersonName,
                tv_loading_PersonMobile, tv_loading_AlterPersonMobile, tv_unloading_PersonName,
                tv_unloading_PersonMobile, tv_lengthAlert, tv_weightAlert, tv_trip_price;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            tv_loading_PersonName =  findViewById(R.id.tv_loading_PersonName);
            tv_loading_PersonMobile =  findViewById(R.id.tv_loading_PersonMobile);
            tv_loading_AlterPersonMobile =  findViewById(R.id.tv_loading_AlterPersonMobile);
            tv_unloading_PersonName =  findViewById(R.id.tv_unloading_PersonName);
            tv_unloading_PersonMobile =  findViewById(R.id.tv_unloading_PersonMobile);
            tv_trip_price =  findViewById(R.id.tv_trip_price);

            tv_car_details =  findViewById(R.id.tv_car_details);
            tv_loading_date_time =  findViewById(R.id.tv_loading_date_time);
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
            txtv_trip_id =  findViewById(R.id.txtv_trip_id);
            tv_trip_status =  findViewById(R.id.tv_trip_status);
            lay_trip_details =  view.findViewById(R.id.lay_trip_details);
            btn_complete_trip =  view.findViewById(R.id.btn_complete_trip);
            cv_trip_header =  view.findViewById(R.id.cv_trip_header);
            tv_lengthAlert =  view.findViewById(R.id.tv_lengthAlert);
            tv_weightAlert =  view.findViewById(R.id.tv_weightAlert);
            cv_trip_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lay_trip_details.setVisibility(lay_trip_details.getVisibility()==View.VISIBLE?View.GONE: View.VISIBLE);
                    tv_loading_full_address.setVisibility(tv_loading_full_address.getVisibility()==View.VISIBLE?View.GONE: View.VISIBLE);
                    tv_unloading_full_address.setVisibility(tv_unloading_full_address.getVisibility()==View.VISIBLE?View.GONE: View.VISIBLE);
                }
            });

            btn_complete_trip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // todo...
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Trip trip= trips.get(getAdapterPosition());
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Have you completed trip?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            trip.setStatus("Trip Completed");
                            SharedPreferences sharedPreferences = context.getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
                            driverPhoneNumber = sharedPreferences.getString("DRIVER_PHONE_NUMBER", "null");

                            String driverID = trip.getDriver().getId();
                            //todo...
                            //DocumentReference tripRef = db.collection("trip").document(trip.getId())
                            DocumentReference tripRef = db.collection("trip").document(trip.getId());
                            tripRef.set(trip).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //Toast.makeText(context, "Trip Completed!", Toast.LENGTH_SHORT).show();
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        Map<String,Object> driverDueMap = new HashMap<>();
                                        double bidPrice= trip.getRentalPrice();
                                        //int ownersAmount = (int) ((bidPrice / 100)*97);
                                        int serviceCharge = (int) ((bidPrice/100)*3);
                                        //serviceCharge+=trip.getDriver().getDue();
                                        Toast.makeText(context, "DUE: "+trip.getDriver().getDue()+"\nService: "+serviceCharge, Toast.LENGTH_SHORT).show();
                                        driverDueMap.put("due", serviceCharge+trip.getDriver().getDue());
                                        db.collection("driver").document(driverID).update(driverDueMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                //Toast.makeText(context, "DueSuccessfully Updated!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        context.startActivity(new Intent(context, MainActivity.class));
                                        context.finish();
                                    }else{
                                        Toast.makeText(context, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    builder.setCancelable(true);
                    builder.show();



                }
            });
        }
    }
    String getVehicleDetails(Trip trip){
        if (trip.getVehicle()==null) return "...";
        else if (
                trip.getVehicle().getType().equalsIgnoreCase("Truck") ||
                trip.getVehicle().getType().equalsIgnoreCase("PickUp") ||
                trip.getVehicle().getType().equalsIgnoreCase("Trailer")){

            return trip.getVehicle().getType()+", "+trip.getVehicle().getSize()+" ("
                    +trip.getVehicle().getVariety()+")";
        }else {
            return trip.getVehicle().getType()+", "
                    +trip.getVehicle().getSeat()+" Seated ("
                    +trip.getVehicle().getVariety()+")";
        }
    }
    public void setCountDownTimer(TextView textView, long remainingMills){
        new CountDownTimer(remainingMills, 1000) {

            public void onTick(long millisUntilFinished) {
                textView.setText("Bidding: " + millisUntilFinished / 1000/60 +":"+ (millisUntilFinished /1000)%60);
                // logic to set the EditText could go here
            }

            public void onFinish() {
                textView.setText("Time Expired");
            }

        }.start();
    }

    private TextView findViewById(int id) {
        return view.findViewById(id);
    }

    public interface OnClickListener{
        void onClick(View view, @Nullable Trip.Bid bid, int position);
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

}
