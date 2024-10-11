package com.tripbd.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    final Fragment tripFragment = new TripFragment();
    final Fragment driverFragment = new DriverFragment();
    Fragment activeFragment = tripFragment;
    FragmentManager fragmentManager;
    BottomNavigationView btm_nav;
    FrameLayout content_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        content_layout = (FrameLayout) findViewById(R.id.content_layout);
        btm_nav = (BottomNavigationView) findViewById(R.id.btm_nav);
        fragmentManager = getSupportFragmentManager();


        btm_nav.setOnItemSelectedListener(getBottomNavigationSelectedListener());
        fragmentManager.beginTransaction().add(R.id.content_layout, driverFragment, "2").hide(driverFragment).commit();
        fragmentManager.beginTransaction().add(R.id.content_layout, tripFragment, "1").commit();

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.detach(activeFragment);
        ft.attach(activeFragment);
        ft.commit();
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
        int itemId = menuItem.getItemId();/*case R.id.home:
                fragment = homeFragment;
                break;*/
        if (itemId == R.id.trip) {
            fragment = tripFragment;
        } else if (itemId == R.id.driver) {
            fragment = driverFragment;
        } else {
            fragment = tripFragment;
        }

        fragmentManager.beginTransaction().hide(activeFragment).show(fragment).commit();
        //Toast.makeText(this, "Active: " + this.activeFragment.getTag() + "\nSelected: " + fragment.getTag(), Toast.LENGTH_SHORT).show();
        activeFragment = fragment;
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
    }


}