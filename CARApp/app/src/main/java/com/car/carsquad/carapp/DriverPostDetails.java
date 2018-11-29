package com.car.carsquad.carapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class DriverPostDetails extends AppCompatActivity implements View.OnClickListener{

    private Button mDeletePost;
    private DatabaseReference mRequestDatabase, mRiderRef, mFriendsRef;
    String postID;
    RecyclerView riderRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_post_details);
        mDeletePost = (Button) findViewById(R.id.delete_ride_button);
        mDeletePost.setOnClickListener(this);

        getIncomingIntent();

        mRequestDatabase = FirebaseDatabase.getInstance().getReference().child("request").child(postID);
        mRiderRef = FirebaseDatabase.getInstance().getReference().child("user");
        mFriendsRef = FirebaseDatabase.getInstance().getReference().child("friends");

        //recycler view for user requests
        riderRequest = (RecyclerView) findViewById(R.id.request_list);
        riderRequest.setHasFixedSize(true);
        riderRequest.setLayoutManager(new LinearLayoutManager(this));

        //showRequestList();

    }
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseRecyclerAdapter<User,DriverPostDetails.RequestViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<User, DriverPostDetails.RequestViewHolder>
                        (User.class, R.layout.ride_request_cardview, DriverPostDetails.RequestViewHolder.class, mRequestDatabase){
                    @Override
                    protected void populateViewHolder(DriverPostDetails.RequestViewHolder viewHolder, final User model, int position){
                        String name = model.getFirstName() + " " + model.getLastName();
                        Toast.makeText(DriverPostDetails.this, name, Toast.LENGTH_LONG).show();
                        //viewHolder.setRiderName(name);
                    }
                };
        riderRequest.setAdapter(firebaseRecyclerAdapter);
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder{
        View mView;
        //private Button viewRideButton;
        public RequestViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }
        public void setRiderName(String start){
            TextView rider_name = (TextView)mView.findViewById(R.id.rider_name);
            rider_name.setText(start);
        }
    }

    /*private void showRequestList(){
        FirebaseRecyclerAdapter<User,DriverPostDetails.RequestViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<User, DriverPostDetails.RequestViewHolder>
                        (User.class, R.layout.ride_request_cardview, DriverPostDetails.RequestViewHolder.class, mRequestDatabase){
                    @Override
                    protected void populateViewHolder(DriverPostDetails.RequestViewHolder viewHolder, final User model, int position){


                        String name = dataSn
                        Toast.makeText(DriverPostDetails.this, name, Toast.LENGTH_LONG).show();
                        //viewHolder.setRiderName(name);
                    }
                };
        riderRequest.setAdapter(firebaseRecyclerAdapter);
    }*/


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
        AlertDialog.Builder builder = new AlertDialog.Builder(DriverPostDetails.this);
        builder.setCancelable(true);
        builder.setTitle("DELETING POST");
        builder.setMessage("Do you want to proceed?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Delete Post", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                DatabaseReference dbRequest = FirebaseDatabase.getInstance().getReference().child("request").child(postID);
                dbRequest.setValue(null);

                DatabaseReference dbPost = FirebaseDatabase.getInstance().getReference().child("post").child(postID);
                dbPost.setValue(null);

                Intent intent = new Intent(DriverPostDetails.this, DriverActivity.class);
                finish();
                startActivity(intent);
            }
        });
        builder.show();
    }
    @Override
    public void onClick(View view) {
        deletePost();
    }

}
