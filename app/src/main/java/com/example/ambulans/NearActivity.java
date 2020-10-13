package com.example.ambulans;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;


import com.example.ambulans.model.IOnLoadLocationListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NearActivity extends FragmentActivity implements OnMapReadyCallback, GeoQueryEventListener, IOnLoadLocationListener {
    private GoogleMap mMap;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker currentUser;
    private DatabaseReference db12;
    private GeoFire geoFire;
    private List<LatLng> dekatArea;
    private IOnLoadLocationListener listener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        buildLocationRequest();
                        buildLocationCallback();

                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(NearActivity.this);
                        initArea();
                        settingGeoFire();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(NearActivity.this, "You must enable permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

    }

    private void initArea() {
        listener = this;
        /*dekatArea = new ArrayList<>();
        dekatArea.add(new LatLng(7.742639, 110.351161));
        dekatArea.add(new LatLng(7.767466, 110.368214));
        dekatArea.add(new LatLng(7.776637, 110.459024));*/
        /*FirebaseDatabase.getInstance()
                .getReference("List_Terdekat")
                .child("DaftarDekat")
                .setValue(dekatArea)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(NearActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NearActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/


        //Load from firebase
        FirebaseDatabase.getInstance()
                .getReference("List_Terdekat")
                .child("DaftarDekat")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<MLatLng> latLngList = new ArrayList<>();
                        for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                            MLatLng latLng = locationSnapshot.getValue(MLatLng.class);
                            latLngList.add(latLng);
                        }
                        listener.onLoadLocationSuccess(latLngList);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        listener.onLoadLocationFailed(databaseError.getMessage());
                    }
                });


    }

    private void settingGeoFire() {
        db12 = FirebaseDatabase.getInstance().getReference("List_Terdekat");
        geoFire = new GeoFire(db12);

    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(final LocationResult locationResult) {
                if (mMap != null) {

                    geoFire.setLocation("You", new GeoLocation(locationResult.getLastLocation().getLatitude(),
                            locationResult.getLastLocation().getLongitude()), new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {
                            if (currentUser != null) currentUser.remove();
                            currentUser = mMap.addMarker(new MarkerOptions().position(new LatLng(locationResult.getLastLocation().getLatitude(),
                                    locationResult.getLastLocation().getLongitude())).title("You"));
                            mMap.animateCamera(CameraUpdateFactory
                                    .newLatLngZoom(currentUser.getPosition(), 12.0f));
                        }
                    });

                }
            }
        };


    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (fusedLocationProviderClient != null)

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        /*LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));*/
        //Add circle for dekat Area
        for (LatLng latLng : dekatArea)
        {
            mMap.addCircle(new CircleOptions().center(latLng)
                    .radius(3000)
                    .strokeColor(Color.BLUE)
                    .fillColor(0x220000FF)
                    .strokeWidth(5.0f)
            );
            GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latLng.latitude,latLng.longitude), 3.0f);//500m
            geoQuery.addGeoQueryEventListener(NearActivity.this);
        }
        //Create GeoQuery when user in dekat area rumah sakit

    }

    @Override
    protected void onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {
        sendNotification("LBS", String.format("anda telah masuk area terdekat rumah sakit", key));
    }

    @Override
    public void onKeyExited(String key) {
        sendNotification("LBS", String.format("anda telah meninggalkan area terdekat rumah sakit", key));

    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {
        sendNotification("LBS", String.format("anda berada diantara rumah sakit-rumah sakit terdekat", key));
    }



    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {
            Toast.makeText(this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
    }
    private void sendNotification(String title, String content) {

        Toast.makeText(this, ""+content, Toast.LENGTH_SHORT).show();

            String NOTIFICATION_CHANNEL_ID = "LBS_multiple_location";
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            //CONFIG
            notificationChannel.setDescription("Channel Description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[] {0,1000,500,1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        Notification notification = builder.build();
        notificationManager.notify(new Random().nextInt(), notification);
    }

    @Override
    public void onLoadLocationSuccess(List<MLatLng> latLngs) {
        dekatArea = new ArrayList<>();
        for(MLatLng mLatLng : latLngs)
        {
            LatLng convert = new LatLng(mLatLng.getLatitude(), mLatLng.getLongitude());
            dekatArea.add(convert);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.near_map);
        mapFragment.getMapAsync(NearActivity.this);
        //Now, after masuk area is have data, we will call map display


    }

    @Override
    public void onLoadLocationFailed(String message) {
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();

    }
}