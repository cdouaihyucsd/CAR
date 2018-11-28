package com.car.carsquad.carapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RiderPostDetails extends AppCompatActivity {

    TextView mStartTv, mDestTv;
    TextView mView;
    ImageView mImageTv;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_post_details);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("post");
        mDatabase.keepSynced(true);
        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Post details");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mStartTv = findViewById(R.id.post_start);
        mDestTv = findViewById(R.id.post_dest);
        //mImageTv = findViewById(R.id.post_image_detail);

        //get data from intent
        String title = getIntent().getStringExtra("title");
        String desc = getIntent().getStringExtra("description");

        mStartTv.setText(title);
        mDestTv.setText(desc);*/

    }

    public void setStart(String start){
        TextView post_start = (TextView)mView.findViewById(R.id.post_start);
        post_start.setText(start);
    }
    public void setDest(String dest){
        TextView post_dest = (TextView)mView.findViewById(R.id.post_dest);
        post_dest.setText(dest);
    }
    public void setDate(String depDate){
        TextView post_date = (TextView)mView.findViewById(R.id.post_date);
        post_date.setText("DATE: " +depDate);
    }
    public void setCost(String cost){
        TextView post_dep_date = (TextView)mView.findViewById(R.id.post_cost);
        post_dep_date.setText("$" + cost);
    }
    public void setDetours(String detours){
        TextView post_detours = (TextView)mView.findViewById(R.id.post_detours);
        post_detours.setText(detours + " stops along the way");
    }
    public void setTime(String depTime){
        TextView post_dep_time = (TextView)mView.findViewById(R.id.post_time);
        post_dep_time.setText("TIME: " + depTime);
    }
    public void setDriver(String driverName) {
        TextView post_driver_name = (TextView)mView.findViewById(R.id.driver_name_text_view);
        post_driver_name.setText(driverName);
    }
    public void setAvailableseats(String availableSeats) {
        TextView post_available_seats = (TextView)mView.findViewById(R.id.seats_available_text_view);
        post_available_seats.setText(availableSeats);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



}