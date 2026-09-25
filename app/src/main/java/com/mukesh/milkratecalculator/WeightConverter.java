package com.mukesh.milkratecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class WeightConverter extends AppCompatActivity {

    private EditText etLtr, etDensity;
    private TextView tvKg;
    String ltr, density;
    float kg = 0.0F, dens;
    DecimalFormat f = new DecimalFormat("#.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_converter);
        setTitle("Weight Converter");
        etLtr = findViewById(R.id.etLtr);
        etDensity = findViewById(R.id.etDensity);

        tvKg = findViewById(R.id.tvKg);

    }

    public void btnOnClickConvert(View view) {

        ltr = etLtr.getText().toString().trim();
        density = etDensity.getText().toString().trim();

        if(density.isEmpty()){
            density = "1030";
        }
        if(ltr.isEmpty())
        {
            ltr = "0";
            etLtr.setError("Enter Liter");
        }
        else {
            dens = Float.parseFloat(density);
            kg = (Float.parseFloat(ltr) * dens)/1000;
            String kgs = Float.toString(kg);
            tvKg.setText(String.valueOf(kgs));
        }

    }
}