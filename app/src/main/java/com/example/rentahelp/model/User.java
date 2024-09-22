package com.example.rentahelp.model;

public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String dob;
    private double credits;
    private double ratingTotal;
    private int ratingCount;

    public User() {
    }

    public User(String userId, String firstName, String lastName, String phoneNumber, String email, String dob, double credits, double ratingTotal, int ratingCount) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dob = dob;
        this.credits = credits;
        this.ratingTotal = ratingTotal;
        this.ratingCount = ratingCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public double getCredits() {
        return credits;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public double getRatingTotal() {
        return ratingTotal;
    }

    public void setRatingTotal(double ratingTotal) {
        this.ratingTotal = ratingTotal;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }
}