package com.mukesh.milkratecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mukesh.milkratecalculator.databinding.ActivityRateBinding;

import java.text.DecimalFormat;
import java.util.Date;

public class Avgfatsnf extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Spinner spiner;
    private EditText etQty1, etQty2, etQty3, etQty4, etQty5, etQty6, etQty7, etQty8, etQty9, etQty10, etStRate;
    private EditText etFat1, etFat2, etFat3, etFat4, etFat5, etFat6, etFat7, etFat8, etFat9, etFat10;
    private EditText etSnf1, etSnf2, etSnf3, etSnf4, etSnf5, etSnf6, etSnf7, etSnf8, etSnf9, etSnf10;

    private TextView tvTMlk,tvAvgFat,tvAvgSnf, tvAvgTAmt, tvRate,tvDate, tvShift;

    private TextView tvRate1, tvRate2, tvRate3, tvRate4, tvRate5, tvRate6, tvRate7, tvRate8, tvRate9, tvRate10;
    private TextView tvRs1, tvRs2, tvRs3, tvRs4, tvRs5, tvRs6, tvRs7, tvRs8, tvRs9, tvRs10;
    DecimalFormat f = new DecimalFormat("#.##");

    private String Qty1, Qty2, Qty3, Qty4, Qty5, Qty6, Qty7, Qty8, Qty9, Qty10;
    private String Fat1, Fat2, Fat3, Fat4, Fat5, Fat6, Fat7, Fat8, Fat9, Fat10;
    private String Snf1, Snf2, Snf3, Snf4, Snf5, Snf6, Snf7, Snf8, Snf9, Snf10;

    private String Rt1, Rt2, Rt3, Rt4, Rt5, Rt6, Rt7, Rt8, Rt9, Rt10;

    float fatAvg = 0.0F, snfAvg = 0.0F, totalMilk = 0.0F;
    float CalcRate, mRate, fsRatio = 50F, StRate;
    String AvgFat, AvgSnf, TotalMilk, stRate, totalAmt;
    String date;
    ActivityRateBinding binding;
    float Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, S1, S2, S3, S4, S5, S6, S7,S8, S9, S10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.etStRate.requestFocus();

        setContentView(R.layout.activity_avgfatsnf);
        setTitle("Average FAT, SNF");
        Date d = new Date();
        CharSequence dateTime  = DateFormat.format("d MMMM,yyyy", d.getTime());
        CharSequence hour  = DateFormat.format("HH", d.getTime());
        date = dateTime.toString().trim();

        etStRate = findViewById(R.id.etStRateAv);
        tvDate = findViewById(R.id.tvDate);

        tvDate.setText(date);

        tvShift = findViewById(R.id.tvShift);

        int hr = Integer.parseInt(hour.toString().trim());
        if( hr >= 12)
        {
            tvShift.setText("EVENING");
        }
        else
        {
            tvShift.setText("MORNING");
        }

        etQty1 = findViewById(R.id.etQty1);
        etQty2 = findViewById(R.id.etQty2);
        etQty3 = findViewById(R.id.etQty3);
        etQty4 = findViewById(R.id.etQty4);
        etQty5 = findViewById(R.id.etQty5);
        etQty6 = findViewById(R.id.etQty6);
        etQty7 = findViewById(R.id.etQty7);
        etQty8 = findViewById(R.id.etQty8);
        etQty9 = findViewById(R.id.etQty9);
        etQty10 = findViewById(R.id.etQty10);

        etFat1 = findViewById(R.id.etFat1);
        etFat2 = findViewById(R.id.etFat2);
        etFat3 = findViewById(R.id.etFat3);
        etFat4 = findViewById(R.id.etFat4);
        etFat5 = findViewById(R.id.etFat5);
        etFat6 = findViewById(R.id.etFat6);
        etFat7 = findViewById(R.id.etFat7);
        etFat8 = findViewById(R.id.etFat8);
        etFat9 = findViewById(R.id.etFat9);
        etFat10 = findViewById(R.id.etFat10);

        etSnf1 = findViewById(R.id.etSnf1);
        etSnf2 = findViewById(R.id.etSnf2);
        etSnf3 = findViewById(R.id.etSnf3);
        etSnf4 = findViewById(R.id.etSnf4);
        etSnf5 = findViewById(R.id.etSnf5);
        etSnf6 = findViewById(R.id.etSnf6);
        etSnf7 = findViewById(R.id.etSnf7);
        etSnf8 = findViewById(R.id.etSnf8);
        etSnf9 = findViewById(R.id.etSnf9);
        etSnf10 = findViewById(R.id.etSnf10);

        tvTMlk = findViewById(R.id.tvTMlk);
        tvAvgFat = findViewById(R.id.tvAvgFat);
        tvAvgSnf = findViewById(R.id.tvAvgSnf);
        tvAvgTAmt =findViewById(R.id.tvAvgTAmt);
        tvRate = findViewById(R.id.tvRate);

        tvRate1 = findViewById(R.id.tvRate1);
        tvRate2 = findViewById(R.id.tvRate2);
        tvRate3 = findViewById(R.id.tvRate3);
        tvRate4 = findViewById(R.id.tvRate4);
        tvRate5 = findViewById(R.id.tvRate5);
        tvRate6 = findViewById(R.id.tvRate6);
        tvRate7 = findViewById(R.id.tvRate7);
        tvRate8 = findViewById(R.id.tvRate8);
        tvRate9 = findViewById(R.id.tvRate9);
        tvRate10 = findViewById(R.id.tvRate10);

        tvRs1 = findViewById(R.id.tvRs1);
        tvRs2 = findViewById(R.id.tvRs2);
        tvRs3 = findViewById(R.id.tvRs3);
        tvRs4 = findViewById(R.id.tvRs4);
        tvRs5 = findViewById(R.id.tvRs5);
        tvRs6 = findViewById(R.id.tvRs6);
        tvRs7 = findViewById(R.id.tvRs7);
        tvRs8 = findViewById(R.id.tvRs8);
        tvRs9 = findViewById(R.id.tvRs9);
        tvRs10 = findViewById(R.id.tvRs10);

        spiner = findViewById(R.id.ratioAvg);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.fat_ratio, R.layout.spiner_color_layout);
        adapter.setDropDownViewResource(R.layout.spiner_dropdown_layout);
        spiner.setAdapter(adapter);
        spiner.setOnItemSelectedListener(this);

        numbermovetonext();
    }

    public void btnOnClickClear(View view) {

        etQty1.setText(" ");
        etQty2.setText(" ");
        etQty3.setText(" ");
        etQty4.setText(" ");
        etQty5.setText(" ");
        etQty6.setText(" ");
        etQty7.setText(" ");
        etQty8.setText(" ");
        etQty9.setText(" ");
        etQty10.setText(" ");

        etFat1.setText(" ");
        etFat2.setText(" ");
        etFat3.setText(" ");
        etFat4.setText(" ");
        etFat5.setText(" ");
        etFat6.setText(" ");
        etFat7.setText(" ");
        etFat8.setText(" ");
        etFat9.setText(" ");
        etFat10.setText(" ");

        etSnf1.setText(" ");
        etSnf2.setText(" ");
        etSnf3.setText(" ");
        etSnf4.setText(" ");
        etSnf5.setText(" ");
        etSnf6.setText(" ");
        etSnf7.setText(" ");
        etSnf8.setText(" ");
        etSnf9.setText(" ");
        etSnf10.setText(" ");

        tvTMlk.setText("0.00");
        tvAvgFat.setText("0.00");
        tvAvgSnf.setText("0.00");
        tvAvgTAmt.setText("0.00");
        tvRate.setText("0.00");

        tvRs1.setText("0.00");
        tvRs2.setText("0.00");
        tvRs3.setText("0.00");
        tvRs4.setText("0.00");
        tvRs5.setText("0.00");
        tvRs6.setText("0.00");
        tvRs7.setText("0.00");
        tvRs8.setText("0.00");
        tvRs9.setText("0.00");
        tvRs10.setText("0.00");

        tvRate1.setText("0.00");
        tvRate2.setText("0.00");
        tvRate3.setText("0.00");
        tvRate4.setText("0.00");
        tvRate5.setText("0.00");
        tvRate6.setText("0.00");
        tvRate7.setText("0.00");
        tvRate8.setText("0.00");
        tvRate9.setText("0.00");
        tvRate10.setText("0.00");

        etQty1.requestFocus();
    }

    public void btnCalculateAvgFatSnf(View view) {

        stRate = etStRate.getText().toString().trim();

        Qty1  = etQty1.getText().toString();
        Qty2  = etQty2.getText().toString();
        Qty3  = etQty3.getText().toString();
        Qty4  = etQty4.getText().toString();
        Qty5  = etQty5.getText().toString();
        Qty6  = etQty6.getText().toString();
        Qty7  = etQty7.getText().toString();
        Qty8  = etQty8.getText().toString();
        Qty9  = etQty9.getText().toString();
        Qty10 = etQty10.getText().toString();

        Fat1 = etFat1.getText().toString();
        Fat2 = etFat2.getText().toString();
        Fat3 = etFat3.getText().toString();
        Fat4 = etFat4.getText().toString();
        Fat5 = etFat5.getText().toString();
        Fat6 = etFat6.getText().toString();
        Fat7 = etFat7.getText().toString();
        Fat8 = etFat8.getText().toString();
        Fat9 = etFat9.getText().toString();
        Fat10 = etFat10.getText().toString();

        Snf1 = etSnf1.getText().toString();
        Snf2 = etSnf2.getText().toString();
        Snf3 = etSnf3.getText().toString();
        Snf4 = etSnf4.getText().toString();
        Snf5 = etSnf5.getText().toString();
        Snf6 = etSnf6.getText().toString();
        Snf7 = etSnf7.getText().toString();
        Snf8 = etSnf8.getText().toString();
        Snf9 = etSnf9.getText().toString();
        Snf10 = etSnf10.getText().toString();

        if(stRate.isEmpty())
        {
            stRate= "0";
            etStRate.setError("Enter ST Rate");
        }
        if(Qty1.isEmpty() || Qty1.equals(" ") )
        {
            Qty1 = "0";
        }
        if(Qty2.isEmpty() || Qty2.equals(" ") )
        {
            Qty2 = "0";
        }
        if(Qty3.isEmpty() || Qty3.equals(" ") )
        {
            Qty3 = "0";
        }
        if(Qty4.isEmpty() || Qty4.equals(" ") )
        {
            Qty4 = "0";
        }
        if(Qty5.isEmpty() || Qty5.equals(" ") )
        {
            Qty5 = "0";
        }
        if(Qty6.isEmpty() || Qty6.equals(" ") )
        {
            Qty6 = "0";
        }
        if(Qty7.isEmpty() || Qty7.equals(" ") )
        {
            Qty7 = "0";
        }
        if(Qty8.isEmpty() || Qty8.equals(" ") )
        {
            Qty8 = "0";
        }
        if(Qty9.isEmpty() || Qty9.equals(" ") )
        {
            Qty9 = "0";
        }
        if(Qty10.isEmpty() || Qty10.equals(" ") )
        {
            Qty10 = "0";
        }


        if(Snf1.isEmpty() || Snf1.equals(" "))
        {
            Snf1 = "0";
        }
        if(Snf2.isEmpty() || Snf2.equals(" "))
        {
            Snf2 = "0";
        }
        if(Snf3.isEmpty() || Snf3.equals(" "))
        {
            Snf3 = "0";
        }
        if(Snf4.isEmpty() || Snf4.equals(" "))
        {
            Snf4 = "0";
        }
        if(Snf5.isEmpty() || Snf5.equals(" "))
        {
            Snf5 = "0";
        }
        if(Snf6.isEmpty() || Snf6.equals(" "))
        {
            Snf6 = "0";
        }
        if(Snf7.isEmpty() || Snf7.equals(" "))
        {
            Snf7 = "0";
        }
        if(Snf8.isEmpty() || Snf8.equals(" "))
        {
            Snf8 = "0";
        }
        if(Snf9.isEmpty() || Snf9.equals(" "))
        {
            Snf9 = "0";
        }
        if(Snf10.isEmpty() || Snf10.equals(" "))
        {
            Snf10 = "0";
        }

        if(Fat1.isEmpty() || Fat1.equals(" "))
        {
            Fat1 = "0";
        }
        if(Fat2.isEmpty() || Fat2.equals(" "))
        {
            Fat2 = "0";
        }
        if(Fat3.isEmpty() || Fat3.equals(" "))
        {
            Fat3 = "0";
        }
        if(Fat4.isEmpty() || Fat4.equals(" "))
        {
            Fat4 = "0";
        }
        if(Fat5.isEmpty() || Fat5.equals(" "))
        {
            Fat5 = "0";
        }
        if(Fat6.isEmpty() || Fat6.equals(" "))
        {
            Fat6 = "0";
        }
        if(Fat7.isEmpty() || Fat7.equals(" "))
        {
            Fat7 = "0";
        }
        if(Fat8.isEmpty() || Fat8.equals(" "))
        {
            Fat8 = "0";
        }
        if(Fat9.isEmpty() || Fat9.equals(" "))
        {
            Fat9 = "0";
        }
        if(Fat10.isEmpty() || Fat10.equals(" "))
        {
            Fat10 = "0";
        }

        StRate = Float.parseFloat(stRate);

        Q1 = Float.parseFloat(Qty1);
        Q2 = Float.parseFloat(Qty2);
        Q3 = Float.parseFloat(Qty3);
        Q4 = Float.parseFloat(Qty4);
        Q5 = Float.parseFloat(Qty5);
        Q6 = Float.parseFloat(Qty6);
        Q7 = Float.parseFloat(Qty7);
        Q8 = Float.parseFloat(Qty8);
        Q9 = Float.parseFloat(Qty9);
        Q10 = Float.parseFloat(Qty10);

        F1 = Float.parseFloat(Fat1);
        F2 = Float.parseFloat(Fat2);
        F3 = Float.parseFloat(Fat3);
        F4 = Float.parseFloat(Fat4);
        F5 = Float.parseFloat(Fat5);
        F6 = Float.parseFloat(Fat6);
        F7 = Float.parseFloat(Fat7);
        F8 = Float.parseFloat(Fat8);
        F9 = Float.parseFloat(Fat9);
        F10 = Float.parseFloat(Fat10);

        S1 = Float.parseFloat(Snf1);
        S2 = Float.parseFloat(Snf2);
        S3 = Float.parseFloat(Snf3);
        S4 = Float.parseFloat(Snf4);
        S5 = Float.parseFloat(Snf5);
        S6 = Float.parseFloat(Snf6);
        S7 = Float.parseFloat(Snf7);
        S8 = Float.parseFloat(Snf8);
        S9 = Float.parseFloat(Snf9);
        S10 = Float.parseFloat(Snf10);

        totalMilk = Q1 + Q2 + Q3 + Q4 + Q5 + Q6 + Q7 + Q8 + Q9 + Q10;
        fatAvg = ((F1*Q1) + (F2*Q2) + (F3*Q3) + (F4*Q4) + (F5*Q5) + (F6*Q6) + (F7*Q7) + (F8*Q8) + (F9*Q9) + (F10*Q10))/totalMilk;
        fatAvg = Float.parseFloat(f.format(fatAvg));
        snfAvg = ((S1*Q1) + (S2*Q2) + (S3*Q3) + (S4*Q4) + (S5*Q5) + (S6*Q6) + (S7*Q7) + (S8*Q8) + (S9*Q9) + (S10*Q10))/totalMilk;
        snfAvg = Float.parseFloat(f.format(snfAvg));

        CalcRate = (float) ((((StRate * fsRatio/100) / 6.50) * (float) fatAvg) + (((StRate * (100 - fsRatio)/100 / 9.00) * (float) snfAvg)));
        CalcRate = Float.parseFloat(f.format(CalcRate));
        String Rate = Float.toString(CalcRate);

        float rate1 = Float.parseFloat(f.format((float) ((((StRate * fsRatio/100) / 6.50) * (float) F1) + (((StRate * (100 - fsRatio)/100 / 9.00) * (float) S1)))));
        float tAmt1 = Float.parseFloat(f.format(rate1 * Q1));
        Rt1 = String.valueOf(rate1);
        tvRate1.setText(Rt1);
        tvRs1.setText(String.valueOf(tAmt1));

        float rate2 = Float.parseFloat(f.format((float) ((((StRate * fsRatio/100) / 6.50) * (float) F2) + (((StRate * (100 - fsRatio)/100 / 9.00) * (float) S2)))));
        float tAmt2 = Float.parseFloat(f.format(rate2 * Q2));
        Rt2 = String.valueOf(rate2);
        tvRate2.setText(Rt2);
        tvRs2.setText(String.valueOf(tAmt2));

        float rate3 = Float.parseFloat(f.format((float) ((((StRate * fsRatio/100) / 6.50) * (float) F3) + (((StRate * (100 - fsRatio)/100 / 9.00) * (float) S3)))));
        float tAmt3 = Float.parseFloat(f.format(rate3 * Q3));
        Rt3 = String.valueOf(rate3);
        tvRate3.setText(Rt3);
        tvRs3.setText(String.valueOf(tAmt3));

        float rate4 = Float.parseFloat(f.format((float) ((((StRate * fsRatio/100) / 6.50) * (float) F4) + (((StRate * (100 - fsRatio)/100 / 9.00) * (float) S4)))));
        float tAmt4 = Float.parseFloat(f.format(rate4 * Q4));
        Rt4 = String.valueOf(rate4);
        tvRate4.setText(Rt4);
        tvRs4.setText(String.valueOf(tAmt4));

        float rate5 = Float.parseFloat(f.format((float) ((((StRate * fsRatio/100) / 6.50) * (float) F5) + (((StRate * (100 - fsRatio)/100 / 9.00) * (float) S5)))));
        float tAmt5 = Float.parseFloat(f.format(rate5 * Q5));
        Rt5 = String.valueOf(rate5);
        tvRate5.setText(Rt5);
        tvRs5.setText(String.valueOf(tAmt5));

        float rate6 = Float.parseFloat(f.format((float) ((((StRate * fsRatio/100) / 6.50) * (float) F6) + (((StRate * (100 - fsRatio)/100 / 9.00) * (float) S6)))));
        float tAmt6 = Float.parseFloat(f.format(rate6 * Q6));
        Rt6 = String.valueOf(rate6);
        tvRate6.setText(Rt6);
        tvRs6.setText(String.valueOf(tAmt6));

        float rate7 = Float.parseFloat(f.format((float) ((((StRate * fsRatio/100) / 6.50) * (float) F7) + (((StRate * (100 - fsRatio)/100 / 9.00) * (float) S7)))));
        float tAmt7 = Float.parseFloat(f.format(rate7 * Q7));
        Rt7 = String.valueOf(rate7);
        tvRate7.setText(Rt7);
        tvRs7.setText(String.valueOf(tAmt7));

        float rate8 = Float.parseFloat(f.format((float) ((((StRate * fsRatio/100) / 6.50) * (float) F8) + (((StRate * (100 - fsRatio)/100 / 9.00) * (float) S8)))));
        float tAmt8 = Float.parseFloat(f.format(rate8 * Q8));
        Rt8 = String.valueOf(rate8);
        tvRate8.setText(Rt8);
        tvRs8.setText(String.valueOf(tAmt8));

        float rate9 = Float.parseFloat(f.format((float) ((((StRate * fsRatio/100) / 6.50) * (float) F9) + (((StRate * (100 - fsRatio)/100 / 9.00) * (float) S9)))));
        float tAmt9 = Float.parseFloat(f.format(rate9 * Q9));
        Rt9 = String.valueOf(rate9);
        tvRate9.setText(Rt9);
        tvRs9.setText(String.valueOf(tAmt9));

        float rate10 = Float.parseFloat(f.format((float) ((((StRate * fsRatio/100) / 6.50) * (float) F10) + (((StRate * (100 - fsRatio)/100 / 9.00) * (float) S10)))));
        float tAmt10 = Float.parseFloat(f.format(rate10 * Q10));
        Rt10 = String.valueOf(rate10);
        tvRate10.setText(Rt10);
        tvRs10.setText(String.valueOf(tAmt10));

        totalAmt = Float.toString(Float.parseFloat(f.format(totalMilk * CalcRate)));

        AvgFat = Float.toString(fatAvg);
        AvgSnf = Float.toString(snfAvg);
        TotalMilk = Float.toString(Float.parseFloat((f.format(totalMilk))));

        tvTMlk.setText(TotalMilk);
        tvAvgFat.setText(AvgFat);
        tvAvgSnf.setText(AvgSnf);
        tvAvgTAmt.setText(totalAmt);
        tvRate.setText(Rate);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        if(i == 0)
            fsRatio = 50;
        if(i == 1)
            fsRatio = 52;
        if(i == 2)
            fsRatio = 60;

        //Toast.makeText(adapterView.getContext(), text,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        fsRatio = 50;
    }


    private void numbermovetonext() {
        etQty1.requestFocus();
        etFat1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==3)
                {
                    etSnf1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etSnf1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==3)
                {
                    etQty2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etFat2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==3)
                {
                    etSnf2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etSnf2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==3)
                {
                    etQty3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etFat3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==3)
                {
                    etSnf3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etSnf3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==3)
                {
                    etQty4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etFat4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==3)
                {
                    etSnf4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etSnf4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==3)
                {
                    etQty5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etFat5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==3)
                {
                    etSnf5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etSnf5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==3)
                {
                    etQty6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etFat6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==3)
                {
                    etSnf6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etSnf6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==3)
                {
                    etQty7.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etFat7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==3)
                {
                    etSnf7.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etSnf7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==3)
                {
                    etQty8.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etFat8.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==3)
                {
                    etSnf8.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etSnf8.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==3)
                {
                    etQty9.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etFat9.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==3)
                {
                    etSnf9.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etSnf9.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==3)
                {
                    etQty10.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etFat10.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==3)
                {
                    etSnf10.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}