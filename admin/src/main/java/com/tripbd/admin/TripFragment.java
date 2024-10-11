package com.tripbd.admin;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TripFragment extends Fragment {
    private static final String TAG = "TripFragment";
    RecyclerView recy_trips;
    TripAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip, container, false);

        recy_trips = view.findViewById(R.id.recy_trips);
        adapter = new TripAdapter(getActivity());
        recy_trips.setHasFixedSize(true);
        recy_trips.setLayoutManager(new LinearLayoutManager(getActivity()));
        recy_trips.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTrips();
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
                    //Toast.makeText(getActivity(), "No Trip found!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //trips.clear();
                for (QueryDocumentSnapshot doc : Objects.<QuerySnapshot>requireNonNull(value))
                //if (doc.get("loadingTime") != null)
                {
                    Trip trip = doc.toObject(Trip.class);
                    trip.setId(doc.getId());
                    long rMills = trip.getCreatedAtMills()+(1000*30*60);
                    long cMills = System.currentTimeMillis();
                   // if (cMills<=rMills ||trip.getStatus().equalsIgnoreCase("Trip Completed"))
                        if (!trip.getStatus().equalsIgnoreCase("Trip Completed")){
                            trips.add(trip);
                        }
                }

                adapter.setData(trips, recy_trips);
                //Log.d(TAG, "Trips: " + trips);

                //btnFilter.setVisibility(View.VISIBLE);
              /*  btnFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence loading =(CharSequence) autocomplete_loading_upazila.getText().toString();
                        CharSequence unloading =(CharSequence) autocomplete_unloading_upazila.getText().toString();
                        CharSequence filterQuery = loading+"+"+unloading;
                        Toast.makeText(getContext(), filterQuery, Toast.LENGTH_SHORT).show();
                        adapter.getFilter().filter(filterQuery);
                    }
                });*/
            }
        });
        return trips;
    }
}