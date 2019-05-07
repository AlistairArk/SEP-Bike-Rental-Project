package com.leedsride.rentalapp.LeedsRide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
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
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.leedsride.rentalapp.LeedsRide.Adapter.NewOrderAdapter;
import com.leedsride.rentalapp.LeedsRide.models.BikeList;
import com.leedsride.rentalapp.LeedsRide.models.Locations;
import com.leedsride.rentalapp.LeedsRide.models.Orders;
import com.leedsride.rentalapp.LeedsRide.models.Scanner;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class BarcodeScanner extends AppCompatActivity implements BarcodeRetriever {

    private ArrayList<String> bikeArray;
    private List<Scanner> callList;
    private static final String BASE_URL = "https://sc17gs.pythonanywhere.com/api/";
    private BarcodeCapture barcodeCapture;
    private TextView bikeNumberText;
    private int bikeNumber;
    private int bookingID;
    private String commit;
    private Dialog instructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            bookingID = extras.getInt("BOOKING_ID");
            bikeNumber = extras.getInt("BIKE_NUMBER");
            commit = extras.getString("COMMIT");

            //The key argument here must match that used in the other activity
        }

        System.out.println(" ############################################################# "+bookingID+" :::: "+bikeNumber+"   ::::   "+commit);

        bikeArray = new ArrayList<>();
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
                            .setMessage("");
                    builder.show();
                }
                else if (bikeArray.size() == bikeNumber){
                    AlertDialog.Builder builder = new AlertDialog.Builder(BarcodeScanner.this)
                            .setTitle("All bikes found!")
                            .setMessage("");
                    builder.show();

                    callList = new ArrayList<>();


                    for (String bike : bikeArray) {
                        try {
                            int id = Integer.parseInt(bike);

                            Scanner scanner = new Scanner(bookingID,
                                    SaveSharedPreference.getPrefUsername(BarcodeScanner.this),
                                    SaveSharedPreference.getPrefPassword(BarcodeScanner.this),
                                    id, "");

                            callList.add(scanner);

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            bikeArray = new ArrayList<>();
                            arraySize = Integer.toString(bikeArray.size());
                            scannedBike = arraySize+"/"+bikeNumber;
                            bikeNumberText.setText(scannedBike);
                            Toast.makeText(getApplicationContext(), "Bike scanning error. Please scan the correct id.", Toast.LENGTH_LONG).show();
                            return;
                        }

                    }

                    if (commit.equals("return")) {
                        returnBikes();
                    }
                    else {
                        collectBikes();
                    }
                }
                else {
                    bikeArray = new ArrayList<>();
                    arraySize = Integer.toString(bikeArray.size());
                    scannedBike = arraySize+"/"+bikeNumber;
                    bikeNumberText.setText(scannedBike);
                    Toast.makeText(getApplicationContext(), "Too many bikes scanned", Toast.LENGTH_LONG).show();
                    return;
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

    private void returnBikes() {

        ////Create retrofit object for network call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ///implement instance of restAPI interface
        restAPI sampleAPI = retrofit.create(restAPI.class);

        //create call which uses attemptLogin method from restAPI interface
        BikeList bikeList = new BikeList(callList);
        Call<List<Scanner>> call = sampleAPI.returnBikes(bikeList);

        call.enqueue(new Callback<List<Scanner>>() {
            @Override
            public void onResponse(Call<List<Scanner>> call, Response<List<Scanner>> response) {
                List<Scanner> responseBody = response.body();

                for (Scanner item : responseBody) {
                    String state = item.getResponse();
                    if (state.equals("error")) {
                        bikeArray = new ArrayList<>();
                        Toast.makeText(getApplicationContext(), "Return Bikes failed! Please try again.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Your bike have been returned.", Toast.LENGTH_LONG).show();
                        Intent startMainMenu = new Intent(getApplicationContext(), MyOrders.class);
                        startMainMenu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startMainMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startMainMenu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startMainMenu.putExtra("EXIT", true);
                        startActivity(startMainMenu);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Scanner>> call, Throwable t) {
                bikeArray = new ArrayList<>();
                System.out.println(t.getMessage());
                Toast.makeText(getApplicationContext(), "Network Error: please try again.", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void collectBikes() {

        barcodeCapture.stopScanning();
        ////Create retrofit object for network call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ///implement instance of restAPI interface
        restAPI sampleAPI = retrofit.create(restAPI.class);

        //create call which uses attemptLogin method from restAPI interface
        BikeList bikeList = new BikeList(callList);
        Call<List<Scanner>> call = sampleAPI.collectBikes(bikeList);

        call.enqueue(new Callback<List<Scanner>>() {
            @Override
            public void onResponse(Call<List<Scanner>> call, Response<List<Scanner>> response) {
                List<Scanner> responseBody = response.body();

                for (Scanner item : responseBody) {
                    String state = item.getResponse();
                    if (state.equals("error")) {
                        bikeArray = new ArrayList<>();
                        Toast.makeText(getApplicationContext(), "Bikes collection has failed! Please try again.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Your bikes have been collected.", Toast.LENGTH_LONG).show();
                        Intent startMainMenu = new Intent(getApplicationContext(), MyOrders.class);
                        startMainMenu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startMainMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startMainMenu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startMainMenu.putExtra("EXIT", true);
                        startActivity(startMainMenu);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Scanner>> call, Throwable t) {
                bikeArray = new ArrayList<>();
                Toast.makeText(getApplicationContext(), "Network Error: Please check connection", Toast.LENGTH_LONG).show();
            }
        });

    }


}
