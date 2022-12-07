package com.vehicle.customer.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_trip, container, false);

        getTrips();

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
        tv_show_bidding =  findViewById(R.id.tv_show_bidding);
        txtv_driver_name =  findViewById(R.id.txtv_driver_name);
        txtv_driver_email =  findViewById(R.id.txtv_driver_email);
        txtv_driver_phone_number =  findViewById(R.id.txtv_driver_phone_number);
        imgv_vehicle =  view.findViewById(R.id.imgv_vehicle);
        imgv_driver =  view.findViewById(R.id.imgv_driver);
        recy_biders =  view.findViewById(R.id.recy_biders);

        getTrips();
        return view;
    }
    String getVehicleDetails(Trip trip){
        if (trip.getVehicle()==null) return "...";
        else if (trip.getVehicle().getType().equalsIgnoreCase("Truck/PickUp/Trailer")){
            return trip.getVehicle().getType()+", "
                    +trip.getVehicle().getSize()+" Feet "
                    +trip.getVehicle().getCapacity()+" Ton ("
                    +trip.getVehicle().getOpenOrCovered()+")";
        }else {
            return trip.getVehicle().getType()+", "
                    +trip.getVehicle().getSize()+" Seats ";
        }
    }
    void setViews(){
        String details = getVehicleDetails(trip);
        tv_car_details.setText(details==null?"":details);
        tv_loading_date_time.setText("( "+trip.getLoadingTime()+", "+trip.getLoadingDate()+" )");
        txtv_trip_id.setText(""+trip.getId());
        long rMills = trip.getCreatedAtMills()+1000*30*60;
        long cMills = System.currentTimeMillis();
        String status = trip.getStatus().isEmpty()?"":
                cMills>rMills && trip.getStatus().equalsIgnoreCase("Bidding")?
                        "Time Expired": trip.getStatus();
        tv_trip_status.setText(status);
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
            String vehicleImageUrl = trip.getVehicle().getImageUrl();
            String driverImageUrl = driver.getImageUrl();
            Picasso.get().load(driverImageUrl).into(imgv_driver);
            txtv_driver_email.setText(driver.getEmail());
            txtv_driver_name.setText(driver.getName());
            txtv_driver_phone_number.setText(driver.getPhoneNumber());
            Picasso.get().load(driverImageUrl).into(imgv_driver);
            Picasso.get().load(vehicleImageUrl).into(imgv_vehicle);

        }


        tv_show_bidding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recy_biders.setVisibility(recy_biders.getVisibility()==View.VISIBLE? View.GONE: View.VISIBLE);
            }
        });
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
                    return;
                }


                assert value != null;
                for (QueryDocumentSnapshot doc : Objects.<QuerySnapshot>requireNonNull(value))
                   // if (doc.get("date") != null)
                    {
                        Trip trip = doc.toObject(Trip.class);
                        trip.setId(doc.getId());
                        trips.add(trip);
                    }


                trip = trips.get(trips.size()-1);
                //Toast.makeText(getActivity(), ": "+trip.getLoadingDate(), Toast.LENGTH_SHORT).show();
                setViews();
                BidAdapter adapter = new BidAdapter(getActivity());
                recy_biders.setHasFixedSize(true);
                recy_biders.setLayoutManager(new LinearLayoutManager(getActivity()));
                recy_biders.setAdapter(adapter);
                adapter.setData(trip.getBids(), recy_biders);
                Log.d(TAG, "Trips: " + trips);
            }
        });
        return trips;
    }
}