package com.tripbd.driver.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tripbd.driver.R;
import com.tripbd.driver.adapter.RunningTripAdapter;
import com.tripbd.driver.model.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RunningTripFragment extends Fragment {
    private static final String TAG = "RunningTripFragment";
    RunningTripAdapter runningTripAdapter;
    SharedPreferences sharedPreferences;
    String driverPhoneNumber;
    RecyclerView recy_running_trips;
    TextView tv_h1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_running_trip, container, false);

        tv_h1 = view.findViewById(R.id.tv_h1);
        tv_h1.setSelected(true);
        sharedPreferences = getContext().getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
        driverPhoneNumber = sharedPreferences.getString("DRIVER_PHONE_NUMBER", "null");
        runningTripAdapter = new RunningTripAdapter(getActivity(), driverPhoneNumber);
        recy_running_trips = view.findViewById(R.id.recy_running_trips);
        recy_running_trips.setHasFixedSize(true);
        recy_running_trips.setLayoutManager(new LinearLayoutManager(getActivity()));
        recy_running_trips.setAdapter(runningTripAdapter);
        getRunningTrips();
        return view;
    }

    List<Trip> getRunningTrips(){
        List<Trip> trips = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trip").whereEqualTo("status","Trip Confirmed").addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                trips.clear();
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value == null || value.getDocuments().size() < 1) {
                    //Toast.makeText(getContext(), "No Trip found!", Toast.LENGTH_SHORT).show();
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
                    trips.add(trip);
                    /*if (trip.getStatus().equalsIgnoreCase("Bidding")){

                    }*/
                }

                runningTripAdapter.setData(trips, recy_running_trips);
                Log.d(TAG, "Trips: " + trips);
            }
        });
        return trips;
    }

}