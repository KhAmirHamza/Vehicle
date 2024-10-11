package com.tripbd.customer.view;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.rpc.context.AttributeContext;
import com.squareup.picasso.Picasso;
import com.tripbd.customer.LanguageManager;
import com.tripbd.customer.R;
import com.tripbd.customer.model.Customer;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private static final int PICK_IMAGE = 1;

    StorageReference storageReference;
    Customer customer;
    CircleImageView imgv_profile_image;
    TextView txtv_name, txtv_email,txtv_phone_number;
    MaterialButton btnLogOut;
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
        btnLogOut = view.findViewById(R.id.btnLogOut);
        getCustomer();
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Do you want to log out?");
                builder.setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
                        sharedPreferences.edit().clear().apply();
                        Task<Void> voidTask = FirebaseAuth.getInstance().getCurrentUser() != null ?
                                FirebaseAuth.getInstance().getCurrentUser().delete() : null;
                        startActivity(new Intent(getContext(), SignInActivity.class));
                        requireActivity().finish();
                    }
                });
                builder.show();
            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        imgv_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
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
                                                db.collection("user").document(customer.getId()).update(driverProfileImageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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


    public void getCustomer(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences sp = getActivity().getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
        String id = sp.getString("USER_ID",null);
        //String notification = sp.getString("NOTIFICATION","None");
        if (id==null) {
            Toast.makeText(getContext(), "You are not sign in.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), SignInActivity.class));
            getActivity().finish();
        }else{
            db.collection("user").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    customer = task.getResult().toObject(Customer.class);
                    if (customer==null){
                        Toast.makeText(getContext(), "USER Not Found! "+id, Toast.LENGTH_SHORT).show();
                    }else{
                        //todo...
                        Picasso.get().load(customer.getImageUrl()).into(imgv_profile_image);
                        txtv_name.setText(customer.getName());
                        txtv_email.setText(customer.getEmail());
                        txtv_phone_number.setText(customer.getPhoneNumber());
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