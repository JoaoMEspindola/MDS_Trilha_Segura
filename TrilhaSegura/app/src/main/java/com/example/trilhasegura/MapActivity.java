package com.example.trilhasegura;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap map;

    private View backDrop;
    private boolean rotate = false;
    private View lytPin, lytTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*backDrop = findViewById(R.id.back);

        final FloatingActionButton fabPin = findViewById(R.id.fab_pin);
        final FloatingActionButton fabTrack = findViewById(R.id.fab_track);
        final FloatingActionButton fabAdd = findViewById(R.id.fab_add);

        lytPin = findViewById(R.id.lyt_pin);
        lytTrack = findViewById(R.id.lyt_track);

        ViewAnimation.initShowOut(lytPin);
        ViewAnimation.initShowOut(lytTrack);
        backDrop.setVisibility(View.GONE);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(v);
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
                Toast.makeText(OptionActivity.this, "Setting Pin", Toast.LENGTH_SHORT).show();
            }
        });

        fabTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OptionActivity.this, "Tracking...", Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;
        List<MarkerOptions> listaMarcadores = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        String type = getIntent().getStringExtra("TYPE");
        LatLng latLng = new LatLng(bundle.getDouble("latitude"), bundle.getDouble("longitude"));

        if(bundle != null && type != null){
            if(type.equals("animal")){
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .icon(setIcon(MapActivity.this, R.drawable.animal_semfundo));
                listaMarcadores.add(markerOptions);
            }
            else if(type.equals("escorregadia")){
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .icon(setIcon(MapActivity.this, R.drawable.escorregadia_semfundo));
                listaMarcadores.add(markerOptions);
            }
            else if(type.equals("mataburro")){
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .icon(setIcon(MapActivity.this, R.drawable.placa_mataburro));
                listaMarcadores.add(markerOptions);
            }
            else if(type.equals("buraco")){
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .icon(setIcon(MapActivity.this, R.drawable.buraco_semfundo));
                listaMarcadores.add(markerOptions);
            }
        }
        for (MarkerOptions markerOptions : listaMarcadores) {
            map.addMarker(markerOptions);
        }
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }


    public BitmapDescriptor setIcon(Activity context, int drawableID){

        Drawable drawable = ActivityCompat.getDrawable(context, drawableID);

        drawable.setBounds(0, 0, drawable.getIntrinsicHeight(), drawable.getIntrinsicWidth());

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        drawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }

    private void toggleFabMode(View v){
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if(rotate){
            ViewAnimation.showIn(lytPin);
            ViewAnimation.showIn(lytTrack);
            backDrop.setVisibility(View.VISIBLE);
        }else{
            ViewAnimation.showOut(lytTrack);
            ViewAnimation.showOut(lytPin);
            backDrop.setVisibility(View.GONE);
        }
    }
}