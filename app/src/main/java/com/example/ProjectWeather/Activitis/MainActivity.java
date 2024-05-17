package com.example.ProjectWeather.Activitis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ProjectWeather.API.APILocalService;
import com.example.ProjectWeather.API.APIService;
import com.example.ProjectWeather.Adapters.HourlyAdapters;
import com.example.ProjectWeather.Domains.Current;
import com.example.ProjectWeather.Domains.HourForecast;
import com.example.ProjectWeather.Domains.ResponseWrapper;
import com.example.ProjectWeather.R;

import java.time.LocalDateTime;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView next7dayBtn, tvDayTimeCur, tvTempCur, tvConditionCur, tvLocationCur, tvUVCur, tvWindSpeedCur, tvHumidCur;

    private View loadingLayout;
    private ProgressBar progressBar;
    private ImageView imgVWeather;

    private TextView minMaxText;
    private SharedPreferences oldUserLocation;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Weather Project");
        // Lấy dữ liệu local của user
        oldUserLocation = getSharedPreferences("UserData", MODE_PRIVATE);
        String userLocation = oldUserLocation.getString("location", "VietNam");
        //call api cho địa điểm cố định ban đầu
        callWeatherAPI(userLocation);
        setVariable();
        setResult(5);
    }
    // gọi API lấy dữ liệu dự đoán nhiệt độ theo giờ trong ngày
    private void callWeatherAPI(String currentCity)
    {
        // show loading screen chờ call xong api
        loadingLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        APIService.serviceapi.getWeatherDay("4c57a8be9b2b4def8d833930240905", currentCity).enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(@NonNull Call<ResponseWrapper> call, @NonNull Response<ResponseWrapper> response) {
                if (response.isSuccessful()) {
                    ResponseWrapper responseWrapper = response.body();
                    if(responseWrapper!=null)
                    {
                        Current location = responseWrapper.getLocation();
                        Current current = responseWrapper.getCurrent();
                        setLocationAndWeather(current, location);
                    }
                }
                else {
                    String errorMessage = "Request failed with code: " + response.message() + response.code() + response.body();
                    Toast.makeText(getApplicationContext(), "Location is not found!", Toast.LENGTH_SHORT).show();
                    Log.d("Call API (Weather in Day): ", errorMessage);
                }
                loadingLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseWrapper> call, @NonNull Throwable t) {
                String failureMessage = "Request failed with code: " + t.getMessage() + t.getCause();
                Toast.makeText(getApplicationContext(), "Location is not found!", Toast.LENGTH_SHORT).show();
                Log.d("Call API (Weather in Day): ", failureMessage);
                loadingLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        });
        APILocalService.serviceapi.getWeatherHourly(currentCity).enqueue(new Callback<ArrayList<HourForecast>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<HourForecast>> call, @NonNull Response<ArrayList<HourForecast>> response) {
                if(response.isSuccessful())
                {
                    ArrayList<HourForecast> hours = response.body();
                    if(hours!=null)
                    {
                        initRecyclerview(hours);
                    }
                }
                else
                {
                    String errorMessage = "Request failed with code: " + response.message() + response.code() + response.body();
                    //Toast.makeText(getApplicationContext(), "Location is not found!", Toast.LENGTH_SHORT).show();
                    Log.d("Call API (Hourly): ", errorMessage);
                }

            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<HourForecast>> call, @NonNull Throwable t) {
                String failureMessage = "Request failed with code: " + t.getMessage() + t.getCause();
                //Toast.makeText(getApplicationContext(), "Request failed: " + failureMessage, Toast.LENGTH_SHORT).show();
                Log.d("Call API (Hourly): ", failureMessage);
            }
        });

    }
    // Chỉnh lại dữ liệu trên app
    private void setLocationAndWeather(Current current, Current location) {

        oldUserLocation.edit().putString("location",location.getCity()).apply(); //chỉnh lại dữ liệu local trên máy

        tvLocationCur.setText(location.getNation() + ", " + location.getCity());
        tvConditionCur.setText(current.getCondition().getStatus());
        tvDayTimeCur.setText(location.getTime());
        tvTempCur.setText(current.getTemp() + "℃");

        Glide.with(getApplicationContext())
                .load("https:" + current.getCondition().getIconPath())
                .into(imgVWeather);
        tvUVCur.setText(String.valueOf(current.getUV()));
        tvWindSpeedCur.setText(String.valueOf(current.getWind_speed()) + " kph");
        tvHumidCur.setText(String.valueOf(current.getHumidity()));

        // Tính toán nhiệt độ trung bình và nhiệt độ cao/thấp nhất trong ngày
        double currentTemp = Double.parseDouble(current.getTemp());
        double Temp_avg;
        int currentHour = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentHour = LocalDateTime.now().getHour();
        }
        if (currentHour >= 0 && currentHour <= 5)
            Temp_avg = currentTemp - Math.sin(((2 * Math.PI) / 24) * (currentHour - 14)) * 4;
        else
            Temp_avg = currentTemp - Math.sin(((2 * Math.PI) * (currentHour - 14) / 24) - (49 * Math.PI) / 32) * 4;

        double currentMaxTemperature = Temp_avg + 4 * Math.sin(((2 * Math.PI) * 0 / 24) - (49 * Math.PI) / 32);
        double currentMinTemperature = Temp_avg + 4 * Math.sin(((2 * Math.PI) * (4 - 14) / 24));
        String minMax = "H:" + String.valueOf(currentMaxTemperature).substring(0,2) + "\t\t\t" + "L:" + String.valueOf(currentMinTemperature).substring(0,2) ;
        minMaxText.setText(minMax);
    }

    private void setVariable() {
        next7dayBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FutureActivity.class)));
    }

    //Ánh xạ các view trong file layout:
    private void Anhxa() {
        //toolbar
        toolbar = findViewById(R.id.toolbar);
        // loading view
        loadingLayout = findViewById(R.id.loadingLayout);
        progressBar = findViewById(R.id.progressBar);
        //
        tvLocationCur = findViewById(R.id.textViewLocation);
        tvConditionCur = findViewById(R.id.textViewWeather);
        tvDayTimeCur = findViewById(R.id.textViewDayTime);
        tvTempCur = findViewById(R.id.textViewTemp);
        imgVWeather = findViewById(R.id.imageViewWeather);
        //
        tvUVCur = findViewById(R.id.textViewRate1);
        tvWindSpeedCur = findViewById(R.id.textViewRate2);
        tvHumidCur = findViewById(R.id.textViewRate3);
        minMaxText = findViewById(R.id.textViewMinMax);
        next7dayBtn = findViewById(R.id.nextBtn);

    }

    private void initRecyclerview(ArrayList<HourForecast> hours) {
        RecyclerView recyclerView = findViewById(R.id.view1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        RecyclerView.Adapter adapterHourly = new HourlyAdapters(hours);
        recyclerView.setAdapter(adapterHourly);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_change_location) {
            // Start CityListActivity for result
            Intent intent = new Intent(MainActivity.this, CityListActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SELECT_CITY);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static final int REQUEST_CODE_SELECT_CITY = 1;
    // call lại api sau khi chọn thành phố cụ thể
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_CITY && resultCode == RESULT_OK && data != null) {
            String selectedCity = data.getStringExtra("selectedCity");
            callWeatherAPI(selectedCity);
        }
    }
}
