package com.vehicle.driver.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vehicle.driver.R;
import com.vehicle.driver.model.Driver;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SignUpActivity";
    TextInputLayout text_input_layout_country_code, text_input_layout_phone_number;
    MaterialButton btn_sign_up,btn_goto_sign_in;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressbar = findViewById(R.id.progressbar);

        text_input_layout_country_code = findViewById(R.id.text_input_layout_countryCode);
        text_input_layout_phone_number = findViewById(R.id.text_input_layout_phone_number);

        btn_sign_up = findViewById(R.id.btn_sign_up);
        btn_goto_sign_in = findViewById(R.id.btn_goto_sign_in);


        btn_sign_up.setOnClickListener(this);
        btn_goto_sign_in.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId()==text_input_layout_phone_number.getId() || v.getId()==text_input_layout_country_code.getId()){
            text_input_layout_phone_number.getEditText().setCursorVisible(true);
        }
        else if (v.getId()==btn_sign_up.getId()){
            String countryCode = text_input_layout_country_code.getEditText().getText().toString();
            if (countryCode.charAt(0)=='+') countryCode = countryCode.substring(1);
            //Toast.makeText(this,countryCode, Toast.LENGTH_SHORT).show();
            String phoneNumber = countryCode+text_input_layout_phone_number.getEditText().getText().toString();

            if (!text_input_layout_phone_number.getEditText().getText().toString().isEmpty()){
                progressbar.setVisibility(View.VISIBLE);
                //todo... verify Registration

                Log.d(TAG, "onResponse: phone_number: "+phoneNumber);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("driver").addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        assert value != null;
                        int user = 0;
                        for (QueryDocumentSnapshot doc : Objects.<QuerySnapshot>requireNonNull(value)){
                            if (doc.get("name") != null) {
                                Driver driver = doc.toObject(Driver.class);
                                if (driver.getPhoneNumber().equalsIgnoreCase(phoneNumber)){
                                    user = 1;
                                }
                            }
                        }
                        progressbar.setVisibility(View.GONE);
                        if (user==1) {
                            Toast.makeText(SignUpActivity.this, "This number is already used in other account, Try another...", Toast.LENGTH_SHORT).show();
                        }else{
                              startActivity(new Intent(SignUpActivity.this, VerificationActivity.class)
                                      .putExtra("phoneNumber", phoneNumber));
                              finish();
                        }

                    }
                });

            }
            else {
                Toast.makeText(this, "Please insert valid phone number", Toast.LENGTH_SHORT).show();
            }

        } else if (v.getId() == R.id.btn_goto_sign_in) {
            startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
            // finish();
        }
    }

}