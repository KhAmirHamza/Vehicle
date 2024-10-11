
package com.tripbd.user.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tripbd.user.LanguageManager;
import com.tripbd.user.R;
import com.tripbd.user.adapter.SplashScreenAdapter;

public class SplashScreenActivity extends AppCompatActivity {

    private LinearLayout dotsLayout;
    int introImageCount = 1;
    Button BtnStartNow,introImageSkip, BtnNextIntroImage;
    ViewPager introViewPager;
    SplashScreenAdapter splashScreenAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dotsLayout = findViewById(R.id.dotsContainer);
        BtnNextIntroImage = findViewById(R.id.introImageNextID);
        BtnStartNow = findViewById(R.id.BtnStartNow);
        introImageSkip = findViewById(R.id.introImageSkipID);

        introViewPager = findViewById(R.id.introViewPager);
        splashScreenAdapter = new SplashScreenAdapter(getApplicationContext());
        introViewPager.setAdapter(splashScreenAdapter);
        prepareDots(introViewPager.getCurrentItem());

        LanguageManager languageManager =  new LanguageManager(SplashScreenActivity.this);
        if (!languageManager.islLanguageInitiallySelected()){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(SplashScreenActivity.this);
            builder1.setTitle("ভাষা / Language");
            builder1.setMessage("আপনার ভাষা নির্বাচন করুন / Select your language\n");
            builder1.setCancelable(false);
            builder1.setPositiveButton(
                    "English",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            languageManager.updateResources("en");

                            dialog.cancel();
                            Intent currentIntent = getIntent();
                            finish();
                            startActivity(currentIntent);
                        }
                    });

            builder1.setNegativeButton(
                    "বাংলা",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            languageManager.updateResources("bn");
                            dialog.cancel();
                            Intent currentIntent = getIntent();
                            finish();
                            startActivity(currentIntent);
                        }
                    });

            builder1.show();
        }

        introViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                // Toast.makeText(IntroActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPageSelected(int position) {
                prepareDots(position);
                if (position==0){
                    introImageSkip.setVisibility(View.VISIBLE);
                    BtnNextIntroImage.setVisibility(View.VISIBLE);
                    BtnStartNow.setVisibility(View.GONE);
                } else if (position==1){
                    introImageSkip.setVisibility(View.VISIBLE);
                    BtnNextIntroImage.setVisibility(View.VISIBLE);
                    BtnStartNow.setVisibility(View.GONE);
                }else if (position==2){
                    introImageSkip.setVisibility(View.GONE);
                    BtnNextIntroImage.setVisibility(View.VISIBLE);
                    BtnStartNow.setVisibility(View.GONE);
                }else if (position==3){
                    introImageSkip.setVisibility(View.GONE);
                    BtnNextIntroImage.setVisibility(View.GONE);
                    BtnStartNow.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //introImageCount= introViewPager.getCurrentItem();
        BtnNextIntroImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (introImageCount==0){
                    introViewPager.setCurrentItem(0,true);
                    introImageCount=1;
//                    BtnStartNow.setVisibility(View.GONE);
//                    introImageSkip.setVisibility(View.VISIBLE);
//                    BtnNextIntroImage.setVisibility(View.VISIBLE);

                }else if (introImageCount==1){
                    introViewPager.setCurrentItem(1,true);
                    introImageCount=2;

//                    BtnStartNow.setVisibility(View.GONE);
//                    introImageSkip.setVisibility(View.VISIBLE);
//                    BtnNextIntroImage.setVisibility(View.VISIBLE);

                }else if (introImageCount==2){
                    introViewPager.setCurrentItem(2,true);
                    introImageCount=3;
//                    BtnStartNow.setVisibility(View.GONE);
//                    introImageSkip.setVisibility(View.VISIBLE);
//                    BtnNextIntroImage.setVisibility(View.VISIBLE);
                }
                else if (introImageCount==3){
                    introImageCount=1;
                    introViewPager.setCurrentItem(3,true);
                    BtnStartNow.setVisibility(View.VISIBLE);
//                    introImageSkip.setVisibility(View.GONE);
//                    BtnNextIntroImage.setVisibility(View.GONE);
                }
            }
        });

        BtnStartNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                finish();
            }
        });
        introImageSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                finish();
            }
        });

    }
    private void prepareDots(int currentSlidePosition){
        if (dotsLayout.getChildCount()>0)
            dotsLayout.removeAllViews();
        ImageView dots[] = new ImageView[4];


        for (int i=0;i<4;i++){
            dots[i] = new ImageView(getApplicationContext());
            //if (customPosition==dataList.size()){ customPosition = 0;}//---->check dot position...
            if (i==currentSlidePosition){
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.active_dot));}
            else {dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.inactive_dots));}
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(4,0,4,0);
            dotsLayout.addView(dots[i],layoutParams);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);

        if(!sharedPreferences.getString("USER_PHONE_NUMBER", "null").equalsIgnoreCase("null") &&
                sharedPreferences.getString("USER_PASSWORD", null) != null) {
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
            //Toast.makeText(this, "User Sign in", Toast.LENGTH_SHORT).show();
        }
    }

}
