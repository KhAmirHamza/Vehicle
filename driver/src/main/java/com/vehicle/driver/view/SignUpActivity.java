package com.vehicle.driver.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.vehicle.driver.R;

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