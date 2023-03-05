package com.vehicle.driver.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vehicle.driver.R;
import com.vehicle.driver.adapter.TripAdapter;
import com.vehicle.driver.model.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllTripFragment extends Fragment {
    private static final String TAG = "AllTripFragment";

    Trip trip;
    RecyclerView recy_all_trips;
    TripAdapter adapter;
    SharedPreferences sharedPreferences;
    String driverPhoneNumber;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sharedPreferences = getContext().getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
        driverPhoneNumber = sharedPreferences.getString("DRIVER_PHONE_NUMBER", "null");
        View view =  inflater.inflate(R.layout.fragment_all_trip, container, false);
        recy_all_trips = view.findViewById(R.id.recy_all_trips);

        adapter = new TripAdapter(getActivity(), driverPhoneNumber);
        recy_all_trips.setHasFixedSize(true);
        recy_all_trips.setLayoutManager(new LinearLayoutManager(getActivity()));
        recy_all_trips.setAdapter(adapter);

        getTrips();
        return view;
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
                if (value == null || value.getDocuments().size() < 1) {
                    Toast.makeText(getActivity(), "No Trip found!", Toast.LENGTH_SHORT).show();
                return;
                }
                for (QueryDocumentSnapshot doc : Objects.<QuerySnapshot>requireNonNull(value))
                 //if (doc.get("loadingTime") != null)
                 {
                    Trip trip = doc.toObject(Trip.class);
                    trip.setId(doc.getId());
                    long rMills = trip.getCreatedAtMills()+1000*30*60;
                    long cMills = System.currentTimeMillis();
                    //if (cMills<=rMills)
                     if (trip.getStatus().equalsIgnoreCase("Bidding")){
                         trips.add(trip);
                     }
                }

                adapter.setData(trips, recy_all_trips);
                Log.d(TAG, "Trips: " + trips);
            }
        });
        return trips;
    }



}