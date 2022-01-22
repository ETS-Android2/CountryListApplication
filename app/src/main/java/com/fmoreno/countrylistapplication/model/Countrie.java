package com.fmoreno.countrylistapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Countrie {
    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("capital")
    @Expose
    public String capital;
    @SerializedName("region")
    @Expose
    public String region;

    @SerializedName("latlng")
    @Expose
    public ArrayList<Double> latlng;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public ArrayList<Double> getLatlng() {
        return latlng;
    }

    public void setLatlng(ArrayList<Double> latlng) {
        this.latlng = latlng;
    }
}
