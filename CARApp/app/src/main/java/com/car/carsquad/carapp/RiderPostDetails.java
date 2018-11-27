
package com.car.carsquad.carapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.widget.ImageView;
import android.widget.TextView;

public class RiderPostDetails extends AppCompatActivity {

    TextView mStartTv, mDestTv;
    ImageView mImageTv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_post_details);

        ActionBar actionBar = getSupportActionBar();
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
        mDestTv.setText(desc);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



}
