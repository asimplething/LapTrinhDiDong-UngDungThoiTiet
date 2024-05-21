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
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ProjectWeather.API.APIService;
import com.example.ProjectWeather.Adapters.CityAdapter;
import com.example.ProjectWeather.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityListActivity extends AppCompatActivity implements CityAdapter.OnCityClickListener {

    private RecyclerView cityRecyclerView;
    private CityAdapter cityAdapter;
    private EditText cityEditText;
    private ProgressBar progressBar;
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
        // call api để tìm list danh sách thành phố dựa trên input
        cityEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent)
            {
                String input = cityEditText.getText().toString();
                if (!input.isEmpty() && i == EditorInfo.IME_ACTION_SEARCH) //search bằng nút tìm kiếm (không search khi user để trống input)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    callCityListAPI(input);
                }
                return false;
            }
        });
        //gọi button gps listener
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Animation shake = AnimationUtils.loadAnimation(CityListActivity.this, R.anim.shake);
                view.startAnimation(shake);
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

        filteredCityList = new ArrayList<>();
        cityAdapter = new CityAdapter(filteredCityList, this);
        // Set up RecyclerView
        cityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cityRecyclerView.setAdapter(cityAdapter);
    }
    private void filterCityList(String query) {
        filteredCityList.clear();
        ArrayList<String> cityListTmp = new ArrayList<>();
        List<String> cityList = new ArrayList<>();
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
        cityList.add("Ha Noi");
        cityList.add("Da Nang");
        cityList.add("Ho Chi Minh");
        //lọc thành phố giống theo input đã nhập
        for (String city : cityList)
            if (city.toLowerCase().contains(query.toLowerCase()))
                filteredCityList.add(city);

        filteredCityList.add(query);
        cityAdapter.notifyDataSetChanged();
    }
    private void callCityListAPI(String query)
    {

        APIService.serviceapi.getLocations("4c57a8be9b2b4def8d833930240905", "%"+query+"%").enqueue(new Callback<ArrayList<com.example.ProjectWeather.Domains.Location>>() {
            @Override
            public void onResponse(Call<ArrayList<com.example.ProjectWeather.Domains.Location>> call, Response<ArrayList<com.example.ProjectWeather.Domains.Location>> response) {
                if(response.isSuccessful())
                {
                    filteredCityList.clear();
                    ArrayList<com.example.ProjectWeather.Domains.Location> cityListAPI = response.body();
                    if(cityListAPI != null && !cityListAPI.isEmpty())
                    {
                        for (com.example.ProjectWeather.Domains.Location location : cityListAPI) {
                            String thisLocation = location.getCity(); //nếu không tìm thấy city thì lấy country
                            if (!thisLocation.equals(""))
                                filteredCityList.add(thisLocation +", "+location.getNation());
                            else
                                filteredCityList.add(location.getNation());
                        }
                        cityAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Toast.makeText(CityListActivity.this,"The location doesn't seem to be found", Toast.LENGTH_LONG).show();
                        Log.d("Call API (Locations):", "API didn't return locations!");
                    }

                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<ArrayList<com.example.ProjectWeather.Domains.Location>> call, Throwable t)
            {
                //nếu không call được API thì dùng mảng thành phố có sẵn
                filterCityList(query);
                //log lỗi
                String failureMessage = "Request failed with code: " + t.getMessage() + t.getCause();
                Toast.makeText(getApplicationContext(), "Request failed: " + failureMessage, Toast.LENGTH_SHORT).show();
                Log.d("Call API (Locations):", failureMessage);
                progressBar.setVisibility(View.GONE);
            }
        });
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