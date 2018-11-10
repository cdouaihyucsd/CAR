package com.car.carsquad.carapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DriverActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private FloatingActionButton mAddPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        mAddPost = (FloatingActionButton) findViewById(R.id.add_post);

        mAddPost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent nextActivityIntent = new Intent(DriverActivity.this, DriverPostActivity.class);
                startActivity(nextActivityIntent);
            }
        });
    }

    //prevent user from pressing the back button to go back from the main app screen
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
