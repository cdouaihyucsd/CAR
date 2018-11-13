package com.car.carsquad.carapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mCancelSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mCancelSearch = (Button) findViewById(R.id.cancel_search);
        mCancelSearch.setOnClickListener(this);
    }

    public void onClick(View view) {
        if(view == mCancelSearch){
            finish();
            startActivity(new Intent(this, RiderActivity.class));
        }
    }
}
