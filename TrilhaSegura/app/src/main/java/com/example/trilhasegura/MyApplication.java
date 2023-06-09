package com.example.trilhasegura;

import android.app.Application;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    private List<LatLng> coordinatesList;

    @Override
    public void onCreate() {
        super.onCreate();
        coordinatesList = new ArrayList<>();
    }

    public List<LatLng> getCoordinatesList() {
        return coordinatesList;
    }

    public void setCoordinatesList(List<LatLng> coordinatesList) {
        this.coordinatesList = coordinatesList;
    }
}


