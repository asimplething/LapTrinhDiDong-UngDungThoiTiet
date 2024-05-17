package com.example.project158.Activitis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project158.API.APILocalService;
import com.example.project158.Adapters.FutureAdapter;
import com.example.project158.Domains.DayForecast;
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
        SharedPreferences sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);
        String userLocation = sharedPreferences.getString("location","ThuDuc");
        setVariable();
        callWeatherAPI(userLocation);
    }
    // call local API cho dự báo 7 ngày tiếp theo
    private void callWeatherAPI(String userLocation)
    {

        APILocalService.serviceapi.getWeatherDaily(userLocation).enqueue(new Callback<ArrayList<DayForecast>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<DayForecast>> call, Response<ArrayList<DayForecast>> response) {
                if(response.isSuccessful())
                {
                    ArrayList <DayForecast> days = response.body();
                    if (days!=null)
                    {
                        initRecyclerView(days);
                        TextView textView10 = findViewById(R.id.textView10);
                        TextView textView = findViewById(R.id.textView);

                        double avrTemp = (Double.parseDouble(days.get(0).getMaxTemperature()) + Double.parseDouble(days.get(0).getMinTemperature()))/2;
                        textView10.setText(String.valueOf(avrTemp).substring(0,5));

                        String chanceOfRain = days.get(0).getChanceOfRain()+"%";
                        textView.setText(chanceOfRain);

                    }
                }
                else
                {
                    String errorMessage = "Request failed with code: " + response.message() + response.code() + response.body();
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    Log.d("Call API (Weather in Day): ", errorMessage);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<DayForecast>> call, Throwable t) {
                String failureMessage = "Request failed with code: " + t.getMessage() + t.getCause();
                Toast.makeText(getApplicationContext(), "Request failed: " + failureMessage, Toast.LENGTH_SHORT).show();
                Log.d("Call API (Weather in 7 days): ", failureMessage);
            }
        });
    }
    // tạo adapter và truyền dự liệu vào recyclerview
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