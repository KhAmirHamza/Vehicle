package com.vehicle.customer.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.vehicle.customer.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.vehicle.customer.utils.SystemUI;

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
                /*
                                SharedPreferences sharedPreferences = getSharedPreferences(AUTHENTICATION, Context.MODE_PRIVATE);
                                sharedPreferences.edit().putString(ID, id).apply();
                                sharedPreferences.edit().putString(NAME, name).apply();
                                sharedPreferences.edit().putString(PHONE_NUMBER, phone_number).apply();
                                sharedPreferences.edit().putString(PASSWORD, password ).apply();
                                */
                                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                finish();


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

//        if (FirebaseAuth.getInstance().getCurrentUser() != null
//                && sharedPreferences.getString("DELIVERYMAN_PHONE_NUMBER", "null").equalsIgnoreCase("null")) {
//            startActivity(new Intent(SignInActivity.this, ProfileSetUpActivity.class)
//                    .putExtra("phoneNumber", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()));
//            finish();
//            //Toast.makeText(this, "User Sign in", Toast.LENGTH_SHORT).show();
//        } else
        if(!sharedPreferences.getString("PHONE_NUMBER", "null").equalsIgnoreCase("null") &&
                sharedPreferences.getString("PHONE_NUMBER", "null") != null) {
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
            //Toast.makeText(this, "User Sign in", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this, "user not sign in", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            new SystemUI(SignInActivity.this).hideSystemUI();
        }
    }

}