package com.car.carsquad.carapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.widget.Button;

public class MainAppActivity extends AppCompatActivity implements View.OnClickListener {

    //Firebase object
    private FirebaseAuth firebaseAuth;

    //UI references
    private TextView textviewUserEmail;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        //initialize fireBase
        firebaseAuth = firebaseAuth.getInstance();

        //if user is not logged in yet
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        //display USER EMAIL
        FirebaseUser user = firebaseAuth.getCurrentUser();
        textviewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        textviewUserEmail.setText("Welcome "+ user.getEmail());

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
       if(view == buttonLogout) {
           //sign user out
           firebaseAuth.signOut();
           //end current activity and go back to main screen
           finish();
           startActivity(new Intent(this, MainActivity.class));
       }
    }
}
