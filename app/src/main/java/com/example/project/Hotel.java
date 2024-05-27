package com.example.project;

public class Hotel {
    private int imageResId;
    private String name;
    private String location;
    private String price;
    private String description;
    private String rating;
    private String type;
    private int numBed;

    public Hotel(int imageResId, String name, String location, String price, String description, String rating, String type, int numBed) {
        this.imageResId = imageResId;
        this.name = name;
        this.location = location;
        this.price = price;
        this.description = description;
        this.rating = rating;
        this.type = type;
        this.numBed = numBed;
    }

    public String getDescription() {
        return description;
    }

    public String getRating() {
        return rating;
    }

    public String getType() {
        return type;
    }

    public int getNumBed() {
        return numBed;
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

    public  String getPrice() {
        return price;
    }
}
