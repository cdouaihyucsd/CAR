package com.car.carsquad.carapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        //Database instance
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();
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
        String firstName = mFirstName.getText().toString().trim();
        String lastName = mLastName.getText().toString().trim();
        String phoneNo = mPhoneNo.getText().toString().trim();

        if(!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(firstName)) {
            //update user info on firebase
            User updatedUser = new User();
            updatedUser.setDriver(true);
            updatedUser.setFirstName(firstName);
            updatedUser.setLastName(lastName);
            updatedUser.setPhoneNo(phoneNo);
            databaseUser.child(userId).setValue(updatedUser);
            finish();
            startActivity(new Intent(this, RiderActivity.class));
            Toast.makeText(this, "Your profile information has been updated", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Please fill out all the required fields", Toast.LENGTH_LONG).show();
            return;
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
