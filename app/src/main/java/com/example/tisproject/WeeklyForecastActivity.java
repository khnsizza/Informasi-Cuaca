package com.example.tisproject;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeeklyForecastActivity extends AppCompatActivity {

    private TextView tvLocation, tvTitle;
    private ImageView ivBack;
    private RecyclerView rvWeeklyForecast;
    private SwipeRefreshLayout swipeRefresh;

    private WeeklyForecastAdapter weeklyAdapter;
    private List<DailyWeatherData> weeklyData;

    private String currentCity = "Jakarta";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_forecast);

        tvLocation = findViewById(R.id.tv_location);
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        rvWeeklyForecast = findViewById(R.id.rv_weekly_forecast);
        swipeRefresh = findViewById(R.id.swipe_refresh);

        tvLocation.setText(currentCity);
        tvTitle.setText("7-Day Forecast");

        weeklyData = new ArrayList<>();
        weeklyAdapter = new WeeklyForecastAdapter(weeklyData);

        rvWeeklyForecast.setLayoutManager(new LinearLayoutManager(this));
        rvWeeklyForecast.setAdapter(weeklyAdapter);

        ivBack.setOnClickListener(v -> finish());

        swipeRefresh.setOnRefreshListener(this::loadWeeklyForecast);

        loadWeeklyForecast();
    }

    private void loadWeeklyForecast() {
        swipeRefresh.setRefreshing(true);

        weeklyData.clear();

        long now = System.currentTimeMillis();

        String[] conditions = {"Light rain", "Sunny", "Cloudy", "Partly cloudy", "Thunderstorm", "Clear", "Rainy", "Sunny"};
        int[] highTemps = {19, 20, 18, 21, 17, 22, 18, 20};
        int[] lowTemps = {12, 13, 11, 12, 10, 14, 12, 13};
        int[] rainChances = {40, 5, 20, 10, 80, 0, 60, 5};
        String[] weatherIcons = {"hujan", "cerah", "awan", "awan_terang", "petir", "cerah", "hujan", "cerah"};

        for (int i = 0; i < 8; i++) {
            long timestamp = (now / 1000) + i * 86400; // + i hari dalam detik
            String day = formatDate(timestamp);

            DailyWeatherData data = new DailyWeatherData(
                    day,
                    conditions[i],
                    highTemps[i],
                    lowTemps[i],
                    rainChances[i],
                    weatherIcons[i]
            );
            weeklyData.add(data);
        }

        weeklyAdapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }

    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd", Locale.ENGLISH);
        return sdf.format(new Date(timestamp * 1000));
    }
}
