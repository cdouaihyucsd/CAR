package com.car.carsquad.carapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).hide();

        //Firebase Authentication Objects
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //if User is already logged in, skip this activity
        if(user != null && user.isEmailVerified()) {
            DatabaseReference databaseUser =
                    FirebaseDatabase.getInstance().getReference("users");
            final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            databaseUser.child(userId).child("currentMode").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String currentMode;
                    currentMode = dataSnapshot.getValue(String.class);
                    if (Objects.equals(currentMode, "driver")) {
                        startActivity(new Intent(getApplicationContext(), DriverActivity.class));
                    } else if (Objects.equals(currentMode, "rider")){
                        startActivity(new Intent(SplashScreenActivity.this, RiderActivity.class));
                    }
                    else{
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        else{
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}
