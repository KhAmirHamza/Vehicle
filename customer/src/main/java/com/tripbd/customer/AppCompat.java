package com.tripbd.customer;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tripbd.customer.LanguageManager;

public class AppCompat extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LanguageManager languageManager = new LanguageManager(this);
        languageManager.updateResources(languageManager.getLanguage());
    }
}
