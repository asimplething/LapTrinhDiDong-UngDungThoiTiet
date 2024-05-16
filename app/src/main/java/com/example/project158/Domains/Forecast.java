package com.example.project158.Domains;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Forecast {
    @SerializedName("forecastday")
    ArrayList<ForecastDay> forecastDay;

    public ArrayList<ForecastDay> getForecastDay() {
        return forecastDay;
    }

    public void setForecastDay(ArrayList<ForecastDay> forecastDay) {
        this.forecastDay = forecastDay;
    }

    public static class ForecastDay {
        @SerializedName("hour")
        ArrayList<Hour> hours;

        public ArrayList<Hour> getHours() {
            return hours;
        }

        public void setHours(ArrayList<Hour> hours) {
            this.hours = hours;
        }
    }
}
