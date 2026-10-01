package com.mukesh.milkratecalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AvgFatClr extends AppCompatActivity {

    private EditText etQty1, etQty2, etQty3, etQty4, etQty5, etStRate;
    private EditText etFat1, etFat2, etFat3, etFat4, etFat5;
    private EditText etClr1, etClr2, etClr3, etClr4, etClr5;

    private TextView tvTMlk, tvAvgFat, tvAvgClr, tvAvgTAmt, tvRate, tvDate, tvShift;

    private TextView tvRate1, tvRate2, tvRate3, tvRate4, tvRate5;
    private TextView tvRs1, tvRs2, tvRs3, tvRs4, tvRs5;
    DecimalFormat f = new DecimalFormat("#.##");

    private String Qty1, Qty2, Qty3, Qty4, Qty5;
    private String Fat1, Fat2, Fat3, Fat4, Fat5;
    private String Clr1, Clr2, Clr3, Clr4, Clr5;
    private String Rt1, Rt2, Rt3, Rt4, Rt5;

    float fatAvg = 0.0F, clrAvg = 0.0F, totalMilk = 0.0F;
    float CalcRate, StRate, fsRatio = 50F;
    String AvgFat, AvgClr, TotalMilk, stRate, totalAmt;
    String date;
    float Q1, Q2, Q3, Q4, Q5, F1, F2, F3, F4, F5, C1, C2, C3, C4, C5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avg_fat_clr);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setTitle("Average FAT, CLR");

        AdView adView = findViewById(R.id.adView);
        if (adView != null) {
            MobileAds.initialize(this);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        Date d = new Date();
        CharSequence dateTime = DateFormat.format("d MMMM,yyyy", d.getTime());
        CharSequence hour = DateFormat.format("HH", d.getTime());
        date = dateTime.toString().trim();

        etStRate = findViewById(R.id.etStRateAv);
        tvDate = findViewById(R.id.tvDate);
        tvDate.setText(date);

        tvShift = findViewById(R.id.tvShift);
        int hr = Integer.parseInt(hour.toString().trim());
        if (hr >= 12) {
            tvShift.setText("EVENING");
        } else {
            tvShift.setText("MORNING");
        }

        etQty1 = findViewById(R.id.etQty1);
        etQty2 = findViewById(R.id.etQty2);
        etQty3 = findViewById(R.id.etQty3);
        etQty4 = findViewById(R.id.etQty4);
        etQty5 = findViewById(R.id.etQty5);

        etFat1 = findViewById(R.id.etFat1);
        etFat2 = findViewById(R.id.etFat2);
        etFat3 = findViewById(R.id.etFat3);
        etFat4 = findViewById(R.id.etFat4);
        etFat5 = findViewById(R.id.etFat5);

        etClr1 = findViewById(R.id.etClr1);
        etClr2 = findViewById(R.id.etClr2);
        etClr3 = findViewById(R.id.etClr3);
        etClr4 = findViewById(R.id.etClr4);
        etClr5 = findViewById(R.id.etClr5);

        tvTMlk = findViewById(R.id.tvTMlk);
        tvAvgFat = findViewById(R.id.tvAvgFat);
        tvAvgClr = findViewById(R.id.tvAvgClr);
        tvAvgTAmt = findViewById(R.id.tvAvgTAmt);
        tvRate = findViewById(R.id.tvRate);

        tvRate1 = findViewById(R.id.tvRate1);
        tvRate2 = findViewById(R.id.tvRate2);
        tvRate3 = findViewById(R.id.tvRate3);
        tvRate4 = findViewById(R.id.tvRate4);
        tvRate5 = findViewById(R.id.tvRate5);

        tvRs1 = findViewById(R.id.tvRs1);
        tvRs2 = findViewById(R.id.tvRs2);
        tvRs3 = findViewById(R.id.tvRs3);
        tvRs4 = findViewById(R.id.tvRs4);
        tvRs5 = findViewById(R.id.tvRs5);

        numbermovetonext();
    }

    public void btnCalculateAvgFatClr(View view) {
        stRate = etStRate.getText().toString().trim();

        Qty1 = etQty1.getText().toString();
        Qty2 = etQty2.getText().toString();
        Qty3 = etQty3.getText().toString();
        Qty4 = etQty4.getText().toString();
        Qty5 = etQty5.getText().toString();

        Fat1 = etFat1.getText().toString();
        Fat2 = etFat2.getText().toString();
        Fat3 = etFat3.getText().toString();
        Fat4 = etFat4.getText().toString();
        Fat5 = etFat5.getText().toString();

        Clr1 = etClr1.getText().toString();
        Clr2 = etClr2.getText().toString();
        Clr3 = etClr3.getText().toString();
        Clr4 = etClr4.getText().toString();
        Clr5 = etClr5.getText().toString();

        if (stRate.isEmpty()) {
            stRate = "0";
            etStRate.setError("Enter ST Rate");
        }
        if (Qty1.isEmpty() || Qty1.equals(" ")) Qty1 = "0";
        if (Qty2.isEmpty() || Qty2.equals(" ")) Qty2 = "0";
        if (Qty3.isEmpty() || Qty3.equals(" ")) Qty3 = "0";
        if (Qty4.isEmpty() || Qty4.equals(" ")) Qty4 = "0";
        if (Qty5.isEmpty() || Qty5.equals(" ")) Qty5 = "0";

        if (Clr1.isEmpty() || Clr1.equals(" ")) Clr1 = "0";
        if (Clr2.isEmpty() || Clr2.equals(" ")) Clr2 = "0";
        if (Clr3.isEmpty() || Clr3.equals(" ")) Clr3 = "0";
        if (Clr4.isEmpty() || Clr4.equals(" ")) Clr4 = "0";
        if (Clr5.isEmpty() || Clr5.equals(" ")) Clr5 = "0";

        if (Fat1.isEmpty() || Fat1.equals(" ")) Fat1 = "0";
        if (Fat2.isEmpty() || Fat2.equals(" ")) Fat2 = "0";
        if (Fat3.isEmpty() || Fat3.equals(" ")) Fat3 = "0";
        if (Fat4.isEmpty() || Fat4.equals(" ")) Fat4 = "0";
        if (Fat5.isEmpty() || Fat5.equals(" ")) Fat5 = "0";

        StRate = Float.parseFloat(stRate);

        Q1 = Float.parseFloat(Qty1);
        Q2 = Float.parseFloat(Qty2);
        Q3 = Float.parseFloat(Qty3);
        Q4 = Float.parseFloat(Qty4);
        Q5 = Float.parseFloat(Qty5);

        F1 = Float.parseFloat(Fat1);
        F2 = Float.parseFloat(Fat2);
        F3 = Float.parseFloat(Fat3);
        F4 = Float.parseFloat(Fat4);
        F5 = Float.parseFloat(Fat5);

        C1 = Float.parseFloat(Clr1);
        C2 = Float.parseFloat(Clr2);
        C3 = Float.parseFloat(Clr3);
        C4 = Float.parseFloat(Clr4);
        C5 = Float.parseFloat(Clr5);

        totalMilk = Q1 + Q2 + Q3 + Q4 + Q5;
        if (totalMilk > 0) {
            fatAvg = ((F1 * Q1) + (F2 * Q2) + (F3 * Q3) + (F4 * Q4) + (F5 * Q5)) / totalMilk;
            clrAvg = ((C1 * Q1) + (C2 * Q2) + (C3 * Q3) + (C4 * Q4) + (C5 * Q5)) / totalMilk;
        } else {
            fatAvg = 0f;
            clrAvg = 0f;
        }
        fatAvg = Float.parseFloat(f.format(fatAvg));
        clrAvg = Float.parseFloat(f.format(clrAvg));

        CalcRate = (float) ((((StRate * fsRatio / 100) / 6.50) * (float) fatAvg) + (((StRate * (100 - fsRatio) / 100 / 9.00) * (float) clrAvg)));
        CalcRate = Float.parseFloat(f.format(CalcRate));
        String Rate = Float.toString(CalcRate);

        float rate1 = Float.parseFloat(f.format((float) ((((StRate * fsRatio / 100) / 6.50) * (float) F1) + (((StRate * (100 - fsRatio) / 100 / 9.00) * (float) C1)))));
        float tAmt1 = Float.parseFloat(f.format(rate1 * Q1));
        Rt1 = String.valueOf(rate1);
        tvRate1.setText(Rt1);
        tvRs1.setText(String.valueOf(tAmt1));

        float rate2 = Float.parseFloat(f.format((float) ((((StRate * fsRatio / 100) / 6.50) * (float) F2) + (((StRate * (100 - fsRatio) / 100 / 9.00) * (float) C2)))));
        float tAmt2 = Float.parseFloat(f.format(rate2 * Q2));
        Rt2 = String.valueOf(rate2);
        tvRate2.setText(Rt2);
        tvRs2.setText(String.valueOf(tAmt2));

        float rate3 = Float.parseFloat(f.format((float) ((((StRate * fsRatio / 100) / 6.50) * (float) F3) + (((StRate * (100 - fsRatio) / 100 / 9.00) * (float) C3)))));
        float tAmt3 = Float.parseFloat(f.format(rate3 * Q3));
        Rt3 = String.valueOf(rate3);
        tvRate3.setText(Rt3);
        tvRs3.setText(String.valueOf(tAmt3));

        float rate4 = Float.parseFloat(f.format((float) ((((StRate * fsRatio / 100) / 6.50) * (float) F4) + (((StRate * (100 - fsRatio) / 100 / 9.00) * (float) C4)))));
        float tAmt4 = Float.parseFloat(f.format(rate4 * Q4));
        Rt4 = String.valueOf(rate4);
        tvRate4.setText(Rt4);
        tvRs4.setText(String.valueOf(tAmt4));

        float rate5 = Float.parseFloat(f.format((float) ((((StRate * fsRatio / 100) / 6.50) * (float) F5) + (((StRate * (100 - fsRatio) / 100 / 9.00) * (float) C5)))));
        float tAmt5 = Float.parseFloat(f.format(rate5 * Q5));
        Rt5 = String.valueOf(rate5);
        tvRate5.setText(Rt5);
        tvRs5.setText(String.valueOf(tAmt5));

        totalAmt = Float.toString(Float.parseFloat(f.format(totalMilk * CalcRate)));

        AvgFat = Float.toString(fatAvg);
        AvgClr = Float.toString(clrAvg);
        TotalMilk = Float.toString(Float.parseFloat((f.format(totalMilk))));

        tvTMlk.setText(TotalMilk);
        tvAvgFat.setText(AvgFat);
        tvAvgClr.setText(AvgClr);
        tvAvgTAmt.setText(totalAmt);
        tvRate.setText(Rate);
    }

    public void btnOnClickClear(View view) {
        etQty1.setText(" ");
        etQty2.setText(" ");
        etQty3.setText(" ");
        etQty4.setText(" ");
        etQty5.setText(" ");

        etFat1.setText(" ");
        etFat2.setText(" ");
        etFat3.setText(" ");
        etFat4.setText(" ");
        etFat5.setText(" ");

        etClr1.setText(" ");
        etClr2.setText(" ");
        etClr3.setText(" ");
        etClr4.setText(" ");
        etClr5.setText(" ");

        tvTMlk.setText("0.00");
        tvAvgFat.setText("0.00");
        tvAvgClr.setText("0.00");
        tvAvgTAmt.setText("0.00");
        tvRate.setText("0.00");

        tvRs1.setText("0.00");
        tvRs2.setText("0.00");
        tvRs3.setText("0.00");
        tvRs4.setText("0.00");
        tvRs5.setText("0.00");

        tvRate1.setText("0.00");
        tvRate2.setText("0.00");
        tvRate3.setText("0.00");
        tvRate4.setText("0.00");
        tvRate5.setText("0.00");

        etQty1.requestFocus();
    }

    public void downloadPdfReport(View view) {
        String stRateVal = etStRate.getText().toString().trim();
        if (stRateVal.isEmpty()) {
            Toast.makeText(this, "Please enter Standard Rate first", Toast.LENGTH_SHORT).show();
            return;
        }

        String tMlkVal = tvTMlk.getText().toString().trim();
        String avgFatVal = tvAvgFat.getText().toString().trim();
        String avgClrVal = tvAvgClr.getText().toString().trim();
        String avgRateVal = tvRate.getText().toString().trim();
        String totalAmtVal = tvAvgTAmt.getText().toString().trim();

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        int startX = 50;
        int startY = 50;
        int tableWidth = 495;

        // Title
        paint.setFakeBoldText(true);
        paint.setTextSize(20);
        paint.setColor(Color.parseColor("#00796B"));
        canvas.drawText("Batch Milk Collection Report (FAT & CLR)", startX, startY, paint);

        startY += 20;
        paint.setFakeBoldText(false);
        paint.setTextSize(11);
        paint.setColor(Color.GRAY);
        canvas.drawText("Generated on: " + new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(new Date()) + " | Shift: " + tvShift.getText().toString(), startX, startY, paint);

        startY += 30;

        int givenDataColor = Color.parseColor("#4F46E5");
        int greenColor = Color.parseColor("#16A34A");

        // Table 1: Parameters & Method
        startY = drawTable(canvas, paint, startX, startY, tableWidth, "1. Batch Parameters & Method", new String[][]{
                {"Parameter", "Value"},
                {"Standard Rate", "₹ " + stRateVal},
                {"Calculation Method", "FAT & CLR Ratio Method"},
                {"Date", tvDate.getText().toString()}
        }, new int[]{1, 2, 3}, new int[]{givenDataColor, greenColor, givenDataColor});

        startY += 16;

        // Table 2: Batch Summary Results
        startY = drawTable(canvas, paint, startX, startY, tableWidth, "2. Batch Calculation Results", new String[][]{
                {"Result Metric", "Value"},
                {"Total Milk Quantity", tMlkVal + " liters"},
                {"Average Rate", "₹ " + avgRateVal + " / liter"},
                {"Average FAT", avgFatVal + " %"},
                {"Average CLR", avgClrVal},
                {"Total Amount", "₹ " + totalAmtVal}
        }, new int[]{1, 2, 3, 4, 5}, new int[]{Color.parseColor("#0284C7"), Color.parseColor("#0284C7"), Color.parseColor("#0284C7"), Color.parseColor("#0284C7"), Color.parseColor("#16A34A")});

        pdfDocument.finishPage(page);

        try {
            OutputStream fos;
            Uri pdfUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, "MilkBatchClrReport_" + System.currentTimeMillis() + ".pdf");
                values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS);
                values.put(MediaStore.MediaColumns.IS_PENDING, 1);

                Uri collection = MediaStore.Files.getContentUri("external");
                pdfUri = getContentResolver().insert(collection, values);
                if (pdfUri != null) {
                    fos = getContentResolver().openOutputStream(pdfUri);
                    pdfDocument.writeTo(fos);
                    if (fos != null) {
                        fos.close();
                    }
                    pdfDocument.close();

                    values.clear();
                    values.put(MediaStore.MediaColumns.IS_PENDING, 0);
                    getContentResolver().update(pdfUri, values, null, null);
                } else {
                    throw new IOException("Failed to create MediaStore entry.");
                }
            } else {
                File documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                if (!documentsDir.exists()) {
                    documentsDir.mkdirs();
                }
                File file = new File(documentsDir, "MilkBatchClrReport_" + System.currentTimeMillis() + ".pdf");
                fos = new FileOutputStream(file);
                pdfDocument.writeTo(fos);
                if (fos != null) {
                    fos.close();
                }
                pdfDocument.close();
                pdfUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
            }

            Toast.makeText(this, "PDF saved to Documents folder", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No PDF viewer application found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
            pdfDocument.close();
        }
    }

    private int drawTable(Canvas canvas, Paint paint, int startX, int startY, int tableWidth, String title, String[][] rows, int[] highlightRows, int[] highlightColors) {
        paint.setFakeBoldText(true);
        paint.setTextSize(13);
        paint.setColor(Color.parseColor("#00796B"));
        canvas.drawText(title, startX, startY, paint);
        startY += 8;

        int rowHeight = 24;
        int col1Width = tableWidth / 2;

        Paint linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#E2E8F0"));
        linePaint.setStrokeWidth(1);

        Paint headerBgPaint = new Paint();
        headerBgPaint.setColor(Color.parseColor("#E2E8F0"));

        Paint altRowPaint = new Paint();
        altRowPaint.setColor(Color.parseColor("#F8FAFC"));

        Paint cellTextPaint = new Paint();
        cellTextPaint.setAntiAlias(true);
        cellTextPaint.setTextSize(11);

        for (int i = 0; i < rows.length; i++) {
            int top = startY + (i * rowHeight);
            int bottom = top + rowHeight;

            boolean isTotalAmountRow = (title.contains("Calculation Results") && i == rows.length - 1);
            if (isTotalAmountRow) {
                rowHeight = 28;
                bottom = top + rowHeight;
            }

            if (i == 0) {
                canvas.drawRect(startX, top, startX + tableWidth, bottom, headerBgPaint);
            } else if (i % 2 == 1) {
                canvas.drawRect(startX, top, startX + tableWidth, bottom, altRowPaint);
            }

            canvas.drawRect(startX, top, startX + tableWidth, bottom, linePaint);
            canvas.drawLine(startX + col1Width, top, startX + col1Width, bottom, linePaint);

            if (i == 0) {
                cellTextPaint.setFakeBoldText(true);
                cellTextPaint.setTextSize(11);
                cellTextPaint.setColor(Color.parseColor("#0F172A"));
            } else {
                boolean isHighlighted = false;
                int customColor = Color.parseColor("#334155");
                if (highlightRows != null && highlightColors != null) {
                    for (int h = 0; h < highlightRows.length; h++) {
                        if (highlightRows[h] == i) {
                            isHighlighted = true;
                            customColor = highlightColors[h];
                            break;
                        }
                    }
                }

                if (isTotalAmountRow) {
                    cellTextPaint.setFakeBoldText(true);
                    cellTextPaint.setTextSize(13);
                } else {
                    cellTextPaint.setFakeBoldText(isHighlighted);
                    cellTextPaint.setTextSize(11);
                }
                cellTextPaint.setColor(customColor);
            }

            canvas.drawText(rows[i][0], startX + 12, top + (isTotalAmountRow ? 18 : 16), cellTextPaint);
            canvas.drawText(rows[i][1], startX + col1Width + 12, top + (isTotalAmountRow ? 18 : 16), cellTextPaint);
        }

        return startY + (rows.length * rowHeight);
    }

    private void numbermovetonext() {
        etQty1.requestFocus();
        etFat1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 3) {
                    etClr1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        etClr1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 4) {
                    etQty2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        etFat2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 3) {
                    etClr2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        etClr2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 4) {
                    etQty3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        etFat3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 3) {
                    etClr3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        etClr3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 4) {
                    etQty4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        etFat4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 3) {
                    etClr4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        etClr4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 4) {
                    etQty5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        etFat5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 3) {
                    etClr5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
}
