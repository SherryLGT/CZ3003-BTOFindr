package com.btofindr.model;

/**
 * Created by Sherry on 31/08/2016.
 */

public class Floor {
    String floor;
    double minPrice;
    double maxPrice;

    public Floor() {};

    public Floor(String floor) {
        this.floor = floor;
    }

    public Floor(String floor, Double minPrice, Double maxPrice) {
        this.floor = floor;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }
}
