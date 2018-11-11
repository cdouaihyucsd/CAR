package com.car.carsquad.carapp;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {

    //User fields
    public String userID;
    public String firstName;
    public String lastName;
    public String email;
    public boolean isDriver;
    public double driverRating;

    //link to database
    //private DatabaseReference mDatabase;
    //mDatabase = FirebaseDatabase.getInstance().getReference();


    public User(){
        //default constructor
    }

    public User(String userID, String firstName, String lastName, String email,
                 boolean isDriver, double driverRating){
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isDriver = isDriver;
        this.driverRating = driverRating;
    }

    /*
    private void writeNewUser(String userID, String firstName, String lastName, String email,
                              boolean isDriver, double driverRating) {
        User user = new User(userID, firstName, lastName, email, isDriver, driverRating);
        mDatabase.child("users").child(userID).setValue(user);
    }*/
}
