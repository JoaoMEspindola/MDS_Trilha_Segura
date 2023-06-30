package com.example.trilhasegura;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private boolean trackButtonClicked = false;
    private boolean pinButtonClicked = false;
    private static final long LOCATION_UPDATE_INTERVAL = 5000; // 5 seconds
    private static final float DEFAULT_ZOOM_LEVEL = 15.0f;

    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private LocationRequest locationRequest;
    private DatabaseReference databaseReference, trailReference;
    private List<LatLng> coordinatesList;
    private Marker userMarker;

    private List<Marker> markerList;
    private Polyline polyline;

    private LinearLayout lytFabs;
    private boolean fabsVisible = false;

    private PIN pin = new PIN();
    private boolean rotate = false;
    private View lytPin, lytTrack, backDrop;
    private FloatingActionButton fabTrack, fabStopTracking, fabPin, fabPista, fabAnimal, fabBuraco, fabMataburro; // Adicionado o FloatingActionButton para o botão Stop Tracking

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        markerList = new ArrayList<>();

        lytFabs = findViewById(R.id.lyt_fabs);
        fabPin = findViewById(R.id.fab_pin);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Habilita o botão de voltar
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        trailReference = FirebaseDatabase.getInstance().getReference().child("Trails");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Location");

        fabStopTracking = findViewById(R.id.fabStopTracking);
        fabStopTracking.setVisibility(View.GONE);
        fabStopTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTracking();
            }
        });

        backDrop = findViewById(R.id.back);

        final FloatingActionButton fabPin = findViewById(R.id.fab_pin);
        fabTrack = findViewById(R.id.fab_track);
        final FloatingActionButton fabAdd = findViewById(R.id.fab_add);

        lytPin = findViewById(R.id.lyt_pin);
        lytTrack = findViewById(R.id.lyt_track);

        ViewAnimation.initShowOut(lytPin);
        ViewAnimation.initShowOut(lytTrack);
        backDrop.setVisibility(View.GONE);

        fabPista = findViewById(R.id.fabPista);
        fabMataburro = findViewById(R.id.fabMataBurro);
        fabAnimal = findViewById(R.id.fabAnimal);
        fabBuraco = findViewById(R.id.fabBuraco);

        fabPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabs();
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFabs();
            }
        });

        fabPista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFabs();
                pinButtonClicked = true;
                startLocationUpdates("escorregadia");
            }
        });

        fabMataburro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFabs();
                pinButtonClicked = true;
                startLocationUpdates("mataburro");
                // Lógica para o clique do fab3
            }
        });

        fabAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFabs();
                pinButtonClicked = true;
                startLocationUpdates("animal");
                // Lógica para o clique do fab4
            }
        });

        fabBuraco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFabs();
                pinButtonClicked = true;
                startLocationUpdates("buraco");
                // Lógica para o clique do fab5
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(v);
                hideFabs();
            }
        });

        backDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(fabAdd);
            }
        });

        fabPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapActivity.this, "Setting Pin", Toast.LENGTH_SHORT).show();
                toggleFabs();
            }
        });

        fabTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackButtonClicked = !trackButtonClicked;
                if(trackButtonClicked){
                    startTracking();
                }else{
                    stopTracking();
                }
            }
        });

        if (checkLocationPermission()) {
            startLocationUpdates("");
        } else {
            requestLocationPermission();
        }

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
        setupMarkers();

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker clickedMarker) {
                showDialog(clickedMarker);
                return true;
            }
        });

    }

    private void clearPolyline() {
        if (polyline != null) {
            polyline.remove(); // Remove a polyline do mapa
        }
    }

    // Método para configurar a polyline inicial no mapa
    private void setupPolyline() {
        PolylineOptions polylineOptions = new PolylineOptions()
                .color(Color.RED)
                .width(5);
        polyline = map.addPolyline(polylineOptions); // Salva a referência da polyline
    }

    // Método para atualizar a polyline no mapa
    private void updatePolyline(LatLng latLng) {
        PolylineOptions polylineOptions = new PolylineOptions()
                .color(Color.RED)
                .width(5)
                .addAll(coordinatesList);

        clearPolyline(); // Limpa a polyline atual

        polyline = map.addPolyline(polylineOptions); // Adiciona a nova polyline ao mapa

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
                addCustomMarker(latLng, tipo, dataSnapshot.getKey());
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

    private void addCustomMarker(final LatLng latLng, final String tipo, String ID) {
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
            return; // Tipo inválido, ignorar marcador
        }

        final MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(setIcon(MapActivity.this, drawableId))
                .snippet(ID);
        final Marker marker = map.addMarker(markerOptions);
        markerList.add(marker);

        pinButtonClicked = false;
    }

    private void showDialog(final Marker marker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseja remover este marcador?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Remove o marcador do mapa
                        marker.remove();
                        markerList.remove(marker);

                        // Remove o marcador do Firebase
                        String ID = marker.getSnippet();
                        databaseReference.child(ID).removeValue();

                        Toast.makeText(MapActivity.this, "Marcador removido!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Nada a fazer, apenas fecha o diálogo
                    }
                });
        builder.create().show();
    }


    private BitmapDescriptor setIcon(MapActivity context, int drawableId) {
        Drawable drawable = ActivityCompat.getDrawable(context, drawableId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void startLocationUpdates(String type) {
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
                    if(trackButtonClicked){
                        coordinatesList.add(latLng); // Adiciona a coordenada na lista
                        updatePolyline(latLng);

                        if (coordinatesList.size() == 1) {
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM_LEVEL);
                        map.animateCamera(cameraUpdate);
                        }
                    }
                    if(pinButtonClicked){
                        pin.setType(type);
                        pin.setLatitude(latLng.latitude);
                        pin.setLongitude(latLng.longitude);
                        DatabaseReference newPinRef = databaseReference.push();
                        String newPinKey = newPinRef.getKey(); // Obter a chave gerada

                        newPinRef.setValue(pin);
                        addCustomMarker(latLng, type, newPinKey);
                    }
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                    addMarker(latLng);
                    map.animateCamera(cameraUpdate);
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

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                startLocationUpdates();
//            } else {
//                // Permission denied, handle accordingly
//            }
//        }
//    }

    private void startTracking() {
        coordinatesList = new ArrayList<>(); // Inicializa a lista coordinatesList
        setupPolyline();

        if (checkLocationPermission()) {
            startLocationUpdates("");
        } else {
            requestLocationPermission();
        }
        Toast.makeText(MapActivity.this, "Tracking...", Toast.LENGTH_SHORT).show();
        fabTrack.setVisibility(View.GONE); // Esconde o botão Start Tracking
        fabStopTracking.setVisibility(View.VISIBLE); // Mostra o botão Stop Tracking
        TextView textView = findViewById(R.id.text_track); // Referência ao TextView
        textView.setText("Stop Tracking"); // Atualiza o texto para "Stop Tracking"
    }

    private void stopTracking() {
        trackButtonClicked = false;
        stopLocationUpdates();
        clearPolyline();

        Item item = new Item(trailReference.push().getKey(), coordinatesList);

        trailReference.push().setValue(item);
        coordinatesList.clear();
        Toast.makeText(MapActivity.this, "Tracking stopped.", Toast.LENGTH_SHORT).show();
        fabTrack.setVisibility(View.VISIBLE); // Mostra o botão Start Tracking
        fabStopTracking.setVisibility(View.GONE); // Esconde o botão Stop Tracking
        TextView textView = findViewById(R.id.text_track); // Referência ao TextView
        textView.setText("Start Tracking"); // Atualiza o texto para "Start Tracking"
    }

    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(lytPin);
            ViewAnimation.showIn(lytTrack);
            backDrop.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(lytTrack);
            ViewAnimation.showOut(lytPin);
            backDrop.setVisibility(View.GONE);
        }
    }

    private void toggleFabs() {
        if (fabsVisible) {
            hideFabs();
        } else {
            showFabs();
        }
    }

    private void showFabs() {
        // Mostrar os FloatingActionButton com animação circular
        int cx = lytFabs.getWidth() / 2;
        int cy = lytFabs.getHeight() / 2;
        float radius = (float) Math.hypot(cx, cy);

        Animator revealAnimator = ViewAnimationUtils.createCircularReveal(lytFabs, cx, cy, 0, radius);
        revealAnimator.setDuration(500);
        lytFabs.setVisibility(View.VISIBLE);
        revealAnimator.start();

        fabPin.setVisibility(View.GONE);
        lytPin.setVisibility(View.GONE);
        fabsVisible = true;
    }

    private void hideFabs() {
        // Esconder os FloatingActionButton com animação circular
        int cx = lytFabs.getWidth() / 2;
        int cy = lytFabs.getHeight() / 2;
        float radius = (float) Math.hypot(cx, cy);

        Animator hideAnimator = ViewAnimationUtils.createCircularReveal(lytFabs, cx, cy, radius, 0);
        hideAnimator.setDuration(500);
        hideAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                lytFabs.setVisibility(View.GONE);
                fabPin.setVisibility(View.VISIBLE);
                lytPin.setVisibility(View.VISIBLE);
            }
        });
        hideAnimator.start();

        fabsVisible = false;
    }

}
