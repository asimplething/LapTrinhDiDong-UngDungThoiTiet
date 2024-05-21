package com.example.ProjectWeather.Activitis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.ProjectWeather.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class Summary_Activity extends AppCompatActivity {
    private LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        lineChart = findViewById(R.id.lineChart);

        Intent intent = getIntent();
        if (intent != null) {
            String maxTemperature = intent.getStringExtra("maxTemperature");
            if (maxTemperature != null) {
                double avgTemp = Double.parseDouble(maxTemperature)-4 * Math.sin(0 - 7 * Math.PI / 6);
                ArrayList<Entry> entries = new ArrayList<>();
                for (int i = 0; i < 24; i++) {
                    double temp = avgTemp + 4 * Math.sin(((2 * Math.PI) / 24) * (i - 14) - 7 * Math.PI / 6);
                    entries.add(new Entry(i, (float) temp));
                }

                LineDataSet dataSet = new LineDataSet(entries, "Temperature");
                LineData lineData = new LineData(dataSet);
                lineChart.setData(lineData);
                lineChart.invalidate(); // Refresh chart
            }
        }
    }

}