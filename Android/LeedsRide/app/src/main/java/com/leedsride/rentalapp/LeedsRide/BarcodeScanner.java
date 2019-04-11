package com.leedsride.rentalapp.LeedsRide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BarcodeScanner extends AppCompatActivity implements BarcodeRetriever {

    private ArrayList<String> bikeArray;
    private BarcodeCapture barcodeCapture;
    private TextView bikeNumberText;
    private int bikeNumber;
    private Dialog instructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        bikeArray = new ArrayList<>();
        bikeNumber = 4;
        bikeNumberText = (TextView)findViewById(R.id.bikeNumber);

        String scannedBike = "0/"+bikeNumber;
        bikeNumberText.setText(scannedBike);

        barcodeCapture = (BarcodeCapture)getSupportFragmentManager()
                .findFragmentById(R.id.barcode);

        barcodeCapture.setRetrieval(this);

        instructions = new Dialog(this);
        instructions.setContentView(R.layout.barcode_instructions);

        ImageButton instructionsCancel = (ImageButton)instructions.findViewById(R.id.barcodeInstructClose);

        instructionsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instructions.dismiss();
            }
        });

        instructions.show();

    }

    @Override
    public void onPermissionRequestDenied() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(BarcodeScanner.this)
                        .setTitle("Permission Error")
                        .setMessage("LeedsRide requires camera access to allow bike collection!");
                builder.show();
            }
        });

    }

    @Override
    public void onRetrievedFailed(String reason) {

    }

    @Override
    public void onRetrieved(final Barcode barcode) {
        barcodeCapture.stopScanning();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                bikeArray.add(barcode.displayValue);
                String arraySize = Integer.toString(bikeArray.size());
                String scannedBike = arraySize+"/"+bikeNumber;
                bikeNumberText.setText(scannedBike);
                Log.d("QR", arraySize);

                if (bikeArray.size() < bikeNumber) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BarcodeScanner.this)
                            .setTitle("Bike Found!")
                            .setMessage(barcode.displayValue);
                    builder.show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BarcodeScanner.this)
                            .setTitle("All bikes found!")
                            .setMessage(barcode.displayValue);
                    builder.show();

                    barcodeCapture.stopScanning();

                    Intent activeBookingIntent = new Intent(getApplicationContext(), OnBookingActivity.class);
                    startActivity(activeBookingIntent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onRetrievedMultiple(Barcode closetToClick, List<BarcodeGraphic> barcodeGraphics) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    private Boolean checkBarcode(String code) {
        String delim = "-";

        StringTokenizer token = new StringTokenizer(code, delim, true);

        boolean expectDelim = false;
        while (token.hasMoreTokens()) {
            String strToken = token.nextToken();
        }

        return true;

    }

}
