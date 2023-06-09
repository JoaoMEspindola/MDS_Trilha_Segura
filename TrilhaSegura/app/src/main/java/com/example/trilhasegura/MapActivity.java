package com.example.trilhasegura;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final long LOCATION_UPDATE_INTERVAL = 5000; // 5 seconds
    private static final float DEFAULT_ZOOM_LEVEL = 15.0f;

    private GoogleMap map;
    private FloatingActionButton fabStopTracking;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private DatabaseReference databaseReference;
    private List<LatLng> coordinatesList;
    private Marker userMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Habilita o botão de voltar
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Location");

        fabStopTracking = findViewById(R.id.fabStopTracking);
        fabStopTracking.setVisibility(View.GONE);
        fabStopTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationUpdates();
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Verifica se o item de menu selecionado é o botão de voltar
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Chama o método onBackPressed() para encerrar a atividade atual
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        boolean buttonClicked = getIntent().getBooleanExtra("buttonClicked", false);

        if (buttonClicked) {
            fabStopTracking.show();
            coordinatesList = new ArrayList<>(); // Inicializa a lista coordinatesList
            setupPolyline();

            if (checkLocationPermission()) {
                startLocationUpdates();
            } else {
                requestLocationPermission();
            }
        } else {
            setupMarkers();
            Bundle bundle = getIntent().getExtras();
            double latitude = bundle.getDouble("latitude");
            double longitude = bundle.getDouble("longitude");
            LatLng currentPosition = new LatLng(latitude, longitude);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 20));
        }
    }

    private void setupPolyline() {
        PolylineOptions polylineOptions = new PolylineOptions()
                .color(Color.RED)
                .width(5);
        map.addPolyline(polylineOptions);
    }

    private void updatePolyline(LatLng latLng) {
        PolylineOptions polylineOptions = new PolylineOptions()
                .color(Color.RED)
                .width(5)
                .addAll(coordinatesList);
        map.addPolyline(polylineOptions);
        addMarker(latLng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
        map.animateCamera(cameraUpdate); // Faz a câmera acompanhar a posição do usuário
    }

    private void addMarker(LatLng latLng) {
        if (userMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title("Usuário");
            userMarker = map.addMarker(markerOptions);
        } else {
            userMarker.setPosition(latLng);
        }

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
        map.moveCamera(cameraUpdate);
    }

    private void setupMarkers() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                String tipo = dataSnapshot.child("type").getValue(String.class);
                LatLng latLng = new LatLng(latitude, longitude);
                addCustomMarker(latLng, tipo);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            // Resto dos métodos ChildEventListener

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database read errors, if needed
            }
        });
    }

    private void addCustomMarker(LatLng latLng, String tipo) {
        int drawableId;
        if (Objects.equals(tipo, "animal")) {
            drawableId = R.drawable.animal_semfundo;
        } else if (Objects.equals(tipo, "buraco")) {
            drawableId = R.drawable.buraco_semfundo;
        } else if (Objects.equals(tipo, "mataburro")) {
            drawableId = R.drawable.placa_mataburro;
        } else if (Objects.equals(tipo, "escorregadia")) {
            drawableId = R.drawable.escorregadia_semfundo;
        } else {
            return; // Invalid tipo, ignore marker
        }

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(setIcon(MapActivity.this, drawableId));
        map.addMarker(markerOptions);
    }

    private BitmapDescriptor setIcon(MapActivity context, int drawableId) {
        Drawable drawable = ActivityCompat.getDrawable(context, drawableId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest.Builder(LOCATION_UPDATE_INTERVAL)
                .setGranularity(Granularity.GRANULARITY_FINE)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setMinUpdateDistanceMeters(5)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    coordinatesList.add(latLng); // Adiciona a coordenada na lista
                    updatePolyline(latLng);
                    databaseReference.push().setValue(location);

                    if (coordinatesList.size() == 1) {
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM_LEVEL);
                        map.animateCamera(cameraUpdate);
                    }
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                // Permission denied, handle accordingly
            }
        }
    }
}
