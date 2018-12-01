package com.car.carsquad.carapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Objects;

public class RiderPostDetails extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mDatabase;
    private DatabaseReference mReference;
    private Button mMessageDriver;
    private Button mRequestRide;
    private Button mCancelRequest;
    String driverFirstName;
    String driverLastName;
    Double driverRating;
    private String driverID;
    private String myID;
    private String postID;
    private String startPt;
    private String endPt;

    //0 = not friend. 1 = request received
    int currentState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_post_details);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("post");
        mDatabase.keepSynced(true);

        mReference = FirebaseDatabase.getInstance().getReference();

        mMessageDriver = (Button) findViewById(R.id.message_driver_button);
        mMessageDriver.setOnClickListener(this);

        mRequestRide = (Button) findViewById(R.id.join_ride_button);
        mRequestRide.setOnClickListener(this);

        mCancelRequest = (Button) findViewById(R.id.cancel_ride_button);
        mCancelRequest.setOnClickListener(this);
        mCancelRequest.setVisibility(View.INVISIBLE);

        getIncomingIntent();
        myID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mReference.child("request").child(postID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(myID)) {
                    String request = dataSnapshot.child(myID).child("request_type").getValue().toString();
                    if(request.equals("received")) {
                        mRequestRide.setEnabled(true);
                        mRequestRide.setText("Cancel Request");
                        currentState = 1;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("postID") && getIntent().hasExtra("startPt") && getIntent().hasExtra("endPt") &&
                getIntent().hasExtra("date") && getIntent().hasExtra("time") && getIntent().hasExtra("cost") &&
                getIntent().hasExtra("driverID")){

            postID = getIntent().getStringExtra("postID");
            startPt = getIntent().getStringExtra("startPt");
            endPt = getIntent().getStringExtra("endPt");
            String date = getIntent().getStringExtra("date");
            String time = getIntent().getStringExtra("time");
            String cost = "$" + getIntent().getStringExtra("cost");
            driverID = getIntent().getStringExtra("driverID");

            //call setDetails
            setDetails(postID, startPt, endPt, date, time, cost, driverID);
        }
    }

    private void setDetails(String postID,String startPt,String endPt,String date,String time, String cost,String driverID){
        TextView startTV = (TextView) findViewById(R.id.start_text_view);
        startTV.setText(startPt.toUpperCase());
        TextView endTV = (TextView) findViewById(R.id.end_text_view);
        endTV.setText(endPt.toUpperCase());
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
    private void requestRide() {
        mRequestRide.setEnabled(false);
        if (currentState == 0) {
            mReference.child("request").child(postID).child(myID).child("request_type")
                    .setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    mReference.child("request").child(myID).child(postID).child("request_type")
                            .setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mRequestRide.setEnabled(true);
                            mRequestRide.setText("Cancel Request");
                            currentState = 1;
                        }
                    });
                }
            });
        }
        if (currentState == 1) {
            mReference.child("request").child(postID).child(myID).child("request_type")
                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mReference.child("request").child(myID).child(postID).child("request_type")
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mRequestRide.setEnabled(true);
                            mRequestRide.setText("Request Ride");
                            currentState = 0;
                        }
                    });
                }
            });
        }
    }

    private void messageDriver() {
        //TODO: do something more than just this (i.e initiate correct chat room)
        //Intent intent = new Intent(RiderPostDetails.this, MessageActivity.class);
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference();

        chatRef.child("chatroom").child(myID).child(startPt.toUpperCase()
                + " - " + endPt.toUpperCase() + " - " + driverID).setValue(driverID);
        chatRef.child("chatroom").child(driverID).child(startPt.toUpperCase()
                + " - " + endPt.toUpperCase() + " - " + myID).setValue(myID);


        /*
        chatRef.child("chatroomUsers").child(myID).child("Room " + startPt.toUpperCase()
                + " - " + endPt.toUpperCase()).setValue(driverID);
        chatRef.child("chatroomUsers").child(driverID).child("Room " + startPt.toUpperCase() +
                " - " + endPt.toUpperCase()).setValue(myID);
        chatRef.child("chatroom").child(startPt.toUpperCase() + " - "
                + endPt.toUpperCase()).setValue("");
        */
        Intent intent = new Intent(RiderPostDetails.this, ChatRoomActivity.class);
        intent.putExtra("driverID", driverID);
        intent.putExtra("startPt", startPt);
        intent.putExtra("endPt", endPt);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view == mRequestRide) {
            requestRide();
        }
        else if (view == mMessageDriver){
            messageDriver();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}