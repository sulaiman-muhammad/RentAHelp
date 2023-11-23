package com.example.rentahelp;

public class Service {
    private String title;
    private String description;
    private double price;

    public Service() {
        this.title = "Car Wash";
        this.description = "Available Anytime";
        this.price = 20.0;
    }

    public Service(String title, String description, double price) {
        this.title = title;
        this.description = description;
        this.price = price;
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

}
