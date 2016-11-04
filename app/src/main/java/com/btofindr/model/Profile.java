package com.btofindr.model;

/**
 * This describes a user's Profile.
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 31/08/2016
 */

public class Profile {
    private String postalCode;
    private double income;
    private double currentCpf;
    private double monthlyCpf;
    private int loanTenure;

    public Profile() {
        this.postalCode = "";
        this.income = 0;
        this.currentCpf = 0;
        this.monthlyCpf = 0;
        this.loanTenure = 0;
    }

    public Profile(String postalCode, double income, double currentCpf, double monthlyCpf, int loanTenure) {
        this.postalCode = postalCode;
        this.income = income;
        this.currentCpf = currentCpf;
        this.monthlyCpf = monthlyCpf;
        this.loanTenure = loanTenure;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getCurrentCpf() {
        return currentCpf;
    }

    public void setCurrentCpf(double currentCpf) {
        this.currentCpf = currentCpf;
    }

    public double getMonthlyCpf() {
        return monthlyCpf;
    }

    public void setMonthlyCpf(double monthlyCpf) {
        this.monthlyCpf = monthlyCpf;
    }

    public int getLoanTenure() {
        return loanTenure;
    }

    public void setLoanTenure(int loanTenure) {
        this.loanTenure = loanTenure;
    }
}
