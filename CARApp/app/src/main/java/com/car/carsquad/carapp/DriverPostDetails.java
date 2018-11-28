package com.car.carsquad.carapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverPostDetails extends AppCompatActivity implements View.OnClickListener{

    private Button mDeletePost;
    private DatabaseReference mDatabase;
    String driverFirstName;
    String driverLastName;
    Double driverRating;

    String postID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_post_details);

        mDeletePost = (Button) findViewById(R.id.delete_ride_button);
        mDeletePost.setOnClickListener(this);

        getIncomingIntent();
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("postID") && getIntent().hasExtra("startPt") && getIntent().hasExtra("endPt") &&
                getIntent().hasExtra("date") && getIntent().hasExtra("time") && getIntent().hasExtra("cost") &&
                getIntent().hasExtra("driverID")){

            postID = getIntent().getStringExtra("postID");
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

    }

    private void deletePost(){
        Toast.makeText(this, "Post delete button clicked", Toast.LENGTH_SHORT).show();

        DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference().child("post").child(postID);
        dbNode.setValue(null);

        Intent intent = new Intent(DriverPostDetails.this, DriverActivity.class);
        finish();
        startActivity(intent);
    }

    //when the user clicks register
    @Override
    public void onClick(View view) {
        if (view == mDeletePost) {
            deletePost();
        }
    }

}
