package com.vehicle.customer.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.vehicle.customer.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    final Fragment homeFragment = new HomeFragment();
    final Fragment tripFragment = new TripFragment();
    final Fragment profileFragment = new ProfileFragment();
    Fragment activeFragment = homeFragment;

    FragmentManager fragmentManager;
    BottomNavigationView btm_nav;
    FrameLayout content_layout;
    SwipeRefreshLayout swiperefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content_layout = (FrameLayout) findViewById(R.id.content_layout);
        btm_nav = (BottomNavigationView) findViewById(R.id.btm_nav);
        fragmentManager = getSupportFragmentManager();


        btm_nav.setOnItemSelectedListener(getBottomNavigationSelectedListener());
        fragmentManager.beginTransaction().add(R.id.content_layout, profileFragment, "3").hide(profileFragment).commit();
        fragmentManager.beginTransaction().add(R.id.content_layout, tripFragment, "2").hide(tripFragment).commit();
        fragmentManager.beginTransaction().add(R.id.content_layout, homeFragment, "1").commit();

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swiperefresh = swipeRefreshLayout;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.detach(activeFragment);
                ft.attach(activeFragment);
                ft.commit();
                swiperefresh.setRefreshing(false);
            }
        });

        // startActivity(new Intent(MainActivity.this, SignUpActivity.class));
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
        switch (menuItem.getItemId()) {
            case R.id.home:
                fragment = homeFragment;
                break;
            case R.id.trip:
                fragment = tripFragment;
                break;
            case R.id.profile:
                fragment = profileFragment;
                break;
            default:
                fragment = homeFragment;
                break;
        }

        fragmentManager.beginTransaction().hide(activeFragment).show(fragment).commit();
        //Toast.makeText(this, "Active: " + this.activeFragment.getTag() + "\nSelected: " + fragment.getTag(), Toast.LENGTH_SHORT).show();
        activeFragment = fragment;
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
    }



}