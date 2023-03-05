package com.vehicle.driver.view;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.vehicle.driver.R;
import com.vehicle.driver.adapter.AutoCompleteCustomAdapter;
import com.vehicle.driver.model.Driver;
import com.vehicle.driver.model.Vehicle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddVehicleActivity extends AppCompatActivity {
    private static final String TAG = "AddVehicleActivity";
    TextInputLayout text_input_layout_model_name,text_input_layout_vehicle_number,
            text_input_layout_sit,text_input_layout_length, text_input_layout_capacity;
    LinearLayout lay_vehicle_image,lay_brta_image,lay_nid_image;
    MaterialButton btn_submit_vehicle;
    StorageReference storageReference;
    String vehicleImageUrl="", nid_image_url="", brta_image_url="";
    ImageView imgv_vehicle_licence, imgv_brta_document, imgv_nid;
    ProgressBar progressbar_add_vehicle;
    Driver driver = null;



    String[] imageUrls = new String[]{"","",""};

    AutoCompleteTextView autocomplete_model_name;

    Spinner spinner_vehicle,spinner_metro, spinner_serial, spinner_vehicleVariety, spinner_vehicle_size, spinner_vehicle_seat, spinner_vehicle_year;
    TextView tv_vehicle_size, tv_vehicle_seat;

    String seat, year, size;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        spinner_vehicle = findViewById(R.id.spinner_vehicle);
        spinner_metro = findViewById(R.id.spinner_metro);
        spinner_serial = findViewById(R.id.spinner_serial);
        spinner_vehicleVariety = findViewById(R.id.spinner_vehicleVariety);
        spinner_vehicle_size = findViewById(R.id.spinner_vehicle_size);
        spinner_vehicle_seat = findViewById(R.id.spinner_vehicle_seat);
        spinner_vehicle_year = findViewById(R.id.spinner_vehicle_year);

        tv_vehicle_size = findViewById(R.id.tv_vehicle_size);
        tv_vehicle_seat = findViewById(R.id.tv_vehicle_seat);




        autocomplete_model_name = findViewById(R.id.autocomplete_model_name);

        imgv_vehicle_licence = findViewById(R.id.imgv_vehicle_licence);
        imgv_brta_document = findViewById(R.id.imgv_brta_document);
        imgv_nid = findViewById(R.id.imgv_nid);

        text_input_layout_model_name = findViewById(R.id.text_input_layout_model_name);
        text_input_layout_vehicle_number = findViewById(R.id.text_input_layout_vehicle_number);


        lay_vehicle_image = findViewById(R.id.lay_vehicle_image);
        lay_brta_image = findViewById(R.id.lay_brta_image);
        lay_nid_image = findViewById(R.id.lay_nid_image);
        progressbar_add_vehicle = findViewById(R.id.progressbar_add_vehicle);
        btn_submit_vehicle = findViewById(R.id.btn_submit_vehicle);

        ArrayAdapter<String> vehicleTypeAdapter = new ArrayAdapter<String>(AddVehicleActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.vehicle_type));
        spinner_vehicle.setAdapter(vehicleTypeAdapter);

        ArrayAdapter<String> vehicleVarietyAdapter = new ArrayAdapter<String>(AddVehicleActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.openCovered));
        spinner_vehicleVariety.setAdapter(vehicleVarietyAdapter);

        ArrayAdapter<String> vehicleSizeAdapter = new ArrayAdapter<String>(AddVehicleActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.pickupSize));
        spinner_vehicle_size.setAdapter(vehicleSizeAdapter);

        ArrayAdapter<String> vehicleSeatAdapter = new ArrayAdapter<String>(AddVehicleActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.privateCarSeat));
        spinner_vehicle_seat.setAdapter(vehicleSeatAdapter);

        ArrayAdapter<String> vehicleYearAdapter = new ArrayAdapter<String>(AddVehicleActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.year));
        spinner_vehicle_year.setAdapter(vehicleYearAdapter);

        getDriver();

        lay_vehicle_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        lay_brta_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
            }
        });

        lay_nid_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 3);
            }
        });
        btn_submit_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitVehicle();
            }
        });

        spinner_vehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(position==0){
                    tv_vehicle_seat.setVisibility(View.GONE);
                    spinner_vehicle_seat.setVisibility(View.GONE);
                    tv_vehicle_size.setVisibility(View.VISIBLE);
                    spinner_vehicle_size.setVisibility(View.VISIBLE);
                    seat = "0";
                    ArrayAdapter<String> vehicleSizeAdapter = new ArrayAdapter<String>(AddVehicleActivity.this,
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.pickupSize));
                    spinner_vehicle_size.setAdapter(vehicleSizeAdapter);

                    ArrayAdapter<String> vehicleVariety = new ArrayAdapter<String>(AddVehicleActivity.this,
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.openCovered));
                    spinner_vehicleVariety.setAdapter(vehicleVariety);

                }else if(position==1){
                    tv_vehicle_seat.setVisibility(View.GONE);
                    spinner_vehicle_seat.setVisibility(View.GONE);
                    tv_vehicle_size.setVisibility(View.VISIBLE);
                    spinner_vehicle_size.setVisibility(View.VISIBLE);
                    seat = "0";
                    ArrayAdapter<String> vehicleSizeAdapter = new ArrayAdapter<String>(AddVehicleActivity.this,
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.truckSize));
                    spinner_vehicle_size.setAdapter(vehicleSizeAdapter);
                    ArrayAdapter<String> vehicleVariety = new ArrayAdapter<String>(AddVehicleActivity.this,
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.openCovered));
                    spinner_vehicleVariety.setAdapter(vehicleVariety);

                }else if(position==2){
                    tv_vehicle_seat.setVisibility(View.GONE);
                    spinner_vehicle_seat.setVisibility(View.GONE);
                    tv_vehicle_size.setVisibility(View.VISIBLE);
                    spinner_vehicle_size.setVisibility(View.VISIBLE);
                    seat = "0";
                     ArrayAdapter<String> vehicleSizeAdapter = new ArrayAdapter<String>(AddVehicleActivity.this,
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.trailerSize));
                    spinner_vehicle_size.setAdapter(vehicleSizeAdapter);

                    ArrayAdapter<String> vehicleVariety = new ArrayAdapter<String>(AddVehicleActivity.this,
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.openCovered));
                    spinner_vehicleVariety.setAdapter(vehicleVariety);

                }else if(position==3){
                    tv_vehicle_seat.setVisibility(View.VISIBLE);
                    spinner_vehicle_seat.setVisibility(View.VISIBLE);
                    spinner_vehicle_size.setVisibility(View.GONE);
                    tv_vehicle_size.setVisibility(View.GONE);
                    size = "0";
                    ArrayAdapter<String> vehicleSeatAdapter = new ArrayAdapter<String>(AddVehicleActivity.this,
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.privateCarSeat));
                    spinner_vehicle_seat.setAdapter(vehicleSeatAdapter);
                    ArrayAdapter<String> vehicleVariety = new ArrayAdapter<String>(AddVehicleActivity.this,
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.acNonAc));
                    spinner_vehicleVariety.setAdapter(vehicleVariety);

                }else if(position==4){
                    tv_vehicle_seat.setVisibility(View.VISIBLE);
                    spinner_vehicle_seat.setVisibility(View.VISIBLE);
                    tv_vehicle_size.setVisibility(View.GONE);
                    spinner_vehicle_size.setVisibility(View.GONE);
                    size = "0";
                    ArrayAdapter<String> vehicleSeatAdapter = new ArrayAdapter<String>(AddVehicleActivity.this,
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.microBusSeat));
                    spinner_vehicle_seat.setAdapter(vehicleSeatAdapter);
                    ArrayAdapter<String> vehicleVariety = new ArrayAdapter<String>(AddVehicleActivity.this,
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.acNonAc));
                    spinner_vehicleVariety.setAdapter(vehicleVariety);

                }else if(position==5){
                    tv_vehicle_seat.setVisibility(View.VISIBLE);
                    spinner_vehicle_seat.setVisibility(View.VISIBLE);
                    tv_vehicle_size.setVisibility(View.GONE);
                    spinner_vehicle_size.setVisibility(View.GONE);
                    size = "0";
                    ArrayAdapter<String> vehicleSeatAdapter = new ArrayAdapter<String>(AddVehicleActivity.this,
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.tourBusSeat));
                    spinner_vehicle_seat.setAdapter(vehicleSeatAdapter);
                    ArrayAdapter<String> vehicleVariety = new ArrayAdapter<String>(AddVehicleActivity.this,
                            android.R.layout.simple_list_item_1,
                            getResources().getStringArray(R.array.acNonAc));
                    spinner_vehicleVariety.setAdapter(vehicleVariety);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        List<String> vehicle_model_list = new ArrayList<>();
        for (String s : getResources().getStringArray(R.array.vehicle_model)) {
            vehicle_model_list.add(s);
        }
/*        List<String> vehicle_metro_list = new ArrayList<>();
        for (String s : getResources().getStringArray(R.array.metro)) {
            vehicle_metro_list.add(s);
        }
        List<String> vehicle_serial_list = new ArrayList<>();
        for (String s : getResources().getStringArray(R.array.serial)) {
            vehicle_serial_list.add(s);
        }*/
        AutoCompleteCustomAdapter modelNameAdapter = new AutoCompleteCustomAdapter(getApplicationContext(), vehicle_model_list);
        //AutoCompleteAdapter addressAdapter = new AutoCompleteAdapter(getContext(), Arrays.asList(addressNames));
        autocomplete_model_name.setAdapter(modelNameAdapter);

        ArrayAdapter<String> vehicleMetro = new ArrayAdapter<String>(AddVehicleActivity.this,
                R.layout.simple_list_item2,
                getResources().getStringArray(R.array.metro));
        spinner_metro.setAdapter(vehicleMetro);
        ArrayAdapter<String> vehicleSerial = new ArrayAdapter<String>(AddVehicleActivity.this,
                R.layout.simple_list_item2,
                getResources().getStringArray(R.array.serial));
        spinner_serial.setAdapter(vehicleSerial);

    }
    void submitVehicle(){

        size = spinner_vehicle_size.getVisibility()==View.VISIBLE ?
                spinner_vehicle_size.getSelectedItem().toString().trim() : "0";
        seat = spinner_vehicle_seat.getVisibility()==View.VISIBLE ?
                spinner_vehicle_seat.getSelectedItem().toString().trim() : "0";
        String variety = spinner_vehicleVariety.getSelectedItem().toString();
        String type = spinner_vehicle.getSelectedItem().toString();
        year = spinner_vehicle_year.getSelectedItem().toString().trim();
        String vehicle_model = autocomplete_model_name.getText().toString().trim();
        String vehicle_metro = spinner_metro.getSelectedItem().toString().trim();
        String vehicle_serial = spinner_serial.getSelectedItem().toString().trim();
        String vehicle_number = text_input_layout_vehicle_number.getEditText().getText().toString().trim();

       vehicleImageUrl = imageUrls[0];
        brta_image_url = imageUrls[1];
        nid_image_url = imageUrls[2];
        SharedPreferences sp1 = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
        String driverMobileNumber = sp1.getString("DRIVER_PHONE_NUMBER",null);
        if(driverMobileNumber==null
                || vehicle_model.isEmpty()
                || vehicle_metro.isEmpty()
                || vehicle_serial.isEmpty()) {
            Toast.makeText(this, "Please enter required info...", Toast.LENGTH_SHORT).show();
            return;
        }
        Vehicle vehicle = new Vehicle(null, vehicle_model, type, variety, seat,
                size, vehicle_metro,vehicle_serial, vehicle_number, year,vehicleImageUrl,
                brta_image_url,nid_image_url, driverMobileNumber);


        List<Vehicle> vehicles = driver.getVehicles();
        vehicles.add(vehicle);
        driver.setVehicles(vehicles);

        if (vehicle.getVehicleImageUrl().equalsIgnoreCase("")
                || vehicle.getBrtaDocumentImageUrl().equalsIgnoreCase("")
                || vehicle.getNidImageUrl().equalsIgnoreCase("")){
            Toast.makeText(this, "Please Upload Documents...", Toast.LENGTH_SHORT).show();
            return;
        }
        updateDriver(driver);
        //Toast.makeText(AddVehicleActivity.this, vehicle.getModel() , Toast.LENGTH_SHORT).show();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        receiveImageFromDeviceRequest(requestCode,resultCode, data);
    }

    private void receiveImageFromDeviceRequest(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imgv_vehicle_licence.setImageURI(data.getData());
            btn_submit_vehicle.setVisibility(View.GONE);
            imageUrls[0] = null;
            uploadImage(0,compressImage(data));

        }else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            //UploadImage uploadImage = new UploadImage(getApplicationContext(),compressImage(data), storageReference);
            //brta_image_url = uploadImage.getUploadedImageUrl();
            imgv_brta_document.setImageURI(data.getData());
            btn_submit_vehicle.setVisibility(View.GONE);
            imageUrls[1] = null;
            uploadImage(1,compressImage(data));

        }else if (requestCode == 3 && resultCode == RESULT_OK && data != null) {
            imgv_nid.setImageURI(data.getData());
            btn_submit_vehicle.setVisibility(View.GONE);
            imageUrls[2] = null;
            uploadImage(2,compressImage(data));
        }
    }
    byte[] compressImage(Intent data){
        Uri imageUri = data.getData();
        byte[] bitmapdata;
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        //Compressed file size if greater than 1 MB;
        if (height>=3000){
            bitmap = Bitmap.createScaledBitmap(bitmap, width/3, height/3, true);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);

        }else if (height>=2500){
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width/2.5), (int) (height/2.5), true);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        }else if (height>=2000){
            bitmap = Bitmap.createScaledBitmap(bitmap, (width/2),  (height/2), true);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        }else if (height>=1500){
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width/1.5), (int) (height/1.5), true);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        }else {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        }

        bitmapdata = byteArrayOutputStream.toByteArray();
            return bitmapdata;
        } catch (IOException e) {
            e.printStackTrace();
            return bitmapdata = null;
        }
    }

    private void updateDriver(Driver driver) {
       // p.setVisibility(View.VISIBLE);
        //todo...submit budget data to server...
        progressbar_add_vehicle.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //progressDialog.setMessage("Uploaded " + 90 + "%");
        db.collection("driver").document(driver.getId()).set(driver).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressbar_add_vehicle.setVisibility(View.GONE);
                Toast.makeText(AddVehicleActivity.this, "Vehicle Added!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddVehicleActivity.this, MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddVehicleActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressbar_add_vehicle.setVisibility(View.GONE);

            }
        });
    }

    void uploadImage(int position, byte[] bitmapdata){
        String imageRef = "images/" + UUID.randomUUID().toString();
        StorageReference ref = storageReference.child(imageRef);
        progressbar_add_vehicle.setVisibility(View.VISIBLE);
        ref.putBytes(bitmapdata)
                .addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Image uploaded successfully
                                // Dismiss dialog
                                ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        progressbar_add_vehicle.setVisibility(View.GONE);
                                        //getUrl(task.getResult().toString());
                                        Toast.makeText(getApplicationContext(), "Image "+position+" Uploaded", Toast.LENGTH_SHORT).show();
                                        imageUrls[position]  = task.getResult().toString();
                                        if (imageUrls[0]!=null && imageUrls[1]!=null&& imageUrls[2]!=null)
                                            btn_submit_vehicle.setVisibility(View.VISIBLE);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressbar_add_vehicle.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Failed: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
    }

    public void getDriver(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences sp = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
        String id = sp.getString("DRIVER_ID",null);
        String notification = sp.getString("NOTIFICATION","None");

        if (id==null) {
            Toast.makeText(getApplicationContext(), "You are not sign in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        }else{
            progressbar_add_vehicle.setVisibility(View.VISIBLE);

            DocumentReference documentReference = db.collection("driver").document(id);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    progressbar_add_vehicle.setVisibility(View.GONE);
                    driver = task.getResult().toObject(Driver.class);
                    if (driver==null) return;
                    driver.setId(documentReference.getId());
                    if (driver ==null){

                        Toast.makeText(getApplicationContext(), "Driver Not Found!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressbar_add_vehicle.setVisibility(View.GONE);
                    Toast.makeText(AddVehicleActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}