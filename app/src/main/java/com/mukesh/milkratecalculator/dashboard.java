package com.mukesh.milkratecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class dashboard extends AppCompatActivity {

    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        adView = findViewById(R.id.adView);
        if (adView != null) {
            MobileAds.initialize(this);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        loadLocale();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.dashboardmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id  == R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(dashboard.this,LoginActivity.class));
            finish();
        }
        if(id  == R.id.english)
        {
            changeLanguage("en");
        }
        if(id  == R.id.gujrati)
        {
            changeLanguage("gu");
        }
        if(id  == R.id.marathi)
        {
            changeLanguage("mr");
        }

        if(id  == R.id.punjabi)
        {
            changeLanguage("pa");
        }

        if(id  == R.id.tamil)
        {
            changeLanguage("ta");
        }
        if(id  == R.id.telgu)
        {
            changeLanguage("te");
        }
        if(id  == R.id.hindi)
        {
            changeLanguage("hi");
        }

        return super.onOptionsItemSelected(item);
    }

    private void changeLanguage(String language)
    {
        SetLocale(language);
        recreate();
    }

    private void SetLocale(String language)
    {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("app_lang", language);
        editor.apply();
    }

    private void loadLocale()
    {
        SharedPreferences preferences  = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = preferences.getString("app_lang", "");
        if (!language.isEmpty()) {
            SetLocale(language);
        }
    }

    public void btnCalculateRateClick(View view) {
        startActivity(new Intent(dashboard.this, RateActivity.class));
    }

    public void btnCalAvgFatSnfClick(View view) {
        startActivity(new Intent(dashboard.this, Avgfatsnf.class));
    }

    public void btnConvKgToLtrClick(View view)
    {
        startActivity(new Intent(dashboard.this, WeightConverter.class));
    }

    public void btnCalAvgFatClrClick(View view) {
        startActivity(new Intent(dashboard.this, AvgFatClr.class));
    }

    public void btnCalRateFatClrClick(View view) {
        startActivity(new Intent(dashboard.this, RateClrMethod.class));
    }

    public void btnAboutMilkClick(View view) {
        startActivity(new Intent(dashboard.this, AboutMilk.class));
    }
}