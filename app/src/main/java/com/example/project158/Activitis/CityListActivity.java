package com.example.project158.Activitis;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project158.Adapters.CityAdapter;
import com.example.project158.R;

import java.util.ArrayList;
import java.util.List;

public class CityListActivity extends AppCompatActivity implements CityAdapter.OnCityClickListener {

    private RecyclerView cityRecyclerView;
    private CityAdapter cityAdapter;
    private EditText cityEditText;
    private ProgressBar progressBar;
    private List<String> cityList;
    private List<String> filteredCityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        // Initialize views
        cityRecyclerView = findViewById(R.id.cityView);
        cityEditText = findViewById(R.id.cityEdt);
        progressBar = findViewById(R.id.progressBar2);

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

