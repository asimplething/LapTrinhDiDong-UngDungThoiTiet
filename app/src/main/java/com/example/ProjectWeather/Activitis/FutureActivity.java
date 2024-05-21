package com.example.ProjectWeather.Activitis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ProjectWeather.API.APILocalService;
import com.example.ProjectWeather.Adapters.FutureAdapter;
import com.example.ProjectWeather.Domains.DayForecast;
import com.example.ProjectWeather.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FutureActivity extends AppCompatActivity {


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
            public void onResponse(@NonNull Call<ArrayList<DayForecast>> call, @NonNull Response<ArrayList<DayForecast>> response) {
                if(response.isSuccessful())
                {
                    ArrayList <DayForecast> days = response.body();
                    if (days!=null)
                    {
                        initRecyclerView(days);
                        TextView textView10 = findViewById(R.id.textView10);
                        TextView textView = findViewById(R.id.textView);
                        TextView textViewWindS = findViewById(R.id.textView3);
                        TextView textViewHumid = findViewById(R.id.textView5);
                        String formattedWindSpeed = days.get(0).getWind_s().substring(0,2); //String.format("%.2f", days.get(0).getWind_s());
                        textViewWindS.setText(formattedWindSpeed + "\tkph");
                        textViewHumid .setText(days.get(0).getHumidity()+"\t%");
                        double avrTemp = (Double.parseDouble(days.get(0).getMaxTemperature()) + Double.parseDouble(days.get(0).getMinTemperature()))/2;
                        textView10.setText(String.valueOf(avrTemp).substring(0,5));

                        String chanceOfRain = days.get(0).getChanceOfRain()+"%";
                        textView.setText(chanceOfRain);


                    }
                }
                else
                {
                    String errorMessage = "Request failed with code: " + response.message() + response.code() + response.body()+"from else";
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    Log.d("Call API (Weather in Day): ", errorMessage);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<DayForecast>> call, Throwable t) {
                String failureMessage = "Request failed with code: " + t.getMessage() + t.getCause();
                Toast.makeText(getApplicationContext(), "Request failed: " + failureMessage+" from onFailure", Toast.LENGTH_SHORT).show();
                Log.d("Call API (Weather in 7 days): ", failureMessage);
            }
        });
    }
    // tạo adapter và truyền dự liệu vào recyclerview
    private void initRecyclerView(ArrayList<DayForecast> days) {

        RecyclerView recyclerView = findViewById(R.id.view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        RecyclerView.Adapter adapterTommorow = new FutureAdapter(days);
        recyclerView.setAdapter(adapterTommorow);


    }

    private void setVariable() {
        ConstraintLayout backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());

    }
}