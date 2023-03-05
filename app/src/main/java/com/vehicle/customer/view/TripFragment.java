package com.vehicle.customer.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.vehicle.customer.Constants;
import com.vehicle.customer.R;
import com.vehicle.customer.adapter.BidAdapter;
import com.vehicle.customer.model.Driver;
import com.vehicle.customer.model.Trip;
import com.vehicle.customer.model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class TripFragment extends Fragment {
    private static final String TAG = "TripFragment";
    String customerPhoneNumber;
    TextView tvNoTrip;

    TextView tv_car_details,tv_loading_date_time,tv_loading_upazila_district,
            tv_loading_full_address,tv_unloading_upazila_district,
            tv_unloading_full_address,tv_description,tv_up_down_trip,
            tv_contain_animal,tv_fragile_product,tv_perishable_product,
            tv_labor_needed, txtv_trip_id, tv_trip_status, tv_show_bidding,txtv_driver_name,
            txtv_driver_email, txtv_driver_phone_number;
    RecyclerView recy_biders;
    ImageView imgv_vehicle, imgv_driver;
    View view;
    Trip trip ;
    List<Trip> trips;
    RelativeLayout rlTripFragment;

    ImageView imgv_car;
    TextView txtv_vehicle_model,txtv_vehicle_number,txtv_vehicle_year,txtv_vehicle_description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_trip, container, false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
        customerPhoneNumber = sharedPreferences.getString("CUSTOMER_PHONE_NUMBER",null);
        getTrips();

        rlTripFragment =  view.findViewById(R.id.rlTripFragment);
        tvNoTrip =  view.findViewById(R.id.tvNoTrip);
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
        tv_trip_status.setSelected(true);
        tv_show_bidding =  findViewById(R.id.tv_show_bidding);
        txtv_driver_name =  findViewById(R.id.txtv_driver_name);
        txtv_driver_email =  findViewById(R.id.txtv_driver_email);
        txtv_driver_phone_number =  findViewById(R.id.txtv_driver_phone_number);
        imgv_vehicle =  view.findViewById(R.id.imgv_vehicle);
        imgv_driver =  view.findViewById(R.id.imgv_driver);
        recy_biders =  view.findViewById(R.id.recy_biders);

        imgv_car =  view.findViewById(R.id.imgv_car);
        txtv_vehicle_model =  findViewById(R.id.txtv_vehicle_model);
        txtv_vehicle_number =  findViewById(R.id.txtv_vehicle_number);
        txtv_vehicle_year =  findViewById(R.id.txtv_vehicle_year);
        txtv_vehicle_description =  findViewById(R.id.txtv_vehicle_description);

        getTrips();
        return view;
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
    void setViews(){
        String details = getVehicleDetails(trip);
        tv_car_details.setText(details==null?"":details);
        tv_loading_date_time.setText("( "+trip.getLoadingTime()+", "+trip.getLoadingDate()+" )");
        txtv_trip_id.setText(""+trip.getId());
        long rMills = trip.getCreatedAtMills()+1000* Constants.BIDDING_TIME_MINUTES *60;
        long cMills = System.currentTimeMillis();
        /*String status = trip.getStatus().isEmpty()?"":
                cMills>rMills && trip.getStatus().equalsIgnoreCase("Bidding")?
                        "Time Expired": "Trip "+trip.getStatus();*/


        if(cMills>rMills && trip.getStatus().equalsIgnoreCase("Bidding")){
            /*String status = trip.getStatus().isEmpty()?"":
                    cMills>rMills && trip.getStatus().equalsIgnoreCase("Bidding")?
                            "Time Expired": trip.getStatus();*/
            String status = trip.getStatus().isEmpty()?"" : "Time Expired";

            if (isBidPlaced(trip)){
                tv_trip_status.setText("Bid Placed");
            }else{
                tv_trip_status.setText(status);
            }

        }else{
            if (isBidPlaced(trip)){
                tv_trip_status.setText("Bid Placed");
                //holder.tv_trip_status.setText(""+bidPlaced);
            }else{
                setCountDownTimer(tv_trip_status, rMills-cMills);
            }
        }
        tv_trip_status.setSelected(true);





        tv_loading_upazila_district.setText("Area-->"+trip.getLoadingUpazilaThana());
        tv_loading_full_address.setText("Full Address-->"+trip.getLoadingFullAddress() +"\nLandMark-->"+trip.getLoadingLandmark());
        tv_unloading_upazila_district.setText("Area-->"+trip.getUnloadingUpazilaThana());
        tv_unloading_full_address.setText("Full Address-->"+trip.getUnloadingFullAddress() +"\nLandMark-->"+trip.getUnloadingLandmark());
        tv_description.setText(trip.getDescription().isEmpty()?"":trip.getDescription());
        tv_up_down_trip.setVisibility(trip.getUpDownTrip()==1? View.VISIBLE:View.GONE);
        tv_contain_animal.setVisibility(trip.getContainAnimal()==1? View.VISIBLE:View.GONE);
        tv_fragile_product.setVisibility(trip.getFragile()==1? View.VISIBLE:View.GONE);
        tv_perishable_product.setVisibility(trip.getPerishable()==1? View.VISIBLE:View.GONE);
        tv_labor_needed.setVisibility(trip.getLaborNeeded()==1? View.VISIBLE:View.GONE);

        Driver driver = trip.getDriver();
        if (driver!=null){
            String vehicleImageUrl = trip.getVehicle().getVehicleImageUrl();
            String driverImageUrl = driver.getImageUrl();
            Picasso.get().load(driverImageUrl).into(imgv_driver);
            txtv_driver_email.setText(driver.getEmail());
            txtv_driver_name.setText(driver.getName());
            txtv_driver_phone_number.setText(driver.getPhoneNumber());

            Picasso.get().load(driverImageUrl).into(imgv_driver);
            Picasso.get().load(vehicleImageUrl).into(imgv_vehicle);
        }
        setVehicleInfo(trip.getVehicle());

        tv_show_bidding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recy_biders.setVisibility(recy_biders.getVisibility()==View.VISIBLE? View.GONE: View.VISIBLE);
            }
        });
    }

    void setVehicleInfo(Vehicle vehicle){
        if (!vehicle.getVehicleImageUrl().isEmpty()){
            txtv_vehicle_description.setVisibility(View.GONE);
            if (vehicle.getType().equalsIgnoreCase("Truck")||
                    vehicle.getType().equalsIgnoreCase("Pickup")||
                    vehicle.getType().equalsIgnoreCase("Trailer")){

                txtv_vehicle_model.setText(vehicle.getModel());
                txtv_vehicle_number.setText(vehicle.getMetro()+"-"+vehicle.getSerial()+"-"+vehicle.getNumber());
                txtv_vehicle_year.setText("Model Year: "+vehicle.getYear());
                txtv_vehicle_description.setText(vehicle.getSize()+" ("+vehicle.getVariety()+")");
                if (vehicle.getVehicleImageUrl() != null) Picasso.get().load(vehicle.getVehicleImageUrl()).into(imgv_car);

            }else {
                txtv_vehicle_model.setText(vehicle.getModel());
                txtv_vehicle_number.setText(vehicle.getMetro()+"-"+vehicle.getSerial()+"-"+vehicle.getNumber());
                txtv_vehicle_year.setText("Model Year: "+vehicle.getYear());
                txtv_vehicle_description.setText(vehicle.getSeat()+" Seated"+" ("+vehicle.getVariety()+")");
                //Toast.makeText(getContext(), "Sit: "+vehicle.getSeat(), Toast.LENGTH_SHORT).show();
                if (vehicle.getVehicleImageUrl() != "") Picasso.get().load(vehicle.getVehicleImageUrl()).into(imgv_car);
            }
        }
    }

    boolean isBidPlaced(Trip trip){
        boolean bidPlaced = false;
        if (trip.getBids()!=null && trip.getBids().size()>0){
            for (Trip.Bid bid : trip.getBids()) {
                if (!bid.getDriver().getPhoneNumber().isEmpty()) {
                    bidPlaced = true;
                    break;
                }
            }
            return  bidPlaced;
        }else{
            return false;
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

    List<Trip> getTrips(){
        List<Trip> trips = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trip").addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                assert value != null;
                if (value.getDocuments().size()<1) {
                    Toast.makeText(getActivity(), "No Trip Found!", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (QueryDocumentSnapshot doc : Objects.<QuerySnapshot>requireNonNull(value))
                   // if (doc.get("date") != null)
                    {
                        Trip trip = doc.toObject(Trip.class);
                        if (trip.getCustomer().getPhoneNumber().equalsIgnoreCase(customerPhoneNumber)  &&
                                (!trip.getStatus().equalsIgnoreCase("Complete") ||
                                        !trip.getStatus().equalsIgnoreCase("Cancel"))){
                            trip.setId(doc.getId());
                            trips.add(trip);
                        }
                    }
                if (trips.size()<1) {
                    Toast.makeText(getActivity(), "No Trip Found! Create Trip...", Toast.LENGTH_SHORT).show();
                    rlTripFragment.setVisibility(View.GONE);
                    tvNoTrip.setVisibility(View.VISIBLE);
                    return;
                }else{
                    rlTripFragment.setVisibility(View.VISIBLE);
                    tvNoTrip.setVisibility(View.GONE);
                }

                trip = trips.get(trips.size()-1);//Taking only latest Trip for bidding,because client didn't want trip history
                Toast.makeText(getActivity(), ": "+trip.getLoadingDate(), Toast.LENGTH_LONG).show();
                setViews();

                if (trip.getStatus().equalsIgnoreCase("Bidding")){
                    setBiddingUI();
                }else{
                    tv_show_bidding.setVisibility(View.GONE);
                }
                Log.d(TAG, "Trips: " + trips);
            }
        });
        return trips;
    }
    void setBiddingUI(){
        BidAdapter adapter = new BidAdapter(getActivity());
        recy_biders.setHasFixedSize(true);
        recy_biders.setLayoutManager(new LinearLayoutManager(getActivity()));
        recy_biders.setAdapter(adapter);
        adapter.setData(trip.getBids(), recy_biders);
        adapter.setOnClickListener(new BidAdapter.OnClickListener() {
            @Override
            public void onClick(View view, @Nullable Trip.Bid bid, int position) {
                //Toast.makeText(getActivity(), bid.getDriver().getName(), Toast.LENGTH_SHORT).show();

                FirebaseFirestore db = FirebaseFirestore.getInstance();


                trip.setVehicle(bid.getVehicle());
                trip.setDriver(bid.getDriver());
                trip.setAdvancePay(bid.getAdvance());
                trip.setFinalPayMethod(bid.getReqPayMethod());
                trip.setStatus("Trip Running");

                DocumentReference tripRef = db.collection("trip").document(trip.getId());
                tripRef.set(trip).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Driver Selected!", Toast.LENGTH_SHORT).show();
                            getContext().startActivity(new Intent(getContext(), MainActivity.class));
                            getActivity().finish();
                        }else{
                            Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}