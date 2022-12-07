package com.vehicle.customer.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.vehicle.customer.R;

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
    Pinview pinview;

    ProgressBar progressbar;

    TextView txtv3;
    String phoneNumberForFirebase;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        Log.d(TAG, "phoneNumber: " + phoneNumber);
        btn_verify_number = (Button) findViewById(R.id.btn_verify_number);
        btn_resend_code = (Button) findViewById(R.id.btn_resend_code);
        txtv3 = (TextView) findViewById(R.id.txtv3);
        pinview = (Pinview) findViewById(R.id.pinview);
        lay_1 = (LinearLayout) findViewById(R.id.lay_1);
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
                String otp = VerificationActivity.this.pinview.getValue();
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
                pinview.setValue("");
            }
        });
        this.pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                OTP_CODE = pinview.getValue();
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
                Toast.makeText(VerificationActivity.this, "Invalid phone number or try after sometime", Toast.LENGTH_SHORT).show();
                lay_1.setVisibility(View.GONE);
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
                    startActivity(new Intent(VerificationActivity.this, ProfileSetUpActivity.class)
                            .putExtra("phoneNumber", phoneNumber));
                    finish();
                    return;
                }
                Log.w(VerificationActivity.TAG, "signInWithCredential:failure", task.getException());
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(VerificationActivity.this, "The verification code entered is invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
