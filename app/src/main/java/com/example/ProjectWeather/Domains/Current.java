package com.example.ProjectWeather.Domains;

import com.google.gson.annotations.SerializedName;

public class Current {
    @SerializedName("last_updated")
    private String time;
    @SerializedName("temp_c")
    private String temp;
    @SerializedName("condition")
    private Condition condition;

    @SerializedName("wind_kph")
    private float wind_speed;
    @SerializedName("humidity")
    private float humidity;         //độ ẩm
    @SerializedName("uv")
    private float UV;               //chỉ số UV

    // getters and setters...

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }


    public float getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(float wind_speed) {
        this.wind_speed = wind_speed;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getUV() {
        return UV;
    }

    public void setUV(float UV) {
        this.UV = UV;
    }

    public static class Condition {
        @SerializedName("icon")
        private String iconPath;
        @SerializedName("text")
        private String status;

        // getters and setters...

        public String getIconPath() {
            return iconPath;
        }

        public void setIconPath(String iconPath) {
            this.iconPath = iconPath;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}

