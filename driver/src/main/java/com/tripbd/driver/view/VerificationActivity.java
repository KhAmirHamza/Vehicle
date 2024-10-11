package com.tripbd.driver.view;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.tripbd.driver.R;
import com.tripbd.driver.model.Driver;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {
    private static final String TAG = "VerificationActivity";

    String OTP_CODE;
    Button btn_verify_number, btn_resend_code;
    TextView txtv3;
    com.chaos.view.PinView PinView;
    String phoneNumber;
    ProgressBar progressbar;
    LinearLayout lay_1,lay_5;
    String token = null;

    FirebaseAuth mAuth;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    static SignInClient oneTapClient;
    static BeginSignInRequest signInRequest;
    MaterialButton btn_continue_google;
    final int REQ_ONE_TAP = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        phoneNumber = getIntent().getStringExtra("phoneNumber");
        Log.d(TAG, "onClick: phoneNumber: " + phoneNumber);

        btn_verify_number = findViewById(R.id.btn_verify_number);
        btn_resend_code = findViewById(R.id.btn_resend_code);

        txtv3 = findViewById(R.id.txtv3);
        PinView = findViewById(R.id.pinview);
        lay_1 = findViewById(R.id.lay_1);
        progressbar = findViewById(R.id.progressbar);
        progressbar.setVisibility(View.VISIBLE);
        btn_continue_google = findViewById(R.id.btn_continue_google);


        txtv3.setText(phoneNumber);
        mAuth = FirebaseAuth.getInstance();
        sendVerificationCode(phoneNumber);


        btn_verify_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressbar.getVisibility() == View.VISIBLE) {
                    Toast.makeText(VerificationActivity.this, "Task is in progress", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressbar.setVisibility(View.VISIBLE);
                String otp = PinView.getText().toString();
                if (otp.length() == 6) {

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
                    signInWithPhoneAuthCredential(credential);

                    //PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationOTP, otp);
                    //signInWithPhoneAuthCredential(credential);

                } else {
                    Toast.makeText(VerificationActivity.this, "Please insert 6 digit verification code", Toast.LENGTH_SHORT).show();
                    progressbar.setVisibility(View.GONE);
                }
            }
        });

        btn_resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                sendVerificationCode(phoneNumber);
                btn_resend_code.setVisibility(View.GONE);
                lay_1.setVisibility(View.VISIBLE);
                PinView.setText("");
            }
        });

        PinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                OTP_CODE = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

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

        btn_continue_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueWithGoogle();
            }
        });
    }

    private void sendVerificationCode(String phoneNumber) {

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
               /* Log.d(TAG, "onVerificationFailed"+ e.getCause());
                Log.d(TAG, "onVerificationFailed"+ e.getLocalizedMessage());*/

                Log.d(TAG, "onVerificationFailed: PhoneNumber" + phoneNumber);
                Log.d(TAG, "onVerificationFailed: Message: " + e.getLocalizedMessage());
                //if (e.getCause() == null) {
                Toast.makeText(VerificationActivity.this, "Phone Number is invalid or try after sometime",Toast.LENGTH_SHORT).show();
                btn_continue_google.setVisibility(View.VISIBLE);

                lay_1.setVisibility(View.INVISIBLE);
                btn_resend_code.setVisibility(View.VISIBLE);
                progressbar.setVisibility(View.GONE);
                //}

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.d(TAG, "onVerificationFailed: FirebaseAuthInvalidCredentialsException: " + e.getLocalizedMessage());
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    Log.d(TAG, "onVerificationFailed: FirebaseTooManyRequestsException: " + e.getLocalizedMessage());
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                progressbar.setVisibility(View.GONE);
                Toast.makeText(VerificationActivity.this, "Code Sent, Check your Mobile.", Toast.LENGTH_SHORT).show();
                // ...
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }
        };
        Log.d(TAG, "sendVerificationCode: phoneNumber: "+phoneNumber);
        String phoneNumberForFirebase = '+' + phoneNumber;
        Log.d(TAG, "sendVerificationCode: phoneNumberForFirebase: "+phoneNumberForFirebase);

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumberForFirebase)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);


    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressbar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            //Toast.makeText(VerificationActivity.this, "signInWithCredential:success", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = task.getResult().getUser();
                            startActivity(new Intent(VerificationActivity.this, ProfileSetUpActivity.class)
                                    .putExtra("phoneNumber", phoneNumber));
                            finish();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
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
                            db.collection("driver").addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
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
                                            Driver driver = doc.toObject(Driver.class);
                                            if (driver.getEmail().equalsIgnoreCase(credential.getId())) {
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
                    Driver driver = doc.toObject(Driver.class);
                    if (driver.getName() != null) {
                        progressbar.setVisibility(View.GONE);

                        if (
                                (continueWithMail && driver.getEmail().equalsIgnoreCase(email))
                                        || (driver.getPhoneNumber().equalsIgnoreCase(phoneNumber) &&
                                        driver.getPassword().equalsIgnoreCase(password))
                        ) {
                            progressbar.setVisibility(View.GONE);
                            driver.setId(doc.getId());
                            SharedPreferences sharedPreferences = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
                            sharedPreferences.edit().putString("DRIVER_PHONE_NUMBER", phoneNumber).apply();
                            sharedPreferences.edit().putString("DRIVER_PHONE_EMAIL", driver.getEmail()).apply();
                            sharedPreferences.edit().putString("DRIVER_NAME", driver.getName()).apply();
                            sharedPreferences.edit().putString("DRIVER_ID", driver.getId()).apply();
                            sharedPreferences.edit().putString("DRIVER_PASSWORD", password).apply();
                            startActivity(new Intent(VerificationActivity.this, MainActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
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