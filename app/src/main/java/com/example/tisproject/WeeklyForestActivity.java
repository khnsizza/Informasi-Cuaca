package com.example.tisproject;

package com.example.tisproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class WeeklyForecastActivity extends AppCompatActivity {

    private static final String TAG = "WeeklyForecastActivity";

    private TextView tvLocation, tvTitle;
    private ImageView ivBack;
    private RecyclerView rvWeeklyForecast;
    private SwipeRefreshLayout swipeRefresh;

    private WeatherApiService weatherApiService;
    private WeeklyForecastAdapter weeklyAdapter;
    private List<DailyWeatherData> weeklyData;

    private double currentLat;
    private double currentLon;
    private String currentCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_forecast);

        // Get data from intent
        currentLat = getIntent().getDoubleExtra("lat", -6.2088);
        currentLon = getIntent().getDoubleExtra("lon", 106.8456);
        currentCity = getIntent().getStringExtra("city");
        if (currentCity == null) currentCity = "Jakarta";

        initViews();
        initData();
        setupRecyclerView();
        setupClickListeners();
        loadWeeklyForecast();
    }

    private void initViews() {
        tvLocation = findViewById(R.id.tv_location);
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        rvWeeklyForecast = findViewById(R.id.rv_weekly_forecast);
        swipeRefresh = findViewById(R.id.swipe_refresh);

        tvLocation.setText(currentCity);
        tvTitle.setText("7-Day Forecast");
    }

    private void initData() {
        weatherApiService = new WeatherApiService();
        weeklyData = new ArrayList<>();
    }

    private void setupRecyclerView() {
        weeklyAdapter = new WeeklyForecastAdapter(weeklyData);
        rvWeeklyForecast.setLayoutManager(new LinearLayoutManager(this));
        rvWeeklyForecast.setAdapter(weeklyAdapter);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(v -> finish());

        swipeRefresh.setOnRefreshListener(this::loadWeeklyForecast);
    }

    private void loadWeeklyForecast() {
        swipeRefresh.setRefreshing(true);

        // Since OpenWeatherMap's 5-day forecast only gives us 5 days,
        // we'll simulate 7 days with some sample data
        weeklyData.clear();

        // Generate sample weekly data (in a real app, you'd call a 7-day forecast API)
        generateSampleWeeklyData();

        weeklyAdapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
    }

    private void generateSampleWeeklyData() {
        String[] days = {"Today", "Tomorrow", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String[] conditions = {"Sunny", "Partly Cloudy", "Rainy", "Cloudy", "Sunny", "Thunderstorm", "Clear"};
        int[] highTemps = {32, 30, 28, 29, 33, 27, 31};
        int[] lowTemps = {24, 22, 20, 21, 25, 19, 23};
        int[] rainChances = {10, 30, 80, 50, 5, 90, 15};

        for (int i = 0; i < days.length; i++) {
            DailyWeatherData data = new DailyWeatherData(
                    days[i],
                    conditions[i],
                    highTemps[i],
                    lowTemps[i],
                    rainChances[i],
                    "cuaca" // icon name
            );
            weeklyData.add(data);
        }
    }
}