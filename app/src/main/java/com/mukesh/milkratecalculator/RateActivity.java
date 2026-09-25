package com.mukesh.milkratecalculator;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class RateActivity extends AppCompatActivity {

    private EditText etStRate, etQty, etFat, etSnf;
    private TextView tvAmt, tvRate, tvClr;
    private RadioGroup radioGroupRatio;

    private float StRate, Qty, Fat, Snf, Amount;
    String mlkRate, TotalAmount;
    float CalcRate, fsRatio = 50F, clr = 0F;
    String stRate, qty, fat, snf, CLR;

    DecimalFormat f = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        setTitle("Rate FAT, SNF Method");

        etStRate = findViewById(R.id.etStRate);
        etQty = findViewById(R.id.etQty);
        etFat = findViewById(R.id.etFat);
        etSnf = findViewById(R.id.etSnf);
        tvRate = findViewById(R.id.tvRate);
        tvAmt = findViewById(R.id.tvAmt);
        tvClr = findViewById(R.id.tvClr);
        radioGroupRatio = findViewById(R.id.radioGroupRatio);

        if (radioGroupRatio != null) {
            radioGroupRatio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.radio5248) {
                        fsRatio = 52f;
                    } else if (checkedId == R.id.radio6040) {
                        fsRatio = 60f;
                    } else {
                        fsRatio = 50f;
                    }
                }
            });
        }

        etStRate.requestFocus();
        numbermovetonext();
    }

    public void btnCalculateRate(View view) {
        stRate = etStRate.getText().toString().trim();
        qty = etQty.getText().toString().trim();
        fat = etFat.getText().toString().trim();
        snf = etSnf.getText().toString().trim();

        if (radioGroupRatio != null) {
            int checkedId = radioGroupRatio.getCheckedRadioButtonId();
            if (checkedId == R.id.radio5248) {
                fsRatio = 52f;
            } else if (checkedId == R.id.radio6040) {
                fsRatio = 60f;
            } else {
                fsRatio = 50f;
            }
        }

        if (stRate.isEmpty()) {
            etStRate.setError("Enter ST Rate");
            return;
        }
        if (fat.isEmpty()) {
            etFat.setError("Enter FAT");
            return;
        }
        if (snf.isEmpty()) {
            etSnf.setError("Enter SNF");
            return;
        }

        if (qty.isEmpty()) {
            qty = "0";
        }

        try {
            StRate = Float.parseFloat(stRate);
            Qty = Float.parseFloat(qty);
            Fat = Float.parseFloat(fat);
            Snf = Float.parseFloat(snf);

            CalcRate = (float) ((((StRate * fsRatio / 100) / 6.50) * Fat) + (((StRate * (100 - fsRatio) / 100 / 9.00) * Snf)));
            CalcRate = Float.parseFloat(f.format(CalcRate));

            mlkRate = Float.toString(CalcRate);
            tvRate.setText(mlkRate);

            clr = (float) (4 * (Snf - (0.20 * Fat) - 0.14));
            clr = Float.parseFloat(f.format(clr));
            CLR = Float.toString(clr);
            tvClr.setText(CLR);

            Amount = Qty * CalcRate;
            Amount = Float.parseFloat(f.format(Amount));
            TotalAmount = Float.toString(Amount);
            tvAmt.setText(TotalAmount);

            Toast.makeText(RateActivity.this, "Rate Calculated!", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numeric values", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnOnClickClear(View view) {
        etStRate.setText("");
        etQty.setText("");
        etFat.setText("");
        etSnf.setText("");
        tvAmt.setText("0.00");
        tvRate.setText("0.00");
        tvClr.setText("0.00");
        etFat.requestFocus();
    }

    private void numbermovetonext() {
        etFat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 3) {
                    etSnf.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        etSnf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 3) {
                    etQty.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
}