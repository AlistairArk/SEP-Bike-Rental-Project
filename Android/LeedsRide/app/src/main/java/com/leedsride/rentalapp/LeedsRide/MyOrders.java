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
    private static final String BASE_URL = "https://sc17gs.pythonanywhere.com/api/";
    
    private RecyclerView ordersRecyclerView;
    private NewOrderAdapter ordersAdapter;
    private RecyclerView.LayoutManager ordersLayoutManager;
    private Dialog collectBikesDialog;
    private LinearLayout noOrders;
    private final static int ALLOWANCE_TIME = 15;

    private ArrayList<MyOrdersRecycler> ordersList;

    private int activeCount = 0;
    private int availableCount = 1;
    private int upComingCount = 2;
    private int completeCount = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        ordersList = new ArrayList<>();

        noOrders = (LinearLayout)findViewById(R.id.noOrdersCont);
        getOrders();

    }

    public void CollectBikesOnCreate(final int bikeNumber, final int bookingID){

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
                startBarcodeScanner.putExtra("BIKE_NUMBER", bikeNumber);
                startBarcodeScanner.putExtra("BOOKING_ID", bookingID);
                startBarcodeScanner.putExtra("COMMIT", "collect");
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
        Orders orders = new Orders(0,0, 0, "", "", "", false, SaveSharedPreference.getPrefUsername(getApplicationContext()), SaveSharedPreference.getPrefPassword(getApplicationContext()));
        Call<List<Orders>> call = sampleAPI.getOrders(orders);

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

                    ordersList.add(new MyOrdersRecycler("Active Bookings","", "active", true, 0, 0, ""));
                    ordersList.add(new MyOrdersRecycler("Available Bookings","", "available", true, 0, 0, ""));
                    ordersList.add(new MyOrdersRecycler("Future bookings","", "upComing", true, 0, 0, ""));
                    ordersList.add(new MyOrdersRecycler("Completed Bookings","", "complete", true, 0, 0, ""));

                    for (Orders order : orders) {

                        System.out.println(order.getStartDate());

                        Calendar startDateTime = Calendar.getInstance();
                        Calendar endDateTime = Calendar.getInstance();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);

                        try {
                            Log.d("dateError", order.getStartDate()+ " :: "+order.getEndDate());
                            startDateTime.setTime(sdf.parse(order.getStartDate()));
                            endDateTime.setTime(sdf.parse(order.getEndDate()));
                            Log.d("dateError", startDateTime.toString()+" :: "+endDateTime.toString());
                        }
                        catch (ParseException e) {
                            Log.e("Parse", "Error");
                            noOrders.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "Network Error: Could not fetch orders", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Calendar allowance = (Calendar)startDateTime.clone();
                        allowance.add(Calendar.MINUTE, -ALLOWANCE_TIME);


                        //int startCompare = calendar.compareTo(startDateTime);
                        int allowanceCompare = calendar.compareTo(allowance);
                        int endCompare = calendar.compareTo(endDateTime);

                        int bookingHour = startDateTime.get(Calendar.HOUR_OF_DAY);

                        int bookingMinute = startDateTime.get(Calendar.MINUTE);
                        int year = startDateTime.get(Calendar.YEAR);
                        int day = startDateTime.get(Calendar.DAY_OF_MONTH);

                        String stringHour = convertDate(bookingHour);
                        String stringMinute = convertDate(bookingMinute);

                        String dayOfWeek = startDateTime.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale);
                        String monthOfYear = startDateTime.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);

                        String header = dayOfWeek+" "+ day  +" "+monthOfYear+", "+year;
                        String info = stringHour+":"+stringMinute+" - "+order.getLocation()+"\n Booking amount £ "+order.getCost();

                        System.out.println(" ########################## Order found ################################### "+order.getId()+" :::: "+order.getBikeNumber()+"   ::::   "+order.getEndDate());

                        if (endCompare == 1 && !order.getBikesInUse()){ //ADD OR: BIKES HAVE BEEN RETURNED.
                            completeCount++;
                            ordersList.add(new MyOrdersRecycler(header,info, "complete", false, order.getBikeNumber(), order.getId(), order.getEndDate()));

                        }
                        else if (endCompare == 1 && order.getBikesInUse()){
                            activeCount++;
                            availableCount++;
                            upComingCount++;
                            completeCount++;
                            Toast.makeText(getApplicationContext(), "Bikes have not been returned for "+header+" booking. You will be charged", Toast.LENGTH_LONG).show();
                            ordersList.add(activeCount, new MyOrdersRecycler(header+" - LATE",info, "late", false, order.getBikeNumber(), order.getId(), order.getEndDate()));
                        }
                        else if (allowanceCompare == -1) {
                            upComingCount++;
                            completeCount++;
                            ordersList.add(upComingCount, new MyOrdersRecycler(header,info, "upComing", false, order.getBikeNumber(), order.getId(), order.getEndDate()));

                        }
                        else if (allowanceCompare >= 0 && order.getBikesInUse()) {
                            availableCount++;
                            upComingCount++;
                            completeCount++;
                            ordersList.add(availableCount, new MyOrdersRecycler(header,info, "available", false, order.getBikeNumber(), order.getId(), order.getEndDate()));

                        }
                        else {
                            activeCount++;
                            availableCount++;
                            upComingCount++;
                            completeCount++;
                            ordersList.add(activeCount, new MyOrdersRecycler(header,info, "active", false, order.getBikeNumber(), order.getId(), order.getEndDate()));
                        }

                    }

                    ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
                    ordersRecyclerView.setHasFixedSize(true);
                    ordersLayoutManager = new LinearLayoutManager(getApplicationContext());
                    ordersAdapter = new NewOrderAdapter(ordersList, getApplicationContext());

                    ordersRecyclerView.setLayoutManager(ordersLayoutManager);
                    ordersRecyclerView.setAdapter(ordersAdapter);

                    collectBikesDialog = new Dialog(MyOrders.this);

                    ordersAdapter.SetOnItemClickListener(new NewOrderAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            String type = ordersList.get(position).getItemOrderType();
                            int bikeNumber = ordersList.get(position).getItemBikeNumber();
                            int id = ordersList.get(position).getItemBookingID();
                            String endDate = ordersList.get(position).getItemEndDate();

                            System.out.println(" ############################ BEFORE SCANNER ################################# "+id+" :::: "+bikeNumber+"   ::::   "+endDate);

                            Log.d("tag", "HEREEEEE "+type);
                            if (type.equals("active") || type.equals("late")) {
                                Intent startActiveBookingIntent = new Intent(getApplicationContext(), OnBookingActivity.class);
                                startActiveBookingIntent.putExtra("BIKE_NUMBER", bikeNumber);
                                startActiveBookingIntent.putExtra("BOOKING_ID", id);
                                startActiveBookingIntent.putExtra("END_DATE", endDate);
                                startActivity(startActiveBookingIntent);
                            }

                            else if (type.equals("available")) {
                                CollectBikesOnCreate(bikeNumber, id);
                            }

                            else if (type.equals("upComing")) {

                            }
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<Orders>> call, Throwable t) {
                System.out.println("HERE                  "+t.getMessage());
                noOrders.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Network Error: Could not fetch orders", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + String.valueOf(input);
        }
    }

}
