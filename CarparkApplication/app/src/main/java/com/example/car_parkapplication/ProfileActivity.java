package com.example.car_parkapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    TextView profileName, profileEmail, profileUsername;
    Button changePasswordButton;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Ana sayfa kodunuzun ilgili kısmı
        



        mAuth = FirebaseAuth.getInstance();

        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profileUsername = findViewById(R.id.profileUsername);

        changePasswordButton = findViewById(R.id.changePassword_Button);

        String username = getIntent().getStringExtra("username_key");


        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            loadUserDataFromFirebase(firebaseUser);
        } else {
            Log.d("ProfileActivity", "Kullanıcı giriş yapmamış.");
        }

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordResetEmail();
            }
        });
    }

    private void loadUserDataFromFirebase(FirebaseUser firebaseUser) {
        // Kullanıcı adını al - bu örnekte statik bir değer kullanıyorum.
        // Uygulamanızın geri kalanında kullanıcı adını nasıl sakladığınıza bağlı olarak bu kısmı güncelleyin.
        String username = "eda"; // Kullanıcının adını buraya girin veya uygulamanızdan alın.

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(username);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Kullanıcı verilerini çek
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String username = dataSnapshot.child("username").getValue(String.class);

                    // TextView'ları güncelle
                    profileName.setText(name);
                    profileEmail.setText(email);
                    profileUsername.setText(username);
                } else {
                    // Kullanıcı verisi bulunamadıysa bilgi ver
                    Toast.makeText(ProfileActivity.this, "Kullanıcı verisi bulunamadı.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Veri çekme işlemi iptal edildiğinde hata mesajını göster
                Toast.makeText(ProfileActivity.this, "Veri yüklenirken hata oluştu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendPasswordResetEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.getEmail() != null) {
            mAuth.sendPasswordResetEmail(user.getEmail())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Şifre sıfırlama e-postası gönderildi.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Şifre sıfırlama e-postası gönderilemedi. Lütfen daha sonra tekrar deneyin.", Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Hata: Kullanıcının e-posta adresi bulunamadı.", Toast.LENGTH_LONG).show();
        }
    }

    // ...
}
