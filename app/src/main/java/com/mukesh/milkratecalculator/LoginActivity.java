package com.mukesh.milkratecalculator;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hbb20.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {

    EditText etMobile;
    Button sendOtp, btnGoogleSignIn;
    String MobileNumber;
    CountryCodePicker codePicker;
    GoogleSignInClient googleSignInClient;
    ActivityResultLauncher<Intent> googleSignInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_login);

            etMobile  = findViewById(R.id.etMobilNumber);
            codePicker = findViewById(R.id.countryCode);
            sendOtp   = findViewById(R.id.SndOtp);
            btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);

            if (codePicker != null) {
                try {
                    codePicker.setDefaultCountryUsingNameCode("IN");
                    codePicker.setCountryForNameCode("IN");
                } catch (Exception ignored) {}
            }

            // Safely initialize Google Sign In without crashing if web_client_id is missing
            try {
                int resId = getResources().getIdentifier("default_web_client_id", "string", getPackageName());
                if (resId != 0) {
                    String webClientId = getString(resId);
                    if (!webClientId.isEmpty()) {
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(webClientId)
                                .requestEmail()
                                .build();
                        googleSignInClient = GoogleSignIn.getClient(this, gso);
                    }
                }
            } catch (Exception e) {
                googleSignInClient = null;
            }

            googleSignInLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                            try {
                                GoogleSignInAccount account = task.getResult(ApiException.class);
                                if (account != null) {
                                    firebaseAuthWithGoogle(account.getIdToken());
                                }
                            } catch (ApiException e) {
                                Toast.makeText(this, "Google Sign-In failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );

            if (btnGoogleSignIn != null) {
                btnGoogleSignIn.setOnClickListener(v -> {
                    if (googleSignInClient == null) {
                        Toast.makeText(this, "Google Sign-In requires configuring Web Client ID in google-services.json", Toast.LENGTH_LONG).show();
                        return;
                    }
                    try {
                        googleSignInClient.signOut().addOnCompleteListener(task -> {
                            Intent signInIntent = googleSignInClient.getSignInIntent();
                            googleSignInLauncher.launch(signInIntent);
                        });
                    } catch (Exception e) {
                        Toast.makeText(this, "Error starting Google Sign-In: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            if (sendOtp != null) {
                sendOtp.setOnClickListener(view -> {
                    String country_code = codePicker != null ? "+" + codePicker.getSelectedCountryCode() : "+91";
                    String mobileStr = etMobile != null ? etMobile.getText().toString().trim() : "";

                    if (!mobileStr.isEmpty()) {
                        if (mobileStr.length() == 10) {
                            MobileNumber = country_code + mobileStr;

                            Intent intent = new Intent(LoginActivity.this, VerifyOtp.class);
                            intent.putExtra("mobil", MobileNumber);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Enter 10 Digit No", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Enter Mobile No", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(this, "Startup Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        if (idToken == null) {
            Toast.makeText(this, "Google ID Token is null", Toast.LENGTH_SHORT).show();
            return;
        }
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, dashboard.class));
                        finish();
                    } else {
                        String errMsg = task.getException() != null ? task.getException().getMessage() : "";
                        Toast.makeText(LoginActivity.this, "Google Authentication Failed: " + errMsg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            if (firebaseUser != null) {
                startActivity(new Intent(LoginActivity.this, dashboard.class));
                finish();
            }
        } catch (Exception ignored) {}
    }
}