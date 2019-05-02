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

    private static final String BASE_URL = "https://733y6weqb0.execute-api.eu-west-2.amazonaws.com/"; ////base url does not include exact path ///should make this available to all activities
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

                        } else if (id == R.id.nav_changePassword) {

                        } else if (id == R.id.nav_logOut) {

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

        LatLng lubs = new LatLng(53.808832, -1.561353);
        mMap.addMarker(new MarkerOptions().position(lubs).title("Leeds University Business School").snippet("Available: 15\nParking Spaces: 1"));

        LatLng theLight = new LatLng(53.800661, -1.545673);
        mMap.addMarker(new MarkerOptions().position(theLight).title("The Light").snippet("Available: 10\nParking Spaces: 4"));

        LatLng artsUniversity = new LatLng(53.809512, -1.551420);
        mMap.addMarker(new MarkerOptions().position(artsUniversity).title("Leeds Arts University").snippet("Available: 5\nParking Spaces: 13"));

        LatLng initial = new LatLng(53.803690, -1.551385);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initial, 14));

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

//                mMap.clear();
//
//                /////Do these need to be here because of the clear?
//                LatLng lubs = new LatLng(53.808832, -1.561353);
//                mMap.addMarker(new MarkerOptions().position(lubs).title("Leeds University Business School"));
//
//                LatLng theLight = new LatLng(53.800661, -1.545673);
//                mMap.addMarker(new MarkerOptions().position(theLight).title("The Light"));
//
//                LatLng artsUniversity = new LatLng(53.809512, -1.551420);
//                mMap.addMarker(new MarkerOptions().position(artsUniversity).title("Leeds Arts University"));
//
//                LatLng user = new LatLng(location.getLatitude(), location.getLongitude());
//                mMap.addMarker(new MarkerOptions().position(user).title("Your location").snippet("Available: 10").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user, 14));

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
    private void sendNetworkRequest() {

        ////Create retrofit object for network call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ///implement instance of restAPI interface
        restAPI sampleAPI = retrofit.create(restAPI.class);

        //create call which uses attemptLogin method from restAPI interface
        Call<Locations> call = sampleAPI.getLocations();

        //add call to queue (in this case nothing in queue)
        call.enqueue(new Callback<Locations>() {
            @Override
            public void onResponse(Call<Locations> call, Response<Locations> response) {

                String reply = response.body().getName();
                String location = response.body().getLocation();
                String bikesAvailable = response.body().getBikesAvailable();

                Log.d(TAG, reply);
                Log.d(TAG, location);
                Log.d(TAG, bikesAvailable);

                if(reply.equals("Login Accepted")){
                    Intent startMainMenu = new Intent(getApplicationContext(), MapsActivity.class);
                    startMainMenu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(startMainMenu);
                    finish();
                }
                if(reply.equals("Incorrect Login Information")){
                    Toast.makeText(getApplicationContext(), reply, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Locations> call, Throwable t) {
                Log.e("error", "Could not connect to external API");
            }
        });
    }
}

