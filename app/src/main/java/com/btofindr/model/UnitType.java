package com.btofindr.model;

import java.util.ArrayList;

public class UnitType {

    private int unitTypeId;
    private String unitTypeName;
    private int quotaChinese;
    private int quotaMalay;
    private int quotaOthers;
    private ArrayList<Unit> units;
    private Block block;

    public UnitType(){};

    public UnitType(int unitTypeId, String unitTypeName, int quotaChinese, int quotaMalay, int quotaOthers, ArrayList<Unit> units, Block block) {
        this.unitTypeId = unitTypeId;
        this.unitTypeName = unitTypeName;
        this.quotaChinese = quotaChinese;
        this.quotaMalay = quotaMalay;
        this.quotaOthers = quotaOthers;
        this.units = units;
        this.block = block;
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

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}