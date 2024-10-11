package com.tripbd.driver.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.tripbd.driver.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DialogLaw extends Dialog {
    MaterialButton btnOkay;
    TextView txtvLaw;
    Activity context;
    public DialogLaw(@NonNull Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_law);
        txtvLaw = findViewById(R.id.txtvLaw);
        txtvLaw.setText(getTermsString());
        btnOkay = findViewById(R.id.btnOkay);
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();

            }
        });
    }

    private String getTermsString() {
        StringBuilder termsString = new StringBuilder();
        BufferedReader reader;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("law.txt")));

            String str;
            while ((str = reader.readLine()) != null) {
                termsString.append(str);
            }

            reader.close();
            return termsString.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
