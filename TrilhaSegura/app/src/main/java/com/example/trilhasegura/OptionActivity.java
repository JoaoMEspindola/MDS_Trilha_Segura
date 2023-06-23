package com.example.trilhasegura;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OptionActivity extends AppCompatActivity {

    private View backDrop;
    private boolean rotate = false;
    private View lytPin, lytTrack;
    private FloatingActionButton fabTrack, fabStop; // Adicionado o FloatingActionButton para o botão Stop Tracking

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        backDrop = findViewById(R.id.back);

        final FloatingActionButton fabPin = findViewById(R.id.fab_pin);
        fabTrack = findViewById(R.id.fab_track);
        fabStop = findViewById(R.id.fab_stop); // Referência ao FloatingActionButton do botão Stop Tracking
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
                startTracking();
            }
        });

        fabStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTracking();
            }
        });
    }

    private void startTracking() {
        Toast.makeText(OptionActivity.this, "Tracking...", Toast.LENGTH_SHORT).show();
        fabTrack.setVisibility(View.GONE); // Esconde o botão Start Tracking
        fabStop.setVisibility(View.VISIBLE); // Mostra o botão Stop Tracking
        TextView textView = findViewById(R.id.text_track); // Referência ao TextView
        textView.setText("Stop Tracking"); // Atualiza o texto para "Stop Tracking"
    }

    private void stopTracking() {
        Toast.makeText(OptionActivity.this, "Tracking stopped.", Toast.LENGTH_SHORT).show();
        fabTrack.setVisibility(View.VISIBLE); // Mostra o botão Start Tracking
        fabStop.setVisibility(View.GONE); // Esconde o botão Stop Tracking
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
}
