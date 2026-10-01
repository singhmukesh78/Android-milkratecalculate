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
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class RateClrMethod extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    private EditText etQtyClr, etFat, etCLR, etRatePfat, etRatePq;
    private TextView tvCalcSnf, tvCalGhee, tvCalPwdr, tvCalGheeAmt, tvCalPwdrAmt, tvCalTotalAmt, tvRate;
    String QtyClr, Fat, CLR, RatePfat, ratePQ;
    float qty, ratePQtl, ratePfat, snf, ghee, gheeAmt, powder, powderAmt, clr, fat, totalAmt;
    boolean clrMethod = true;
    DecimalFormat f = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_clr_method);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setTitle("Rate FAT CLR Method");

        AdView adView = findViewById(R.id.adView);
        if (adView != null) {
            MobileAds.initialize(this);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }

        etQtyClr = findViewById(R.id.etQtyClr);
        etFat = findViewById(R.id.etFATPer);
        etCLR = findViewById(R.id.etCLR);
        etRatePfat = findViewById(R.id.etRatePF);
        etRatePq = findViewById(R.id.etRatePQ);

        tvCalcSnf = findViewById(R.id.tvCalcSnf);
        tvRate = findViewById(R.id.tvRate);
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

            float ratePerKg = (qty > 0) ? (totalAmt / qty) : 0f;
            ratePerKg = Float.parseFloat(f.format(ratePerKg));

            tvCalcSnf.setText(snf + " %");
            if (tvRate != null) {
                tvRate.setText(f.format(ratePerKg));
            }
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
        if (tvRate != null) {
            tvRate.setText("0.00");
        }
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

    public void downloadPdfReport(View view) {
        String qtyVal = etQtyClr.getText().toString().trim();
        String fatVal = etFat.getText().toString().trim();
        String clrVal = etCLR.getText().toString().trim();
        String rateVal = clrMethod ? etRatePq.getText().toString().trim() : etRatePfat.getText().toString().trim();

        if (qtyVal.isEmpty() || fatVal.isEmpty() || clrVal.isEmpty() || rateVal.isEmpty()) {
            Toast.makeText(this, "Please enter Qty, FAT, CLR and Rate first", Toast.LENGTH_SHORT).show();
            return;
        }

        String snfVal = tvCalcSnf.getText().toString().trim();
        String ratePerKgVal = (tvRate != null) ? tvRate.getText().toString().trim() : "0.00";
        String gheeVal = tvCalGhee.getText().toString().trim();
        String gheeAmtVal = tvCalGheeAmt.getText().toString().trim();
        String pwdrVal = tvCalPwdr.getText().toString().trim();
        String pwdrAmtVal = tvCalPwdrAmt.getText().toString().trim();
        String totalAmtVal = tvCalTotalAmt.getText().toString().trim();

        String methodStr = clrMethod ? "SNF Method (Rate Per 100kg)" : "Direct Method (Rate Per FAT)";
        String rateLabelStr = clrMethod ? "Rate Per 100kg" : "Rate Per FAT";

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
        canvas.drawText("Lacto Master Report (FAT & CLR Method)", startX, startY, paint);

        startY += 20;
        paint.setFakeBoldText(false);
        paint.setTextSize(11);
        paint.setColor(Color.GRAY);
        canvas.drawText("Generated on: " + new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(new Date()), startX, startY, paint);

        startY += 30;

        int givenDataColor = Color.parseColor("#4F46E5");

        // Table 1: Entered Parameters & Method
        startY = drawTable(canvas, paint, startX, startY, tableWidth, "1. Milk Parameters & Method", new String[][]{
                {"Parameter", "Value"},
                {"Milk Quantity", qtyVal + " kg"},
                {"FAT", fatVal + " %"},
                {"CLR", clrVal},
                {"Calculation Method", methodStr},
                {rateLabelStr, "₹ " + rateVal}
        }, new int[]{1, 2, 3, 4, 5}, new int[]{givenDataColor, givenDataColor, givenDataColor, givenDataColor, givenDataColor});

        startY += 16;

        // Table 2: Calculation Results
        startY = drawTable(canvas, paint, startX, startY, tableWidth, "2. Calculation Results", new String[][]{
                {"Result Metric", "Value"},
                {"Calculated Rate / KG", "₹ " + ratePerKgVal},
                {"Calculated SNF", snfVal},
                {"Ghee Weight", gheeVal},
                {"Ghee Amount", "₹ " + gheeAmtVal},
                {"Powder Weight", pwdrVal},
                {"Powder Amount", "₹ " + pwdrAmtVal},
                {"Total Amount", "₹ " + totalAmtVal}
        }, new int[]{1, 2, 4, 6, 7}, new int[]{Color.parseColor("#0284C7"), Color.parseColor("#0284C7"), Color.parseColor("#0284C7"), Color.parseColor("#0284C7"), Color.parseColor("#16A34A")});

        pdfDocument.finishPage(page);

        try {
            OutputStream fos;
            Uri pdfUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, "MilkRateClrReport_" + System.currentTimeMillis() + ".pdf");
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
                File file = new File(documentsDir, "MilkRateClrReport_" + System.currentTimeMillis() + ".pdf");
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
                    cellTextPaint.setTextSize(13); // Larger font size for Total Amount
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
}