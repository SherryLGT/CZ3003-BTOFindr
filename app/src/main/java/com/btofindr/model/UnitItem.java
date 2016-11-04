package com.btofindr.model;

/**
 * This describes a HDB BTO Unit in a Unit Type.
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 06/10/2016
 */

public class UnitItem {

    private int unitId;
    private String unitNo;
    private Double price;
    private UnitType unitType;


    public UnitItem(){};

    public UnitItem(int unitId, String unitNo, Double price){
        this.unitId = unitId;
        this.unitNo = unitNo;
        this.price = price;
        this.unitType = unitType;
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

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }
}
