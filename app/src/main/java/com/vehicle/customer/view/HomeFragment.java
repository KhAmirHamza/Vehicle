package com.vehicle.customer.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.vehicle.customer.R;
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
    Spinner spinner_vehicle;
    RadioGroup radio_group;
    final Calendar myCalendar= Calendar.getInstance();
    MaterialButton btn_review;
    DatePickerDialog.OnDateSetListener date;
    DatePicker datePicker;
    NumberPicker numberPicker;

    List<Address> addresses = new ArrayList<>();
    String[] addressNames;

    AutoCompleteTextView autocomplete_loading_upazila, autocomplete_unloading_upazila;

    TextInputLayout text_input_layout_full_loading_address, text_input_layout_full_unloading_address,
            text_input_layout_loading_landmark,text_input_layout_unloading_landmark, text_input_layout_product_description;

    CheckBox cbUpDownTrip, cbContainAnimal, cbFragile, cbPerishable,cbLaborNeeded;
    List<Vehicle> vehicles_t1 = new ArrayList<>();
    List<Vehicle> vehicles_t2 = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view     = inflater.inflate(R.layout.fragment_home, container, false);

        cbUpDownTrip = view.findViewById(R.id.cbUpDownTrip);
        cbFragile = view.findViewById(R.id.cbFragile);
        cbPerishable = view.findViewById(R.id.cbPerishable);
        cbContainAnimal = view.findViewById(R.id.cbContainAnimal);
        cbLaborNeeded = view.findViewById(R.id.cbLaborNeeded);

        btn_review = view.findViewById(R.id.btn_review);


        autocomplete_loading_upazila = view.findViewById(R.id.autocomplete_loading_upazila);
        autocomplete_unloading_upazila = view.findViewById(R.id.autocomplete_unloading_upazila);
        text_input_layout_full_loading_address = view.findViewById(R.id.text_input_layout_full_loading_address);
        text_input_layout_full_unloading_address = view.findViewById(R.id.text_input_layout_full_unloading_address);
        text_input_layout_loading_landmark = view.findViewById(R.id.text_input_layout_loading_landmark);
        text_input_layout_unloading_landmark = view.findViewById(R.id.text_input_layout_unloading_landmark);
        text_input_layout_product_description = view.findViewById(R.id.text_input_layout_product_description);

        datePicker = view.findViewById(R.id.datePicker);
        numberPicker = view.findViewById(R.id.numberPicker);
        spinner_vehicle = view.findViewById(R.id.spinner_vehicle);
        radio_group = view.findViewById(R.id.radio_group);

        Vehicle vehicle1 = new Vehicle(null,"Mdl", "Pickup", "Open",7, 1, 0, 0, 0, "Dhaka-Metro", "SerialNo","12345","url", "dUrl");
        Vehicle vehicle2 = new Vehicle(null,"Mdl", "Truck", "Covered",7, 1, 0, 0, 0, "Dhaka-Metro", "SerialNo","12345","url", "dUrl");
        Vehicle vehicle3 = new Vehicle(null,"Mdl", "Truck", "Open",9, 1, 0, 0, 0, "Dhaka-Metro", "SerialNo","789","url", "dUrl");
        Vehicle vehicle4 = new Vehicle(null,"Mdl", "Trailer", "Covered",9, 1, 0, 0, 0, "Dhaka-Metro", "SerialNo","789","url", "dUrl");
        vehicles_t1.add(vehicle1);
        vehicles_t1.add(vehicle2);
        vehicles_t1.add(vehicle3);
        vehicles_t1.add(vehicle4);

        String[] trucks = {
                "Choose a Truck/Pickup/Trailer",
                "7 Feet 1 Ton (Open)",
                "7 Feet 1 Ton (Covered)",
                "9 Feet 1.5 Ton (Open)",
                "9 Feet 1.5 Ton (Covered)",

        };
        /*
        "11 Feet 2 Ton (Open)",
        "11 Feet 2 Ton (Covered)"
                */

        Vehicle car1 = new Vehicle(null,"Mdl", "Private Car", "AC",4, 1, 0, 0, 0, "Dhaka-Metro", "SerialNo","12345","url", "dUrl");
        Vehicle car2 = new Vehicle(null,"Mdl", "Private Car", "",4, 1, 0, 0, 0, "Dhaka-Metro", "SerialNo","12345","url", "dUrl");
        Vehicle car3 = new Vehicle(null,"Mdl", "Micro", "AC",5, 1, 0, 0, 0, "Dhaka-Metro", "SerialNo","789","url", "dUrl");
        Vehicle car4 = new Vehicle(null,"Mdl", "Micro", "",5, 1, 0, 0, 0, "Dhaka-Metro", "SerialNo","789","url", "dUrl");
        vehicles_t2.add(car1);
        vehicles_t2.add(car2);
        vehicles_t2.add(car3);
        vehicles_t2.add(car4);


        String[] cars ={
                "Choose a Private Car/Micro",
                "4 seated Private Car",
                "4 seated Private Car(AC)",
                "5 seated Private Car",
                "5 seated Private Car(AC)",

        };

        /*
        *    "6 seated Private Car",
                "6 seated Private Car(AC)",
                "7 seated private Car",
                "7 seated Private Car(AC)",
                "8 seated Private Car",
                "8 seated Private Car(AC)",
                "7 seated Micro Bus",
                "7 seated Micro Bus(AC)",
                "8 seated Micro Bus",
                "8 seated Micro Bus(AC)",
                "9 seated Micro Bus",
                "9 seated Micro Bus(AC)",
                "10 seated Micro Bus",
                "10 seated Micro Bus(AC)",
                "11 seated Micro Bus",
                "11 seated Micro Bus(AC)"
        * */

        ArrayAdapter<String> trucksAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, trucks);
        ArrayAdapter<String> carsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, cars);
        spinner_vehicle.setAdapter(trucksAdapter);

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
        });

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



        getAddresses();

        addressNames = new String[addresses.size()];

        for (int i = 0; i < addresses.size(); i++) {
            addressNames[i] = addresses.get(i).getName();
        }

        Toast.makeText(getContext(), "Address Size: "+addresses.size(), Toast.LENGTH_SHORT).show();


        ArrayAdapter<String> upazilaAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, addressNames);
        ArrayAdapter<String> upazilaAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, addressNames);
        autocomplete_loading_upazila.setAdapter(upazilaAdapter);
        autocomplete_unloading_upazila.setAdapter(upazilaAdapter2);


        //todo...prepare trip...

        //Toast.makeText(getContext(), "Division Size: "+divisions.size(), Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Customer", Context.MODE_PRIVATE);
        String customerName = sharedPreferences.getString("name","");
        String phoneNumber = sharedPreferences.getString("phoneNumber","");;
        String customerPass = sharedPreferences.getString("password","");;
        String customerEmail = sharedPreferences.getString("email","");;
        String customerImageUrl = sharedPreferences.getString("imageUrl","");;
        String customerAddress = sharedPreferences.getString("address","");;
        Customer customer = new Customer(null, customerName,phoneNumber, customerPass, customerEmail,
                customerImageUrl, customerAddress);

        btn_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loadingUpaz = autocomplete_loading_upazila.getText().toString().trim();
                String unloadingUpaz = autocomplete_unloading_upazila.getText().toString().trim();
                String loadingFullAddr = text_input_layout_full_loading_address.getEditText().getText().toString();
                String unloadingFullAddr = text_input_layout_full_unloading_address.getEditText().getText().toString();
                String loadingLandmark = text_input_layout_loading_landmark.getEditText().getText().toString();
                String unloadingLandmark = text_input_layout_unloading_landmark.getEditText().getText().toString();
                String date = datePicker.getDayOfMonth()+"/"+(datePicker.getMonth()+1)+datePicker.getYear();
                String loadingTime = numberPicker.getDisplayedValues()[numberPicker.getValue()];
                String productDescription = text_input_layout_product_description.getEditText().getText().toString().trim();

                int upDownTrip = cbUpDownTrip.isChecked()?1:0;
                int containAnimal = cbContainAnimal.isChecked()?1:0;
                int fragile = cbFragile.isChecked()?1:0;
                int perishable = cbPerishable.isChecked()?1:0;
                int laborNeeded = cbLaborNeeded.isChecked()?1:0;
                int rentalPrice = 0;
                int paymentMethod = 0;

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
                }

                Vehicle selectedVehicle = radio_group.getCheckedRadioButtonId()==R.id.truck?
                        vehicles_t1.get(spinner_vehicle.getSelectedItemPosition()-1):
                        vehicles_t2.get(spinner_vehicle.getSelectedItemPosition()-1);

                Trip trip = new Trip(null, customer, null, selectedVehicle,
                        System.currentTimeMillis(), loadingUpaz,
                        loadingFullAddr, loadingLandmark, date, loadingTime, unloadingUpaz, unloadingFullAddr,
                        unloadingLandmark, productDescription,upDownTrip,containAnimal,fragile,perishable,laborNeeded,
                        rentalPrice,paymentMethod, "Pending", new ArrayList<>());


                DialogTripPreview dialogTripPreview = new DialogTripPreview(getContext(), trip);
                dialogTripPreview.setCancelable(true);
                dialogTripPreview.show();
            }
        });

        return view;
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