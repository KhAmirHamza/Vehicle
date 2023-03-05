package com.vehicle.customer.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vehicle.customer.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.vehicle.customer.model.Customer;
import com.vehicle.customer.utils.SystemUI;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";

    TextInputLayout text_input_layout_country_code, textInputLayout_phone_number, textInputLayout_password;
    MaterialButton btn_sign_in, btn_goto_sign_up;
    MaterialToolbar signin_toolbar;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        progressbar = findViewById(R.id.progressbar);

        signin_toolbar = findViewById(R.id.signin_toolbar);
        signin_toolbar = findViewById(R.id.signin_toolbar);
        setSupportActionBar(signin_toolbar);
        setTitle("Sign In");

        text_input_layout_country_code = findViewById(R.id.text_input_layout_country_code);
        textInputLayout_phone_number = findViewById(R.id.text_input_layout_phone_number);
        textInputLayout_password = findViewById(R.id.textInputLayout_password);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_goto_sign_up = findViewById(R.id.btn_goto_sign_up);


        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countryCode = text_input_layout_country_code.getEditText().getText().toString();
                if (countryCode.charAt(0)=='+') countryCode = countryCode.substring(1);
                String phoneNumber = textInputLayout_phone_number.getEditText().getText().toString();
                String password = textInputLayout_password.getEditText().getText().toString();

                if (countryCode.isEmpty()) {
                    text_input_layout_country_code.setError("Required.");
                    text_input_layout_country_code.requestFocus();
                    return;
                }else if (phoneNumber.isEmpty()) {
                    textInputLayout_phone_number.setError("Required.");
                    textInputLayout_phone_number.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    textInputLayout_password.setError("Required.");
                    textInputLayout_password.requestFocus();
                    return;
                }

                //todo...Log in...

                phoneNumber = countryCode + phoneNumber;
                progressbar .setVisibility(View.VISIBLE);
                Log.d(TAG, "onResponse: phone_number: "+phoneNumber);
                Log.d(TAG, "onClick: password: "+password);


                verifyLogIn(phoneNumber, password);
            }
        });

        btn_goto_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);

        if(!sharedPreferences.getString("CUSTOMER_PHONE_NUMBER", "null").equalsIgnoreCase("null") &&
                sharedPreferences.getString("CUSTOMER_PASSWORD", "null") != null) {
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
            //Toast.makeText(this, "User Sign in", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "user not sign in", Toast.LENGTH_SHORT).show();
        }
    }

    void verifyLogIn(String phoneNumber, String password){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("customer").addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w(TAG, "Listen failed: "+e.getMessage());
                    progressbar.setVisibility(View.GONE);
                    return;
                }
/*
                String countryCode = text_input_layout_country_code.getEditText().getText().toString();
                if (countryCode.charAt(0)=='+') countryCode = countryCode.substring(1);
                String phoneNumber = textInputLayout_phone_number.getEditText().getText().toString();
                String password = textInputLayout_password.getEditText().getText().toString();
                phoneNumber = countryCode + phoneNumber;*/

                //assert value != null;
                else if ( value==null || value.getDocuments().size()<1) {
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(SignInActivity.this, "Registration First...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                }
                for (QueryDocumentSnapshot doc : Objects.<QuerySnapshot>requireNonNull(value)) {
                    Customer customer = doc.toObject(Customer.class);
                    if (customer.getName() != null) {
                        progressbar.setVisibility(View.GONE);
                        customer.setId(doc.getId());
                        if (customer.getPhoneNumber().equalsIgnoreCase(phoneNumber) &&
                                customer.getPassword().equalsIgnoreCase(password)) {
                            SharedPreferences sharedPreferences = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
                            sharedPreferences.edit().putString("CUSTOMER_PHONE_NUMBER", phoneNumber).apply();
                            sharedPreferences.edit().putString("CUSTOMER_EMAIL", customer.getEmail()).apply();
                            sharedPreferences.edit().putString("CUSTOMER_NAME", customer.getName()).apply();
                            sharedPreferences.edit().putString("CUSTOMER_IMAGE_URL", customer.getImageUrl()).apply();
                            sharedPreferences.edit().putString("CUSTOMER_ID", customer.getId()).apply();
                            sharedPreferences.edit().putString("CUSTOMER_PASSWORD", password).apply();
                            sharedPreferences.edit().putString("CUSTOMER_ADDRESS", customer.getAddress()).apply();
                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SignInActivity.this, "Does not match Phone Number or Password", Toast.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(SignInActivity.this, "Something went wrong, try again!", Toast.LENGTH_SHORT).show();
                        progressbar.setVisibility(View.GONE);
                    }
                }
            }
        });

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            new SystemUI(SignInActivity.this).hideSystemUI();
        }
    }

}