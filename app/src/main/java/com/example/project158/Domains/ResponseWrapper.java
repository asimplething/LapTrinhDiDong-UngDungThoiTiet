package com.example.project158.Domains;

import com.google.gson.annotations.SerializedName;

public class ResponseWrapper {
    @SerializedName("location")
    private Current location;
    @SerializedName("current")
    private Current current;
    @SerializedName("forecast")
    private Forecast forecast;

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    public Current getLocation() {
        return location;
    }

    public void setLocation(Current location) {
        this.location = location;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }
}
