package com.devcon.devise.model;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Person implements ClusterItem {
    public final String name;
    public final String snippet;
    public final Bitmap profilePhoto;
    private final LatLng mPosition;

    public Person(LatLng position, String name, String snippet, Bitmap pictureResource) {
        this.name = name;
        this.snippet = snippet;
        profilePhoto = pictureResource;
        mPosition = position;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
