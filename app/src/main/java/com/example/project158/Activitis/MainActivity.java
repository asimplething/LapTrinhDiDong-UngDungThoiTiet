package com.example.project158.Activitis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project158.API.APIService;
import com.example.project158.Adapters.HourlyAdapters;
import com.example.project158.Domains.Current;
import com.example.project158.Domains.Hour;
import com.example.project158.Domains.ResponseWrapper;
import com.example.project158.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterHourly;
    private RecyclerView recyclerView;
    private TextView next7dayBtn, tvDayTimeCur, tvTempCur, tvConditionCur, tvLocationCur, tvUVCur, tvWindSpeedCur, tvHumidCur;

    ImageView imgVWeather;
    SharedPreferences oldUserLocation;
    String userLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        // Lấy dữ liệu local của user
        oldUserLocation = getSharedPreferences("UserData", MODE_PRIVATE);
        userLocation =  oldUserLocation.getString("location","VietNam");
        //call api cho địa điểm cố định ban đầu
        //callWeatherAPI(userLocation);
        callForecastAPI(userLocation);
        Anhxa();
        setVariable();

    }
    //gọi API
    private void callWeatherAPI(String currentCity) {
        APIService.servieapi.getWeatherDay("4c57a8be9b2b4def8d833930240905", currentCity).enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                if (response.isSuccessful()) {
                    ResponseWrapper responseWrapper = response.body();
                    Current location = responseWrapper.getLocation();
                    Current current = responseWrapper.getCurrent();
                    if (location != null && current != null) {
                        // Hiển thị dữ liệu lên các view
                        setLocationAndWeather(current, location);
                    }
                } else {
                    String errorMessage = "Request failed with code: " + response.message() + response.code() + response.body();
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    Log.d("Call API (Weather): ", errorMessage);
                }

            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                String failureMessage = "Request failed with code: " + t.getMessage() + t.getCause();
                Toast.makeText(getApplicationContext(), "Request failed: " + failureMessage, Toast.LENGTH_SHORT).show();
                Log.d("Call API (Weather): ", failureMessage);
            }
        });
    }
    // gọi API lấy dữ liệu dự đoán nhiệt độ theo giờ trong ngày
    private void callForecastAPI(String currentCity)
    {
        APIService.servieapi.getForeCastDay("4c57a8be9b2b4def8d833930240905", currentCity).enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                if (response.isSuccessful()) {
                    ResponseWrapper responseWrapper = response.body();
                    if(responseWrapper!=null)
                    {
                        Current location = responseWrapper.getLocation();
                        Current current = responseWrapper.getCurrent();
                        ArrayList<Hour> hours = responseWrapper.getForecast().getForecastDay().get(0).getHours();
                        setLocationAndWeather(current, location);
                        initRecyclerview(hours);
                    }

                } else {
                    String errorMessage = "Request failed with code: " + response.message() + response.code() + response.body();
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    Log.d("Call API (Forecast): ", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                String failureMessage = "Request failed with code: " + t.getMessage() + t.getCause();
                Toast.makeText(getApplicationContext(), "Request failed: " + failureMessage, Toast.LENGTH_SHORT).show();
                Log.d("Call API (Forecast): ", failureMessage);
            }
        });

    }
    // Chỉnh lại dữ liệu trên app
    private void setLocationAndWeather(Current current, Current location) {

        oldUserLocation.edit().putString("location",location.getCity()).apply();
        tvLocationCur.setText(location.getNation() + ", " + location.getCity());
        tvConditionCur.setText(current.getCondition().getStatus());
        tvDayTimeCur.setText(location.getTime());
        tvTempCur.setText(String.valueOf(current.getTemp()) + "℃");

        Glide.with(getApplicationContext())
                .load("https:" + current.getCondition().getIconPath())
                .into(imgVWeather);
        tvUVCur.setText(String.valueOf(current.getUV()));
        tvWindSpeedCur.setText(String.valueOf(current.getWind_speed()) + " kph");
        tvHumidCur.setText(String.valueOf(current.getHumidity()));
    }

    private void setVariable() {
        next7dayBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FutureActivity.class)));
    }

    //Ánh xạ các view trong file layout:
    private void Anhxa() {
        tvLocationCur = findViewById(R.id.textViewLocation);
        tvConditionCur = findViewById(R.id.textViewWeather);
        tvDayTimeCur = findViewById(R.id.textViewDayTime);
        tvTempCur = findViewById(R.id.textViewTemp);
        imgVWeather = findViewById(R.id.imageViewWeather);

        tvUVCur = findViewById(R.id.textViewRate1);
        tvWindSpeedCur = findViewById(R.id.textViewRate2);
        tvHumidCur = findViewById(R.id.textViewRate3);

        next7dayBtn = findViewById(R.id.nextBtn);
    }

    private void initRecyclerview(ArrayList<Hour> hours) {
        recyclerView = findViewById(R.id.view1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterHourly = new HourlyAdapters(hours);
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
            callForecastAPI(selectedCity);
        }
    }
}
