package com.example.ProjectWeather.Activitis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ProjectWeather.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Locale;

public class Summary_Activity extends AppCompatActivity {
    private LineChart lineChart;
    private ImageView backButton, imageViewWeatherChart;
    private TextView minTempText, maxTempText, chanceOfRainText, chartText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        anhXa();
        goBack();
        drawChartAndDetails();
    }
    private void anhXa()
    {
        lineChart = findViewById(R.id.lineChart);
        backButton = findViewById(R.id.backBtnChart);
        minTempText =findViewById(R.id.textViewMinTemp);
        maxTempText = findViewById(R.id.textViewMaxTemp);
        chanceOfRainText = findViewById(R.id.textViewChanceOfRain);
        chartText = findViewById(R.id.textViewChart);
        imageViewWeatherChart = findViewById(R.id.imageViewWeatherChart);
    }
    private void goBack()
    {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void drawChartAndDetails()
    {
        Intent intent = getIntent();
        String maxTemperature = intent.getStringExtra("maxTemperature");
        String minTemerature = intent.getStringExtra("minTemperature");
        String chanceOfRain = intent.getStringExtra("chanceOfRain");
        String thisDay = intent.getStringExtra("day");
        if (intent != null) {
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
        chanceOfRainText.setText("\tChance of rain: " + chanceOfRain + " %");
        try
        {
            minTempText.setText("\tMin Temperature: " + minTemerature.substring(0,5) + " ℃");
            maxTempText.setText("\tMax Temperature: " + maxTemperature.substring(0,5) + " ℃");
        }
        catch(Exception e)
        {
            minTempText.setText("\tMin Temperature: " + minTemerature.substring(0,2) + " ℃");
            maxTempText.setText("\tMax Temperature: " + maxTemperature.substring(0,2) + " ℃");
        }
        chartText.setText( thisDay + "'S CHART");
        if(Double.parseDouble(chanceOfRain)<45)
            imageViewWeatherChart.setImageResource(R.drawable.sunny);
        else if(Double.parseDouble(chanceOfRain)>75)
            imageViewWeatherChart.setImageResource(R.drawable.rainy);
        else
            imageViewWeatherChart.setImageResource(R.drawable.cloudy);
    }

}