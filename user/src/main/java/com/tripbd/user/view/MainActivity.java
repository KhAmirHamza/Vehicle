package com.tripbd.user.view;

import static com.tripbd.user.Constants.MAIN_URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;
import com.tripbd.user.AppCompat;
import com.tripbd.user.R;
import com.tripbd.user.model.Trip;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompat {
    private static final String TAG = "MainActivity";

    //final Fragment homeFragment = new HomeFragment();
    final Fragment tripFragment = new TripFragment();
    final Fragment profileFragment = new ProfileFragment();
    Fragment activeFragment = tripFragment;

   // ExtendedFloatingActionButton fabCreateTrip;

    FragmentManager fragmentManager;
    BottomNavigationView btm_nav;
    FrameLayout content_layout;
    SwipeRefreshLayout swiperefresh;
    String customerPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
        customerPhoneNumber = sharedPreferences.getString("USER_PHONE_NUMBER",null);

       // fabCreateTrip = findViewById(R.id.fabCreateTrip);
        //fabCreateTrip.setVisibility(View.GONE);
        content_layout = (FrameLayout) findViewById(R.id.content_layout);
        btm_nav = (BottomNavigationView) findViewById(R.id.btm_nav);
        fragmentManager = getSupportFragmentManager();


        btm_nav.setOnItemSelectedListener(getBottomNavigationSelectedListener());
        fragmentManager.beginTransaction().add(R.id.content_layout, profileFragment, "2").hide(profileFragment).commit();
        //fragmentManager.beginTransaction().add(R.id.content_layout, tripFragment, "2").hide(tripFragment).commit();
        fragmentManager.beginTransaction().add(R.id.content_layout, tripFragment, "1").commit();

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

        //getTrips();
        ApiClient.getInstance(MAIN_URL).activeServer().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                assert response.body() != null;
                Toast.makeText(MainActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_right, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.logout){

        }
        return super.onOptionsItemSelected(item);
    }

   /* boolean isTripRunning(){
        List<Trip> trips = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trip").addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                assert value != null;
                if (value.getDocuments().size()<1) {
                    Toast.makeText(getApplicationContext(), "No Trip Found!", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (QueryDocumentSnapshot doc : Objects.<QuerySnapshot>requireNonNull(value)){
                    Trip trip = doc.toObject(Trip.class);
                    if (trip.getCustomer().getPhoneNumber().equalsIgnoreCase(customerPhoneNumber)  &&
                            (trip.getStatus().equalsIgnoreCase("Bidding") ||
                             trip.getStatus().equalsIgnoreCase("Running"))){
                        trip.setId(doc.getId());
                        trips.add(trip);
                    }
                }

            }
        });
        Toast.makeText(this, trips.size(), Toast.LENGTH_SHORT).show();
        return trips.size() > 0;
    }*/

/*    void getTrips(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trip").addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
              //  fabCreateTrip.setVisibility(View.VISIBLE);
               *//* fabCreateTrip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        assert value != null;
                        if (value.getDocuments().size()<1) {
                            //Toast.makeText(getApplicationContext(), "No Trip Found!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), CreateTripActivity.class));
                        }else{
                            for (QueryDocumentSnapshot doc : Objects.<QuerySnapshot>requireNonNull(value))
                            // if (doc.get("date") != null)
                            {
                                Trip trip = doc.toObject(Trip.class);
                                //trip.setId(doc.getId());
                                //trips.add(trip);
                                Toast.makeText(MainActivity.this, customerPhoneNumber, Toast.LENGTH_SHORT).show();
                                if (trip.getCustomer().getPhoneNumber().equalsIgnoreCase(customerPhoneNumber)  &&
                                        (!trip.getStatus().equalsIgnoreCase("Complete") ||
                                                !trip.getStatus().equalsIgnoreCase("Cancel"))){
                                    trip.setId(doc.getId());
                                    trips.add(trip);
                                }
                            }
                            if (trips.size()>0){
                                Toast.makeText(MainActivity.this, "A Trip is Running! Create another after complete it.", Toast.LENGTH_LONG).show();
                            }else{

                                startActivity(new Intent(getApplicationContext(), CreateTripActivity.class));
                            }
                    }
                    }
                });*//*


            }
        });
    }*/




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
        } else if (itemId == R.id.profile) {
            fragment = profileFragment;
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