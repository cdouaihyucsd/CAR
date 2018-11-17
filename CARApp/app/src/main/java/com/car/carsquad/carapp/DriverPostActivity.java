package com.car.carsquad.carapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Driver;
import java.util.Calendar;
import java.util.Objects;

public class DriverPostActivity extends AppCompatActivity implements View.OnClickListener {

    //private AutoCompleteTextView mStartPoint;
    //private AutoCompleteTextView mEndPoint;
    private TextView message;
    private String startPt;
    private String endPt;
    private TextView mDisplayDate;
    private TextView mDisplayTime;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private Button mPostRide;
    private Button mCancelPost;

    DatabaseReference databasePosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_post);

        //GOOGLE PLACES AUTOCOMPLETE
        PlaceAutocompleteFragment autocompleteFragment1 = (PlaceAutocompleteFragment) getFragmentManager()
                .findFragmentById(R.id.post_start_point1);
        autocompleteFragment1.setHint("ENTER START POINT");
        autocompleteFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                startPt = place.getName().toString();
                //place.getLatLng();
            }
            @Override
            public void onError(Status status) {
            }
        });
        PlaceAutocompleteFragment autocompleteFragment2 = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.post_destination_point1);
        autocompleteFragment2.setHint("ENTER DESTINATION");
        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                endPt = place.getName().toString();
            }
            @Override
            public void onError(Status status) {
            }
        });

        databasePosts = FirebaseDatabase.getInstance().getReference("post");

        //UI References
        mDisplayDate = (TextView) findViewById(R.id.tvDate);
        mDisplayTime = (TextView) findViewById(R.id.tvTime);
        mPostRide = (Button) findViewById(R.id.confirm_post);
        mCancelPost = (Button) findViewById(R.id.post_cancel);
        //mStartPoint = (AutoCompleteTextView) findViewById(R.id.post_start_point);
        //mEndPoint = (AutoCompleteTextView) findViewById(R.id.post_destination_point);
        message = (TextView) findViewById(R.id.note_to_riders);

        //set action
        mPostRide.setOnClickListener(this);
        mCancelPost.setOnClickListener(this);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        DriverPostActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,
                        year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                TimePickerDialog dialog = new TimePickerDialog(
                        DriverPostActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mTimeSetListener,
                        hour, minute, true);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                Log.d("DriverPostActivity", "onDateSet: mm/dd/yyyy: " + month + "/" + day + "/" + year);
                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };
        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Log.d("DriverPostActivity", "onTimeSet: hh:mm: " + hour + "/" + minute);
                String time = checkDigit(hour) + ":" + checkDigit(minute);
                mDisplayTime.setText(time);
            }
        };
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }
    private void postRide(){
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        //String start = mStartPoint.getText().toString().trim();
        //String dest = mEndPoint.getText().toString().trim();
        String departureDate = mDisplayDate.getText().toString().trim();
        String departureTime = mDisplayTime.getText().toString().trim();
        String note = message.getText().toString().trim();

        if(!TextUtils.isEmpty(startPt) && !TextUtils.isEmpty(endPt) &&
                !TextUtils.isEmpty(departureDate) && !TextUtils.isEmpty(departureTime)) {

            String postId = databasePosts.push().getKey();
            Post newPost = new Post(userId, postId,startPt,endPt,departureDate,departureTime, note);
            databasePosts.child(Objects.requireNonNull(postId)).setValue(newPost);
            finish();
            startActivity(new Intent(this, DriverActivity.class));
            Toast.makeText(this, "Your ride has been posted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Please fill out the required fields", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        if(view == mCancelPost){
            finish();
            startActivity(new Intent(this, DriverActivity.class));
        } else if(view == mPostRide){
            String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("post");
            postRide();
        }
    }

    //prevent user from pressing the back button to go back from the main app screen
   // @Override
   // public void onBackPressed() {
   //     moveTaskToBack(true);
    //}
}
