package com.example.ProjectWeather.Domains;

import com.google.gson.annotations.SerializedName;

public class HourForecast {
    @SerializedName("time")
    String time;
    @SerializedName("temperature")
    String temp_c;
    @SerializedName("condition")
    String condition;
    @SerializedName("icon")
    String iconPath;
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemp_c() {
        return temp_c;
    }

    public void setTemp_c(String temp_c) {
        this.temp_c = temp_c;
    }
}
