package com.vehicle.driver.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vehicle.driver.R;
import com.vehicle.driver.adapter.AutoCompleteCustomAdapter;
import com.vehicle.driver.adapter.TripAdapter;
import com.vehicle.driver.model.Trip;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
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
    AutoCompleteTextView autocomplete_loading_upazila, autocomplete_unloading_upazila;
    MaterialButton btnFilter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sharedPreferences = getContext().getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
        driverPhoneNumber = sharedPreferences.getString("DRIVER_PHONE_NUMBER", "null");
        View view =  inflater.inflate(R.layout.fragment_all_trip, container, false);
        recy_all_trips = view.findViewById(R.id.recy_all_trips);
        autocomplete_loading_upazila = view.findViewById(R.id.autocomplete_loading_upazila);
        autocomplete_unloading_upazila = view.findViewById(R.id.autocomplete_unloading_upazila);
        btnFilter = view.findViewById(R.id.btnFilter);

        adapter = new TripAdapter(getActivity(), driverPhoneNumber);
        recy_all_trips.setHasFixedSize(true);
        recy_all_trips.setLayoutManager(new LinearLayoutManager(getActivity()));
        recy_all_trips.setAdapter(adapter);
        AutoCompleteCustomAdapter addressAdapter = new AutoCompleteCustomAdapter(getContext(), getAddresses());
        //AutoCompleteAdapter addressAdapter = new AutoCompleteAdapter(getContext(), Arrays.asList(addressNames));
        autocomplete_loading_upazila.setAdapter(addressAdapter);
        autocomplete_unloading_upazila.setAdapter(addressAdapter);
        getTrips();

        autocomplete_loading_upazila.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CharSequence loading =(CharSequence) autocomplete_loading_upazila.getText().toString();
                CharSequence unloading =(CharSequence) autocomplete_unloading_upazila.getText().toString();
                CharSequence filterQuery = loading+"+"+unloading;
                Toast.makeText(getContext(), filterQuery, Toast.LENGTH_SHORT).show();
                adapter.getFilter().filter(filterQuery);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        autocomplete_unloading_upazila.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CharSequence loading =(CharSequence) autocomplete_loading_upazila.getText().toString();
                CharSequence unloading =(CharSequence) autocomplete_unloading_upazila.getText().toString();
                CharSequence filterQuery = loading+"+"+unloading;
                //Toast.makeText(getContext(), filterQuery, Toast.LENGTH_SHORT).show();
                adapter.getFilter().filter(filterQuery);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }
    public List<String> getAddresses(){
        //loadDistrict...
        List<String> addresses = new ArrayList<>();
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(loadJSONFromAsset("address.json"));
            for (int i = 0; i < jsonArray.length(); i++) {
                String district =  jsonArray.getJSONObject(i).get("district").toString();
                String name =  jsonArray.getJSONObject(i).get("name").toString();
                String thana =  jsonArray.getJSONObject(i).get("thana").toString();

                //Address address = new Address(name, thana, district);
                addresses.add(name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    public String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
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

                btnFilter.setVisibility(View.VISIBLE);
                btnFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence loading =(CharSequence) autocomplete_loading_upazila.getText().toString();
                        CharSequence unloading =(CharSequence) autocomplete_unloading_upazila.getText().toString();
                        CharSequence filterQuery = loading+"+"+unloading;
                        Toast.makeText(getContext(), filterQuery, Toast.LENGTH_SHORT).show();
                        adapter.getFilter().filter(filterQuery);
                    }
                });
            }
        });
        return trips;
    }



}