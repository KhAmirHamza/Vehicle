package com.vehicle.customer.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.vehicle.customer.R;
import com.vehicle.customer.model.Customer;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    CircleImageView imgv_profile_image;
    TextView txtv_name, txtv_email,txtv_phone_number;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imgv_profile_image = view.findViewById(R.id.imgv_profile_image);
        txtv_name = view.findViewById(R.id.txtv_name);
        txtv_email = view.findViewById(R.id.txtv_email);
        txtv_phone_number = view.findViewById(R.id.txtv_phone_number);
        getCustomer();
        return  view;
    }

    public void getCustomer(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences sp = getActivity().getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
        String id = sp.getString("CUSTOMER_ID",null);
        //String notification = sp.getString("NOTIFICATION","None");
        if (id==null) {
            Toast.makeText(getContext(), "You are not sign in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), SignInActivity.class));
            getActivity().finish();
        }else{
            db.collection("customer").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Customer customer = task.getResult().toObject(Customer.class);
                    if (customer==null){
                        Toast.makeText(getContext(), "Customer Not Found! "+id, Toast.LENGTH_SHORT).show();
                    }else{
                        //todo...
                        Picasso.get().load(customer.getImageUrl()).into(imgv_profile_image);
                        txtv_name.setText(customer.getName());
                        txtv_email.setText(customer.getEmail());
                        txtv_phone_number.setText(customer.getPhoneNumber());
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