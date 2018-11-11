package com.car.carsquad.carapp;

public class Post {
    String userID;
    String postID;
    String startPt;
    String endPt;
    String date;
    String time;
    String note;

    public Post(){
        //default constructor
    }

    public Post(String userID, String postID, String startPt, String endPt, String date, String time, String note){
        this.userID = userID;
        this.postID = postID;
        this.startPt = startPt;
        this.endPt = endPt;
        this.date = date;
        this.time = time;
        this.note = note;
    }

    public String getUserID() {
        return userID;
    }
    public String getPostID() {
        return postID;
    }
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public String getEndPt() {
        return endPt;
    }
    public String getStartPt() {
        return startPt;
    }
    public String getNote() {
        return note;
    }
}
