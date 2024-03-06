package com.example.car_parkapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
        Button showMap ;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            TextView textViewAddress = findViewById(R.id.textViewAddress);

            // Intent'ten gelen konum bilgisini alın
            Intent intent = getIntent();
            if (intent != null) {
                double latitude = intent.getDoubleExtra("Latitude", 0);
                double longitude = intent.getDoubleExtra("Longitude", 0);
                if (latitude != 0 && longitude != 0) {
                    showAddress(latitude, longitude, textViewAddress);
                }
            }

            showMap = findViewById(R.id.showMap) ;

            showMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(com.example.car_parkapplication.MainActivity.this, MapsActivity.class) ;
                    startActivity(intent);
                }
            });


            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
            bottomNavigationView.setSelectedItemId(R.id.bottom_home);

            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.bottom_home) {
                    return true;
                } else if (itemId == R.id.bottom_settings) {
                    startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (itemId == R.id.bottom_profile) {
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (itemId == R.id.bottom_logout) {
                    startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                }
                return false;
            });

        }


    private void showAddress(double latitude, double longitude, TextView textViewAddress) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                String[] randomMarkerNames = {"Kent Meydanı otoparkı %16", "Terminal Otoparkı %85", "Türkan Saylan Otoparkı %06", "Kampüs Otopark %20", "Kurşunlu Katlı Otoparkı"};
                Address address = addresses.get(0);
                for (int i = 0; i < 5; i++) { // Örnek olarak 5 rastgele marker ekleyeceğiz
                    System.out.println(randomMarkerNames[i]+"\n");
                }
                    String addressLine = address.getAddressLine(0);
                textViewAddress.setText(addressLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
