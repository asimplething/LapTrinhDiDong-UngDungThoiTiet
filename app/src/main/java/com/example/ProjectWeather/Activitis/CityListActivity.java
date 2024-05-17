package com.example.ProjectWeather.Activitis;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ProjectWeather.Adapters.CityAdapter;
import com.example.ProjectWeather.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import java.util.ArrayList;
import java.util.List;

public class CityListActivity extends AppCompatActivity implements CityAdapter.OnCityClickListener {

    private RecyclerView cityRecyclerView;
    private CityAdapter cityAdapter;
    private EditText cityEditText;
    private ProgressBar progressBar;
    private List<String> cityList;
    private List<String> filteredCityList;
    private Button gpsButton;


    Boolean PERMISSION_FINE_LOCATION=false; //biến kiểm tra quyền cho phép truy cập địa chỉ máy

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        anhXa();
        initializeCitylist();
        TextAndButtonAction();
    }

    private void anhXa()
    {
        // Initialize views
        cityRecyclerView = findViewById(R.id.cityView);
        cityEditText = findViewById(R.id.cityEdt);
        progressBar = findViewById(R.id.progressBar2);
        gpsButton = findViewById(R.id.gpsButton);
    }
    private void TextAndButtonAction()
    {
        // Add a text changed listener to filter cities
        cityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCityList(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        //gọi button gps listener
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                progressBar.setVisibility(View.VISIBLE);
                takeGPSLocation();
            }
        });
    }
    private void takeGPSLocation()
    {
        FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        checkPermission(); // kiểm tra quyền lấy địa chỉ
        if(PERMISSION_FINE_LOCATION)
        {
            // lấy địa chỉ mới nhất
            locationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                @NonNull
                @Override
                public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                    return null;
                }
                @Override
                public boolean isCancellationRequested() {
                    return false;
                }
            }).addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null)
                    {
                        //decode tọa độ để hiển thị tên địa chỉ
                        Geocoder geocoder = new Geocoder(CityListActivity.this);
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                            StringBuilder tmpString = new StringBuilder();

                            if(addresses.get(0).getLocality()!=null) tmpString.append(addresses.get(0).getLocality()).append(", ");
                            tmpString.append(addresses.get(0).getAdminArea()).append(", ");
                            tmpString.append(addresses.get(0).getCountryName());
                            String thisLocation =  tmpString.toString();

                            Toast.makeText(CityListActivity.this,"Near: " + thisLocation, Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e)
                        {
                            Log.d("GPS request: ", "Fail to take address Location - "+ e.getMessage());
                            return;
                        }
                        progressBar.setVisibility(View.GONE);
                        //trả tọa độ địa chỉ (không phải tên địa chỉ) đến MainActivity
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("selectedCity", location.getLatitude() + "," + location.getLongitude());
                        setResult(RESULT_OK, resultIntent);
                        finish();
                        Log.d("GPS request: ", "Success in requesting Location");
                    }
                }
            });
        }

    }
    private void checkPermission()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            PERMISSION_FINE_LOCATION = true;
        }
        else
        {
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==2)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                PERMISSION_FINE_LOCATION = true;
            }
            else
            {
                PERMISSION_FINE_LOCATION = false;
                Log.d("GPS request: ", "Fail to request Location");
            }
        }
    }
    private void initializeCitylist()
    {
        // Initialize city list and adapter
        cityList = new ArrayList<>();
        filteredCityList = new ArrayList<>();
        cityAdapter = new CityAdapter(filteredCityList, this);
        // Set up RecyclerView
        cityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cityRecyclerView.setAdapter(cityAdapter);

        // Simulated data for city list
        cityList.add("New York");
        cityList.add("Los Angeles");
        cityList.add("Chicago");
        cityList.add("Houston");
        cityList.add("Phoenix");
        cityList.add("Philadelphia");
        cityList.add("San Antonio");
        cityList.add("San Diego");
        cityList.add("Dallas");
        cityList.add("San Jose");
        cityList.add("HaNoi");
    }
    private void filterCityList(String query) {
        filteredCityList.clear();
        for (String city : cityList) {
            if (city.toLowerCase().contains(query.toLowerCase())) {
                filteredCityList.add(city);
            }
        }
        filteredCityList.add(query);
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCityClick(String city) {
        // Send the selected city back to MainActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("selectedCity", city);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}

