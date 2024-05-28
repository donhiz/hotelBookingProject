package com.example.project;

public class Hotel {
    private int imageResId;
    private String name;
    private String location;
    private String price;
    private String description;
    private String rating;

    private int numBed;

    private int bath;
    private boolean wifi;

    public Hotel(int imageResId, String name, String location, String price, String description, String rating, int numBed, int bath, boolean wifi) {
        this.imageResId = imageResId;
        this.name = name;
        this.location = location;
        this.price = price;
        this.description = description;
        this.rating = rating;
        this.numBed = numBed;
        this.bath = bath;
        this.wifi = wifi;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getRating() {
        return rating;
    }

    public int getBeds() {
        return numBed;
    }

    public int getBaths() {
        return bath;
    }

    public boolean isWifi() {
        return wifi;
    }
}
