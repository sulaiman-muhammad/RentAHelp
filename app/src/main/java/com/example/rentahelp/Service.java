package com.example.rentahelp;

public class Service {
        private String title;
        private String description;
        private double price;

        public Service() {
            // Default constructor required for Firebase
            this.title = "Option 1";
            this.description = "I am Batman";
            this.price = 100.0;
        }

        public Service(String title, String description, double price) {
            this.title = title;
            this.description = description;
            this.price = price;
        }

    // Getter for the title property
    public String getTitle() {
        return title;
    }

    // Setter for the title property
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter for the description property
    public String getDescription() {
        return description;
    }

    // Setter for the description property
    public void setDescription(String description) {
        this.description = description;
    }

    // Getter for the price property
    public double getPrice() {
        return price;
    }

    // Setter for the price property
    public void setPrice(double price) {
        this.price = price;
    }

}
