package com.example.trilhasegura;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap map;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    private FloatingActionButton fabStopTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        database = FirebaseDatabase.getInstance();

        fabStopTracking = findViewById(R.id.fabStopTracking);
        fabStopTracking.setVisibility(View.GONE);
        fabStopTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Envia um broadcast para parar as solicitações de localização na LocationActivity
                Intent stopLocationUpdatesIntent = new Intent("STOP_LOCATION_UPDATES");
                sendBroadcast(stopLocationUpdatesIntent);
                finish();
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Location");
        map = googleMap;
        boolean buttonClicked = getIntent().getBooleanExtra("buttonClicked", false);

        final boolean[] isZoomApplied = {false};
        final Marker[] userMarker = {null};

        if (buttonClicked) {
            fabStopTracking.show();
            MyApplication myApp = (MyApplication) getApplication();
            List<LatLng> coordinatesList = myApp.getCoordinatesList();

            // Traçar a polyline com as coordenadas recuperadas
            PolylineOptions polylineOptions = new PolylineOptions()
                    .color(Color.RED)
                    .width(5)
                    .addAll(coordinatesList);
            map.addPolyline(polylineOptions);

            if (!isZoomApplied[0] && !coordinatesList.isEmpty()) {
                // Aplicar o zoom inicial desejado
                float zoomLevel = 20.0f; // Defina o nível de zoom inicial desejado
                CameraUpdate zoomUpdate = CameraUpdateFactory.zoomTo(zoomLevel);
                map.moveCamera(zoomUpdate);
                isZoomApplied[0] = true;
            }

            if (!coordinatesList.isEmpty()) {
                LatLng lastLatLng = coordinatesList.get(coordinatesList.size() - 1);

                if (userMarker[0] == null) {
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(lastLatLng)
                            .title("Usuário");
                    userMarker[0] = map.addMarker(markerOptions);
                } else {
                    userMarker[0].setPosition(lastLatLng);
                }

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(lastLatLng);
                map.moveCamera(cameraUpdate);
            }
        }
        else {

            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    // Obtenha a chave da child adicionada
                    String childKey = dataSnapshot.getKey();

                    // Acesse os valores dos campos da child adicionada
                    double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                    double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                    String tipo = dataSnapshot.child("type").getValue(String.class);

                    LatLng latLng = new LatLng(latitude, longitude);

                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLng);

                    if (Objects.equals(tipo, "animal")) {
                        markerOptions.icon(setIcon(MapActivity.this, R.drawable.animal_semfundo));
                    } else if (Objects.equals(tipo, "buraco")) {
                        markerOptions.icon(setIcon(MapActivity.this, R.drawable.buraco_semfundo));
                    } else if (Objects.equals(tipo, "mataburro")) {
                        markerOptions.icon(setIcon(MapActivity.this, R.drawable.placa_mataburro));
                    } else if (Objects.equals(tipo, "escorregadia")) {
                        markerOptions.icon(setIcon(MapActivity.this, R.drawable.escorregadia_semfundo));
                    }

                    map.addMarker(markerOptions);

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    // Chamado quando os dados de uma child existente são modificados
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    // Chamado quando uma child é removida
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    // Chamado quando uma child é movida para uma nova posição
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Lidere com erros de leitura do banco de dados, se necessário
                }
            });
            Bundle bundle = getIntent().getExtras();
            LatLng currentPosition = new LatLng(bundle.getDouble("latitude"), bundle.getDouble("longitude"));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 17));
        }
    }


    public BitmapDescriptor setIcon(Activity context, int drawableID){

        Drawable drawable = ActivityCompat.getDrawable(context, drawableID);

        drawable.setBounds(0, 0, drawable.getIntrinsicHeight(), drawable.getIntrinsicWidth());

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        drawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }
}