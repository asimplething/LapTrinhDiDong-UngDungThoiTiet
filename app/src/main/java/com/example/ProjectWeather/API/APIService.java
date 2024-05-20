package com.example.ProjectWeather.API;

import com.example.ProjectWeather.Domains.Location;
import com.example.ProjectWeather.Domains.ResponseWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    public static final String BASE_URL = "https://api.weatherapi.com/v1/";
    Gson gson = new GsonBuilder(). setDateFormat("yyyy MM dd HH:mm: ss").create();
    APIService serviceapi = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIService.class);
    @GET("current.json")
    Call<ResponseWrapper> getWeatherDay(@Query("key") String APIKey, @Query("q") String CityName);
    @GET("search.json")
    Call<ArrayList<Location>> getLocations(@Query("key") String APIKey, @Query("q") String CityName);

}
