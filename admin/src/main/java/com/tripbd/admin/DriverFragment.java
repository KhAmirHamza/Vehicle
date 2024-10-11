package com.tripbd.admin;

import static com.tripbd.admin.Constants.MAIN_URL;

import android.content.Intent;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverFragment extends Fragment {
    private static final String TAG = "DriverFragment";
    RecyclerView recy_drivers;
    DriverAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver, container, false);

        recy_drivers = view.findViewById(R.id.recy_drivers);
        adapter = new DriverAdapter(getActivity());
        recy_drivers.setHasFixedSize(true);
        recy_drivers.setLayoutManager(new LinearLayoutManager(getActivity()));
        recy_drivers.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDrivers();
    }

    List<Driver> getDrivers(){
        List<Driver> drivers = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("driver").orderBy("due", Query.Direction.DESCENDING).addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
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
                    Driver driver = doc.toObject(Driver.class);
                    driver.setId(doc.getId());
                    drivers.add(driver);
                }
                adapter.setData(drivers, recy_drivers);
            }
        });

        return drivers;
    }

}