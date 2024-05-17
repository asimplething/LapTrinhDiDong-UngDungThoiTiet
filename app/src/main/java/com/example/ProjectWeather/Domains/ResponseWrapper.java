package com.example.ProjectWeather.Domains;

import com.google.gson.annotations.SerializedName;

public class ResponseWrapper {
    @SerializedName("location")
    private Current location;
    @SerializedName("current")
    private Current current;



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
