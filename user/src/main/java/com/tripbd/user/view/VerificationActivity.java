package com.tripbd.user.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import com.goodiebag.pinview.Pinview;
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
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tripbd.user.R;
import com.tripbd.user.model.UserModel;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {
    private static final String TAG = "VerificationActivity";
    String OTP_CODE;
    Button btn_resend_code;
    Button btn_verify_number;
    LinearLayout lay_1;
    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    String mVerificationId;
    String phoneNumber;
  //  Pinview pinview;

    ProgressBar progressbar;

    TextView txtv3;
    String phoneNumberForFirebase;

    static SignInClient oneTapClient;
    static BeginSignInRequest signInRequest;
    MaterialButton btn_continue_google;
    final int REQ_ONE_TAP = 100;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        Log.d(TAG, "phoneNumber: " + phoneNumber);
        btn_verify_number = (Button) findViewById(R.id.btn_verify_number);
        btn_resend_code = (Button) findViewById(R.id.btn_resend_code);
        txtv3 = (TextView) findViewById(R.id.txtv3);
      //  pinview = (Pinview) findViewById(R.id.pinview);
        lay_1 = (LinearLayout) findViewById(R.id.lay_1);
        btn_continue_google = findViewById(R.id.btn_continue_google2);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressbar = progressBar;
        progressBar.setVisibility(View.VISIBLE);
        this.txtv3.setText(phoneNumber);
        this.mAuth = FirebaseAuth.getInstance();
        sendVerificationCode(phoneNumber);
        btn_verify_number.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (progressbar.getVisibility() == View.VISIBLE) {
                    Toast.makeText(VerificationActivity.this, "Task is in progress", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressbar.setVisibility(View.VISIBLE);
                String otp = "";//VerificationActivity.this.pinview.getValue();
                if (otp.length() == 6) {
                    signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(VerificationActivity.this.mVerificationId, otp));
                    return;
                }
                Toast.makeText(VerificationActivity.this, "Please insert 6 digit verification code", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.GONE);
            }
        });
        this.btn_resend_code.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                Log.d(TAG, "onClick: phoneNumber: "+phoneNumber);
                resendVerificationCode('+'+phoneNumber,mResendToken);
                btn_resend_code.setVisibility(View.GONE);
                lay_1.setVisibility(View.VISIBLE);
               // pinview.setValue("");
            }
        });
//        this.pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
//            public void onDataEntered(Pinview pinview, boolean fromUser) {
//                OTP_CODE = pinview.getValue();
//            }
//        });

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

        btn_continue_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueWithGoogle();
            }
        });
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.verifyPhoneNumber(PhoneAuthOptions.newBuilder(mAuth)
                .setActivity(this)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(mCallbacks)
                .setForceResendingToken(token)
                .build());
    }



    /* access modifiers changed from: private */
    public void sendVerificationCode(String phoneNumber) {
        this.mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(VerificationActivity.TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            public void onVerificationFailed(FirebaseException e) {
                Log.d(VerificationActivity.TAG, "onVerificationFailed: " + phoneNumberForFirebase);
                Log.d(VerificationActivity.TAG, "onVerificationFailed: " + e.getMessage());
                Toast.makeText(VerificationActivity.this, "Invalid phone number or try after sometime", Toast.LENGTH_SHORT).show();
                lay_1.setVisibility(View.GONE);
                btn_continue_google.setVisibility(View.VISIBLE);
                btn_resend_code.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);
                if (e != null) {
                    Log.d(TAG, "onVerificationFailed: "+e.getLocalizedMessage());
                }
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Log.d(VerificationActivity.TAG, "onVerificationFailed: FirebaseAuthInvalidCredentialsException: " + e.getLocalizedMessage());
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Log.d(VerificationActivity.TAG, "onVerificationFailed: FirebaseTooManyRequestsException: " + e.getLocalizedMessage());
                }
            }

            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                Log.d(VerificationActivity.TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
                progressbar.setVisibility(View.GONE);
                if (verificationId != null && token != null) {
                    lay_1.setVisibility(View.GONE);
                    btn_resend_code.setVisibility(View.VISIBLE);
                    progressbar.setVisibility(View.GONE);
                }
                Toast.makeText(VerificationActivity.this, "Code Sent, Check Mobile, if not send resend Code", Toast.LENGTH_LONG).show();
            }

            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }
        };
        Log.d(TAG, "sendVerificationCode: phoneNumber: "+phoneNumber);
        phoneNumberForFirebase = '+'+phoneNumber;
        Log.d(TAG, "sendVerificationCode: phoneNumberForFirebase: "+phoneNumberForFirebase);
        PhoneAuthProvider.verifyPhoneNumber(PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumberForFirebase)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this).setCallbacks(this.mCallbacks).build());
    }

    /* access modifiers changed from: private */
    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        this.mAuth.signInWithCredential(credential).addOnCompleteListener((Activity) this, new OnCompleteListener<AuthResult>() {
            public void onComplete(Task<AuthResult> task) {
                progressbar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Log.d(VerificationActivity.TAG, "signInWithCredential:success");
                    //Toast.makeText(VerificationActivity.this, "signInWithCredential:success", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onComplete: phoneNumber: "+ phoneNumber);

                    //todo.......................
                    startActivity(new Intent(VerificationActivity.this, ProfileSetUpActivity.class)
                            .putExtra("phoneNumber", phoneNumber));
                    finish();
                }
                else{
                    Log.w(VerificationActivity.TAG, "signInWithCredential:failure", task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(VerificationActivity.this, "The verification code entered is invalid", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "Got displayname."+credential.getDisplayName());
                            Log.d(TAG, "Got uri."+credential.getProfilePictureUri());
                            Log.d(TAG, "Got username."+credential.getId());
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
                                            }
                                        }
                                    }
                                    if (user == 1) {
                                        //progressbar.setVisibility(View.GONE);
                                        signIn("", "", true, credential.getId());
                                    } else {
                                        startActivity(new Intent(VerificationActivity.this, ProfileSetUpActivity.class)
                                                .putExtra("email", credential.getId())
                                                .putExtra("name", credential.getDisplayName())
                                                .putExtra("imageUrl", credential.getProfilePictureUri().toString())
                                        );
                                        finish();
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


    void signIn(String phoneNumber, String password, boolean continueWithMail, String email){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("driver").addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
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
                if (value == null || value.getDocuments().size()<1) {
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(VerificationActivity.this, "Registration First...", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                }

                for (QueryDocumentSnapshot doc : Objects.<QuerySnapshot>requireNonNull(value)) {
                    UserModel UserModel = doc.toObject(UserModel.class);
                    if (UserModel.getName() != null) {
                        progressbar.setVisibility(View.GONE);

                        if (
                                (continueWithMail && UserModel.getEmail().equalsIgnoreCase(email))
                                        || (UserModel.getPhoneNumber().equalsIgnoreCase(phoneNumber) &&
                                        UserModel.getPassword().equalsIgnoreCase(password))
                        ) {
                            progressbar.setVisibility(View.GONE);
                            UserModel.setId(doc.getId());
                            SharedPreferences sharedPreferences = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
                            sharedPreferences.edit().putString("USER_PHONE_NUMBER", phoneNumber).apply();
                            sharedPreferences.edit().putString("USER_EMAIL", email).apply();
                            sharedPreferences.edit().putString("USER_NAME", UserModel.getName()).apply();
                            sharedPreferences.edit().putString("USER_IMAGE_URL", UserModel.getImageUrl()).apply();
                            sharedPreferences.edit().putString("USER_ID", doc.getId()).apply();
                            sharedPreferences.edit().putString("USER_PASSWORD", password).apply();
                            sharedPreferences.edit().putString("USER_ADDRESS", UserModel.getAddress()).apply();

                            startActivity(new Intent(VerificationActivity.this, MainActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            finish();
                        }
                    } else {
                        Toast.makeText(VerificationActivity.this, "Something went wrong, try again!", Toast.LENGTH_SHORT).show();
                        progressbar.setVisibility(View.GONE);
                    }
                }
            }
        });
    }


}
