package com.vehicle.customer.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.vehicle.customer.R;
import com.vehicle.customer.adapter.AutoCompleteCustomAdapter;
import com.vehicle.customer.model.Address;
import com.vehicle.customer.model.Customer;
import com.vehicle.customer.model.Trip;
import com.vehicle.customer.model.Vehicle;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    RadioGroup radio_group;
    final Calendar myCalendar= Calendar.getInstance();
    MaterialButton btn_review;
    DatePickerDialog.OnDateSetListener date;
    DatePicker datePicker, datePickerReturnSchedule;
    NumberPicker numberPicker, numberPickerReturnSchedule;

    List<Address> addresses = new ArrayList<>();
    String[] addressNames;
    String variety = "";
    AutoCompleteTextView autocomplete_loading_upazila, autocomplete_unloading_upazila;
    MaterialCardView cv_additional;

    TextInputLayout text_input_layout_full_loading_address, text_input_layout_full_unloading_address,
            text_input_layout_loading_landmark,text_input_layout_unloading_landmark, text_input_layout_product_description,
             text_input_layout_stop_point_address, text_input_layout_stopPointPerson_name,
            text_input_layout_stopPointPerson_mobile_number,text_input_layout_return_point_address,
            text_input_layout_loadingAlternative_person_number, text_input_layout_loadingArea,
            text_input_layout_unloading_area, text_input_layout_unloading_personName,
            text_input_layout_unloading_mobileNumber;

    //CheckBox cbUpDownTrip;
    CheckBox cbContainAnimal, cbFragile, cbPerishable,cbLaborNeeded, cbWeightAlert,cbLengthAlert;


    Spinner spinner_vehicle, spinner_vehicleVariety, spinner_vehicle_size, spinner_vehicle_seat,
            spinner_product_category;
    TextView tv_vehicle_size, tv_vehicle_seat, tv_vehicle_variety_header, tv_loading_header,tv_unloading_header;

    String seat, size;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view     = inflater.inflate(R.layout.fragment_home, container, false);

        spinner_vehicleVariety = view.findViewById(R.id.spinner_vehicleVariety);
        spinner_vehicle_size = view.findViewById(R.id.spinner_vehicle_size);
        spinner_vehicle_seat = view.findViewById(R.id.spinner_vehicle_seat);
        spinner_product_category = view.findViewById(R.id.spinner_product_category);

        tv_vehicle_size = view.findViewById(R.id.tv_vehicle_size);
        tv_vehicle_seat = view.findViewById(R.id.tv_vehicle_seat);
        tv_vehicle_variety_header = view.findViewById(R.id.tv_vehicle_variety_header);
        tv_loading_header = view.findViewById(R.id.tv_loading_header);
        tv_unloading_header = view.findViewById(R.id.tv_unloading_header);

        cv_additional = view.findViewById(R.id.cv_additional);

        //cbUpDownTrip = view.findViewById(R.id.cbUpDownTrip);
        cbFragile = view.findViewById(R.id.cbFragile);
        cbPerishable = view.findViewById(R.id.cbPerishable);
        cbContainAnimal = view.findViewById(R.id.cbContainAnimal);
        cbLaborNeeded = view.findViewById(R.id.cbLaborNeeded);
        cbWeightAlert = view.findViewById(R.id.cbWeightAlert);
        cbLengthAlert = view.findViewById(R.id.cbLengthAlert);

        btn_review = view.findViewById(R.id.btn_review);


        autocomplete_loading_upazila = view.findViewById(R.id.autocomplete_loading_upazila);
        autocomplete_unloading_upazila = view.findViewById(R.id.autocomplete_unloading_upazila);
        text_input_layout_full_loading_address = view.findViewById(R.id.text_input_layout_full_loading_address);
        text_input_layout_full_unloading_address = view.findViewById(R.id.text_input_layout_full_unloading_address);
        text_input_layout_loading_landmark = view.findViewById(R.id.text_input_layout_loading_landmark);
        text_input_layout_unloading_landmark = view.findViewById(R.id.text_input_layout_unloading_landmark);
        text_input_layout_product_description = view.findViewById(R.id.text_input_layout_product_description);
        text_input_layout_stop_point_address = view.findViewById(R.id.text_input_layout_stop_point_address);
        text_input_layout_stopPointPerson_name = view.findViewById(R.id.text_input_layout_stopPointPerson_name);
        text_input_layout_stopPointPerson_mobile_number = view.findViewById(R.id.text_input_layout_stopPointPerson_mobile_number);
        text_input_layout_return_point_address = view.findViewById(R.id.text_input_layout_return_point_address);
        text_input_layout_loadingAlternative_person_number = view.findViewById(R.id.text_input_layout_loadingAlternative_person_number);
        text_input_layout_loadingArea = view.findViewById(R.id.text_input_layout_loadingArea);
        text_input_layout_unloading_area = view.findViewById(R.id.text_input_layout_unloading_area);
        text_input_layout_unloading_personName = view.findViewById(R.id.text_input_layout_unloading_personName);
        text_input_layout_unloading_mobileNumber = view.findViewById(R.id.text_input_layout_unloading_mobileNumber);

        datePicker = view.findViewById(R.id.datePicker);
        datePickerReturnSchedule = view.findViewById(R.id.datePickerReturnSchedule);
        numberPicker = view.findViewById(R.id.numberPicker);
        numberPickerReturnSchedule = view.findViewById(R.id.numberPickerReturnSchedule);
        spinner_vehicle = view.findViewById(R.id.spinner_vehicle);


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
        ArrayAdapter<String> productCategory = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.productCategory));
        spinner_product_category.setAdapter(productCategory);


   /*     radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radio_group.getCheckedRadioButtonId()==R.id.truck){
                    if (spinner_vehicle.getChildCount()>0)
                        spinner_vehicle.setAdapter(null);
                    spinner_vehicle.setAdapter(trucksAdapter);
                } else {
                    if (spinner_vehicle.getChildCount()>0)
                        spinner_vehicle.setAdapter(null);
                    spinner_vehicle.setAdapter(carsAdapter);
                }
            }
        });*/

  /*     btn_schedule.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                new DatePickerDialog(getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
           }
       });*/

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
        ArrayAdapter<String> upazilaAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, addressNames);
        //AddressAdapter addressAdapter = new AddressAdapter(getContext(), addressNames);

        List<String> list = new ArrayList<>();
        for (String s : addressNames) {
            list.add(s);
        }
        AutoCompleteCustomAdapter addressAdapter = new AutoCompleteCustomAdapter(getContext(), list);
        //AutoCompleteAdapter addressAdapter = new AutoCompleteAdapter(getContext(), Arrays.asList(addressNames));
        autocomplete_loading_upazila.setAdapter(addressAdapter);
        autocomplete_unloading_upazila.setAdapter(upazilaAdapter2);


        //todo...prepare trip...

        //Toast.makeText(getContext(), "Division Size: "+divisions.size(), Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
        String customerName = sharedPreferences.getString("CUSTOMER_NAME","");
        String phoneNumber = sharedPreferences.getString("CUSTOMER_PHONE_NUMBER","");;
        String customerPass = "Hidden";
        String customerEmail = sharedPreferences.getString("CUSTOMER_EMAIL","");;
        String customerImageUrl = sharedPreferences.getString("CUSTOMER_IMAGE_URL","");;
        String customerAddress = sharedPreferences.getString("CUSTOMER_ADDRESS","");;
        Customer customer = new Customer(null, customerName,phoneNumber, customerPass, customerEmail,
                customerImageUrl, customerAddress);


        spinner_vehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                setVehicleUiBehaviour(position);
/*                if(position==0){
                    tv_vehicle_seat.setVisibility(View.GONE);
                    spinner_vehicle_seat.setVisibility(View.GONE);
                    tv_vehicle_size.setVisibility(View.VISIBLE);
                    spinner_vehicle_size.setVisibility(View.VISIBLE);
                    seat = "0";
                    ArrayAdapter<String> vehicleSizeAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.pickupSize));
                    spinner_vehicle_size.setAdapter(vehicleSizeAdapter);
                    ArrayAdapter<String> vehicleVarietyAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.openCovered));
                    spinner_vehicleVariety.setAdapter(vehicleVarietyAdapter);

                }else if(position==1){
                    tv_vehicle_seat.setVisibility(View.GONE);
                    spinner_vehicle_seat.setVisibility(View.GONE);
                    tv_vehicle_size.setVisibility(View.VISIBLE);
                    spinner_vehicle_size.setVisibility(View.VISIBLE);
                    seat = "0";
                    ArrayAdapter<String> vehicleSizeAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.truckSize));
                    spinner_vehicle_size.setAdapter(vehicleSizeAdapter);
                    ArrayAdapter<String> vehicleVarietyAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.openCovered));
                    spinner_vehicleVariety.setAdapter(vehicleVarietyAdapter);

                }else if(position==2){
                    tv_vehicle_seat.setVisibility(View.GONE);
                    spinner_vehicle_seat.setVisibility(View.GONE);
                    tv_vehicle_size.setVisibility(View.VISIBLE);
                    spinner_vehicle_size.setVisibility(View.VISIBLE);
                    seat = "0";
                    ArrayAdapter<String> vehicleSizeAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.trailerSize));
                    spinner_vehicle_size.setAdapter(vehicleSizeAdapter);
                    ArrayAdapter<String> vehicleVarietyAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.openCovered));
                    spinner_vehicleVariety.setAdapter(vehicleVarietyAdapter);


                }else if(position==3){
                    tv_vehicle_seat.setVisibility(View.VISIBLE);
                    spinner_vehicle_seat.setVisibility(View.VISIBLE);
                    spinner_vehicle_size.setVisibility(View.GONE);
                    tv_vehicle_size.setVisibility(View.GONE);
                    size = "0";
                    ArrayAdapter<String> vehicleSeatAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.privateCarSeat));
                    spinner_vehicle_seat.setAdapter(vehicleSeatAdapter);

                    ArrayAdapter<String> vehicleVarietyAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.acNonAc));
                    spinner_vehicleVariety.setAdapter(vehicleVarietyAdapter);

                }else if(position==4){
                    tv_vehicle_seat.setVisibility(View.VISIBLE);
                    spinner_vehicle_seat.setVisibility(View.VISIBLE);
                    tv_vehicle_size.setVisibility(View.GONE);
                    spinner_vehicle_size.setVisibility(View.GONE);
                    size = "0";
                    ArrayAdapter<String> vehicleSeatAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.microBusSeat));
                    spinner_vehicle_seat.setAdapter(vehicleSeatAdapter);
                    ArrayAdapter<String> vehicleVarietyAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.acNonAc));
                    spinner_vehicleVariety.setAdapter(vehicleVarietyAdapter);
                }else if(position==5){
                    tv_vehicle_seat.setVisibility(View.VISIBLE);
                    spinner_vehicle_seat.setVisibility(View.VISIBLE);
                    tv_vehicle_size.setVisibility(View.GONE);
                    spinner_vehicle_size.setVisibility(View.GONE);
                    size = "0";
                    ArrayAdapter<String> vehicleSeatAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.tourBusSeat));
                    spinner_vehicle_seat.setAdapter(vehicleSeatAdapter);
                    ArrayAdapter<String> vehicleVarietyAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.acNonAc));
                    spinner_vehicleVariety.setAdapter(vehicleVarietyAdapter);
                }*/
                tv_vehicle_variety_header.setVisibility(position==2?View.GONE:View.VISIBLE);
                spinner_vehicleVariety.setVisibility(position==2?View.GONE:View.VISIBLE);
                variety = position==2?"":variety;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loadingUpaz = autocomplete_loading_upazila.getText().toString().trim();
                String unloadingUpaz = autocomplete_unloading_upazila.getText().toString().trim();
                String loadingFullAddr = text_input_layout_full_loading_address.getEditText().getText().toString();
                String unloadingFullAddr = text_input_layout_full_unloading_address.getEditText().getText().toString();
                String loadingLandmark = text_input_layout_loading_landmark.getEditText().getText().toString();
                String unloadingLandmark = text_input_layout_unloading_landmark.getEditText().getText().toString();
                String loadingDate = datePicker.getDayOfMonth()+"/"+(datePicker.getMonth()+1)+"/"+datePicker.getYear();
                String returnDate = datePickerReturnSchedule.getDayOfMonth()+"/"+(datePickerReturnSchedule.getMonth()+1)+datePickerReturnSchedule.getYear();
                String loadingTime = numberPicker.getDisplayedValues()[numberPicker.getValue()];
                String returnTime = numberPickerReturnSchedule.getDisplayedValues()[numberPickerReturnSchedule.getValue()];
                String productDescription = text_input_layout_product_description.getEditText().getText().toString().trim();
                String stopAddress = "none ";
                //String stopAddress = text_input_layout_stop_point_address.getEditText().getText().toString().trim();
                String stopPointPersonName = text_input_layout_stopPointPerson_name.getEditText().getText().toString().trim();
                String stopPersonPhoneNumber = text_input_layout_stopPointPerson_mobile_number.getEditText().getText().toString().trim();
                String returnAddress = text_input_layout_return_point_address.getEditText().getText().toString().trim();
                String loadingAlternative_person_number = text_input_layout_loadingAlternative_person_number.getEditText().getText().toString().trim();
                String loadingArea = text_input_layout_loadingArea.getEditText().getText().toString().trim();
                String unloadingArea = text_input_layout_unloading_area.getEditText().getText().toString().trim();
                String unloading_personName = text_input_layout_unloading_personName.getEditText().getText().toString().trim();
                String unloading_mobileNumber = text_input_layout_unloading_mobileNumber.getEditText().getText().toString().trim();

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
                    autocomplete_loading_upazila.setError("Select an Valid Address");
                    autocomplete_loading_upazila.requestFocus();
                }else if (!isStringContainsInArray(unloadingUpaz, addressNames)){
                    autocomplete_unloading_upazila.setError("Select an Valid Address");
                    autocomplete_unloading_upazila.requestFocus();
                }else if (loadingFullAddr.isEmpty()){
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
                }else if (productDescription.isEmpty()){
                    text_input_layout_product_description.setError("Enter Product Description");
                    text_input_layout_product_description.requestFocus();
                }/*else if (stopAddress.isEmpty()){
                    text_input_layout_stop_point_address.setError("Enter Stop Point Address");
                    text_input_layout_stop_point_address.requestFocus();
                }*/else if (stopPointPersonName.isEmpty()){
                    text_input_layout_stopPointPerson_name.setError("Enter Person Name");
                    text_input_layout_stopPointPerson_name.requestFocus();
                }else if (stopPersonPhoneNumber.isEmpty()){
                    text_input_layout_stopPointPerson_mobile_number.setError("Enter Person Mobile Number");
                    text_input_layout_stopPointPerson_mobile_number.requestFocus();
                }else if (returnAddress.isEmpty()){
                    text_input_layout_return_point_address.setError("Enter Return Point Address");
                    text_input_layout_return_point_address.requestFocus();
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

                Vehicle vehicle = new Vehicle(null, "", type, variety, seat,
                        size, "","", "", "","",
                        "","", "");



                Trip trip = new Trip(null, customer, null, vehicle,
                        System.currentTimeMillis(), loadingUpaz, loadingFullAddr,loadingArea, loadingLandmark, loadingDate, loadingTime,
                        loadingAlternative_person_number,
                        unloadingUpaz, unloadingFullAddr,unloadingArea, unloadingLandmark, unloading_personName,unloading_mobileNumber,
                        productDescription, productCategory,
                        upDownTrip,containAnimal,fragile,perishable,laborNeeded, rentalPrice, paymentMethod,
                        lengthAlert, weightAlert, "Bidding", (List) new ArrayList<>(),
                        stopAddress, stopPointPersonName,0, stopPersonPhoneNumber, returnAddress,returnDate, returnTime);


                DialogTripPreview dialogTripPreview = new DialogTripPreview(getActivity(), trip);
                dialogTripPreview.setCancelable(true);
                dialogTripPreview.show();
            }
        });





        return view;
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
            tv_loading_header.setText("Detail information on load location");
            tv_unloading_header.setText("Detail information on unload location");
            text_input_layout_loading_landmark.setHint("Landmark of loading Site");
            text_input_layout_unloading_landmark.setHint("Landmark of Unloading Site");
            text_input_layout_unloading_personName.setVisibility(View.VISIBLE);
            text_input_layout_unloading_mobileNumber.setVisibility(View.VISIBLE);
            cv_additional.setVisibility(View.VISIBLE);
        }else{
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
            tv_loading_header.setText("Detail info on trip start location");
            tv_unloading_header.setText("Detail info on trip end location");
            text_input_layout_loading_landmark.setHint("Landmark of trip start Site");
            text_input_layout_unloading_landmark.setHint("Landmark of trip end Site");
            text_input_layout_unloading_personName.setVisibility(View.GONE);
            text_input_layout_unloading_mobileNumber.setVisibility(View.GONE);
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
}