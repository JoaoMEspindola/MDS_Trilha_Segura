package com.example.trilhasegura;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Item {

    String key;
    List<LatLng> coordinatesList;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<LatLng> getCoordinatesList() {
        return coordinatesList;
    }

    public void setCoordinatesList(List<LatLng> coordinatesList) {
        this.coordinatesList = coordinatesList;
    }

    public Item(String key, List<LatLng> coordinatesList) {
        this.key = key;
        this.coordinatesList = coordinatesList;
    }
}
