package com.example.seb.ema.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.seb.ema.R;
import com.example.seb.ema.Utils.mLatLng;
import com.example.seb.ema.Utils.stringMLatLng;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final Handler mHandler = new Handler();
    private final Handler mHandler2 = new Handler();
    private Runnable mTimer2;
    private Runnable mTimerMap;
    private DatabaseReference mRef;
    private Location currentLocation;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;

    static String key;
    static  String username;

    private static final String TAG = "MapsActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;


    FusedLocationProviderClient mFusedLocationProviderClient;
    FirebaseDatabase database;
    FirebaseUser user;
    ValueEventListener listener;
    List<stringMLatLng>lngs = new ArrayList<>();

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        user=mAuth.getCurrentUser();

        if(user==null) return;

        mRef.child("users/"+user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("ppl",dataSnapshot.getValue(String.class));

                username=dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       listener= mRef.child("positions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                        LatLng mapsLatLng;
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                        for (DataSnapshot d:children) {
                            stringMLatLng latLng = d.getValue(stringMLatLng.class);

                            if(d.getValue()==null) return;

                            Log.v("KeyIs", d.getKey());
                            Log.v("ValueIs", d.getValue().toString());

                            lngs.add(latLng);
                        }

                        //For every user create a marker
                        for (stringMLatLng lng : lngs) {
                            try{
                                mapsLatLng = new LatLng(lng.getmLatLng().getLatitude(), lng.getmLatLng().getLongitude());
                                mMap.addMarker(new MarkerOptions()
                                        .position(mapsLatLng)
                                        .alpha(454)
                                        .title(username)
                                        .snippet("")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                            }catch(Exception e){
                                Log.v("Map", "Expected ");
                            }
                        }
                        lngs.clear();
                    }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        if (mLocationPermissionsGranted) {
            getDeviceLocation(1);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(MapsActivity.this, "Permission required",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        key= mRef.child("positions").push().getKey();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getLocationPermission();
    }

    private void getDeviceLocation(int i){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d(TAG, "onComplete: found location!");
                         currentLocation = (Location) task.getResult();
                        try{
                            if(i==1){
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                        DEFAULT_ZOOM);
                            }
                        }catch (Exception e){
                            Log.d(TAG, "Failed to moveCamera");
                        }

                        if(username==null) return;

                        myRef=mRef.child("positions/"+username);
                        FirebaseUser user =mAuth.getCurrentUser();


                        mLatLng lng= new mLatLng();
                        try{
                            lng.setLatitude(currentLocation.getLatitude());
                            lng.setLongitude(currentLocation.getLongitude());
                        }catch(Exception e){
                            Log.d(TAG, "Failed to set Long and Lat");
                        }

                        stringMLatLng stringMLatLng= new stringMLatLng();
                        stringMLatLng.setmLatLng(lng);



                        stringMLatLng.setUid(user.getUid());

                        myRef.setValue(stringMLatLng);

                    }else{
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapsActivity.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPause() {

        // Thread doesn't run when App is minimized
        mHandler.removeCallbacks(mTimer2);
        mHandler2.removeCallbacks(mTimerMap);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

       getDeviceLocation(1);
        mTimer2 = new Runnable() {
            @Override
            public void run() {

               getDeviceLocation(0);
                mHandler.postDelayed(this, 200);
            }
        };

        mTimerMap = new Runnable() {
            @Override
            public void run() {
                try{
                    mMap.clear();
                    Log.d(TAG, "Map cleared");
                }catch (Exception ex){
                    Log.d(TAG, "Map clear failed");
                }
                mHandler2.postDelayed(this, 10000);
            }
        };

        mHandler.postDelayed(mTimer2, 1000);
        mHandler2.postDelayed(mTimerMap, 1000);
    }
}


