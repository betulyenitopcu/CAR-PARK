package com.example.car_parkapplication;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity ;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import android.Manifest;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    Location currentLocation;
    FusedLocationProviderClient fusedClient ;
    private static final int REQUEST_CODE = 101 ;
    FrameLayout map ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        map = findViewById(R.id.map) ;

        fusedClient = LocationServices.getFusedLocationProviderClient(this) ;
        getLocation() ;
    }

    private void getLocation(){
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedClient.getLastLocation() ;

        task.addOnSuccessListener(this, new OnSuccessListener<Location>() {

            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location ;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null ;
                    supportMapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getLocation();
        }
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng latLng = new LatLng( currentLocation.getLatitude(), currentLocation.getLongitude()) ;
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("My Current Location") ;
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng)) ;
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10)) ;
        googleMap.addMarker(markerOptions);

        String[] randomMarkerNames = {"Kent Meydanı otoparkı %16", "Terminal Otoparkı %85", "Türkan Saylan Otoparkı %06", "Kampüs Otopark %20", "Kurşunlu Katlı Otoparkı"};


        for (int i = 0; i < 5; i++) { // Örnek olarak 5 rastgele marker ekleyeceğiz
            // Mevcut konumunuza yakın rastgele konumlar oluşturun
            double randomLat = latLng.latitude + Math.random() * 0.02 - 0.01; // Bu, yaklaşık 1 kilometrelik bir varyasyon oluşturur
            double randomLng = latLng.longitude + Math.random() * 0.02 - 0.01; // Bu, yaklaşık 1 kilometrelik bir varyasyon oluşturur

            LatLng randomLatLng = new LatLng(randomLat, randomLng);
            MarkerOptions randomMarkerOptions = new MarkerOptions().position(randomLatLng).title(randomMarkerNames[i]);
            googleMap.addMarker(randomMarkerOptions);
        }

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {


            @Override
            public void onMapClick(LatLng point) {
                double latitude = point.latitude;
                double longitude = point.longitude;
                // Burada enlem ve boylamı işleyin, örneğin bir işaretçi ekleyin veya bir Toast mesajı gösterin
                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                intent.putExtra("Latitude", latitude);
                intent.putExtra("Longitude", longitude);
                startActivity(intent);


            }
        });
    }

}