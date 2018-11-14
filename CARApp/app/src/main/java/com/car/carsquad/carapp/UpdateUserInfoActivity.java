package com.car.carsquad.carapp;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class UpdateUserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    //firebase references
    private DatabaseReference databaseUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    //UI references
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mPhoneNo;

    private Button mSubmitInfo;
    private Button mCancel;
    private String userId;
    private double driverRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        //Database instance
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = Objects.requireNonNull(user).getUid();
        databaseUser = FirebaseDatabase.getInstance().getReference("users");

        //UI References
        mFirstName = (EditText) findViewById(R.id.user_first_name);
        mLastName = (EditText) findViewById(R.id.user_last_name);
        mPhoneNo = (EditText) findViewById(R.id.user_phone_number);
        mSubmitInfo = (Button) findViewById(R.id.submit_user_info);
        mCancel = (Button) findViewById(R.id.cancel_update_info);

        mSubmitInfo.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    private void updateInfo(){
        final String firstName = mFirstName.getText().toString().trim();
        final String lastName = mLastName.getText().toString().trim();
        final String phoneNo = mPhoneNo.getText().toString().trim();

        if(!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(phoneNo)) {
            databaseUser.child(userId).child("driverRating").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    driverRating = Double.parseDouble(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseUser.child(userId).child("isDriver").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String isDriver = dataSnapshot.getValue(String.class);
                    //update user info on firebase
                    User updatedUser = new User(userId, firstName, lastName, phoneNo, isDriver, driverRating);
                    databaseUser.child(userId).setValue(updatedUser);
                    finish();
                    startActivity(new Intent(UpdateUserInfoActivity.this, RiderActivity.class));
                    Toast.makeText(UpdateUserInfoActivity.this, "Your profile information has been updated", Toast.LENGTH_LONG).show();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });

        } else {
            Toast.makeText(this, "Please fill out all the required fields", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        if(view == mCancel){
            finish();
            startActivity(new Intent(this, RiderActivity.class));
        } else if(view == mSubmitInfo){
            updateInfo();
        }
    }
}
