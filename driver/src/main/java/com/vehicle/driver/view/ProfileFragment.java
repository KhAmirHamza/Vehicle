package com.vehicle.driver.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
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
import com.vehicle.driver.adapter.AutoCompleteCustomAdapter;
import com.vehicle.driver.adapter.VehicleAdapter;
import com.vehicle.driver.model.Address;
import com.vehicle.driver.model.Driver;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    CircleImageView imgv_profile_image;
    TextView txtv_name, txtv_email,txtv_phone_number,txtv_due,txtv_notification_area;
    MaterialButton btn_notification_area, btn_add_vehicle;
    RecyclerView recy_vehicles;
    VehicleAdapter vehicleAdapter;
    MaterialButton btnLogOut;
    List<String> addressList = new ArrayList<>();
    //Spinner spinner_notificationArea;
    AutoCompleteTextView text_input_layout_notifi_thana;
    ArrayAdapter<String> notificationAreaAdapter;
    String driverID=" ";
    AutoCompleteCustomAdapter addressAdapter;
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
        btn_notification_area = view.findViewById(R.id.btn_notification_area);
        btn_add_vehicle = view.findViewById(R.id.btn_add_vehicle);
        recy_vehicles = view.findViewById(R.id.recy_vehicles);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        text_input_layout_notifi_thana = view.findViewById(R.id.autocomplete_notificationArea);
        getAddresses();

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Do you want to log out?");
                builder.setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
                        sharedPreferences.edit().clear().apply();
                        startActivity(new Intent(getContext(), SignInActivity.class));
                        getActivity().finish();
                    }
                });
                builder.show();
            }
        });
        getDriver();

        btn_notification_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo change or set area...

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String,Object> fcmAreaMap = new HashMap<>();
                fcmAreaMap.put("fcmArea", text_input_layout_notifi_thana.getText().toString());
                db.collection("driver").document(driverID).update(fcmAreaMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Area Successfully Updated!", Toast.LENGTH_SHORT).show();
                    }
                });

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

        //spinner_notificationArea = view.findViewById(R.id.spinner_notificationArea);
        notificationAreaAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                addressList);
        addressAdapter = new AutoCompleteCustomAdapter(getContext(), addressList);
        text_input_layout_notifi_thana.setAdapter(addressAdapter);
        //spinner_notificationArea.setAdapter(notificationAreaAdapter);
        return  view;
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
    public void getAddresses(){
        //loadDistrict...
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(loadJSONFromAsset("address.json"));
            for (int i = 0; i < jsonArray.length(); i++) {
                String district =  jsonArray.getJSONObject(i).get("district").toString();
                String name =  jsonArray.getJSONObject(i).get("name").toString();
                String thana =  jsonArray.getJSONObject(i).get("thana").toString();

               // Address address = new Address(name, thana, district);
                addressList.add(name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                    driver.setId(task.getResult().getId());
                    driverID = task.getResult().getId();
                    if (driver==null){
                        Toast.makeText(getContext(), "Driver Not Found! "+id, Toast.LENGTH_SHORT).show();
                    }else{
                        //todo...
                        Picasso.get().load(driver.getImageUrl()).into(imgv_profile_image);
                        txtv_name.setText(driver.getName());
                        txtv_email.setText(driver.getEmail());
                        txtv_phone_number.setText(driver.getPhoneNumber());
                        txtv_due.setText("Due: "+driver.getDue());
                        if (driver.getFcmArea()!=null && driver.getFcmArea().length()>3){
                            text_input_layout_notifi_thana.setText(driver.getFcmArea());
                            //spinner_notificationArea.setSelection(notificationAreaAdapter.getPosition(driver.getFcmArea()));
                        }

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