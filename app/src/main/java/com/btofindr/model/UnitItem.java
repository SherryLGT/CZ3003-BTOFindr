package com.btofindr.model;

/**
 * Created by Sherry on 06/10/2016.
 */

public class UnitItem {

    private String unitNo;
    private Double price;

    public UnitItem(){};

    public UnitItem(String unitNo, Double price){
        this.unitNo = unitNo;
        this.price = price;
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
