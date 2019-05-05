package com.leedsride.rentalapp.LeedsRide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.leedsride.rentalapp.LeedsRide.Adapter.NewOrderAdapter;
import com.leedsride.rentalapp.LeedsRide.models.Locations;
import com.leedsride.rentalapp.LeedsRide.models.Login;
import com.leedsride.rentalapp.LeedsRide.models.Orders;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyOrders extends AppCompatActivity {
    private static final String BASE_URL = "https://733y6weqb0.execute-api.eu-west-2.amazonaws.com/";
    
    private RecyclerView ordersRecyclerView;
    private NewOrderAdapter ordersAdapter;
    private RecyclerView.LayoutManager ordersLayoutManager;
    private Dialog collectBikesDialog;
    private LinearLayout noOrders;
    private final static int ALLOWANCE_TIME = 15;

    private ArrayList<MyOrdersRecycler> ordersList;

    private int activeCount = 0;
    private int availableCount = 0;
    private int upComingCount = 0;
    private int  completeCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        ordersList = new ArrayList<>();

        noOrders = (LinearLayout)findViewById(R.id.noOrdersCont);
        getOrders();

        /*
        ordersList.add(new MyOrdersRecycler("Active Bookings","", "active", true));
        ordersList.add(new MyOrdersRecycler("Friday 25 June, 2019","10:00 AM - University Union Bikes", "active", false));

        ordersList.add(new MyOrdersRecycler("Available Bookings","", "available", true));
        ordersList.add(new MyOrdersRecycler("Monday 12 July, 2019","11:30 AM - University Union Bikes", "available", false));

        ordersList.add(new MyOrdersRecycler("Future bookings","", "upComing", true));
        ordersList.add(new MyOrdersRecycler("Monday 12 July, 2019","11:30 AM - University Union Bikes", "upComing", false));
        ordersList.add(new MyOrdersRecycler("Monday 12 July, 2019","11:30 AM - University Union Bikes", "upComing", false));

        ordersList.add(new MyOrdersRecycler("Completed Bookings","", "complete", true));
        ordersList.add(new MyOrdersRecycler("Wednesday 3 February, 2019","13:25 PM - University Union Bikes", "complete", false));
        ordersList.add(new MyOrdersRecycler("Thursday 3 February, 2018","13:25 PM - University Union Bikes", "complete", false));
        ordersList.add(new MyOrdersRecycler("Monday 17 March, 2018","13:25 PM - University Union Bikes", "complete", false));
        */




        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setHasFixedSize(true);
        ordersLayoutManager = new LinearLayoutManager(this);
        ordersAdapter = new NewOrderAdapter(ordersList, this);

        ordersRecyclerView.setLayoutManager(ordersLayoutManager);
        ordersRecyclerView.setAdapter(ordersAdapter);

        collectBikesDialog = new Dialog(this);

        ordersAdapter.SetOnItemClickListener(new NewOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String type = ordersList.get(position).getItemOrderType();
                Log.d("tag", "HEREEEEE "+type);
                if (type.equals("active")) {
                    Intent startActiveBookingIntent = new Intent(getApplicationContext(), OnBookingActivity.class);
                    startActivity(startActiveBookingIntent);
                }

                else if (type.equals("available")) {
                    CollectBikesOnCreate();
                }

                else if (type.equals("upComing")) {

                }
            }
        });

    }

    public void CollectBikesOnCreate(){

        Button collectBikesButton;
        final ImageButton collectBikesCancelButton;

        collectBikesDialog.setContentView(R.layout.collect_bike_popup);

        collectBikesButton =(Button)collectBikesDialog.findViewById(R.id.collectBikesButton);
        collectBikesCancelButton=(ImageButton)collectBikesDialog.findViewById(R.id.collectBikesCancelButton);

        collectBikesCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectBikesDialog.dismiss();
            }
        });

        collectBikesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectBikesDialog.dismiss();
                Intent startBarcodeScanner = new Intent(getApplicationContext(), BarcodeScanner.class);
                startActivity(startBarcodeScanner);
            }
        });

        collectBikesDialog.show();

    }

    private void getOrders() {

        ////Create retrofit object for network call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ///implement instance of restAPI interface
        restAPI sampleAPI = retrofit.create(restAPI.class);

        //create call which uses attemptLogin method from restAPI interface
        Call<List<Orders>> call = sampleAPI.getOrders();

        //add call to queue (in this case nothing in queue)
        call.enqueue(new Callback<List<Orders>>() {
            @Override
            public void onResponse(Call<List<Orders>> call, Response<List<Orders>> response) {

                List<Orders> orders = response.body();

                if (orders.size() < 1) {
                    noOrders.setVisibility(View.VISIBLE);
                }
                else {

                    Calendar calendar = Calendar.getInstance();
                    Locale locale = Locale.getDefault();

                    for (Orders order : orders) {
                        Calendar startDateTime = Calendar.getInstance();
                        Calendar endDateTime = Calendar.getInstance();

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy", locale);

                        try {
                            startDateTime.setTime(sdf.parse(order.getStartDate()));
                            endDateTime.setTime(sdf.parse(order.getEndDate()));
                        }
                        catch (ParseException e) {
                            Log.e("Parse", "Error");
                            return;
                        }

                        Calendar allowance = startDateTime;
                        allowance.add(Calendar.MINUTE, -ALLOWANCE_TIME);


                        int startCompare = calendar.compareTo(startDateTime);
                        int allowanceCompare = calendar.compareTo(allowance);
                        int endCompare = calendar.compareTo(endDateTime);

                        if (endCompare == 1){ //ADD OR: BIKES HAVE BEEN RETURNED.
                            //Add to completed
                        }
                        if (allowanceCompare >= 0) {
                            //check if bikes have been taken out yet. If so //active
                            //else available

                        }
                        else {
                            //Add to future
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Orders>> call, Throwable t) {
                noOrders.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network Error: Could not fetch orders", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
