package com.car.carsquad.carapp;

public class Car {

    String carID;
    String numSeats;
    String model;
    String licensePlate;

    public Car(){
        //default constructor
    }

    public Car(String carID, String numSeats, String model, String licensePlate){
        this.carID = carID;
        this.numSeats = numSeats;
        this.model = model;
        this.licensePlate = licensePlate;
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
