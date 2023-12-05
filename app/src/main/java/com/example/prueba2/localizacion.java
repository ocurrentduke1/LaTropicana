package com.example.prueba2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class localizacion extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener {

    GoogleMap mMap;
    EditText txtlongitud;
    EditText txtlatitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacion);

        txtlatitud = findViewById(R.id.txtlat);
        txtlongitud = findViewById(R.id.txtlong);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    //
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);

        LatLng tropi = new LatLng(20.682555, -103.346781);
        mMap.addMarker(new MarkerOptions().position(tropi));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tropi));
        LatLng tropi1 = new LatLng(20.711142, -103.338837);
        mMap.addMarker(new MarkerOptions().position(tropi1));
        LatLng tropi2 = new LatLng(20.688093, -103.334062);
        mMap.addMarker(new MarkerOptions().position(tropi2));
        LatLng ceti = new LatLng(20.7017863,-103.3893635);
        mMap.addMarker(new MarkerOptions().position(ceti));
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        txtlongitud.setText("" + latLng.longitude);
        txtlatitud.setText("" + latLng.latitude);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        txtlongitud.setText("" + latLng.longitude);
        txtlatitud.setText("" + latLng.latitude);
    }
}