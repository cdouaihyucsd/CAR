package com.car.carsquad.carapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DriverPostDetails extends AppCompatActivity implements View.OnClickListener{

    private Button mDeletePost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_post_details);

        mDeletePost = (Button) findViewById(R.id.delete_ride_button);
        mDeletePost.setOnClickListener(this);
    }

    private void deletePost(){
        Toast.makeText(this, "Post delete button clicked", Toast.LENGTH_SHORT).show();

    }

    //when the user clicks register
    @Override
    public void onClick(View view) {
        if (view == mDeletePost) {
            deletePost();
        }
    }

}
