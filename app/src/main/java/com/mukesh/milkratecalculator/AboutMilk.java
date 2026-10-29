package com.mukesh.milkratecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AboutMilk extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_milk);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setTitle("About Milk");
    }


}