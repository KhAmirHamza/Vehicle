package com.tripbd.driver.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.tripbd.driver.AppCompat;
import com.tripbd.driver.R;

public class MainActivity extends AppCompat {
    private static final String TAG = "MainActivity";
    final Fragment allTripFragment = new AllTripFragment();
    final Fragment runningTripFragmentFragment = new RunningTripFragment();
    final Fragment profileFragment = new ProfileFragment();
    Fragment activeFragment = profileFragment;

    FragmentManager fragmentManager;
    BottomNavigationView btm_nav;
    FrameLayout content_layout;
    SwipeRefreshLayout swiperefresh;
    MyPushNotificationHandler myPushNotificationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content_layout = (FrameLayout) findViewById(R.id.content_layout);
        btm_nav = (BottomNavigationView) findViewById(R.id.btm_nav);
        fragmentManager = getSupportFragmentManager();

        btm_nav.setOnItemSelectedListener(getBottomNavigationSelectedListener());
        btm_nav.getMenu().getItem(2).setChecked(true);

        fragmentManager.beginTransaction().add(R.id.content_layout, allTripFragment, "1").hide(allTripFragment).commit();
        fragmentManager.beginTransaction().add(R.id.content_layout, runningTripFragmentFragment, "2").hide(runningTripFragmentFragment).commit();
        fragmentManager.beginTransaction().add(R.id.content_layout, profileFragment, "3").commit();

        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        //swiperefresh = swipeRefreshLayout;
        swiperefresh.setNestedScrollingEnabled(false);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.detach(activeFragment);
                ft.attach(activeFragment);
                ft.commit();
                swiperefresh.setRefreshing(false);
            }
        });

        myPushNotificationHandler = new MyPushNotificationHandler(MainActivity.this);
        myPushNotificationHandler.createChannel("ChannelId1","ChannelName");

        myPushNotificationHandler.subscribeTopic();
    }

    private NavigationBarView.OnItemSelectedListener getBottomNavigationSelectedListener () {
        return new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setFragmentToContentViewBottomNav(item);
                return true;
            }
        };

    };

    public void setFragmentToContentViewBottomNav(MenuItem menuItem) {
        Fragment fragment;
        //Toast.makeText(this, menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
        int itemId = menuItem.getItemId();
        if (itemId == R.id.all_trips) {
            fragment = allTripFragment;
        } else if (itemId == R.id.running_trip) {
            fragment = runningTripFragmentFragment;
        } else if (itemId == R.id.profile) {
            fragment = profileFragment;
        } else {
            fragment = profileFragment;
        }

        fragmentManager.beginTransaction().hide(activeFragment).show(fragment).commit();
        //Toast.makeText(this, "Active: " + this.activeFragment.getTag() + "\nSelected: " + fragment.getTag(), Toast.LENGTH_SHORT).show();
        activeFragment = fragment;
       //menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        menuItem.setChecked(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
        String driverID = sharedPreferences.getString("DRIVER_ID", "null");

        myPushNotificationHandler.getAndSaveToken(driverID);

    }
}