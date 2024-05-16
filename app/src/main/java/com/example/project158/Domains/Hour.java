package com.example.project158.Domains;

import com.google.gson.annotations.SerializedName;

public class Hour {
    @SerializedName("time")
    String time;
    @SerializedName("temp_c")
    String temp_c;
    @SerializedName("condition")
    Condition condition;

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

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public static class Condition
    {
        @SerializedName("icon")
        String iconPath;

        public String getIconPath() {
            return iconPath;
        }

        public void setIconPath(String iconPath) {
            this.iconPath = iconPath;
        }
    }
}
