package com.btofindr.model;

import java.sql.Date;
import java.util.ArrayList;

public class Block {

    private int blockId;
    private String blockNo;
    private String street;
    private Date deliveryDate;
    private Double locLat;
    private Double locLong;
    private String sitePlan;
    private String townMap;
    private String blockPlan;
    private String unitDist;
    private String floorPlan;
    private String layoutIdeas;
    private String specs;
    private Project project;
    private ArrayList<UnitType> unitTypes;

    public Block(){};

    public Block(int blockId, String blockNo, String street, Date deliveryDate, Double locLat, Double locLong, String sitePlan, String townMap, String blockPlan, String unitDist, String floorPlan, String layoutIdeas, String specs, Project project, ArrayList<UnitType> unitTypes) {
        this.blockId = blockId;
        this.blockNo = blockNo;
        this.street = street;
        this.deliveryDate = deliveryDate;
        this.locLat = locLat;
        this.locLong = locLong;
        this.sitePlan = sitePlan;
        this.townMap = townMap;
        this.blockPlan = blockPlan;
        this.unitDist = unitDist;
        this.floorPlan = floorPlan;
        this.layoutIdeas = layoutIdeas;
        this.specs = specs;
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

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
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