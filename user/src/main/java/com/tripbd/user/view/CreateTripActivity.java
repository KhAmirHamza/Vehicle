package com.tripbd.user.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tripbd.user.LanguageManager;
import com.tripbd.user.R;
import com.tripbd.user.adapter.AutoCompleteCustomAdapter;
import com.tripbd.user.model.Address;
import com.tripbd.user.model.UserModel;
import com.tripbd.user.model.Driver;
import com.tripbd.user.model.Trip;
import com.tripbd.user.model.Vehicle;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CreateTripActivity extends AppCompatActivity {
    private static final String TAG = "CreateTripActivity";
    RadioGroup radio_group;
    final Calendar myCalendar= Calendar.getInstance();
    MaterialButton btn_review;
    DatePickerDialog.OnDateSetListener date;
    DatePicker datePicker, datePickerReturnSchedule;
    NumberPicker numberPicker, numberPickerReturnSchedule;

    List<Address> addresses = new ArrayList<>();
    String[] addressNames;
    String variety = "", unloadingLandmark;
    AutoCompleteTextView autocomplete_loading_upazila, autocomplete_unloading_upazila;
    MaterialCardView cv_additional;

   /* TextInputLayout text_input_layout_full_loading_address, text_input_layout_full_unloading_address,
            text_input_layout_loading_landmark,text_input_layout_unloading_landmark, text_input_layout_product_description,
            text_input_layout_stop_point_address, text_input_layout_stopPointPerson_name,
            text_input_layout_stopPointPerson_mobile_number,text_input_layout_return_point_address,
            text_input_layout_loadingAlternative_person_number, text_input_layout_loadingArea,
            text_input_layout_unloading_area, text_input_layout_unloading_personName,
            text_input_layout_unloading_mobileNumber;*/

    TextInputLayout text_input_layout_product_description, text_input_layout_return_point_address;


    //CheckBox cbUpDownTrip;
    CheckBox cbContainAnimal, cbFragile, cbPerishable,cbLaborNeeded, cbWeightAlert,cbLengthAlert;


    Spinner spinner_vehicle, spinner_vehicleVariety, spinner_vehicle_size, spinner_vehicle_seat,
            spinner_product_category;
    TextView tv_vehicle_size, tv_vehicle_seat, tv_vehicle_variety_header, tv_loading_header,tv_unloading_header;

    String seat, size;
    DialogTripPreview dialogTripPreview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        spinner_vehicleVariety = findViewById(R.id.spinner_vehicleVariety);
        spinner_vehicle_size = findViewById(R.id.spinner_vehicle_size);
        spinner_vehicle_seat = findViewById(R.id.spinner_vehicle_seat);
        spinner_product_category = findViewById(R.id.spinner_product_category);

        tv_vehicle_size = findViewById(R.id.tv_vehicle_size);
        tv_vehicle_seat = findViewById(R.id.tv_vehicle_seat);
        tv_vehicle_variety_header = findViewById(R.id.tv_vehicle_variety_header);
        tv_loading_header = findViewById(R.id.tv_loading_header);
        tv_unloading_header = findViewById(R.id.tv_unloading_header);

        cv_additional = findViewById(R.id.cv_additional);

        //cbUpDownTrip = findViewById(R.id.cbUpDownTrip);
        cbFragile = findViewById(R.id.cbFragile);
        cbPerishable = findViewById(R.id.cbPerishable);
        cbContainAnimal = findViewById(R.id.cbContainAnimal);
        cbLaborNeeded = findViewById(R.id.cbLaborNeeded);
        cbWeightAlert = findViewById(R.id.cbWeightAlert);
        cbLengthAlert = findViewById(R.id.cbLengthAlert);

        btn_review = findViewById(R.id.btn_review);


        autocomplete_loading_upazila = findViewById(R.id.autocomplete_loading_upazila);
        autocomplete_unloading_upazila = findViewById(R.id.autocomplete_unloading_upazila);
        text_input_layout_product_description = findViewById(R.id.text_input_layout_product_description);
        text_input_layout_return_point_address = findViewById(R.id.text_input_layout_return_point_address);

/*
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
*/

        datePicker = findViewById(R.id.datePicker);
        datePickerReturnSchedule = findViewById(R.id.datePickerReturnSchedule);
        numberPicker = findViewById(R.id.numberPicker);
        numberPickerReturnSchedule = findViewById(R.id.numberPickerReturnSchedule);
        spinner_vehicle = findViewById(R.id.spinner_vehicle);



        ArrayAdapter<String> vehicleTypeAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.vehicle_type));
        spinner_vehicle.setAdapter(vehicleTypeAdapter);

        ArrayAdapter<String> vehicleVarietyAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.openCovered));
        spinner_vehicleVariety.setAdapter(vehicleVarietyAdapter);

        ArrayAdapter<String> vehicleSizeAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.pickupSize));
        spinner_vehicle_size.setAdapter(vehicleSizeAdapter);

        ArrayAdapter<String> vehicleSeatAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.privateCarSeat));
        spinner_vehicle_seat.setAdapter(vehicleSeatAdapter);
        ArrayAdapter<String> productCategory;

        LanguageManager languageManager =  new LanguageManager(getContext());
        if (languageManager.getLanguage().equalsIgnoreCase("bn")){
            productCategory = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1,
                    getResources().getStringArray(R.array.productCategory));
        }else{
            productCategory = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1,
                    getResources().getStringArray(R.array.productCategory));
        }
        spinner_product_category.setAdapter(productCategory);


        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                String myFormat="dd/MM/yy";
                SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
                //btn_schedule.setText(dateFormat.format(myCalendar.getTime()));

            }
        };

        //ArrayAdapter<String> scheduleAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.timeEng));
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(23);
        numberPicker.setDisplayedValues(getResources().getStringArray(R.array.timeEng));
        numberPickerReturnSchedule.setMinValue(0);
        numberPickerReturnSchedule.setMaxValue(23);
        numberPickerReturnSchedule.setDisplayedValues(getResources().getStringArray(R.array.timeEng));



        getAddresses();

        addressNames = new String[addresses.size()];

        for (int i = 0; i < addresses.size(); i++) {
            addressNames[i] = addresses.get(i).getName();
        }

        //Toast.makeText(getContext(), "Address Size: "+addresses.size(), Toast.LENGTH_SHORT).show();


        //ArrayAdapter<String> upazilaAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, addressNames);
        //ArrayAdapter<String> upazilaAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, addressNames);
        //AddressAdapter addressAdapter = new AddressAdapter(getContext(), addressNames);

        List<String> list = new ArrayList<>();
        for (String s : addressNames) {
            list.add(s);
        }
        AutoCompleteCustomAdapter addressAdapter = new AutoCompleteCustomAdapter(getContext(), list);
        //AutoCompleteAdapter addressAdapter = new AutoCompleteAdapter(getContext(), Arrays.asList(addressNames));
        autocomplete_loading_upazila.setAdapter(addressAdapter);
        autocomplete_unloading_upazila.setAdapter(addressAdapter);


        //todo...prepare trip...

        //Toast.makeText(getContext(), "Division Size: "+divisions.size(), Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
        String customerName = sharedPreferences.getString("USER_NAME","");
        String phoneNumber = sharedPreferences.getString("USER_PHONE_NUMBER","");;
        String customerPass = "Hidden";
        String customerEmail = sharedPreferences.getString("USER_EMAIL","");;
        String customerImageUrl = sharedPreferences.getString("USER_IMAGE_URL","");;
        String customerAddress = sharedPreferences.getString("USER_ADDRESS","");;
        UserModel UserModel = new UserModel(null, customerName,phoneNumber, customerPass, customerEmail,
                customerImageUrl, customerAddress);


        spinner_vehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                setVehicleUiBehaviour(position);
                tv_vehicle_variety_header.setVisibility(position==2?View.GONE:View.VISIBLE);
                spinner_vehicleVariety.setVisibility(position==2?View.GONE:View.VISIBLE);
                variety = position==2?"":variety;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dialogTripPreview = new DialogTripPreview(getActivity());
        dialogTripPreview.setCancelable(true);
        getDriversThenReviewTrip();
        WindowManager.LayoutParams lp = dialogTripPreview.getWindow().getAttributes();
        lp.dimAmount = 2.0f;
        dialogTripPreview.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loadingUpaz = autocomplete_loading_upazila.getText().toString().trim();
                String unloadingUpaz = autocomplete_unloading_upazila.getText().toString().trim();
                String loadingDate = datePicker.getDayOfMonth()+"/"+(datePicker.getMonth()+1)+"/"+datePicker.getYear();
                String returnDate = datePickerReturnSchedule.getDayOfMonth()+"/"+(datePickerReturnSchedule.getMonth()+1)+datePickerReturnSchedule.getYear();
                String loadingTime = numberPicker.getDisplayedValues()[numberPicker.getValue()];
                String returnTime = numberPickerReturnSchedule.getDisplayedValues()[numberPickerReturnSchedule.getValue()];
                String productDescription = text_input_layout_product_description.getEditText().getText().toString().trim();
                String returnAddress = text_input_layout_return_point_address.getEditText().getText().toString().trim();


                /*String loadingFullAddr = text_input_layout_full_loading_address.getEditText().getText().toString();
                String unloadingFullAddr = text_input_layout_full_unloading_address.getEditText().getText().toString();
                String loadingLandmark = text_input_layout_loading_landmark.getEditText().getText().toString();
                unloadingLandmark = text_input_layout_unloading_landmark.getEditText().getText().toString();
                String stopAddress = "none ";
                //String stopAddress = text_input_layout_stop_point_address.getEditText().getText().toString().trim();
                String stopPointPersonName = text_input_layout_stopPointPerson_name.getEditText().getText().toString().trim();
                String stopPersonPhoneNumber = text_input_layout_stopPointPerson_mobile_number.getEditText().getText().toString().trim();
                String loadingAlternative_person_number = text_input_layout_loadingAlternative_person_number.getEditText().getText().toString().trim();
                String loadingArea = text_input_layout_loadingArea.getEditText().getText().toString().trim();
                String unloadingArea = text_input_layout_unloading_area.getEditText().getText().toString().trim();
                String unloading_personName = text_input_layout_unloading_personName.getEditText().getText().toString().trim();
                String unloading_mobileNumber = text_input_layout_unloading_mobileNumber.getEditText().getText().toString().trim();
*/
                //int upDownTrip = cbUpDownTrip.isChecked()?1:0;
                int upDownTrip = 0;
                int containAnimal = cbContainAnimal.isChecked()?1:0;
                int fragile = cbFragile.isChecked()?1:0;
                int perishable = cbPerishable.isChecked()?1:0;
                int laborNeeded = cbLaborNeeded.isChecked()?1:0;
                int lengthAlert = cbLengthAlert.isChecked()?1:0;
                int weightAlert = cbWeightAlert.isChecked()?1:0;
                int rentalPrice = 0;
                String paymentMethod = "None";


                size = spinner_vehicle_size.getVisibility()==View.VISIBLE ?
                        spinner_vehicle_size.getSelectedItem().toString().trim() : "0";
                seat = spinner_vehicle_seat.getVisibility()==View.VISIBLE ?
                        spinner_vehicle_seat.getSelectedItem().toString().trim() : "0";
                variety = spinner_vehicleVariety.getSelectedItem().toString();
                String type = spinner_vehicle.getSelectedItem().toString();
                String productCategory = spinner_product_category.getSelectedItem().toString();


                if (!isStringContainsInArray(loadingUpaz, addressNames)){
                    autocomplete_loading_upazila.setError(getString(R.string.sorryhoumissedsomeinfogivealltheinfo));
                    autocomplete_loading_upazila.requestFocus();
                    return;
                }else if (!isStringContainsInArray(unloadingUpaz, addressNames)){
                    autocomplete_unloading_upazila.setError(getString(R.string.sorryhoumissedsomeinfogivealltheinfo));
                    autocomplete_unloading_upazila.requestFocus();
                    return;
                }
                /*else if (loadingFullAddr.isEmpty()){
                    text_input_layout_full_loading_address.setError("Enter Full Address of Loading Point");
                    text_input_layout_full_loading_address.requestFocus();
                }else if (unloadingFullAddr.isEmpty()){
                    text_input_layout_full_unloading_address.setError("Enter Full Address of Unloading Point");
                    text_input_layout_full_unloading_address.requestFocus();
                }else if (loadingLandmark.isEmpty()){
                    text_input_layout_loading_landmark.setError("Enter Landmark of Loading Point");
                    text_input_layout_loading_landmark.requestFocus();
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

                */

                else if (productDescription.isEmpty() &&
                        (
                                type.equalsIgnoreCase("Pickup")||
                                        type.equalsIgnoreCase("Truck")||
                                        type.equalsIgnoreCase("Trailer")
                                )){
                    text_input_layout_product_description.setError(getString(R.string.sorryhoumissedsomeinfogivealltheinfo));
                    text_input_layout_product_description.requestFocus();
                    return;
                }/*else if (stopAddress.isEmpty()){
                    text_input_layout_stop_point_address.setError("Enter Stop Point Address");
                    text_input_layout_stop_point_address.requestFocus();
                }*/else if (returnAddress.isEmpty()){
                    //text_input_layout_return_point_address.setError("Enter Return Point Address");
                   // text_input_layout_return_point_address.requestFocus();
                    //return;
                }

                Vehicle vehicle = new Vehicle(null, "", type, variety, seat,
                        size, "","", "", "","",
                        "","", "");



                /*Trip trip = new Trip(null, customer, null, vehicle,
                        System.currentTimeMillis(), loadingUpaz, loadingFullAddr,loadingArea, loadingLandmark, loadingDate, loadingTime,
                        loadingAlternative_person_number,
                        unloadingUpaz, unloadingFullAddr,unloadingArea, unloadingLandmark, unloading_personName,unloading_mobileNumber,
                        productDescription, productCategory,
                        upDownTrip,containAnimal,fragile,perishable,laborNeeded, rentalPrice, paymentMethod,
                        lengthAlert, weightAlert, "Bidding", (List) new ArrayList<>(),
                        stopAddress, stopPointPersonName,0, stopPersonPhoneNumber, returnAddress,returnDate, returnTime);
                */

                Trip trip = new Trip(null, UserModel, null, vehicle, System.currentTimeMillis(), loadingUpaz,
                        "","", "", loadingDate, loadingTime, "",
                        unloadingUpaz, "","", "", "","",
                        productDescription, productCategory, upDownTrip,containAnimal,fragile,perishable,laborNeeded, rentalPrice, paymentMethod,
                        lengthAlert, weightAlert, "Bidding", (List) new ArrayList<>(),
                        "", "",0, "", returnAddress,returnDate, returnTime);



                // dialogTripPreview.getWindow().getDecorView().setBackgroundResource(android.R.color.black);
                //dialogTripPreview.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                //dialogTripPreview.getWindow().setDimAmount(0.7f);

                dialogTripPreview.setTrip(trip);
                dialogTripPreview.show();
            }
        });
    }

    void getDriversThenReviewTrip(){
        List<Driver> drivers = new ArrayList<>();
         FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("driver")
                //.whereEqualTo("fcmArea", trip.getLoadingUpazilaThana())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w(TAG, "Listen failed: " + error.getMessage());

                            return;
                        }
                        if (value == null || value.getDocuments().size() < 1) {
                            //progressbar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "No Tokens!", Toast.LENGTH_SHORT).show();
                        } else {
                            for (QueryDocumentSnapshot doc : Objects.<QuerySnapshot>requireNonNull(value)) {
                                Driver driver = doc.toObject(Driver.class);
                                if (driver.getName() != null) {
                                    //progressbar.setVisibility(View.GONE);
                                    drivers.add(driver);
                                    //Toast.makeText(getContext(), driver.getFcmToken(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "No Driver Found", Toast.LENGTH_SHORT).show();
                                    //progressbar.setVisibility(View.GONE);
                                }
                            }
                           // dialogTripPreview = new DialogTripPreview(getActivity());
                           // dialogTripPreview.setCancelable(true);
                            dialogTripPreview.setDrivers(drivers);
                           // Log.d(TAG, "onEvent: Driver size: "+dialogTripPreview.getDrivers().size());
                        }
                    }
                });
    }

    private void setVehicleUiBehaviour(int position){
        ArrayAdapter<String> vehicleSeatAdapter = null, vehicleSizeAdapter = null;
        //tv_vehicle_variety_header.setVisibility(View.VISIBLE);
        //spinner_vehicleVariety.setVisibility(View.VISIBLE);
        if(position==0){
            vehicleSizeAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.pickupSize));
        }else if(position==1){
            vehicleSizeAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,
                    getResources().getStringArray(R.array.truckSize));
        }else if(position==2){
            vehicleSizeAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,
                    getResources().getStringArray(R.array.trailerSize));
        }else if(position==3){
            vehicleSeatAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.privateCarSeat));
        }else if(position==4){
            vehicleSeatAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.microBusSeat));
        }else if(position==5){
            vehicleSeatAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.tourBusSeat));
            //tv_vehicle_variety_header.setVisibility(View.GONE);
            // spinner_vehicleVariety.setVisibility(View.GONE);
        }

        if (position==0||position==1||position==2){
            tv_vehicle_seat.setVisibility(View.GONE);
            spinner_vehicle_seat.setVisibility(View.GONE);
            tv_vehicle_size.setVisibility(View.VISIBLE);
            spinner_vehicle_size.setVisibility(View.VISIBLE);
            seat = "0";
            ArrayAdapter<String> vehicleVarietyAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1,
                    getResources().getStringArray(R.array.openCovered));
            spinner_vehicleVariety.setAdapter(vehicleVarietyAdapter);
            spinner_vehicle_size.setAdapter(vehicleSizeAdapter);
            tv_loading_header.setText(R.string.informationonloadlocationmustenglish);
            tv_unloading_header.setText(R.string.informationonunloadLocationmustenglish);
            //text_input_layout_loading_landmark.setHint("Landmark of loading Site");
           // text_input_layout_unloading_landmark.setHint("Landmark of Unloading Site");

            /*text_input_layout_unloading_personName.setVisibility(View.VISIBLE);
            text_input_layout_unloading_mobileNumber.setVisibility(View.VISIBLE);
            text_input_layout_unloading_landmark.setVisibility(View.VISIBLE);*/

            cv_additional.setVisibility(View.VISIBLE);
        }else{
            unloadingLandmark = "";
            /*text_input_layout_unloading_landmark.setVisibility(View.GONE);
            text_input_layout_unloading_personName.setVisibility(View.GONE);
            text_input_layout_unloading_mobileNumber.setVisibility(View.GONE);*/

            tv_vehicle_seat.setVisibility(View.VISIBLE);
            spinner_vehicle_seat.setVisibility(View.VISIBLE);
            spinner_vehicle_size.setVisibility(View.GONE);
            tv_vehicle_size.setVisibility(View.GONE);
            size = "0";
            ArrayAdapter<String> vehicleVarietyAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1,
                    getResources().getStringArray(R.array.acNonAc));
            spinner_vehicleVariety.setAdapter(vehicleVarietyAdapter);

            spinner_vehicle_seat.setAdapter(vehicleSeatAdapter);
            tv_loading_header.setText(R.string.detailinfoontripstartlocationmustenglish);
            tv_unloading_header.setText(R.string.detailinfoontripEndlocationmustenglish);
            //text_input_layout_loading_landmark.setHint("Landmark of trip start Site");
            //text_input_layout_unloading_landmark.setHint("Landmark of trip end Site");

            cv_additional.setVisibility(View.GONE);

        }
    }


    private boolean isStringContainsInArray(String stringToCheck, String[] addressNames){
        boolean result = false;
        for (String name: addressNames) {
            if (name.equalsIgnoreCase(stringToCheck)) result = true;
        }
        return result;
    }

    public List<Address> getAddresses(){
        //loadDistrict...
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(loadJSONFromAsset("address.json"));
            for (int i = 0; i < jsonArray.length(); i++) {
                String district =  jsonArray.getJSONObject(i).get("district").toString();
                String name =  jsonArray.getJSONObject(i).get("name").toString();
                String thana =  jsonArray.getJSONObject(i).get("thana").toString();

                Address address = new Address(name, thana, district);
                addresses.add(address);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    public String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }
    Context getContext(){
        return CreateTripActivity.this;
    }
    Activity getActivity(){
        return CreateTripActivity.this;
    }

}