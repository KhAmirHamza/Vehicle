package com.tripbd.user.view;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tripbd.user.DialogLaw;
import com.tripbd.user.R;
import com.tripbd.user.model.UserModel;
import com.tripbd.user.utils.SystemUI;

import org.json.JSONObject;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    int agree = 0;
    TextInputLayout text_input_layout_country_code, textInputLayout_phone_number, textInputLayout_password;
    MaterialButton btn_sign_in, btn_goto_sign_up, btn_goto_rest_password, btn_continue_google;
    MaterialToolbar signin_toolbar;
    ProgressBar progressbar;
    final int REQ_ONE_TAP = 100;  // Can be any integer unique to the Activity.
    static SignInClient oneTapClient;
    static BeginSignInRequest signInRequest;
    static FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        FirebaseApp.initializeApp(SignInActivity.this);

        progressbar = findViewById(R.id.progressbar);

        signin_toolbar = findViewById(R.id.signin_toolbar);
        signin_toolbar = findViewById(R.id.signin_toolbar);
        setSupportActionBar(signin_toolbar);
        setTitle("Sign In");

        text_input_layout_country_code = findViewById(R.id.text_input_layout_country_code);
        textInputLayout_phone_number = findViewById(R.id.text_input_layout_phone_number);
        textInputLayout_password = findViewById(R.id.textInputLayout_password);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_goto_sign_up = findViewById(R.id.btn_goto_sign_up);
        btn_goto_rest_password = findViewById(R.id.btn_goto_rest_password);
        btn_continue_google = findViewById(R.id.btn_continue_google);


        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String countryCode = text_input_layout_country_code.getEditText().getText().toString();
                if (countryCode.charAt(0)=='+') countryCode = countryCode.substring(1);
                String phoneNumber = textInputLayout_phone_number.getEditText().getText().toString();
                String password = textInputLayout_password.getEditText().getText().toString();

                if (countryCode.isEmpty()) {
                    text_input_layout_country_code.setError("Required.");
                    text_input_layout_country_code.requestFocus();
                    return;
                }else if (phoneNumber.isEmpty()) {
                    textInputLayout_phone_number.setError("Required.");
                    textInputLayout_phone_number.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    textInputLayout_password.setError("Required.");
                    textInputLayout_password.requestFocus();
                    return;
                }

                //todo...Log in...

                phoneNumber = countryCode + phoneNumber;
                progressbar .setVisibility(View.VISIBLE);
                Log.d(TAG, "onResponse: phone_number: "+phoneNumber);
                Log.d(TAG, "onClick: password: "+password);

//                if(agree<1) {
//                    DialogLaw dialogLaw = new DialogLaw(SignInActivity.this);
//                    dialogLaw.setCancelable(false);
//                    dialogLaw.show();
//                    agree++;
//                    return;
//                }

                signIn(phoneNumber, password, false , "");
                //verifyLogIn(phoneNumber, password);
            }
        });

        btn_goto_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });
        btn_goto_rest_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, ResetPasswordActivity.class));
            }
        });
        btn_continue_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueWithGoogle();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId("305024146036-6muj0a1fei595v9fa6koi67m0hj7r2p3.apps.googleusercontent.com")
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .setAutoSelectEnabled(true)
                .build();


    }
    void signIn(String phoneNumber, String password, boolean continueWithMail, String email){
        Log.d(TAG, "signIn: phoneNumber:"+phoneNumber+", password;"+password);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user").addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed: "+e.getMessage());
                    progressbar.setVisibility(View.GONE);
                    return;
                }
                //String countryCode = text_input_layout_country_code.getEditText().getText().toString();
                //if (countryCode.charAt(0)=='+') countryCode = countryCode.substring(1);
                // String phoneNumber = textInputLayout_phone_number.getEditText().getText().toString();
                // String password = textInputLayout_password.getEditText().getText().toString();
                //  phoneNumber = countryCode + phoneNumber;

                //assert value != null;
                else if (value == null || value.getDocuments().size()<1) {
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(SignInActivity.this, "Registration First...", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                }
                progressbar.setVisibility(View.GONE);

                for (QueryDocumentSnapshot doc : Objects.<QuerySnapshot>requireNonNull(value)) {
                    UserModel userModel = doc.toObject(UserModel.class);



                    if (userModel.getName() != null) {
                        if (
                                (continueWithMail && userModel.getEmail().equalsIgnoreCase(email))
                                        || (userModel.getPhoneNumber().equalsIgnoreCase(phoneNumber) &&
                                        userModel.getPassword().equalsIgnoreCase(password))
                        ) {

                            userModel.setId(doc.getId());
                            SharedPreferences sharedPreferences = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
                            sharedPreferences.edit().putString("USER_PHONE_NUMBER", userModel.getPhoneNumber()).apply();
                            sharedPreferences.edit().putString("USER_EMAIL", userModel.getEmail()).apply();
                            sharedPreferences.edit().putString("USER_NAME", userModel.getName()).apply();
                            sharedPreferences.edit().putString("USER_IMAGE_URL", userModel.getImageUrl()).apply();
                            sharedPreferences.edit().putString("USER_ID", userModel.getId()).apply();
                            sharedPreferences.edit().putString("USER_PASSWORD", userModel.getPassword()).apply();
                            sharedPreferences.edit().putString("USER_ADDRESS", userModel.getAddress()).apply();
                            startActivity(new Intent(SignInActivity.this, MainActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }
                    } else {
                       Toast.makeText(SignInActivity.this, "Something went wrong, try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);

        if(!sharedPreferences.getString("USER_PHONE_NUMBER", "null").equalsIgnoreCase("null") &&
                sharedPreferences.getString("USER_PASSWORD", null) != null) {
          //  startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
            //Toast.makeText(this, "User Sign in", Toast.LENGTH_SHORT).show();
        }
    }
    void continueWithGoogle(){
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        try {
                            startIntentSenderForResult(
                                    result.getPendingIntent().getIntentSender(), REQ_ONE_TAP,
                                    null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
                        Log.d(TAG, e.getLocalizedMessage());
                    }
                });

    }
    void verifyLogIn(String phoneNumber, String password){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user").addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w(TAG, "Listen failed: "+e.getMessage());
                    progressbar.setVisibility(View.GONE);
                    return;
                }
/*
                String countryCode = text_input_layout_country_code.getEditText().getText().toString();
                if (countryCode.charAt(0)=='+') countryCode = countryCode.substring(1);
                String phoneNumber = textInputLayout_phone_number.getEditText().getText().toString();
                String password = textInputLayout_password.getEditText().getText().toString();
                phoneNumber = countryCode + phoneNumber;*/

                //assert value != null;
                else if ( value==null || value.getDocuments().size()<1) {
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(SignInActivity.this, "Registration First...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                finish();
                }
                for (QueryDocumentSnapshot doc : Objects.<QuerySnapshot>requireNonNull(value)) {
                    UserModel UserModel = doc.toObject(UserModel.class);
                    if (UserModel.getName() != null) {
                        progressbar.setVisibility(View.GONE);

                        UserModel.setId(doc.getId());
                        if (UserModel.getPhoneNumber().equalsIgnoreCase(phoneNumber) &&
                                UserModel.getPassword().equalsIgnoreCase(password)) {
                            SharedPreferences sharedPreferences = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
                            sharedPreferences.edit().putString("USER_PHONE_NUMBER", phoneNumber).apply();
                            sharedPreferences.edit().putString("USER_EMAIL", UserModel.getEmail()).apply();
                            sharedPreferences.edit().putString("USER_NAME", UserModel.getName()).apply();
                            sharedPreferences.edit().putString("USER_IMAGE_URL", UserModel.getImageUrl()).apply();
                            sharedPreferences.edit().putString("USER_ID", UserModel.getId()).apply();
                            sharedPreferences.edit().putString("USER_PASSWORD", password).apply();
                            sharedPreferences.edit().putString("USER_ADDRESS", UserModel.getAddress()).apply();
                            startActivity(new Intent(SignInActivity.this, MainActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
                        } else {
                            Toast.makeText(SignInActivity.this, "Does not match Phone Number or Password", Toast.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(SignInActivity.this, "Something went wrong, try again!", Toast.LENGTH_SHORT).show();
                        progressbar.setVisibility(View.GONE);
                    }
                }
            }
        });

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            new SystemUI(SignInActivity.this).hideSystemUI();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_ONE_TAP:
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                    signInWithCredentials(credential);
                    /*String idToken = credential.getGoogleIdToken();
                    String username = credential.getId();
                    String password = credential.getPassword();


                    if (idToken !=  null) {
                        // Got an ID token from Google. Use it to authenticate
                        // with your backend.
                        Log.d(TAG, "Got ID token.: "+idToken);
                    }  if (password != null) {
                        // Got a saved username and password. Use them to authenticate
                        // with your backend.
                        Log.d(TAG, "Got username."+username);
                        Log.d(TAG, "Got password."+password);
                    }*/
                } catch (ApiException e) {

                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    // ...
                }
                break;
        }
    }
    void signInWithCredentials (SignInCredential credential){
        progressbar.setVisibility(View.VISIBLE);
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(credential.getGoogleIdToken(), null);

        mAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Log.d(TAG, "Got displayname."+credential.getDisplayName());
                           // Log.d(TAG, "Got uri."+credential.getProfilePictureUri());
                           // Log.d(TAG, "Got username."+credential.getId());
                            assert credential.getProfilePictureUri() != null;

                            //Check Email Is used or not start........
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("user").addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {

                                    if (e != null) {
                                        Log.w(TAG, "Listen failed.", e);
                                        return;
                                    }

                                    assert value != null;
                                    int user = 0;
                                    for (QueryDocumentSnapshot doc : Objects.<QuerySnapshot>requireNonNull(value)) {
                                        if (doc.get("name") != null) {
                                            UserModel UserModel = doc.toObject(UserModel.class);
                                            if (UserModel.getEmail().equalsIgnoreCase(credential.getId())) {
                                                user = 1;
                                                break;
                                            }
                                        }
                                    }
                                    if (user == 1) {
                                        //progressbar.setVisibility(View.GONE);
                                        signIn("", "", true, credential.getId());
                                    } else {
                                        Intent i = new Intent(SignInActivity.this, ProfileSetUpActivity.class)
                                                .putExtra("email", credential.getId())
                                                .putExtra("name", credential.getDisplayName())
                                                .putExtra("imageUrl", credential.getProfilePictureUri().toString());
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                    }
                                }
                            });
                            //Check Email Is Used or not end........

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                        }
                    }
                });
    }
}