package com.car.carsquad.carapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).hide();

        setContentView(R.layout.activity_main);

        //Firebase Authentication Objects
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //if User is already logged in, skip this activity
        if(user != null && user.isEmailVerified()) {
            //start profile activity
            //finish();
            startActivity(new Intent(getApplicationContext(), RiderActivity.class));

        }
    }
    //what activity to jump to
    public void nextActivity(View view) {
        int viewId = view.getId();
        Intent nextActivityIntent;
        switch(viewId) {
            case R.id.login_button:
                nextActivityIntent = new Intent(this, LoginActivity.class);
                startActivity(nextActivityIntent);
                break;
            case R.id.register_button:
                nextActivityIntent = new Intent(this, RegisterActivity.class);
                startActivity(nextActivityIntent);
                break;
        }
    }
    //prevent user from pressing the back button to go back from the main app screen
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
