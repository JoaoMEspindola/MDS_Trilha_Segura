package com.example.trilhasegura;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.example.trilhasegura.databinding.ActivityMainBinding;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Granularity;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.BuildConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LocationActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        binding.buttonPIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasLocationPermissions()) {
                    getLastLocation("Pin");
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        showCustomDialog("Location Permission", "This app needs the location permission to track your location.", "Ok.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                multiplePermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
                            }
                        }, "Cancel", null);
                    } else {
                        multiplePermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
                    }
                }
            }
        });

        binding.buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasLocationPermissions()) {
                    getLastLocation("Map");
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        showCustomDialog("Location Permission", "This app needs the location permission to track your location.", "Ok.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                multiplePermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
                            }
                        }, "Cancel", null);
                    } else {
                        multiplePermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
                    }
                }
            }
        });
        binding.buttonTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasLocationPermissions()) {
                    Intent intent = new Intent(LocationActivity.this, MapActivity.class);
                    intent.putExtra("buttonClicked", true);
                    startActivity(intent);
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        showCustomDialog("Location Permission", "This app needs the location permission to track your location.", "Ok.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                multiplePermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
                            }
                        }, "Cancel", null);
                    } else {
                        multiplePermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
                    }
                }
            }
        });
    }

    void showCustomDialog(String title, String message, String positivieBtnTitle, DialogInterface.OnClickListener positiveListener, String negativeBtnTitle, DialogInterface.OnClickListener negativeListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positivieBtnTitle, positiveListener)
                .setNegativeButton(negativeBtnTitle, negativeListener);
        builder.create().show();
    }

    private boolean hasLocationPermissions() {
        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private ActivityResultLauncher<String[]> multiplePermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @Override
        public void onActivityResult(Map<String, Boolean> result) {
            boolean finePermissionAllowed = false;

            if (result.get(Manifest.permission.ACCESS_FINE_LOCATION) != null) {
                finePermissionAllowed = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                if (finePermissionAllowed) {
                    getLastLocation("");
                } else {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        showCustomDialog("Location Permission", "The app needs the fine location permission to function.", "Ok.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.LIBRARY_PACKAGE_NAME));
                                startActivity(intent);
                            }
                        }, "Cancel", null);
                    }
                }
            }
        }
    });

    @SuppressLint("MissingPermission")
    private void getLastLocation(String button) {
        CurrentLocationRequest currentLocationRequest = new CurrentLocationRequest.Builder()
                .setGranularity(Granularity.GRANULARITY_FINE)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setDurationMillis(5000)
                .setMaxUpdateAgeMillis(0)
                .build();

        CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();

        fusedLocationProviderClient.getCurrentLocation(currentLocationRequest, cancellationTokenSource.getToken()).addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    Log.d("demo", "onComplete" + location);
                    Bundle bundle = new Bundle();
                    bundle.putDouble("latitude", location.getLatitude());
                    bundle.putDouble("longitude", location.getLongitude());

                    Intent intent;
                    if (Objects.equals(button, "Map")) {
                        intent = new Intent(LocationActivity.this, MapActivity.class);
                    } else {
                        intent = new Intent(LocationActivity.this, PinActivity.class);
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    task.getException().printStackTrace();
                }
            }
        });
    }
}