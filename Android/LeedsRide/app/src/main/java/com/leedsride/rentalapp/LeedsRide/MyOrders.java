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

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.leedsride.rentalapp.LeedsRide.Adapter.NewOrderAdapter;
import com.leedsride.rentalapp.LeedsRide.models.Locations;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        /*

        ////Create retrofit object for network call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ///implement instance of restAPI interface
        restAPI sampleAPI = retrofit.create(restAPI.class);

        //create call which uses attemptLogin method from restAPI interface
        Call<List<Locations>> call = sampleAPI.getLocations();

        //add call to queue (in this case nothing in queue)
        call.enqueue(new Callback<List<Locations>>() {
            @Override
            public void onResponse(Call<List<Locations>> call, Response<List<Locations>> response) {

                List<Locations> locations = response.body();

                for(Locations location : locations){
//                    String content = "";
//                    content += "Name: " + location.getName() + " ";
//                    content += "Latitude: " + location.getLatitude();
//                    content += "Longitude: " + location.getLongitude();
//                    content += "Available: " + location.getBikesAvailable();
//
//                    Log.d(TAG, content + "!!!!!!!");

                    LatLng marker = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(marker).title(location.getName()).snippet("Available: " + location.getBikesAvailable()));
                }

            }

            @Override
            public void onFailure(Call<List<Locations>> call, Throwable t) {
                Log.e("error", "Could not connect to external API");
            }
        });

        */

        final ArrayList<MyOrdersRecycler> ordersList = new ArrayList<>();
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

}
