package com.car.carsquad.carapp;

public class Car {

    String userID;
    String carID;
    String numSeats;
    String model;
    String licensePlate;

    public Car(){
        //default constructor
    }

    public Car(String userID, String carID, String numSeats, String model, String licensePlate){
        this.userID = userID;
        this.carID = carID;
        this.numSeats = numSeats;
        this.model = model;
        this.licensePlate = licensePlate;
    }

    public String getUserID() {
        return userID;
    }
    public String getCarID() {
        return carID;
    }
    public String getNumSeats() {
        return numSeats;
    }
    public String getModel() {
        return model;
    }
    public String getLicensePlate() {
        return licensePlate;
    }
}
