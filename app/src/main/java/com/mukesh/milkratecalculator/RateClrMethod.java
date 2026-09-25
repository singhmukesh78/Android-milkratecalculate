package com.mukesh.milkratecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class RateClrMethod extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    private EditText etQtyClr, etFat, etCLR, etRatePfat, etRatePq;
    private TextView tvCalcSnf, tvCalGhee, tvCalPwdr, tvCalGheeAmt, tvCalPwdrAmt, tvCalTotalAmt;
    String QtyClr, Fat, CLR, RatePfat, ratePQ;
    float qty, ratePQtl, ratePfat, snf, ghee, gheeAmt, powder, powderAmt, clr, fat, totalAmt;
    boolean clrMethod = true;
    DecimalFormat f = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_clr_method);

        setTitle("Rate FAT CLR Method");

        etQtyClr = findViewById(R.id.etQtyClr);
        etFat = findViewById(R.id.etFATPer);
        etCLR = findViewById(R.id.etCLR);
        etRatePfat = findViewById(R.id.etRatePF);
        etRatePq = findViewById(R.id.etRatePQ);

        tvCalcSnf = findViewById(R.id.tvCalcSnf);
        tvCalGhee = findViewById(R.id.tvCalGhee);
        tvCalPwdr = findViewById(R.id.tvCalPwdr);
        tvCalGheeAmt = findViewById(R.id.tvCalGheeAmt);
        tvCalPwdrAmt = findViewById(R.id.tvCalPwdrAmt);
        tvCalTotalAmt = findViewById(R.id.tvCalTotalAmt);

        radioGroup = findViewById(R.id.radioGroup);

        updateInputFieldsState();
    }

    private void updateInputFieldsState() {
        View layoutRatePQ = findViewById(R.id.layoutRatePQ);
        View layoutRatePF = findViewById(R.id.layoutRatePF);

        if (layoutRatePQ != null && layoutRatePF != null) {
            if (clrMethod) {
                // SNF Method: Show Rate Per 100kg, Hide Rate Per FAT
                layoutRatePQ.setVisibility(View.VISIBLE);
                layoutRatePF.setVisibility(View.GONE);
                if (etRatePfat != null) etRatePfat.setText("");
            } else {
                // Direct Method: Show Rate Per FAT, Hide Rate Per 100kg
                layoutRatePF.setVisibility(View.VISIBLE);
                layoutRatePQ.setVisibility(View.GONE);
                if (etRatePq != null) etRatePq.setText("");
            }
        }
    }

    public void btnCalculateRateClr(View view) {
        QtyClr = etQtyClr.getText().toString().trim();
        Fat = etFat.getText().toString().trim();
        CLR = etCLR.getText().toString().trim();
        RatePfat = etRatePfat.getText().toString().trim();
        ratePQ = etRatePq.getText().toString().trim();

        if (QtyClr.isEmpty()) {
            QtyClr = "0";
            etQtyClr.setError("Enter Qty");
        }
        if (Fat.isEmpty()) {
            Fat = "0";
            etFat.setError("Enter FAT");
        }
        if (CLR.isEmpty()) {
            CLR = "0";
            etCLR.setError("Enter CLR");
        }

        if (clrMethod && ratePQ.isEmpty()) {
            etRatePq.setError("Enter Rate Per 100kg");
            return;
        }
        if (!clrMethod && RatePfat.isEmpty()) {
            etRatePfat.setError("Enter Rate Per FAT");
            return;
        }

        try {
            qty = QtyClr.isEmpty() ? 0f : Float.parseFloat(QtyClr);
            fat = Fat.isEmpty() ? 0f : Float.parseFloat(Fat);
            clr = CLR.isEmpty() ? 0f : Float.parseFloat(CLR);

            snf = (float) ((clr / 4.0) + (0.20 * fat) + 0.14);
            snf = Float.parseFloat(f.format(snf));

            ghee = (float) (qty * fat * 10);
            ghee = Float.parseFloat(f.format(ghee));

            powder = (float) (qty * snf * 10);
            powder = Float.parseFloat(f.format(powder));

            if (clrMethod) {
                ratePQtl = Float.parseFloat(ratePQ);

                gheeAmt = (float) (ghee * ((ratePQtl * 60 / 100) / 6.5) / 1000);
                gheeAmt = Float.parseFloat(f.format(gheeAmt));

                powderAmt = (float) (powder * ((ratePQtl * 40 / 100) / 8.5) / 1000);
                powderAmt = Float.parseFloat(f.format(powderAmt));
            } else {
                ratePfat = Float.parseFloat(RatePfat);
                float rateSt = ratePfat * 650;

                gheeAmt = (float) (ghee * ((rateSt * 60 / 100) / 6.5) / 1000);
                gheeAmt = Float.parseFloat(f.format(gheeAmt));

                powderAmt = (float) (powder * ((rateSt * 40 / 100) / 8.5) / 1000);
                powderAmt = Float.parseFloat(f.format(powderAmt));
            }

            totalAmt = gheeAmt + powderAmt;
            totalAmt = Float.parseFloat(f.format(totalAmt));

            tvCalcSnf.setText(snf + " %");
            tvCalGhee.setText(ghee + " gm");
            tvCalPwdr.setText(powder + " gm");
            tvCalGheeAmt.setText(String.valueOf(gheeAmt));
            tvCalPwdrAmt.setText(String.valueOf(powderAmt));
            tvCalTotalAmt.setText(String.valueOf(totalAmt));

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numeric values", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkButton(View view) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        if (radioId != -1) {
            radioButton = findViewById(radioId);
            if (radioButton != null && radioButton.getText() != null) {
                String buttonSel = radioButton.getText().toString().trim();
                clrMethod = buttonSel.equals("SNF Method");
                updateInputFieldsState();
            }
        }
    }

    public void btnOnClickClear(View view) {
        tvCalcSnf.setText("0.00 %");
        tvCalGhee.setText("0.00 gm");
        tvCalPwdr.setText("0.00 gm");
        tvCalGheeAmt.setText("0.00");
        tvCalPwdrAmt.setText("0.00");
        tvCalTotalAmt.setText("0.00");
        etQtyClr.setText("");
        etFat.setText("");
        etCLR.setText("");
        etRatePq.setText("");
        etRatePfat.setText("");
    }
}