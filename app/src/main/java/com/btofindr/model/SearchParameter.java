package com.btofindr.model;

import java.util.ArrayList;

/**
 * This describes Search Parameters that a user
 * will pass to BTOFindr when searching for Blocks.
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 31/08/2016
 */

public class SearchParameter {
    private ArrayList<String> townNames;
    private char ethnic;
    private ArrayList<String> unitTypes;
    private double maxPrice;
    private double minPrice;
    private char orderBy;
    private String postalCode;

    public SearchParameter(ArrayList<String> townNames, char ethic, ArrayList<String> unitTypes, double maxPrice, double minPrice, char orderBy, String postalCode) {
        this.townNames = townNames;
        this.ethnic = ethic;
        this.unitTypes = unitTypes;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.orderBy = orderBy;
        this.postalCode = postalCode;
    }

    public ArrayList<String> getTownNames() {
        return townNames;
    }

    public void setTownNames(ArrayList<String> townNames) {
        this.townNames = townNames;
    }

    public char getEthnic() {
        return ethnic;
    }

    public void setEthnic(char ethnic) {
        this.ethnic = ethnic;
    }

    public ArrayList<String> getUnitTypes() {
        return unitTypes;
    }

    public void setUnitTypes(ArrayList<String> unitTypes) {
        this.unitTypes = unitTypes;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public char getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(char orderBy) {
        this.orderBy = orderBy;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
