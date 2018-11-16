
package com.car.carsquad.carapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.widget.ImageView;
import android.widget.TextView;

public class RiderPostDetails extends AppCompatActivity {

    TextView mTitleTv, mDetailTv;
    ImageView mImageTv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_post_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Post details");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mTitleTv = findViewById(R.id.post_title_detail);
        mDetailTv = findViewById(R.id.post_desc_detail);
        mImageTv = findViewById(R.id.post_image_detail);

        //get data from intent
        String title = getIntent().getStringExtra("title");
        String desc = getIntent().getStringExtra("description");

        mTitleTv.setText(title);
        mDetailTv.setText(desc);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



}
