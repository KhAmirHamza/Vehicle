package com.vehicle.driver.view;

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




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_all_trip, container, false);
        recy_all_trips = view.findViewById(R.id.recy_all_trips);

        adapter = new TripAdapter(getActivity());
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
                    return;
                }
                assert value != null;
                for (QueryDocumentSnapshot doc : Objects.<QuerySnapshot>requireNonNull(value))
                // if (doc.get("date") != null)
                {
                    Trip trip = doc.toObject(Trip.class);
                    trip.setId(doc.getId());
                    long rMills = trip.getCreatedAtMills()+1000*30*60;
                    long cMills = System.currentTimeMillis();
                    //if (cMills<=rMills)
                        trips.add(trip);
                }

                adapter.setData(trips, recy_all_trips);
                Log.d(TAG, "Trips: " + trips);
            }
        });
        return trips;
    }



}