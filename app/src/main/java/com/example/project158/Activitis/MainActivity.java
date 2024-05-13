package com.example.project158.Activitis;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.project158.API.APIService;
import com.example.project158.Adapters.HourlyAdapters;
import com.example.project158.Domains.Current;
import com.example.project158.Domains.Hourly;
import com.example.project158.Domains.ResponseWrapper;
import com.example.project158.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterHourly;
    private RecyclerView recyclerView;
private TextView next7dayBtn, tvDayTimeCur, tvTempCur, tvConditionCur, tvLocationCur,tvUVCur,tvWindSpeedCur,tvHumidCur;
ImageView imgVWeather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        initRecyclerview();
        Anhxa();
        setVariable();
        APIService.servieapi.getWeatherDay("4c57a8be9b2b4def8d833930240905","Singapore").enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                if (response.isSuccessful()) {
                    ResponseWrapper responseWrapper = response.body();
                    Current location = responseWrapper.getLocation();
                    Current current = responseWrapper.getCurrent();
                    if (location!=null && current != null ) {
                        // Hiển thị dữ liệu lên các view
                        tvLocationCur.setText(location.getNation()+", "+location.getCity());
                        tvConditionCur.setText(current.getCondition().getStatus());
                        tvDayTimeCur.setText(location.getTime());
                        tvTempCur.setText(String.valueOf(current.getTemp()) +"℃");

                        Glide.with(getApplicationContext())
                                        .load("https:"+current.getCondition().getIconPath())
                                                .into(imgVWeather);
                        tvUVCur.setText(String.valueOf(current.getUV()));
                        tvWindSpeedCur.setText(String.valueOf(current.getWind_speed())+" kph");
                        tvHumidCur.setText(String.valueOf(current.getHumidity()));
                    }
                }
                else
                {
                    String errorMessage = "Request failed with code: " + response.message() + response.code() + response.body();
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Request failed: " + t.getMessage() + t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });

        setLocation("Nation", "City"); // Set location information
    }

    private void setVariable() {
        next7dayBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FutureActivity.class)));
    }
    //Ánh xạ các view trong file layout:
private void Anhxa ()
{
    tvLocationCur = findViewById(R.id.textViewLocation);
    tvConditionCur = findViewById(R.id.textViewWeather);
    tvDayTimeCur = findViewById(R.id.textViewDayTime);
    tvTempCur = findViewById(R.id.textViewTemp);
    imgVWeather = findViewById(R.id.imageViewWeather);

    tvUVCur = findViewById(R.id.textViewRate1);
    tvWindSpeedCur = findViewById(R.id.textViewRate2);
    tvHumidCur = findViewById(R.id.textViewRate3);

    next7dayBtn=findViewById(R.id.nextBtn);
}
    private void initRecyclerview() {
        ArrayList<Hourly> items = new ArrayList<>();

        items.add(new Hourly("9 pm", 28, "cloudy"));
        items.add(new Hourly("10 pm", 29, "sunny"));
        items.add(new Hourly("11 pm", 30, "wind"));
        items.add(new Hourly("12 pm", 31, "rainy"));
        items.add(new Hourly("1 am", 32, "storm"));


        recyclerView = findViewById(R.id.view1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapterHourly = new HourlyAdapters(items);
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

    private void setLocation(String nation, String city) {
        TextView locationTextView = findViewById(R.id.textViewLocation);
        String location = nation + ", " + city;
        locationTextView.setText(location);
    }
    private static final int REQUEST_CODE_SELECT_CITY = 1;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_CITY && resultCode == RESULT_OK && data != null) {
            String selectedCity = data.getStringExtra("selectedCity");
            setLocation("Nation", selectedCity);
        }
    }
}
