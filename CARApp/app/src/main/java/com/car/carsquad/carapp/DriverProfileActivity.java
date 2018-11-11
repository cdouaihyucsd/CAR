package com.car.carsquad.carapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverProfileActivity extends AppCompatActivity implements View.OnClickListener {

    //firebase references
    DatabaseReference databaseCar;
    DatabaseReference databaseUser;

    //UI references
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mPhoneNo;
    private EditText mCarModel;
    private EditText mLicensePlate;
    private EditText mNumSeats;

    private Button mSubmitRiderSignup;
    private Button mCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);


/*
        //SKIP THIS PAGE IF USER ALREADY REGISTERED AS DRIVER
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        if() {
            //start profile activity
            //finish();
            startActivity(new Intent(getApplicationContext(), RiderActivity.class));
            //finish();
        }
*/


        databaseCar = FirebaseDatabase.getInstance().getReference("car");
        databaseUser = FirebaseDatabase.getInstance().getReference("users");

        //UI References
        mFirstName = (EditText) findViewById(R.id.driver_first_name);
        mLastName = (EditText) findViewById(R.id.driver_last_name);
        mPhoneNo = (EditText) findViewById(R.id.driver_phone_number);
        mCarModel = (EditText) findViewById(R.id.car_model);
        mLicensePlate = (EditText) findViewById(R.id.license_plate);
        mNumSeats = (EditText) findViewById(R.id.num_seats);
        mSubmitRiderSignup = (Button) findViewById(R.id.submit_driver_signup);
        mCancel = (Button) findViewById(R.id.cancel_signup);

        mSubmitRiderSignup.setOnClickListener(this);
        mCancel.setOnClickListener(this);

    }
    private void confirmRider(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String firstName = mFirstName.getText().toString().trim();
        String lastName = mLastName.getText().toString().trim();
        String phoneNo = mPhoneNo.getText().toString().trim();
        String carModel = mCarModel.getText().toString().trim();
        String licenseNo = mLicensePlate.getText().toString().trim();
        String numSeats = mNumSeats.getText().toString().trim();

        if(!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) &&
                !TextUtils.isEmpty(phoneNo) && !TextUtils.isEmpty(carModel) &&
                !TextUtils.isEmpty(licenseNo)&& !TextUtils.isEmpty(numSeats)) {

            String carId = databaseCar.push().getKey();
            //String userId = databaseUser.push().getKey();
            Car newCar = new Car(userId, carId, numSeats,carModel,licenseNo);
            databaseCar.child(carId).setValue(newCar);
            Toast.makeText(this, "You have successfully enrolled as a driver", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Please fill out the required fields", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View view) {
        if(view == mCancel){
            finish();
            startActivity(new Intent(this, RiderActivity.class));
        } else if(view == mSubmitRiderSignup){
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
            confirmRider();
            startActivity(new Intent(this, DriverActivity.class));
        }
    }
}
