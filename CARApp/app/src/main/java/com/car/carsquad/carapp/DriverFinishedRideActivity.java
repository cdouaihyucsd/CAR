package com.car.carsquad.carapp;

import android.content.Intent;
import android.icu.text.StringSearch;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Driver;
import java.util.Objects;

public class DriverFinishedRideActivity extends AppCompatActivity implements View.OnClickListener  {

    public RatingBar ratingBar;
    private Button payCash;
    private Button payCard;
    private Button mCancel;
    private String postId;
    private String riderId;
    private int ratingCount;
    private double rating;
    DatabaseReference finishDB = FirebaseDatabase.getInstance().getReference().child("completed");

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
        ratingBar = (RatingBar) findViewById(R.id.rider_rate_bar);

        getIncomingIntent();
    }

    @Override
    public void onClick(View view) {
        if(view == payCard){

            FirebaseDatabase.getInstance().getReference().child("post").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Post completedPost = dataSnapshot.getValue(Post.class);
                    finishDB.child(riderId).child(postId).setValue(completedPost);
                    finishDB.child(riderId).child(postId).child("paymentType").setValue("card");
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
            //remove user from accepted list
            FirebaseDatabase.getInstance().getReference().child("accepted").child(postId).child(riderId).removeValue();
            FirebaseDatabase.getInstance().getReference().child("accepted").child(riderId).child(postId).removeValue();
            FirebaseDatabase.getInstance().getReference().child("accepted_obj").child(postId).child(riderId).removeValue();
            FirebaseDatabase.getInstance().getReference().child("accepted_obj").child(riderId).child(postId).removeValue();
            FirebaseDatabase.getInstance().getReference().child("seatsAvailable").child(postId).child("seatsAvail")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int seatsAvail = dataSnapshot.getValue(Integer.class);
                            seatsAvail = seatsAvail + 1;
                            FirebaseDatabase.getInstance().getReference().child("seatsAvailable")
                                    .child(postId).child("seatsAvail").setValue(seatsAvail);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
            updateDriverRating();
            finish();
        } else if (view == payCash) {
            FirebaseDatabase.getInstance().getReference().child("post").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Post completedPost = dataSnapshot.getValue(Post.class);
                    finishDB.child(riderId).child(postId).setValue(completedPost);
                    finishDB.child(riderId).child(postId).child("paymentType").setValue("cash");
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
            //remove user from accepted list
            FirebaseDatabase.getInstance().getReference().child("accepted").child(postId).child(riderId).removeValue();
            FirebaseDatabase.getInstance().getReference().child("accepted").child(riderId).child(postId).removeValue();
            FirebaseDatabase.getInstance().getReference().child("accepted_obj").child(postId).child(riderId).removeValue();
            FirebaseDatabase.getInstance().getReference().child("accepted_obj").child(riderId).child(postId).removeValue();
            FirebaseDatabase.getInstance().getReference().child("seatsAvailable").child(postId).child("seatsAvail")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int seatsAvail = dataSnapshot.getValue(Integer.class);
                            seatsAvail = seatsAvail + 1;
                            FirebaseDatabase.getInstance().getReference().child("seatsAvailable")
                                    .child(postId).child("seatsAvail").setValue(seatsAvail);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
            updateDriverRating();
            finish();

        } else if (view == mCancel) {
            finish();
        }
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("postID") && getIntent().hasExtra("riderID")){
            postId = getIntent().getStringExtra("postID");
            riderId = getIntent().getStringExtra("riderID");
        }
    }

    private void updateDriverRating(){
        final DatabaseReference riderDB = FirebaseDatabase.getInstance().getReference().child("users");
        final String driverId = FirebaseAuth.getInstance().getUid();
        riderDB.child(riderId).child("riderRating").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rating = dataSnapshot.getValue(Double.class);

                riderDB.child(riderId).child("riderRatingCount").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ratingCount = dataSnapshot.getValue(Integer.class);
                        double total = rating * ratingCount;
                        double newRating = (ratingBar.getRating() + total)/(ratingCount+1);
                        riderDB.child(riderId).child("riderRating").setValue(newRating);
                        riderDB.child(riderId).child("riderRatingCount").setValue(ratingCount+1);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
