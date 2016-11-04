package com.btofindr.model;

import java.util.ArrayList;

/**
 * This describes a HDB BTO Block in a Project that contains one or more Unit Types.
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 31/08/2016
 */

public class BlockItem {

    private int blockId;
    private String icon;
    private String projectName;
    private String blockNo;
    private String street;
    private ArrayList<UnitType> unitTypes;
    private Double minPrice;
    private Double maxPrice;
    private int travelTime;
    private int travelDist;

    public BlockItem(){};

    public BlockItem(String icon, String projectName, String blockNo, String street, ArrayList<UnitType> unitTypes, Double minPrice, Double maxPrice, int travelTime, int travelDist) {
        this.blockId = blockId;
        this.icon = icon;
        this.projectName = projectName;
        this.blockNo = blockNo;
        this.street = street;
        this.unitTypes = unitTypes;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.travelTime = travelTime;
        this.travelDist = travelDist;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
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

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }

    public int getTravelDist() {
        return travelDist;
    }

    public void setTravelDist(int travelDist) {
        this.travelDist = travelDist;
    }
}
