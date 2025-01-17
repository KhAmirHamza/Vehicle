package com.tripbd.customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageManager {
    private Context context;
    SharedPreferences sharedPreferences;
    public LanguageManager(Context context) {
        this.context = context;
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences("LANG",Context.MODE_PRIVATE);
    }

    public void updateResources(String code){
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        //https://youtu.be/cJeiGSzPyq0
        setLanguage(code);
    }

    public void setLanguage(String language){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", language);
        editor.apply();
    }
    public String getLanguage(){
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences("LANG",Context.MODE_PRIVATE);
        return sharedPreferences.getString("language", "en");
    }
}
