package com.example.ProjectWeather.API;

import com.example.ProjectWeather.Domains.DayForecast;
import com.example.ProjectWeather.Domains.HourForecast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APILocalService {
    public static final String BASE_URL = "http://192.168.1.3:8188/api/weather/";
    Gson gson = new GsonBuilder(). setDateFormat("yyyy MM dd HH:mm : ss").create();
    APILocalService serviceapi = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APILocalService.class);
    @GET("daily")
    Call<ArrayList<DayForecast>> getWeatherDaily(@Query("city") String CityName);
    @GET("hourly")
    Call<ArrayList<HourForecast>> getWeatherHourly(@Query("city") String CityName);
}
