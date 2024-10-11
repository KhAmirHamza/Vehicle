package com.tripbd.driver.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tripbd.driver.R;
import com.tripbd.driver.model.Driver;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class ResetPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ResetPasswordActivity";

    MaterialButton btn_send_otp, btn_reset_password;
    TextInputLayout text_input_layout_phone_number,
            textInputLayout_enter_otp,
            textInputLayout_new_password,
            text_input_layout_country_code;
    ProgressBar reset_progressbar;
    String phoneNumberForFirebase;
    String mVerificationId;
    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        btn_send_otp = findViewById(R.id.btn_send_otp);
        btn_reset_password = findViewById(R.id.btn_reset_password);

        text_input_layout_phone_number = findViewById(R.id.text_input_layout_phone_number);
        textInputLayout_enter_otp = findViewById(R.id.textInputLayout_enter_otp);
        textInputLayout_new_password = findViewById(R.id.textInputLayout_new_password);
        text_input_layout_country_code = findViewById(R.id.text_input_layout_country_code);

        reset_progressbar = findViewById(R.id.reset_progressbar);






        btn_send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_send_otp.setVisibility(View.GONE);
                btn_reset_password.setVisibility(View.VISIBLE);
                // text_input_layout_country_code.setVisibility(View.GONE);
                // text_input_layout_phone_number.setVisibility(View.GONE);
                textInputLayout_enter_otp.setVisibility(View.VISIBLE);
                textInputLayout_new_password.setVisibility(View.VISIBLE);

                String countryCode = Objects.requireNonNull(text_input_layout_country_code.getEditText()).getText().toString();
                if (countryCode.charAt(0)=='+') countryCode = countryCode.substring(1);
                //Toast.makeText(this,countryCode, Toast.LENGTH_SHORT).show();
                String phoneNumber = countryCode+ Objects.requireNonNull(text_input_layout_phone_number.getEditText()).getText().toString();

                if (!text_input_layout_phone_number.getEditText().getText().toString().isEmpty()){
                    reset_progressbar.setVisibility(View.VISIBLE);
                    Log.d(TAG, "phone_number: "+phoneNumber);

                    mAuth = FirebaseAuth.getInstance();
                    sendVerificationCode(phoneNumber);
                }
                else {
                    Toast.makeText(ResetPasswordActivity.this, "Please insert valid phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = Objects.requireNonNull(textInputLayout_enter_otp.getEditText()).getText().toString();
                String newPassword = Objects.requireNonNull(textInputLayout_new_password.getEditText()).getText().toString();


                if (reset_progressbar.getVisibility() == View.VISIBLE) {
                    Toast.makeText(ResetPasswordActivity.this, "Task is in progress", Toast.LENGTH_SHORT).show();
                    return;
                }
                reset_progressbar.setVisibility(View.VISIBLE);
                if (newPassword.isEmpty()){
                    textInputLayout_new_password.getEditText().requestFocus();
                    return;
                }
                if (otp.length() == 6) {
                    signInWithPhoneAuthCredential(
                            (phoneNumberForFirebase.charAt(0)=='+')?
                                    phoneNumberForFirebase.substring(1)
                                    :
                                    phoneNumberForFirebase,
                            PhoneAuthProvider.getCredential(ResetPasswordActivity.this.mVerificationId, otp), newPassword
                    );
                    return;
                }
                Toast.makeText(ResetPasswordActivity.this, "Please insert 6 digit verification code", Toast.LENGTH_SHORT).show();
                reset_progressbar.setVisibility(View.GONE);

            }
        });
    }



    /* access modifiers changed from: private */
    public void sendVerificationCode(String phoneNumber) {
        this.mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                //signInWithPhoneAuthCredential(credential);
            }

            public void onVerificationFailed(FirebaseException e) {
                Log.d(TAG, "onVerificationFailed: " + phoneNumberForFirebase);
                Toast.makeText(ResetPasswordActivity.this, "Invalid phone number or try after sometime", Toast.LENGTH_SHORT).show();
                reset_progressbar.setVisibility(View.GONE);
                if (e != null) {
                    Log.d(TAG, "onVerificationFailed: "+e.getLocalizedMessage());
                }
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Log.d(TAG, "onVerificationFailed: FirebaseAuthInvalidCredentialsException: " + e.getLocalizedMessage());
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Log.d(TAG, "onVerificationFailed: FirebaseTooManyRequestsException: " + e.getLocalizedMessage());
                }
            }

            public void onCodeSent(@NonNull String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                reset_progressbar.setVisibility(View.GONE);




                Toast.makeText(ResetPasswordActivity.this, "Code Sent, Check Mobile, if not send resend Code", Toast.LENGTH_LONG).show();
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
    public void signInWithPhoneAuthCredential(String phoneNumber, PhoneAuthCredential credential, String newPassword) {

        reset_progressbar.setVisibility(View.VISIBLE);
        this.mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            public void onComplete(@NonNull Task<AuthResult> task) {
                reset_progressbar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithCredential:success");
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("driver").addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {

                            if (e != null) {
                                Log.w(TAG, "Listen failed: "+e.getMessage());
                                reset_progressbar.setVisibility(View.GONE);
                                return;
                            }

                            for (QueryDocumentSnapshot doc : Objects.<QuerySnapshot>requireNonNull(value)) {
                                Driver driverModel = doc.toObject(Driver.class);
                                if (driverModel.getName() != null && driverModel.getPhoneNumber().equals(phoneNumber)) {
                                    DocumentReference userRef = db.collection("driver").document(doc.getId());
                                    userRef.update("password", newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                    Toast.makeText(ResetPasswordActivity.this, "Password Successfully Changed!", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(ResetPasswordActivity.this, SignInActivity.class)
                                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                                }
                                            })

                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error updating document", e);
                                                }
                                            });
                                    break;
                                }
                            }
                            reset_progressbar.setVisibility(View.GONE);
                        }
                    });



                }
                else{
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(ResetPasswordActivity.this, "The verification code entered is invalid", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}