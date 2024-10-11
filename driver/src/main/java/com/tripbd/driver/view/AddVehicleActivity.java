package com.tripbd.driver.view;

import static android.Manifest.permission_group.CAMERA;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
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

import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.api.LogDescriptor;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.tripbd.driver.R;
import com.tripbd.driver.adapter.AutoCompleteCustomAdapter;
import com.tripbd.driver.model.Driver;
import com.tripbd.driver.model.Vehicle;
import com.tripbd.driver.utils.CustomCameraGallery;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class AddVehicleActivity extends AppCompatActivity {
    private static final String TAG = "AddVehicleActivity";
    TextInputLayout text_input_layout_model_name, text_input_layout_vehicle_number,
            text_input_layout_sit, text_input_layout_length, text_input_layout_capacity;
    LinearLayout lay_vehicle_image, lay_brta_image, lay_nid_image;
    MaterialButton btn_submit_vehicle;
    StorageReference storageReference;
    String vehicleImageUrl = "", nid_image_url = "", brta_image_url = "";
    ImageView imgv_vehicle_licence, imgv_brta_document, imgv_nid;
    ProgressBar progressbar_add_vehicle;
    Driver driver = null;


    String[] imageUrls = new String[]{"", "", ""};

    AutoCompleteTextView autocomplete_model_name;

    Spinner spinner_vehicle, spinner_metro, spinner_serial, spinner_vehicleVariety, spinner_vehicle_size, spinner_vehicle_seat, spinner_vehicle_year;
    TextView tv_vehicle_size, tv_vehicle_seat;

    String seat, year, size;
    CustomCameraGallery customCameraGallery;
    CustomCameraGallery.MyCamera myCamera;
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


        customCameraGallery = new CustomCameraGallery(AddVehicleActivity.this);

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

        myCamera = new CustomCameraGallery.MyCamera() {
            @Override
            public void gotUri(Uri picUri, int requestCode) {
                Log.d(TAG, "gotUri: RequestCode: "+requestCode);
                byte[] compressedImage = customCameraGallery.compressImage(picUri);

                if (requestCode == 0) {
                    imgv_vehicle_licence.setImageURI(picUri);
                    uploadImage(0, compressedImage);
                    btn_submit_vehicle.setVisibility(View.VISIBLE);

                }else if (requestCode == 1) {
                    imgv_brta_document.setImageURI(picUri);
                    uploadImage(1, compressedImage);
                }else if (requestCode == 2) {
                    imgv_nid.setImageURI(picUri);
                    uploadImage(2, compressedImage);
                }
            }

            @Override
            public void somethingWrong(String errorMessage) {

            }
        };

        lay_vehicle_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);*/
                //pickImage(1);
                chooseImage(0);

            }
        });

        lay_brta_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
                //pickImage(2);*/
                chooseImage(1);
            }
        });

        lay_nid_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 3);*/
                // pickImage(3);
                chooseImage(2);
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
                if (position == 0) {
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

                } else if (position == 1) {
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

                } else if (position == 2) {
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

                } else if (position == 3) {
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

                } else if (position == 4) {
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

                } else if (position == 5) {
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


        customCameraGallery.checkAndRequestPermission();
    }


/*
    void pickImage(int requestCode){
        ActivityResultLauncher<Intent> launcher=
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
                    if(result.getResultCode()==RESULT_OK){
                        Uri uri=result.getData().getData();
                        // Use the uri to load the image
                        receiveImageFromDeviceRequest(requestCode, result.getResultCode(), result.getData());
                    }else if(result.getResultCode()== ImagePicker.RESULT_ERROR){
                        // Use ImagePicker.Companion.getError(result.getData()) to show an error
                    }
                });
        ImagePicker.with(this).bothCameraGallery().createIntent();

       */
/* ImagePicker.Companion.with(this)
                .crop()
                .cropOval()
                .provider(ImageProvider.BOTH)
                .maxResultSize(512,512,true)
                .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                .createIntentFromDialog((Function1)(new Function1(){
                    public Object invoke(Object var1){
                        this.invoke((Intent)var1);
                        return Unit.INSTANCE;
                    }

                    public void invoke(@NotNull Intent it){
                        Intrinsics.checkNotNullParameter(it,"it");
                        launcher.launch(it);
                    }
                }));*//*

    }
*/

    void chooseImage(int requestCode) {
        customCameraGallery.pickImage(AddVehicleActivity.this, myCamera, requestCode);
    }

    void submitVehicle() {

        size = spinner_vehicle_size.getVisibility() == View.VISIBLE ?
                spinner_vehicle_size.getSelectedItem().toString().trim() : "0";
        seat = spinner_vehicle_seat.getVisibility() == View.VISIBLE ?
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
        String driverMobileNumber = sp1.getString("DRIVER_PHONE_NUMBER", null);
        if (driverMobileNumber == null
                || vehicle_model.isEmpty()
                || vehicle_metro.isEmpty()
                || vehicle_serial.isEmpty()) {
            Toast.makeText(this, "Please enter required info...", Toast.LENGTH_SHORT).show();
            return;
        }
        Vehicle vehicle = new Vehicle(null, vehicle_model, type, variety, seat,
                size, vehicle_metro, vehicle_serial, vehicle_number, year, vehicleImageUrl,
                brta_image_url, nid_image_url, driverMobileNumber);

        if (vehicle.getVehicleImageUrl().equalsIgnoreCase("")
                || vehicle.getBrtaDocumentImageUrl().equalsIgnoreCase("")
                || vehicle.getNidImageUrl().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please Upload Documents...", Toast.LENGTH_SHORT).show();
            return;
        }
        List<Vehicle> vehicles = driver.getVehicles();
        vehicles.add(vehicle);
        driver.setVehicles(vehicles);

        updateDriver(driver);
        //Toast.makeText(AddVehicleActivity.this, vehicle.getModel() , Toast.LENGTH_SHORT).show();

    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        receiveImageFromDeviceRequest(requestCode,resultCode, data);
    }
*/


/*
    private void receiveImageFromDeviceRequest(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap;
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
            Log.d(TAG, "receiveImageFromDeviceRequest: old: "+data.getData().toString());
            uploadImage(1,compressImage(data));

        }else if (requestCode == 3 && resultCode == RESULT_OK && data != null) {
            imgv_nid.setImageURI(data.getData());
            btn_submit_vehicle.setVisibility(View.GONE);
            imageUrls[2] = null;
            uploadImage(2,compressImage(data));
        }
    }
*/
   /* byte[] compressImage(Intent data){
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
    }*/

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
                startActivity(new Intent(AddVehicleActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddVehicleActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressbar_add_vehicle.setVisibility(View.GONE);

            }
        });
    }

    void uploadImage(int position, byte[] bitmapData) {
        Toast.makeText(getApplicationContext(), "Image " + (position+1) + " Uploading", Toast.LENGTH_SHORT).show();

        String imageRef = "images/" + UUID.randomUUID().toString();
        StorageReference ref = storageReference.child(imageRef);
        progressbar_add_vehicle.setVisibility(View.VISIBLE);
        ref.putBytes(bitmapData)
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
                                        Toast.makeText(getApplicationContext(), "Image " + (position+1) + " Uploaded", Toast.LENGTH_SHORT).show();
                                        imageUrls[position] = null;
                                        imageUrls[position] = task.getResult().toString();

                                       // if (imageUrls[0] != null && imageUrls[1] != null && imageUrls[2] != null)
                                       //     btn_submit_vehicle.setVisibility(View.VISIBLE);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressbar_add_vehicle.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
    }

    public void getDriver() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences sp = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
        String id = sp.getString("DRIVER_ID", null);
        String notification = sp.getString("NOTIFICATION", "None");

        if (id == null) {
            Toast.makeText(getApplicationContext(), "You are not sign in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            finish();
        } else {
            progressbar_add_vehicle.setVisibility(View.VISIBLE);

            DocumentReference documentReference = db.collection("driver").document(id);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    progressbar_add_vehicle.setVisibility(View.GONE);
                    driver = task.getResult().toObject(Driver.class);
                    if (driver == null) return;
                    driver.setId(documentReference.getId());
                    if (driver == null) {

                        Toast.makeText(getApplicationContext(), "Driver Not Found!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressbar_add_vehicle.setVisibility(View.GONE);
                    Toast.makeText(AddVehicleActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        customCameraGallery.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        customCameraGallery.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        customCameraGallery.onRestoreInstanceState(savedInstanceState);
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        customCameraGallery.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
