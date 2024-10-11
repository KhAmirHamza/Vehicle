package com.tripbd.customer.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tripbd.customer.LanguageManager;
import com.tripbd.customer.R;
import com.tripbd.customer.model.Trip;
import com.tripbd.customer.view.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UpdateTripInfoActivity extends AppCompatActivity {

    TextInputLayout text_input_layout_full_loading_address, text_input_layout_full_unloading_address,
            text_input_layout_loading_landmark,text_input_layout_unloading_landmark,
            text_input_layout_stop_point_address, text_input_layout_stopPointPerson_name,
            text_input_layout_stopPointPerson_mobile_number,
            text_input_layout_loadingAlternative_person_number, text_input_layout_loadingArea,
            text_input_layout_unloading_area, text_input_layout_unloading_personName,
            text_input_layout_unloading_mobileNumber;

    MaterialCardView cv_stop_point;
    MaterialButton btnUpdateTrip;
    Trip trip;
    String selectedLoadingArea, selectedUnloadingArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_trip_info);

        cv_stop_point = findViewById(R.id.cv_stop_point);
        btnUpdateTrip = findViewById(R.id.btnUpdateTrip);

        text_input_layout_full_loading_address = findViewById(R.id.text_input_layout_full_loading_address);
        text_input_layout_full_unloading_address = findViewById(R.id.text_input_layout_full_unloading_address);
        text_input_layout_loading_landmark = findViewById(R.id.text_input_layout_loading_landmark);
        text_input_layout_unloading_landmark = findViewById(R.id.text_input_layout_unloading_landmark);
        text_input_layout_stop_point_address = findViewById(R.id.text_input_layout_stop_point_address);
        text_input_layout_stopPointPerson_name = findViewById(R.id.text_input_layout_stopPointPerson_name);
        text_input_layout_stopPointPerson_mobile_number = findViewById(R.id.text_input_layout_stopPointPerson_mobile_number);
        text_input_layout_loadingAlternative_person_number = findViewById(R.id.text_input_layout_loadingAlternative_person_number);
        text_input_layout_loadingArea = findViewById(R.id.text_input_layout_loadingArea);
        text_input_layout_unloading_area = findViewById(R.id.text_input_layout_unloading_area);
        text_input_layout_unloading_personName = findViewById(R.id.text_input_layout_unloading_personName);
        text_input_layout_unloading_mobileNumber = findViewById(R.id.text_input_layout_unloading_mobileNumber);




        trip = (Trip) getIntent().getSerializableExtra("trip");
        setVehicleUiBehaviour(trip);
        Toast.makeText(this, trip.getVehicle().getType(), Toast.LENGTH_SHORT).show();
        TextView tv_languagewritingalert = findViewById(R.id.tv_languagewritingalert);
        LanguageManager languageManager =  new LanguageManager(UpdateTripInfoActivity.this);
        if (languageManager.getLanguage().equalsIgnoreCase("bn")){
            selectedLoadingArea =getString(R.string.write_area_within_above_chosen_place) + " "+ trip.getLoadingUpazilaThana()+ " "+" তে" ;
            selectedUnloadingArea =getString(R.string.write_area_within_above_chosen_place) + " "+ trip.getUnloadingUpazilaThana()+ " "+" তে" ;
            tv_languagewritingalert.setVisibility(View.VISIBLE);
        }else{
            selectedLoadingArea = getString(R.string.write_area_within_above_chosen_place) + " "+ trip.getLoadingUpazilaThana() ;
            selectedUnloadingArea = getString(R.string.write_area_within_above_chosen_place)+ " "+trip.getUnloadingUpazilaThana();
            tv_languagewritingalert.setVisibility(View.GONE);
        }
        text_input_layout_unloading_area.setHint(selectedUnloadingArea);
        text_input_layout_loadingArea.setHint(selectedLoadingArea);


        TextView tv_loading_header = findViewById(R.id.tv_loading_header);
        TextView tv_unloading_header = findViewById(R.id.tv_unloading_header);

        if(trip.getVehicle().getType().equalsIgnoreCase("Pickup")||
                        trip.getVehicle().getType().equalsIgnoreCase("Truck")||
                        trip.getVehicle().getType().equalsIgnoreCase("Trailer")){
            tv_loading_header.setText(R.string.detailsInformationonloadlocation);
            tv_unloading_header.setText(R.string.detailsInformationonunloadLocation);
        }else{

            tv_loading_header.setText(R.string.detailinfoontripstartlocation);
            tv_unloading_header.setText(R.string.detailinfoontripEndlocation);
        }








        btnUpdateTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loadingFullAddr = text_input_layout_full_loading_address.getEditText().getText().toString();
                String unloadingFullAddr = text_input_layout_full_unloading_address.getEditText().getText().toString();
                String loadingLandmark = text_input_layout_loading_landmark.getEditText().getText().toString();
                String unloadingLandmark = text_input_layout_unloading_landmark.getEditText().getText().toString();
                String stopAddress = "none ";
                //String stopAddress = text_input_layout_stop_point_address.getEditText().getText().toString().trim();
                String stopPointPersonName = text_input_layout_stopPointPerson_name.getEditText().getText().toString().trim();
                String stopPersonPhoneNumber = text_input_layout_stopPointPerson_mobile_number.getEditText().getText().toString().trim();
                String loadingAlternative_person_number = text_input_layout_loadingAlternative_person_number.getEditText().getText().toString().trim();
                String loadingArea = text_input_layout_loadingArea.getEditText().getText().toString().trim();
                String unloadingArea = text_input_layout_unloading_area.getEditText().getText().toString().trim();
                String unloading_personName = text_input_layout_unloading_personName.getEditText().getText().toString().trim();
                String unloading_mobileNumber = text_input_layout_unloading_mobileNumber.getEditText().getText().toString().trim();

                if (loadingFullAddr.isEmpty()){
                    text_input_layout_full_loading_address.setError("Enter Full Address of Loading Point");
                    text_input_layout_full_loading_address.requestFocus();
                  //  return;
                }else if (unloadingFullAddr.isEmpty()){
                    text_input_layout_full_unloading_address.setError("Enter Full Address of Unloading Point");
                    text_input_layout_full_unloading_address.requestFocus();
                  //  return;
                }else if (loadingLandmark.isEmpty()){
                    text_input_layout_loading_landmark.setError("Enter Landmark of Loading Point");
                    text_input_layout_loading_landmark.requestFocus();
                  //  return;
                }else if (unloadingLandmark.isEmpty()){
                    text_input_layout_unloading_landmark.setError("Enter Landmark of Unloading Point");
                    text_input_layout_unloading_landmark.requestFocus();

                }else if (stopPointPersonName.isEmpty()){
                    text_input_layout_stopPointPerson_name.setError("Enter Person Name");
                    text_input_layout_stopPointPerson_name.requestFocus();
                }else if (stopPersonPhoneNumber.isEmpty()){
                    text_input_layout_stopPointPerson_mobile_number.setError("Enter Person Mobile Number");
                    text_input_layout_stopPointPerson_mobile_number.requestFocus();
                }else if (loadingAlternative_person_number.isEmpty()){
                    text_input_layout_loadingAlternative_person_number.setError("Enter Loading Alt. Person Number");
                    text_input_layout_loadingAlternative_person_number.requestFocus();
                } else if (unloading_personName.isEmpty()){
                    text_input_layout_unloading_personName.setError("Enter Unloading person Name");
                    text_input_layout_unloading_personName.requestFocus();
                }else if (unloading_mobileNumber.isEmpty()){
                    text_input_layout_unloading_mobileNumber.setError("Enter Unloading person mobile number");
                    text_input_layout_unloading_mobileNumber.requestFocus();
                }else if (unloadingArea.isEmpty()){
                    text_input_layout_unloading_area.setError("Enter Unloading Area");
                    text_input_layout_unloading_area.requestFocus();
                }else if (loadingArea.isEmpty()){
                    text_input_layout_loadingArea.setError("Enter loading Area");
                    text_input_layout_loadingArea.requestFocus();
                }
         /*       String actualFullAddress;
                if (loadingFullAddr.contains("For villagers-") && loadingFullAddr.contains("For town/city-")) {
                    if (loadingFullAddr.contains("House Name/Point")
                    &&(
                            loadingFullAddr.contains("House no:") ||
                                    loadingFullAddr.contains("Road:")

                            )){ //forVillagers
                        String tempFullAddrs = loadingFullAddr;
                        actualFullAddress = tempFullAddrs.substring(
                                tempFullAddrs.indexOf("For villagers-")+"For villagers-".length(),
                                tempFullAddrs.indexOf("For town/city-"));

                    }else if(){ //forTownCity

                    }
                }*/



                Trip updatedTrip = new Trip(trip.getId(), trip.getCustomer(), trip.getDriver(), trip.getVehicle(),
                        trip.getCreatedAtMills(), trip.getLoadingUpazilaThana(), loadingFullAddr,loadingArea, loadingLandmark, trip.getLoadingDate(), trip.getLoadingTime(),
                        loadingAlternative_person_number,
                        trip.getUnloadingUpazilaThana(), unloadingFullAddr,unloadingArea, unloadingLandmark, unloading_personName,unloading_mobileNumber,
                        trip.getDescription(), trip.getProductCategory(),
                        trip.getUpDownTrip(),trip.getContainAnimal(),trip.getFragile(), trip.getPerishable(), trip.getLaborNeeded(), trip.getRentalPrice(), trip.getFinalPayMethod(),
                        trip.getLengthAlert(), trip.getWeightAlert(), trip.getStatus(),trip.getBids(),
                        stopAddress, stopPointPersonName,trip.getAdvancePay(), stopPersonPhoneNumber, trip.getReturnAddress(),trip.getReturnDate(), trip.getReturnTime());

                updateTripInfo(updatedTrip);
            }
        });
    }

    void updateTripInfo(Trip updatedTrip){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference tripRef = db.collection("trip").document(updatedTrip.getId());
        tripRef.set(updatedTrip).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Trip Info Successfully Updated", Toast.LENGTH_SHORT).show();
                    //todo... notification...

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setVehicleUiBehaviour(Trip trip){
        String vType = trip.getVehicle().getType();
        if (vType.equalsIgnoreCase("Pickup")
                ||vType.equalsIgnoreCase("Truck")
                ||vType.equalsIgnoreCase("Trailer")){
            text_input_layout_loading_landmark.setHint(R.string.nearby_school_mosque_others);
            text_input_layout_unloading_landmark.setHint(R.string.nearby_school_mosque_others);
            text_input_layout_unloading_personName.setVisibility(View.VISIBLE);
            text_input_layout_unloading_mobileNumber.setVisibility(View.VISIBLE);
        }else{
            text_input_layout_loading_landmark.setHint(R.string.nearby_school_mosque_others);
            text_input_layout_unloading_landmark.setHint(R.string.nearby_school_mosque_others);
            text_input_layout_unloading_personName.setVisibility(View.GONE);
            text_input_layout_unloading_mobileNumber.setVisibility(View.GONE);
        }

    }

}