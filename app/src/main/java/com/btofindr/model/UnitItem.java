package com.btofindr.model;

/**
 * Created by Sherry on 06/10/2016.
 */

public class UnitItem {

    private int unitId;
    private String unitNo;
    private Double price;

    public UnitItem(){};

    public UnitItem(int unitId, String unitNo, Double price){
        this.unitId = unitId;
        this.unitNo = unitNo;
        this.price = price;
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
}
