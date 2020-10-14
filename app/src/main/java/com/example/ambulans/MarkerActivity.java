package com.example.ambulans;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;

import com.example.ambulans.model.Requests;
import com.example.ambulans.model.SelectePLaceEvent;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class MarkerActivity extends AppCompatActivity implements OnMapReadyCallback {
    public MyLatLng latlng;
    private GeoPoint geoPoint;
    private GoogleMap googleMap;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    Button btnAmbulan;
    CardView confirmLayout, finding_your_layout;
    SelectePLaceEvent selectePLaceEvent;
    View fillMaps;
    Marker markerOptions;
    private Circle lastUserCircle;
    private long duration;
    private ValueAnimator lastPulseAnimator;
    private FirebaseAuth mAuth;
    private DatabaseReference db12;
    private GeoFire geofire;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnAmbulan = findViewById(R.id.btn_confirm_ambulans);
        confirmLayout = findViewById(R.id.confirm_ambulans_layout);
        finding_your_layout = findViewById(R.id.layout_finding_your_ride_layout);
        fillMaps = findViewById(R.id.fill_maps);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }
    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude()
                            +""+currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    /*SupportMapFragment supportMapFragment = (SupportMapFragment)
                            getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(MarkerActivity.this);*/
                    setDataPickUp();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fetchLastLocation();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }
    private void setDataPickUp() {

        //googleMap.clear();
        addPickUpMarker();
    }

    private void addPickUpMarker() {

        //View view = getLayoutInflater().inflate(R.layout.pickup_info_windows, null);

        //final LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        //MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here");
        btnAmbulan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPickUpHere(FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
        });
        //fetchLastLocation();
        //create icon form marker
    }

    private void requestPickUpHere(String uid) {
        if (mMap != null) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference("PickupRequest");
            GeoFire geofire = new GeoFire(database);
            geofire.setLocation(uid, new GeoLocation(currentLocation.getLatitude(), currentLocation.getLongitude()), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    if (markerOptions != null) markerOptions.remove();
                    markerOptions = mMap.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(),
                            currentLocation.getLongitude())).title("Pesan"));
                    mMap.animateCamera(CameraUpdateFactory
                            .newLatLngZoom(markerOptions.getPosition(), 12.0f));
                }
            });
        }
    }

    /*private void requestPickUpHere(String uid) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("PickupRequest");
        GeoFire geofire = new GeoFire(database);
        geofire.setLocation(uid, new GeoLocation(currentLocation.getLatitude(), currentLocation.getLongitude()));
        if(markerOptions.isVisible())
            markerOptions.remove();
        markerOptions = mMap.addMarker(new MarkerOptions()
                .title("Pickup here")
                .snippet("")
                .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }*/


    /*private void addMarkerwithPulseAnimation() {
        confirmLayout.setVisibility(View.GONE);
        fillMaps.setVisibility(View.VISIBLE);
        finding_your_layout.setVisibility(View.VISIBLE);

        markerOptions = googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker())
                .position(selectePLaceEvent.getOrigin()));
    }*/

}