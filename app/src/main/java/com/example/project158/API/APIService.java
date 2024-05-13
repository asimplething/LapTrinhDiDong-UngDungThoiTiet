package com.example.project158.API;

import com.example.project158.Domains.Hourly;
import com.example.project158.Domains.Current;
import com.example.project158.Domains.ResponseWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    public static final String BASE_URL = "https://api.weatherapi.com/v1/";
    Gson gson = new GsonBuilder(). setDateFormat("yyyy MM dd HH:mm: ss").create();
    APIService servieapi = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIService.class);
@GET("current.json")
    Call<ResponseWrapper> getWeatherDay(@Query("key") String APIKey, @Query("q") String CityName) ;
}
