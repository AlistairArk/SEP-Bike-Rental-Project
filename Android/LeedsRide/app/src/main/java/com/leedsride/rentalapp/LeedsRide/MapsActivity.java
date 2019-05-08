package com.leedsride.rentalapp.LeedsRide;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.leedsride.rentalapp.LeedsRide.Data.Booking;
import com.leedsride.rentalapp.LeedsRide.models.Locations;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;
    private BottomSheetDialog createBookingDialog;
    private TextView dialogTextView;
    private DrawerLayout drawer;

    private Booking booking;

    private static final String BASE_URL = "https://sc17gs.pythonanywhere.com/api/"; ////base url does not include exact path ///should make this available to all activities
    private static final String TAG = MapsActivity.class.getSimpleName();


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        booking = new Booking();

        NavigationView navigationView = findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        Log.v("TAG", " clicked");


                        if (id == R.id.nav_home) {
                            // Handle the home action
                        } else if (id == R.id.nav_orders) {
                            Intent seeOrders = new Intent(getApplicationContext(), MyOrders.class);
                            startActivity(seeOrders);
                        } else if (id == R.id.nav_payment) {

                        } else if (id == R.id.nav_help) {
                            //sendNetworkRequest();
                        } else if (id == R.id.nav_changePassword) {

                        } else if (id == R.id.nav_logOut) {
                            SaveSharedPreference.clearLoginDetails(getApplicationContext());
                            Intent loginOutIntent = new Intent(getApplicationContext(), StartMenu.class);
                            loginOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            loginOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            loginOutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            loginOutIntent.putExtra("EXIT", true);
                            startActivity(loginOutIntent);
                            finish();
                        }

                        drawer.closeDrawer(GravityCompat.START);

                        return true;
                    }
                });

        createBookingDialog = new BottomSheetDialog(this);
        createBookingDialog.setContentView(R.layout.create_booking_popup);
        createBookingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        createBookingDialog.getWindow().setDimAmount(0);
        dialogTextView = (TextView) createBookingDialog.findViewById(R.id.CB_popup_title);
        Button bookNowButton = (Button)createBookingDialog.findViewById(R.id.CB_popup_BookNow);

        bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startBooking = new Intent(getApplicationContext(), CreateBooking.class);
                startBooking.putExtra("booking", booking);
                startActivity(startBooking);
            }
        });

        ImageButton burgerMenu = (ImageButton)findViewById(R.id.burgerMenu);
        burgerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            Toast.makeText(MapsActivity.this, "You have to accept to enjoy all app's services!", Toast.LENGTH_LONG).show();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }

        LatLng initial = new LatLng(53.803690, -1.551385);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initial, 14));

        ////Create retrofit object for network call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ///implement instance of restAPI interface
        restAPI sampleAPI = retrofit.create(restAPI.class);

        //create call which uses getLocations method from restAPI interface
        Call<List<Locations>> call = sampleAPI.getLocations();

        //add call to queue (in this case nothing in queue)
        call.enqueue(new Callback<List<Locations>>() {
            @Override
            public void onResponse(Call<List<Locations>> call, Response<List<Locations>> response) {


                System.out.println(response.message());

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

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String location = marker.getTitle();
                booking.setBookingLocation(location);
                dialogTextView.setText(location);
                createBookingDialog.show();
                return false;
            }
        });

        if(Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }
}

