package com.caffeine.covid_19stats.Model;

public class CountryModel {

    String country;
    int total_confirmed, total_death, total_recovered, newConfirmed, newRecovered, newDeaths, active, critical, cpom, dpom, tests, tpom;

    public CountryModel() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getTotal_confirmed() {
        return total_confirmed;
    }

    public void setTotal_confirmed(int total_confirmed) {
        this.total_confirmed = total_confirmed;
    }

    public int getTotal_death() {
        return total_death;
    }

    public void setTotal_death(int total_death) {
        this.total_death = total_death;
    }

    public int getTotal_recovered() {
        return total_recovered;
    }

    public void setTotal_recovered(int total_recovered) {
        this.total_recovered = total_recovered;
    }

    public int getNewConfirmed() {
        return newConfirmed;
    }

    public void setNewConfirmed(int newConfirmed) {
        this.newConfirmed = newConfirmed;
    }

    public int getNewRecovered() {
        return newRecovered;
    }

    public void setNewRecovered(int newRecovered) {
        this.newRecovered = newRecovered;
    }

    public int getNewDeaths() {
        return newDeaths;
    }

    public void setNewDeaths(int newDeaths) {
        this.newDeaths = newDeaths;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getCritical() {
        return critical;
    }

    public void setCritical(int critical) {
        this.critical = critical;
    }

    public int getCpom() {
        return cpom;
    }

    public void setCpom(int cpom) {
        this.cpom = cpom;
    }

    public int getDpom() {
        return dpom;
    }

    public void setDpom(int dpom) {
        this.dpom = dpom;
    }

    public int getTests() {
        return tests;
    }

    public void setTests(int tests) {
        this.tests = tests;
    }

    public int getTpom() {
        return tpom;
    }

    public void setTpom(int tpom) {
        this.tpom = tpom;
    }
}
