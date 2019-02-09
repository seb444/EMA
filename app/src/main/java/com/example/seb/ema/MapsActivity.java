package com.example.seb.ema;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import java.lang.Object;
import android.icu.util.TimeUnit;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.seb.ema.fragmentpagerefresh.mLatLng;
import com.example.seb.ema.fragmentpagerefresh.stringMLatLng;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by User on 10/2/2017.
 */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final Handler mHandler = new Handler();
    private Runnable mTimer2;
    FirebaseDatabase database;
    DatabaseReference mRef;
    Location currentLocation;
    FirebaseUser user;
    DatabaseReference myRef;
    private FirebaseAuth mAuth;
    public static int count =0;
     List<mLatLng> mLatLngs=new ArrayList<>();
     static int test=0;
    ValueEventListener listener;
    static String key;
    static  String username;
    Job myJob;

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

        final Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = null;



       listener= mRef.child("positions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                        LatLng mapsLatLng;
                        Map<String,mLatLng> stringmLatLngMap= new HashMap<>();
                        //mLatLng post= dataSnapshot.getValue(mLatLng.class);
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();


                        for (DataSnapshot d:children) {

                            //   mRef.child("positions").child(d.getKey()).removeValue();

                            stringMLatLng latLng = d.getValue(stringMLatLng.class);

                            Log.v("KEYIS", d.getKey());
                            Log.v("VALUEIS", d.getValue().toString());
                            // Log.v("HIERFEHLER", latLng.getmLatLng().getLatitude()+toString());
                            /* if(!d.getKey().equals(user.getUid()))**/   lngs.add(latLng);


                        }



                        for (stringMLatLng lng : lngs) {
                            try{
                                Log.v("HIERFEHLER", lng.getmLatLng().getLatitude() + toString());
                                Toast.makeText(MapsActivity.this, "nice" + lng.getmLatLng().getLatitude() + "\n" + lng.getmLatLng().getLongitude(), Toast.LENGTH_SHORT).show();
                                mapsLatLng = new LatLng(lng.getmLatLng().getLatitude(), lng.getmLatLng().getLongitude());
                                mMap.addMarker(new MarkerOptions()
                                        .position(mapsLatLng)
                                        .alpha(454)
                                        .title("test")
                                        .snippet("and snippet")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                //   moveCamera(new LatLng(lng.getmLatLng().getLatitude(), lng.getmLatLng().getLongitude()), 20);
                            }catch(Exception e){

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
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

        }





        key= mRef.child("positions").push().getKey();


    }

    private static final String TAG = "MapsActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
     String username1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getLocationPermission();
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                             currentLocation = (Location) task.getResult();
                            try{
                                if(count==0){
                                    moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                            DEFAULT_ZOOM);
                                }
                                count++;
                            }catch (Exception e){

                            }


                            Map<String, mLatLng> user_info = new HashMap<String, mLatLng>();
                            if(username==null) return;
                            myRef=mRef.child("positions/"+username);
                            FirebaseUser user =mAuth.getCurrentUser();
                            mLatLng lng= new mLatLng();
                            try{
                                lng.setLatitude(currentLocation.getLatitude());
                                lng.setLongitude(currentLocation.getLongitude());
                            }catch(Exception e){}

                            user_info.put(user.getUid(),lng);




                            stringMLatLng stringMLatLng= new stringMLatLng();
                            stringMLatLng.setmLatLng(lng);
                            Random random =new Random(30);




                                    mMap.clear();
                                     stringMLatLng.setUid(user.getUid()+random);

                                    myRef.setValue(stringMLatLng);



                            Log.v("Threaisalive", stringMLatLng.toString());
                               Log.v("Threaisalive", "nice");


                           test=0;






                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
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
        mHandler.removeCallbacks(mTimer2);
        super.onPause();
    }

//    @Override
//    protected void onResume() {
//        if(myJob!=null){
//            myJob.resumeMyRunnable();
//
//        }
//        super.onResume();
//    }

    @Override
    public void onResume() {
        super.onResume();

       getDeviceLocation();
        mTimer2 = new Runnable() {
            @Override
            public void run() {

               getDeviceLocation();
                mHandler.postDelayed(this, 200);
            }
        };
        mHandler.postDelayed(mTimer2, 1000);
    }

    private class Job implements Runnable{
        private Handler handler;
        private boolean stop=false;
        public Job () {
            handler = new Handler(Looper.getMainLooper());
            loop();
        }

        @Override
        public void run() {
            // funky stuff
            if(!stop){
                getDeviceLocation();
                loop();
            }

        }
        public void stopMyRunnable(){
            stop=true;
        }

        public void resumeMyRunnable(){
            stop=false;
        }

        private void loop() {
            handler.postDelayed(this, 2000);
        }
    }


}


