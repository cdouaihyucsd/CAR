package com.car.carsquad.carapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Objects;

public class RiderPostDetails extends AppCompatActivity {

    private DatabaseReference mDatabase;
    String driverFirstName;
    String driverLastName;
    Double driverRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_post_details);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("post");
        mDatabase.keepSynced(true);

        getIncomingIntent();
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("postID") && getIntent().hasExtra("startPt") && getIntent().hasExtra("endPt") &&
                getIntent().hasExtra("date") && getIntent().hasExtra("time") && getIntent().hasExtra("cost") &&
                getIntent().hasExtra("driverID")){

            String postID = getIntent().getStringExtra("postID");
            String startPt = getIntent().getStringExtra("startPt");
            String endPt = getIntent().getStringExtra("endPt");
            String date = getIntent().getStringExtra("date");
            String time = getIntent().getStringExtra("time");
            String cost = "$" + getIntent().getStringExtra("cost");
            String driverID = getIntent().getStringExtra("driverID");

            //call setDetails
            setDetails(postID, startPt, endPt, date, time, cost, driverID);
        }
    }

    private void setDetails(String postID,String startPt,String endPt,String date,String time, String cost,String driverID){
        TextView startTV = (TextView) findViewById(R.id.start_text_view);
        startTV.setText(startPt);
        TextView endTV = (TextView) findViewById(R.id.end_text_view);
        endTV.setText(endPt);
        TextView dateTV = (TextView) findViewById(R.id.date_text_view);
        dateTV.setText(date);
        TextView timeTV = (TextView) findViewById(R.id.time_text_view);
        timeTV.setText(time);
        TextView costTV = (TextView) findViewById(R.id.cost_text_view);
        costTV.setText(cost);

        final String driverId = driverID;

        DatabaseReference databaseUser = FirebaseDatabase.getInstance().getReference("users");
        databaseUser.child(driverID).child("firstName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                driverFirstName = dataSnapshot.getValue(String.class);
                TextView driverNameTV = (TextView) findViewById(R.id.driver_name_text_view);
                String name = driverFirstName + " ";
                driverNameTV.setText(name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        databaseUser.child(driverID).child("lastName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                driverLastName = dataSnapshot.getValue(String.class);
                TextView driverNameTV = (TextView) findViewById(R.id.driver_name_text_view);
                driverNameTV.append(driverLastName);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        databaseUser.child(driverID).child("driverRating").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                driverRating = dataSnapshot.getValue(Double.class);
                TextView ratingTV = (TextView) findViewById(R.id.rating_text_view);
                ratingTV.setText(driverRating.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



}