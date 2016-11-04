package com.btofindr.model;

/**
 * This describes a HDB BTO Unit in a Unit Type.
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 31/08/2016
 */

public class Unit {

    private int unitId;
    private String unitNo;
    private Double price;
    private int floorArea;
    private boolean avail;
    private UnitType unitType;
    private int faveCount;
    private FeesPayable fees;

    public Unit(){};

    public Unit(int unitId, String unitNo, Double price, int floorArea, boolean avail, UnitType unitType
        ,int faveCount, FeesPayable fees) {
        this.unitId = unitId;
        this.unitNo = unitNo;
        this.price = price;
        this.floorArea = floorArea;
        this.avail = avail;
        this.unitType = unitType;
        this.faveCount = faveCount;
        this.fees = fees;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(int floorArea) {
        this.floorArea = floorArea;
    }

    public boolean isAvail() {
        return avail;
    }

    public void setAvail(boolean avail) {
        this.avail = avail;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public int getFaveCount() {
        return faveCount;
    }

    public void setFaveCount(int faveCount) {
        this.faveCount = faveCount;
    }

    public FeesPayable getFees() {
        return fees;
    }

    public void setFees(FeesPayable fees) {
        this.fees = fees;
    }
}