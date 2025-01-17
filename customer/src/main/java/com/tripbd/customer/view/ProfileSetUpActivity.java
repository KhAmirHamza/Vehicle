package com.tripbd.customer.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.firestore.DocumentReference;
import com.tripbd.customer.R;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tripbd.customer.model.Customer;
import com.tripbd.customer.utils.SystemUI;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class ProfileSetUpActivity extends AppCompatActivity {
    private static final String TAG = "ProfileSetUpActivity";
    private static final int PICK_IMAGE = 1;

    TextInputLayout text_input_layout_name,text_input_layout_email,text_input_layout_phone_number, text_input_layout_address,
            text_input_layout_password, text_input_layout_confirm_pass;
    String name, email, phone_number, password, address;
    Button btn_submit_profile;
    ImageView imgvProfileImage;
    ProgressBar progressDialog;
    MaterialToolbar profile_toolbar;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_set_up);

        profile_toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(profile_toolbar);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        imgvProfileImage = findViewById(R.id.imgv_pharmacy_image);
        text_input_layout_name = findViewById(R.id.text_input_layout_name);
        text_input_layout_email = findViewById(R.id.text_input_layout_email);
        text_input_layout_phone_number = findViewById(R.id.text_input_layout_phone_number);
        text_input_layout_password = findViewById(R.id.text_input_layout_password);
        text_input_layout_confirm_pass = findViewById(R.id.text_input_layout_confirm_pass);
        text_input_layout_address = findViewById(R.id.text_input_layout_address);

        btn_submit_profile = findViewById(R.id.btn_submit_profile);
        progressDialog = findViewById(R.id.progressbar_add_deliveryman);

        progressDialog.setVisibility(View.GONE);
        text_input_layout_phone_number.getEditText().setText(getIntent().getStringExtra("phoneNumber"));
        text_input_layout_phone_number.setFocusable(false);
        text_input_layout_phone_number.getEditText().setFocusable(false);

        imgvProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                        .getBitmap(getContentResolver(), imageUri);
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
                imgvProfileImage.setImageBitmap(bitmap);
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
                                                Toast.makeText(ProfileSetUpActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();

                                                btn_submit_profile.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Log.d(TAG, "onClick: called");

                                                        progressDialog.setVisibility(View.VISIBLE);
                                                        name = text_input_layout_name.getEditText().getText().toString();
                                                        email = text_input_layout_email.getEditText().getText().toString();
                                                        phone_number = text_input_layout_phone_number.getEditText().getText().toString();
                                                        password = text_input_layout_password.getEditText().getText().toString();
                                                        String confirm_pass = text_input_layout_confirm_pass.getEditText().getText().toString();
                                                        address = text_input_layout_address.getEditText().getText().toString();

                                                        if (name.isEmpty()) {
                                                            text_input_layout_name.setError("Name required!");
                                                            text_input_layout_name.requestFocus();
                                                        } else if (password.isEmpty()) {
                                                            text_input_layout_password.setError("Password required!");
                                                            text_input_layout_password.requestFocus();
                                                        } else if (confirm_pass.isEmpty() || !confirm_pass.equals(password)) {
                                                            text_input_layout_confirm_pass.setError("Password can't match!");
                                                            text_input_layout_confirm_pass.requestFocus();
                                                        }else {
                                                            if(email.isEmpty()) {
                                                                email = " ";}
                                                            if (address.isEmpty()) {
                                                                    address =" ";
                                                            /*text_input_layout_address.setError("Address required!");
                                                            text_input_layout_address.requestFocus();*/
                                                                }
                                                            Customer customer = new Customer(null, name, phone_number, password, email, task.getResult().toString(), address);
                                                            addCustomer(customer);
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error, Image not uploaded
                                progressDialog.setVisibility(View.GONE);
                                Toast.makeText(ProfileSetUpActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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


    private void addCustomer(Customer customer) {
        progressDialog.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Add Customer Called", Toast.LENGTH_SHORT).show();

        //todo...submit budget data to server...
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //progressDialog.setMessage("Uploaded " + 90 + "%");
        db.collection("user").add(customer).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                SharedPreferences sharedPreferences = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("USER_PHONE_NUMBER", phone_number).apply();
                sharedPreferences.edit().putString("USER_EMAIL", email).apply();
                sharedPreferences.edit().putString("USER_NAME", name).apply();
                sharedPreferences.edit().putString("USER_IMAGE_URL", customer.getImageUrl()).apply();
                sharedPreferences.edit().putString("USER_ID", documentReference.getId()).apply();
                sharedPreferences.edit().putString("USER_PASSWORD", password).apply();
                sharedPreferences.edit().putString("USER_ADDRESS", address).apply();
                DialogWelcome dialogWelcome = new DialogWelcome(ProfileSetUpActivity.this,
                        "Welcome to "+getResources().getString(R.string.app_name)+"\n  Always stay with us!",
                        new Intent(ProfileSetUpActivity.this, MainActivity.class));
                dialogWelcome.setCancelable(false);
                dialogWelcome.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "addCustomer: Error adding document"+e.getMessage());
                //progressDialog.dismiss();
                progressDialog.setVisibility(View.GONE);
            }
        });

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            new SystemUI(ProfileSetUpActivity.this).hideSystemUI();
        }
    }

}