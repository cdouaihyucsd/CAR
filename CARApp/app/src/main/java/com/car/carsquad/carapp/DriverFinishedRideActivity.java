package com.car.carsquad.carapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Driver;
import java.util.Objects;

public class DriverFinishedRideActivity extends AppCompatActivity implements View.OnClickListener  {

    private Button payCash;
    private Button payCard;
    private Button mCancel;
    DatabaseReference finishDB = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).hide();
        setContentView(R.layout.activity_driver_finished_ride);

        payCard = (Button) findViewById(R.id.btn_pay_card);
        payCard.setOnClickListener(this);
        payCash = (Button) findViewById(R.id.btn_paid_cash);
        payCash.setOnClickListener(this);
        mCancel = (Button) findViewById(R.id.btn_cancel_finish);
        mCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == payCard){
            Toast.makeText(DriverFinishedRideActivity.this, "paid by card", Toast.LENGTH_LONG).show();

            finishDB.child("");


            finish();
        } else if (view == payCash) {
            Toast.makeText(DriverFinishedRideActivity.this, "paid by cash", Toast.LENGTH_LONG).show();




            finish();
        } else if (view == mCancel) {
            finish();
        }
    }
}
