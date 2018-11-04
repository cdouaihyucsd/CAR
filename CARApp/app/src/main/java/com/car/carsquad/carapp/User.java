package com.car.carsquad.carapp;

public class User {
    String email;
    String password;
    boolean verified;

    public User(String email, String password, boolean verified){
        this.email = email;
        this.password = password;
        this.verified = verified;
    }
}
