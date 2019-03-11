package com.leedsride.rentalapp.LeedsRide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

public class MyOrders extends AppCompatActivity {
    private RecyclerView ordersRecyclerView;
    private OrderAdapter ordersAdapter;
    private RecyclerView.LayoutManager ordersLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

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

        Log.d("tag", "HEREEEEE: "+ordersList.get(0).getItemHeader());

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setHasFixedSize(true);
        ordersLayoutManager = new LinearLayoutManager(this);
        ordersAdapter = new OrderAdapter(ordersList, this);

        ordersRecyclerView.setLayoutManager(ordersLayoutManager);
        ordersRecyclerView.setAdapter(ordersAdapter);

        ordersAdapter.SetOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String type = ordersList.get(position).getItemOrderType();
                Log.d("tag", "HEREEEEE "+type);
                if (type.equals("active")) {
                    Intent startActiveBookingIntent = new Intent(getApplicationContext(), OnBookingActivity.class);
                    startActivity(startActiveBookingIntent);
                }
            }
        });


    }
}
