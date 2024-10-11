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

public class DialogWelcome extends Dialog {
    TextView tv_welcome;
    String text;
    MaterialButton btnOkay;
    Intent intent;
    Activity context;
    public DialogWelcome(@NonNull Activity context, String welcomeText, Intent intent) {
        super(context);
        this.context = context;
        this.text = welcomeText;
        this.intent = intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_welcome);
        tv_welcome = findViewById(R.id.tv_welcome);
        btnOkay = findViewById(R.id.btnOkay);
        tv_welcome.setText(text);
        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
                context.startActivity(intent);
                context.finish();
            }
        });
    }
}
