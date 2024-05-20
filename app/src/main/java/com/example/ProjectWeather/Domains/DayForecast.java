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
    @SerializedName("humidity")
    int humidity;
    @SerializedName( "wind_speed")
    float wind_s;

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public float getWind_s() {
        return wind_s;
    }

    public void setWind_s(float wind_s) {
        this.wind_s = wind_s;
    }

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
