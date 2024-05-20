package com.example.ProjectWeather.Domains;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("country")
    private String nation;
    @SerializedName("name")
    private String city;
    @SerializedName("localtime")
    private String localtime;

    public String getLocaltime() {
        return localtime;
    }

    public void setLocaltime(String localtime) {
        this.localtime = localtime;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
