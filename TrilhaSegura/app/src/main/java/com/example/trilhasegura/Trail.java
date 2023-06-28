package com.example.trilhasegura;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Trail {
    private String name;
    private List<LatLng> coordinates = new ArrayList<>();

    public Trail(String name, List<LatLng> coordinates) {
        this.name = name;
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public List<LatLng> getCoordinates() {
        return coordinates;
    }
}

