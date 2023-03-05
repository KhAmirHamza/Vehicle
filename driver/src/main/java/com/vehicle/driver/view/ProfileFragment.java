package com.vehicle.driver.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.vehicle.driver.R;
import com.vehicle.driver.adapter.VehicleAdapter;
import com.vehicle.driver.model.Driver;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    CircleImageView imgv_profile_image;
    TextView txtv_name, txtv_email,txtv_phone_number,txtv_due,txtv_notification_area;
    MaterialButton btn_change_area, btn_add_vehicle;
    RecyclerView recy_vehicles;
    VehicleAdapter vehicleAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imgv_profile_image = view.findViewById(R.id.imgv_profile_image);
        txtv_name = view.findViewById(R.id.txtv_name);
        txtv_email = view.findViewById(R.id.txtv_email);
        txtv_phone_number = view.findViewById(R.id.txtv_phone_number);
        txtv_due = view.findViewById(R.id.txtv_due);
        txtv_notification_area = view.findViewById(R.id.txtv_notification_area);
        btn_change_area = view.findViewById(R.id.btn_change_area);
        btn_add_vehicle = view.findViewById(R.id.btn_add_vehicle);
        recy_vehicles = view.findViewById(R.id.recy_vehicles);

        getDriver();


        btn_change_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo change or set area...

            }
        });
        btn_add_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo add vehicle...
                startActivity(new Intent(getContext(), AddVehicleActivity.class));
            }
        });

        vehicleAdapter = new VehicleAdapter(getActivity());
        recy_vehicles.setHasFixedSize(true);
        recy_vehicles.setLayoutManager(new LinearLayoutManager(getContext()));
        recy_vehicles.setAdapter(vehicleAdapter);

        return  view;
    }
    public void getDriver(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences sp = getActivity().getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
        String id = sp.getString("DRIVER_ID",null);
        String notification = sp.getString("NOTIFICATION","None");
        if (id==null) {
            Toast.makeText(getContext(), "You are not sign in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), SignInActivity.class));
            getActivity().finish();
        }else{
            db.collection("driver").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Driver driver = task.getResult().toObject(Driver.class);
                    if (driver==null){
                        Toast.makeText(getContext(), "Driver Not Found! "+id, Toast.LENGTH_SHORT).show();
                    }else{
                        //todo...
                        Picasso.get().load(driver.getImageUrl()).into(imgv_profile_image);
                        txtv_name.setText(driver.getName());
                        txtv_email.setText(driver.getEmail());
                        txtv_phone_number.setText(driver.getPhoneNumber());
                        txtv_due.setText("Due: "+driver.getDue());
                        txtv_notification_area.setText("Area: "+notification);

                        vehicleAdapter.setData(driver.getVehicles(),recy_vehicles);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }
}