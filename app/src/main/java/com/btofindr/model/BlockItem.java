package com.btofindr.model;

import java.util.ArrayList;

public class BlockItem {

    private int icon;
    private String projectName;
    private String blockNo;
    private String street;
    private ArrayList<UnitType> unitTypes;
    private Double minPrice;
    private Double maxPrice;

    public BlockItem(){};

    public BlockItem(int icon, String projectName, String blockNo, String street, ArrayList<UnitType> unitTypes, Double minPrice, Double maxPrice){
        this.icon = icon;
        this.projectName = projectName;
        this.blockNo = blockNo;
        this.street = street;
        this.unitTypes = unitTypes;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getBlockNo() {
        return blockNo;
    }

    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public ArrayList<UnitType> getUnitTypes() {
        return unitTypes;
    }

    public void setUnitTypes(ArrayList<UnitType> unitTypes) {
        this.unitTypes = unitTypes;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }
}
