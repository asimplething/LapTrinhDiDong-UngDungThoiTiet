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
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.example.project158.Adapters.HourlyAdapters;
import com.example.project158.Domains.Hourly;
import com.example.project158.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterHourly;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initRecyclerview();
        setVariable();
        setLocation("Nation", "City"); // Set location information
    }

    private void setVariable() {
        TextView next7dayBtn=findViewById(R.id.nextBtn);
        next7dayBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FutureActivity.class)));
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
