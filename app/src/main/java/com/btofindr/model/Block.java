package com.btofindr.model;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by Sherry on 31/08/2016.
 */

public class Block {

    private int blockId;
    private String blockNo;
    private String street;
    private String deliveryDate;
    private Double locLat;
    private Double locLong;
    private int travelTime;
    private int travelDist;
    private String sitePlan;
    private String townMap;
    private String blockPlan;
    private String unitDist;
    private String floorPlan;
    private String layoutIdeas;
    private String specs;
    private Double minPrice;
    private Double maxPrice;
    private Project project;
    private ArrayList<UnitType> unitTypes;

    public Block(){};

    public Block(int blockId, String blockNo, String street, String deliveryDate, Double locLat, Double locLong, int travelTime, int travelDist, String sitePlan, String townMap, String blockPlan, String unitDist, String floorPlan, String layoutIdeas, String specs, Double minPrice, Double maxPrice, Project project, ArrayList<UnitType> unitTypes) {
        this.blockId = blockId;
        this.blockNo = blockNo;
        this.street = street;
        this.deliveryDate = deliveryDate;
        this.locLat = locLat;
        this.locLong = locLong;
        this.travelTime = travelTime;
        this.travelDist = travelDist;
        this.sitePlan = sitePlan;
        this.townMap = townMap;
        this.blockPlan = blockPlan;
        this.unitDist = unitDist;
        this.floorPlan = floorPlan;
        this.layoutIdeas = layoutIdeas;
        this.specs = specs;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.project = project;
        this.unitTypes = unitTypes;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
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

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Double getLocLat() {
        return locLat;
    }

    public void setLocLat(Double locLat) {
        this.locLat = locLat;
    }

    public Double getLocLong() {
        return locLong;
    }

    public void setLocLong(Double locLong) {
        this.locLong = locLong;
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

    public String getSitePlan() {
        return sitePlan;
    }

    public void setSitePlan(String sitePlan) {
        this.sitePlan = sitePlan;
    }

    public String getTownMap() {
        return townMap;
    }

    public void setTownMap(String townMap) {
        this.townMap = townMap;
    }

    public String getBlockPlan() {
        return blockPlan;
    }

    public void setBlockPlan(String blockPlan) {
        this.blockPlan = blockPlan;
    }

    public String getUnitDist() {
        return unitDist;
    }

    public void setUnitDist(String unitDist) {
        this.unitDist = unitDist;
    }

    public String getFloorPlan() {
        return floorPlan;
    }

    public void setFloorPlan(String floorPlan) {
        this.floorPlan = floorPlan;
    }

    public String getLayoutIdeas() {
        return layoutIdeas;
    }

    public void setLayoutIdeas(String layoutIdeas) {
        this.layoutIdeas = layoutIdeas;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ArrayList<UnitType> getUnitTypes() {
        return unitTypes;
    }

    public void setUnitTypes(ArrayList<UnitType> unitTypes) {
        this.unitTypes = unitTypes;
    }
}