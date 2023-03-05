package com.vehicle.driver.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.vehicle.driver.R;

public class DialogWelcome extends Dialog {
    TextView tv_welcome;
    String text;
    public DialogWelcome(@NonNull Context context, String welcomeText) {
        super(context);
        this.text = welcomeText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_welcome);
        tv_welcome = findViewById(R.id.tv_welcome);
        tv_welcome.setText(text);
    }
}
