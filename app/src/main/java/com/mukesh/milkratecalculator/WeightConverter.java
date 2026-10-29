package com.mukesh.milkratecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;

public class WeightConverter extends AppCompatActivity {

    private EditText etLtr;
    private Spinner spinnerLiquid;
    private TextView tvKg, tvDensityDisplay;
    String ltr;
    float kg = 0.0F, selectedDensity = 1030f;
    DecimalFormat f = new DecimalFormat("#.##");

    private final String[] liquidNames = {
            "Cow Milk (1030 g/L)",
            "Buffalo Milk (1032 g/L)",
            "Pure Water (1000 g/L)",
            "Skimmed Milk (1035 g/L)",
            "Cream (994 g/L)",
            "Honey (1420 g/L)",
            "Vegetable Oil (920 g/L)"
    };

    private final float[] liquidDensities = {
            1030f,
            1032f,
            1000f,
            1035f,
            994f,
            1420f,
            920f
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_converter);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setTitle("Weight Converter");

        etLtr = findViewById(R.id.etLtr);
        spinnerLiquid = findViewById(R.id.spinnerLiquid);
        tvKg = findViewById(R.id.tvKg);
        tvDensityDisplay = findViewById(R.id.tvDensityDisplay);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, liquidNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLiquid.setAdapter(adapter);

        spinnerLiquid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < liquidDensities.length) {
                    selectedDensity = liquidDensities[position];
                    if (tvDensityDisplay != null) {
                        tvDensityDisplay.setText("Density: " + (int) selectedDensity + " g/L");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedDensity = 1030f;
                if (tvDensityDisplay != null) {
                    tvDensityDisplay.setText("Density: 1030 g/L");
                }
            }
        });
    }

    public void btnOnClickConvert(View view) {
        ltr = etLtr.getText().toString().trim();

        if (ltr.isEmpty()) {
            ltr = "0";
            etLtr.setError("Enter Liter");
        }

        try {
            float liters = Float.parseFloat(ltr);
            kg = (liters * selectedDensity) / 1000.0f;
            kg = Float.parseFloat(f.format(kg));
            tvKg.setText(String.valueOf(kg));
            if (tvDensityDisplay != null) {
                tvDensityDisplay.setText("Density: " + (int) selectedDensity + " g/L");
            }
        } catch (NumberFormatException e) {
            tvKg.setText("0.00");
        }
    }
}
