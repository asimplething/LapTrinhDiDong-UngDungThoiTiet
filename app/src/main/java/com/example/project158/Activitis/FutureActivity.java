package com.example.project158.Activitis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.project158.API.APILocalService;
import com.example.project158.Adapters.FutureAdapter;
import com.example.project158.Domains.DayForecast;
import com.example.project158.Domains.FutureDomain;
import com.example.project158.Domains.HourForecast;
import com.example.project158.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FutureActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterTommorow;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future);
        setVariable();
        callWeatherAPI();
    }

    private void callWeatherAPI()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);
        String userLocation = sharedPreferences.getString("location","ThuDuc");
        APILocalService.serviceapi.getWeatherDaily(userLocation).enqueue(new Callback<ArrayList<DayForecast>>() {
            @Override
            public void onResponse(Call<ArrayList<DayForecast>> call, Response<ArrayList<DayForecast>> response) {
                if(response.isSuccessful())
                {
                    ArrayList <DayForecast> days = response.body();
                    if (days!=null)
                    {
                        initRecyclerView(days);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<DayForecast>> call, Throwable t) {

            }
        });
    }
    private void initRecyclerView(ArrayList<DayForecast> days) {

        recyclerView = findViewById(R.id.view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapterTommorow = new FutureAdapter(days);
        recyclerView.setAdapter(adapterTommorow);
    }

    private void setVariable() {
        ConstraintLayout backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> startActivity(new Intent(FutureActivity.this, MainActivity.class)));
    }
}