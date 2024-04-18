package com.example.rentahelp.model;

import java.io.Serializable;
import java.util.List;

public class Service implements Serializable {
    private String serviceId;
    private String title;
    private String description;
    private double price;
    private String availability;
    private String startTime;
    private String endTime;
    private String address;
    private double rating;
    private String review;
    private String postedBy;
    private String acceptedBy;
    private List<String> potential;
    private Status status;

    public Service() {
    }

    public Service(String serviceId, String title, String description, double price, String availability, String startTime, String endTime, String address, double rating, String review, String postedBy, String acceptedBy, List<String> potential) {
        this.serviceId = serviceId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.availability = availability;
        this.startTime = startTime;
        this.endTime = endTime;
        this.address = address;
        this.rating = rating;
        this.review = review;
        this.postedBy = postedBy;
        this.acceptedBy = acceptedBy;
        this.potential = potential;
        this.status = Status.INITIATED;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(String acceptedBy) {
        this.acceptedBy = acceptedBy;
    }

    public List<String> getPotential() {
        return potential;
    }

    public void setPotential(List<String> potential) {
        this.potential = potential;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
