package com.example.trilhasegura;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    private Button buttonMap;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        buttonMap = findViewById(R.id.inicializeMap);

        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MapActivity.class));
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    // Lógica para lidar com a seleção do Item 1
                    return true;
                } else if (item.getItemId() == R.id.search) {
                    startActivity(new Intent(HomeActivity.this, searchPage.class));
                    return true;
                } else if (item.getItemId() == R.id.person) {
                    // Lógica para lidar com a seleção do Item 3
                    return true;
                } else if (item.getItemId() == R.id.settings) {
                    // Lógica para lidar com a seleção do Item 4
                    return true;
                }
                return false;
            }
        });
    }

    private void openItem1Screen() {
        // Código para abrir a tela correspondente ao Item 1
    }

    private void openItem2Screen() {
        // Código para abrir a tela correspondente ao Item 2
    }

    private void openItem3Screen() {
        // Código para abrir a tela correspondente ao Item 3
    }

    private void openItem4Screen() {
        // Código para abrir a tela correspondente ao Item 4
    }
}