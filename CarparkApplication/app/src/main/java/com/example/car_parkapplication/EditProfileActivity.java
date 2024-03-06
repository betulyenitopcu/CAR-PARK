package com.example.car_parkapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {
    EditText editName, editEmail, editUsername, editPassword;
    Button saveButton;
    String nameUser, emailUser, usernameUser, passwordUser;
    DatabaseReference reference;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("users");

        editName= findViewById(R.id.editName);
        editEmail= findViewById(R.id.editEmail);
        editUsername= findViewById(R.id.editUsername);
        editPassword= findViewById(R.id.editPassword);
        saveButton=findViewById(R.id.saveButton);

        showData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editUsername.getText().toString().isEmpty() ||
                        !editEmail.getText().toString().isEmpty() ||
                        !editPassword.getText().toString().isEmpty()) {
                    if (isUserNameChanged() || isEmailChanged() || isPasswordChanged()) {
                        Toast.makeText(EditProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        // You may want to navigate to the home page here.
                    } else {
                        Toast.makeText(EditProfileActivity.this, "No changes found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean isUserNameChanged() {
        if (!usernameUser.equals(editUsername.getText().toString())) {
            reference.child(usernameUser).child("username").setValue(editUsername.getText().toString());
            usernameUser=editUsername.getText().toString();
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isEmailChanged() {
        if (!emailUser.equals(editEmail.getText().toString())) {
            reference.child(usernameUser).child("email").setValue(editEmail.getText().toString());
            emailUser=editEmail.getText().toString();
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isPasswordChanged() {
        if (!passwordUser.equals(editPassword.getText().toString())) {
            reference.child(usernameUser).child("password").setValue(editPassword.getText().toString());
            passwordUser=editPassword.getText().toString();
            return true;
        }
        else {
            return false;
        }
    }

    public void showData(){
        Intent intent=getIntent();

        nameUser=intent.getStringExtra("name");
        emailUser=intent.getStringExtra("email");
        usernameUser=intent.getStringExtra("username");
        passwordUser=intent.getStringExtra("password");

        editName.setText(nameUser);
        editEmail.setText(emailUser);
        editUsername.setText(usernameUser);
        editPassword.setText(passwordUser);
    }
}