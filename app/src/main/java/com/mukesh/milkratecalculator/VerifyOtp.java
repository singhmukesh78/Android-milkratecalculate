package com.mukesh.milkratecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOtp extends AppCompatActivity {

    EditText etOtp1, etOtp2, etOtp3, etOtp4, etOtp5, etOtp6;
    TextView tvMobil;
    Button resendOtp, verifyOtp;
    String MobilNumber, otpid;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        mAuth = FirebaseAuth.getInstance();
        if (getIntent() != null && getIntent().hasExtra("mobil")) {
            String mobil = getIntent().getStringExtra("mobil");
            MobilNumber = mobil != null ? mobil : "";
        } else {
            MobilNumber = "";
        }

        tvMobil = findViewById(R.id.tvMobil);
        tvMobil.setText(MobilNumber);

        etOtp1 = findViewById(R.id.otp1);
        etOtp2 = findViewById(R.id.otp2);
        etOtp3 = findViewById(R.id.otp3);
        etOtp4 = findViewById(R.id.otp4);
        etOtp5 = findViewById(R.id.otp5);
        etOtp6 = findViewById(R.id.otp6);

        verifyOtp = findViewById(R.id.verifyOtp);

        sendOtp();

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etOtp1.getText().toString().isEmpty() && !etOtp2.getText().toString().isEmpty()  && !etOtp3.getText().toString().isEmpty()  && !etOtp4.getText().toString().isEmpty() && !etOtp5.getText().toString().isEmpty() && !etOtp6.getText().toString().isEmpty()) {
                    String enterCodeOtp = etOtp1.getText().toString().trim() + etOtp2.getText().toString().trim() + etOtp3.getText().toString().trim() + etOtp4.getText().toString().trim() + etOtp5.getText().toString().trim() + etOtp6.getText().toString().trim();
                    if (otpid != null && !otpid.isEmpty()) {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, enterCodeOtp);
                        signInWithPhoneAuthCredential(credential);
                        Toast.makeText(getApplicationContext(), "Verifying OTP...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please wait for OTP to be sent", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                }

            }
        });

        numberotpmove();
    }

    private void numberotpmove() {
        etOtp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty())
                {
                    etOtp2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        etOtp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty())
                {
                    etOtp3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        etOtp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty())
                {
                    etOtp4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        etOtp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty())
                {
                    etOtp5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        etOtp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty())
                {
                    etOtp6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void sendOtp() {
        if (MobilNumber == null || MobilNumber.isEmpty()) {
            Toast.makeText(this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(MobilNumber)
                        .setTimeout(30L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                otpid = s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Toast.makeText(getApplicationContext(), "onVerificationCompleted", Toast.LENGTH_LONG).show();
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(getApplicationContext(), "Verification Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (!isFinishing() && !isDestroyed()) {
                                Intent intent = new Intent(VerifyOtp.this, dashboard.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            String errorMsg = task.getException() != null ? task.getException().getMessage() : "OTP Mismatch";
                            Toast.makeText(getApplicationContext(), "Verification Failed: " + errorMsg, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}