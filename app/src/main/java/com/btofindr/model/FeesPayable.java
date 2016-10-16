package com.btofindr.model;

/**
 * Created by JHNG on 15/10/2016.
 */

public class FeesPayable {
    private double grantAmt;
    private double afterGrantAmt;
    private double applFee;
    private double optionFee;
    private double signingFeesCash;
    private double signingFeesCpf;
    private double collectionFeesCpf;
    private double collectionFeesCash;
    private double monthlyCash;
    private double monthlyCpf;

    public FeesPayable(){}

    public FeesPayable(double grantAmt, double afterGrantAmt, double applFee, double optionFee,
             double signingFeesCash, double signingFeesCpf, double collectionFeesCpf,
                       double collectionFeesCash, double monthlyCash, double monthlyCpf){
        this.grantAmt = grantAmt;
        this.afterGrantAmt = afterGrantAmt;
        this.applFee = applFee;
        this.optionFee = optionFee;
        this.signingFeesCash = signingFeesCash;
        this.signingFeesCpf = signingFeesCpf;
        this.collectionFeesCash = collectionFeesCash;
        this.collectionFeesCpf = collectionFeesCpf;
        this.monthlyCash = monthlyCash;
        this.monthlyCpf = monthlyCpf;
    }

    public double getGrantAmt() {
        return grantAmt;
    }

    public void setGrantAmt(double grantAmt) {
        this.grantAmt = grantAmt;
    }

    public double getAfterGrantAmt() {
        return afterGrantAmt;
    }

    public void setAfterGrantAmt(double afterGrantAmt) {
        this.afterGrantAmt = afterGrantAmt;
    }

    public double getApplFee() {
        return applFee;
    }

    public void setApplFee(double applFee) {
        this.applFee = applFee;
    }

    public double getOptionFee() {
        return optionFee;
    }

    public void setOptionFee(double optionFee) {
        this.optionFee = optionFee;
    }

    public double getSigningFeesCash() {
        return signingFeesCash;
    }

    public void setSigningFeesCash(double signingFeesCash) {
        this.signingFeesCash = signingFeesCash;
    }

    public double getSigningFeesCpf() {
        return signingFeesCpf;
    }

    public void setSigningFeesCpf(double signingFeesCpf) {
        this.signingFeesCpf = signingFeesCpf;
    }

    public double getCollectionFeesCpf() {
        return collectionFeesCpf;
    }

    public void setCollectionFeesCpf(double collectionFeesCpf) {
        this.collectionFeesCpf = collectionFeesCpf;
    }

    public double getCollectionFeesCash() {
        return collectionFeesCash;
    }

    public void setCollectionFeesCash(double collectionFeesCash) {
        this.collectionFeesCash = collectionFeesCash;
    }

    public double getMonthlyCash() {
        return monthlyCash;
    }

    public void setMonthlyCash(double monthlyCash) {
        this.monthlyCash = monthlyCash;
    }

    public double getMonthlyCpf() {
        return monthlyCpf;
    }

    public void setMonthlyCpf(double monthlyCpf) {
        this.monthlyCpf = monthlyCpf;
    }
}
