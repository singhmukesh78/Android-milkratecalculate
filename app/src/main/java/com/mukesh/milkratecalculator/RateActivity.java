package com.mukesh.milkratecalculator;

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
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
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

            if (Fat > 13f) {
                etFat.setError("FAT cannot be greater than 13");
                etFat.requestFocus();
                return;
            }
            if (Snf > 12f) {
                etSnf.setError("SNF cannot be greater than 12");
                etSnf.requestFocus();
                return;
            }

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

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numeric values", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnOnClickClear(View view) {
        etQty.setText("");
        etFat.setText("");
        etSnf.setText("");
        tvAmt.setText("0.00");
        tvRate.setText("0.00");
        tvClr.setText("0.00");
        etFat.requestFocus();
    }

    public void downloadPdfReport(View view) {
        String stRateVal = etStRate.getText().toString().trim();
        String fatVal = etFat.getText().toString().trim();
        String snfVal = etSnf.getText().toString().trim();
        String qtyVal = etQty.getText().toString().trim();
        String rateVal = tvRate.getText().toString().trim();
        String clrVal = tvClr.getText().toString().trim();
        String amtVal = tvAmt.getText().toString().trim();

        String ratioStr = "50:50";
        if (radioGroupRatio != null) {
            int checkedId = radioGroupRatio.getCheckedRadioButtonId();
            if (checkedId == R.id.radio5248) {
                ratioStr = "52:48";
            } else if (checkedId == R.id.radio6040) {
                ratioStr = "60:40";
            } else {
                ratioStr = "50:50";
            }
        }

        if (stRateVal.isEmpty() || fatVal.isEmpty() || snfVal.isEmpty()) {
            Toast.makeText(this, "Please enter Standard Rate, FAT and SNF first", Toast.LENGTH_SHORT).show();
            return;
        }

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
        canvas.drawText("Milk Rate Calculation Report", startX, startY, paint);

        startY += 20;
        paint.setFakeBoldText(false);
        paint.setTextSize(11);
        paint.setColor(Color.GRAY);
        canvas.drawText("Generated on: " + new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(new Date()), startX, startY, paint);

        startY += 30;

        int givenDataColor = Color.parseColor("#4F46E5");

        // Table 1: Entered Parameters
        startY = drawTable(canvas, paint, startX, startY, tableWidth, "1. Entered Parameters", new String[][]{
                {"Parameter", "Value"},
                {"Standard Rate", "₹ " + stRateVal},
                {"FAT", fatVal + " %"},
                {"SNF", snfVal + " %"},
                {"Quantity", (qtyVal.isEmpty() ? "0" : qtyVal) + " kg"}
        }, new int[]{1, 2, 3, 4}, new int[]{Color.parseColor("#0F172A"), givenDataColor, givenDataColor, givenDataColor});

        startY += 16;

        // Table 2: Selected Options
        startY = drawTable(canvas, paint, startX, startY, tableWidth, "2. Selected Options", new String[][]{
                {"Option", "Selected Value"},
                {"FAT & SNF Ratio", ratioStr}
        }, new int[]{1}, new int[]{givenDataColor});

        startY += 16;

        // Table 3: Calculation Results
        startY = drawTable(canvas, paint, startX, startY, tableWidth, "3. Calculation Results", new String[][]{
                {"Result Metric", "Value"},
                {"Calculated Rate", "₹ " + (rateVal.isEmpty() ? "0.00" : rateVal) + " / liter"},
                {"CLR", (clrVal.isEmpty() ? "0.0" : clrVal)},
                {"Total Amount", "₹ " + (amtVal.isEmpty() ? "0.00" : amtVal)}
        }, new int[]{1, 3}, new int[]{Color.parseColor("#0284C7"), Color.parseColor("#16A34A")});

        pdfDocument.finishPage(page);

        try {
            OutputStream fos;
            Uri pdfUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, "MilkRateReport_" + System.currentTimeMillis() + ".pdf");
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
                File file = new File(documentsDir, "MilkRateReport_" + System.currentTimeMillis() + ".pdf");
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

            if (i == 0) {
                canvas.drawRect(startX, top, startX + tableWidth, bottom, headerBgPaint);
            } else if (i % 2 == 1) {
                canvas.drawRect(startX, top, startX + tableWidth, bottom, altRowPaint);
            }

            canvas.drawRect(startX, top, startX + tableWidth, bottom, linePaint);
            canvas.drawLine(startX + col1Width, top, startX + col1Width, bottom, linePaint);

            if (i == 0) {
                cellTextPaint.setFakeBoldText(true);
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
                cellTextPaint.setFakeBoldText(isHighlighted);
                cellTextPaint.setColor(customColor);
            }

            canvas.drawText(rows[i][0], startX + 12, top + 16, cellTextPaint);
            canvas.drawText(rows[i][1], startX + col1Width + 12, top + 16, cellTextPaint);
        }

        return startY + (rows.length * rowHeight);
    }
}