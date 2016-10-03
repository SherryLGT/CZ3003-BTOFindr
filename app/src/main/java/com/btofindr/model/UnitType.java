package com.btofindr.model;

import java.util.ArrayList;

/**
 * Created by Sherry on 31/08/2016.
 */

public class UnitType {

    private int unitTypeId;
    private String unitTypeName;
    private int quotaChinese;
    private int quotaMalay;
    private int quotaOthers;
    private Block block;
    private ArrayList<Unit> units;

    public UnitType(){};

    public UnitType(int unitTypeId, String unitTypeName, int quotaChinese, int quotaMalay, int quotaOthers, Block block, ArrayList<Unit> units) {
        this.unitTypeId = unitTypeId;
        this.unitTypeName = unitTypeName;
        this.quotaChinese = quotaChinese;
        this.quotaMalay = quotaMalay;
        this.quotaOthers = quotaOthers;
        this.block = block;
        this.units = units;
    }

    public int getUnitTypeId() {
        return unitTypeId;
    }

    public void setUnitTypeId(int unitTypeId) {
        this.unitTypeId = unitTypeId;
    }

    public String getUnitTypeName() {
        return unitTypeName;
    }

    public void setUnitTypeName(String unitTypeName) {
        this.unitTypeName = unitTypeName;
    }

    public int getQuotaChinese() {
        return quotaChinese;
    }

    public void setQuotaChinese(int quotaChinese) {
        this.quotaChinese = quotaChinese;
    }

    public int getQuotaMalay() {
        return quotaMalay;
    }

    public void setQuotaMalay(int quotaMalay) {
        this.quotaMalay = quotaMalay;
    }

    public int getQuotaOthers() {
        return quotaOthers;
    }

    public void setQuotaOthers(int quotaOthers) {
        this.quotaOthers = quotaOthers;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }
}