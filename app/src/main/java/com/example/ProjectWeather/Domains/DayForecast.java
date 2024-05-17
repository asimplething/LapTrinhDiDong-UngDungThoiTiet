package com.example.ProjectWeather.Domains;

import com.google.gson.annotations.SerializedName;

public class DayForecast {
    @SerializedName("dayOfWeek")
    String dayOfWeek;
    @SerializedName("icon")
    String icon;
    @SerializedName("chanceOfRain")
    String chanceOfRain;
    @SerializedName("minTemperature")
    String minTemperature;

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getChanceOfRain() {
        return chanceOfRain;
    }

    public void setChanceOfRain(String chanceOfRain) {
        this.chanceOfRain = chanceOfRain;
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    @SerializedName("maxTemperature")
    String maxTemperature;
}
