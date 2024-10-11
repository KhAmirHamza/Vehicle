package com.tripbd.driver.view;

import static android.app.Activity.RESULT_OK;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tripbd.driver.LanguageManager;
import com.tripbd.driver.R;
import com.tripbd.driver.adapter.AutoCompleteCustomAdapter;
import com.tripbd.driver.adapter.VehicleAdapter;
import com.tripbd.driver.model.Driver;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private static final int PICK_IMAGE = 1;

    StorageReference storageReference;
    CircleImageView imgv_profile_image;
    TextView txtv_name, txtv_email,txtv_phone_number,txtv_due,txtv_notification_area;
    MaterialButton btn_notification_area, btn_add_vehicle;
    RecyclerView recy_vehicles;
    VehicleAdapter vehicleAdapter;
    MaterialButton btnLogOut;
    List<String> addressList = new ArrayList<>();
    //Spinner spinner_notificationArea;
    AutoCompleteTextView text_input_layout_notifi_unloading_thana, text_input_layout_notifi_loading_thana;
    ArrayAdapter<String> notificationAreaAdapter;
    String driverID=" ";
    AutoCompleteCustomAdapter loadingAddressAdapter,unloadingAddressAdapter;
    MaterialButton btnPayNow;

    RadioGroup radioGroupLanguage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        radioGroupLanguage = view.findViewById(R.id.radioGroupLanguage);

        imgv_profile_image = view.findViewById(R.id.imgv_profile_image);
        txtv_name = view.findViewById(R.id.txtv_name);
        txtv_email = view.findViewById(R.id.txtv_email);
        txtv_phone_number = view.findViewById(R.id.txtv_phone_number);
        txtv_due = view.findViewById(R.id.txtv_due);
        btn_notification_area = view.findViewById(R.id.btn_notification_area);
        btn_add_vehicle = view.findViewById(R.id.btn_add_vehicle);
        recy_vehicles = view.findViewById(R.id.recy_vehicles);
        recy_vehicles.setNestedScrollingEnabled(false);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnPayNow = view.findViewById(R.id.btnPayNow);
        text_input_layout_notifi_loading_thana = view.findViewById(R.id.autocomplete_notificationLoadingArea);
        text_input_layout_notifi_unloading_thana = view.findViewById(R.id.autocomplete_notificationUnloadingArea);
        getAddresses();

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getContext().getString(R.string.payusingbkshrocketnagad));
                builder.setMessage(getContext().getString(R.string.sendmoneyto));

                builder.setPositiveButton("Copy Number", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Bkash/Nagad/Rocket Personal:", "01763414164");
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getContext(), "Copied: 01763414164", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setCancelable(true);
                builder.show();
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle(getContext().getString(R.string.doyouwanttologout));
                builder.setPositiveButton(getContext().getString(R.string.log_out), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
                        sharedPreferences.edit().clear().apply();
                        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                            FirebaseAuth.getInstance().getCurrentUser().delete();
                        }
                        startActivity(new Intent(getContext(), SignInActivity.class));
                        requireActivity().finish();
                    }
                });
                builder.show();
            }
        });
        getDriver();

        btn_notification_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo change or set area...

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String,Object> fcmAreaMap = new HashMap<>();
                fcmAreaMap.put("fcmLoadingArea", text_input_layout_notifi_loading_thana.getText().toString());
                fcmAreaMap.put("fcmUnloadingArea", text_input_layout_notifi_unloading_thana.getText().toString());
                db.collection("driver").document(driverID).update(fcmAreaMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Area Successfully Updated!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        btn_add_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo add vehicle...
                startActivity(new Intent(getContext(), AddVehicleActivity.class));
            }
        });

        vehicleAdapter = new VehicleAdapter(getActivity());
        recy_vehicles.setHasFixedSize(true);
        recy_vehicles.setLayoutManager(new LinearLayoutManager(getContext()));
        recy_vehicles.setAdapter(vehicleAdapter);

        //spinner_notificationArea = view.findViewById(R.id.spinner_notificationArea);
//        notificationAreaAdapter = new ArrayAdapter<String>(getContext(),
//                android.R.layout.simple_list_item_1,
//                addressList);
        loadingAddressAdapter = new AutoCompleteCustomAdapter(getContext(), addressList);
        unloadingAddressAdapter = new AutoCompleteCustomAdapter(getContext(), addressList);
        text_input_layout_notifi_loading_thana.setAdapter(loadingAddressAdapter);
        text_input_layout_notifi_unloading_thana.setAdapter(unloadingAddressAdapter);
        //spinner_notificationArea.setAdapter(notificationAreaAdapter);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        imgv_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.selectpicture)), PICK_IMAGE);
            }
        });



        LanguageManager languageManager =  new LanguageManager(getContext());
        if (languageManager.getLanguage().equalsIgnoreCase("bn")){
            RadioButton banglaBtn = view.findViewById(R.id.radioBtnBangla);
            banglaBtn.setChecked(true);
        }
        radioGroupLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.radioBtnBangla) {
                    languageManager.updateResources("bn");
                    Intent currentIntent = getActivity().getIntent();
                    getActivity().finish();
                    startActivity(currentIntent);
                }else if (checkedId==R.id.radioBtnEnglish) {
                    languageManager.updateResources("en");
                    Intent currentIntent = getActivity().getIntent();
                    getActivity().finish();
                    startActivity(currentIntent);
                }
            }
        });





        return  view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        receiveImageFromDeviceRequest(requestCode,resultCode, data);

    }

    private void receiveImageFromDeviceRequest(int requestCode, int resultCode, @Nullable Intent data){
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            //imgv_pharmacy_image.setImageURI(imageUri);

            try {
                // Defining the child of storageReference
                String imageRef = "images/" + UUID.randomUUID().toString();
                StorageReference ref = storageReference.child(imageRef);


                Bitmap bitmap = MediaStore.Images.Media
                        .getBitmap(getActivity().getContentResolver(), imageUri);
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

                byte[] bitmapdata = byteArrayOutputStream.toByteArray();
                imgv_profile_image.setImageBitmap(bitmap);
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
                                                Log.d(TAG, "onComplete: Image Uploaded");
                                               Toast.makeText(getContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                                                String imageUrl = task.getResult().toString();

                                                //UpdateProfileImageUrl
                                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                Map<String,Object> driverProfileImageMap = new HashMap<>();

                                                driverProfileImageMap.put("imageUrl", imageUrl);
                                                db.collection("driver").document(driverID).update(driverProfileImageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(getContext(), "Profile Image Successfully Updated!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error, Image not uploaded
                                Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            // Progress Listener for loading
                            // percentage on the dialog box
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()
                                        / taskSnapshot.getTotalByteCount());
                                //progress = (startIndex*(100.0/(uris.size()+1))) + (progress/(uris.size()+1));
                                // progressDialog.setMessage("Uploaded " + (int)progress + "%");
                            }
                        });









            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    public void getAddresses(){
        //loadDistrict...
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(loadJSONFromAsset("address.json"));
            for (int i = 0; i < jsonArray.length(); i++) {
                String district =  jsonArray.getJSONObject(i).get("district").toString();
               // String name =  jsonArray.getJSONObject(i).get("name").toString();
               // String thana =  jsonArray.getJSONObject(i).get("thana").toString();

               // Address address = new Address(name, thana, district);
                if (!addressList.contains(district)) {
                    addressList.add(district);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getDriver(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences sp = getActivity().getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
        String id = sp.getString("DRIVER_ID",null);
        String notification = sp.getString("NOTIFICATION","None");
        if (id==null) {
            Toast.makeText(getContext(), "You are not sign in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), SignInActivity.class));
            getActivity().finish();
        }else{
            db.collection("driver").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Driver driver = task.getResult().toObject(Driver.class);
                    driver.setId(task.getResult().getId());
                    driverID = task.getResult().getId();
                    if (driver==null){
                        Toast.makeText(getContext(), "Driver Not Found! "+id, Toast.LENGTH_SHORT).show();
                    }else{
                        //todo...
                        Picasso.get().load(driver.getImageUrl()).into(imgv_profile_image);
                        txtv_name.setText(driver.getName());
                        txtv_email.setText(driver.getEmail());
                        txtv_phone_number.setText(driver.getPhoneNumber());
                        txtv_due.setText(getString(R.string.due)+": "+driver.getDue());
                        if ( driver.getFcmLoadingArea()!=null && driver.getFcmLoadingArea().length()>3){
                            text_input_layout_notifi_loading_thana.setText(driver.getFcmLoadingArea());
                        }
                        if (driver.getFcmUnloadingArea()!=null && driver.getFcmUnloadingArea().length()>3){
                            text_input_layout_notifi_unloading_thana.setText(driver.getFcmUnloadingArea());
                            //spinner_notificationArea.setSelection(notificationAreaAdapter.getPosition(driver.getFcmArea()));
                        }

                        vehicleAdapter.setData(driver.getVehicles(),recy_vehicles);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }
}