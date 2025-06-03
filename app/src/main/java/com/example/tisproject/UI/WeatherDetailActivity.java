package com.example.tisproject.UI;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tisproject.Adapter.HourlyForecastAdapter;
import com.example.tisproject.Model.HourlyWeatherData;
import com.example.tisproject.R;
import com.example.tisproject.Model.WeatherApiService;
import com.example.tisproject.Network.WeatherData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherDetailActivity extends AppCompatActivity {

    private static final String TAG = "WeatherDetailActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    // Views
    private TextView tvLocation, tvCurrentDate, tvTemperature, tvFeelsLike;
    private TextView tvWeatherDescription, tvHighLowTemp, tvWindSpeed;
    private TextView tvHumidity, tvRainChance, tvSunrise, tvVisibility;
    private ImageView ivWeatherIcon, ivRefresh, ivMenu;
    private RecyclerView rvHourlyForecast;
    private SwipeRefreshLayout swipeRefresh;

    // Data
    private WeatherApiService weatherApiService;
    private FusedLocationProviderClient fusedLocationClient;
    private HourlyForecastAdapter hourlyAdapter;
    private List<HourlyWeatherData> hourlyData;

    // Default location (Jakarta)
    private String currentCity = "Jakarta";
    private double currentLat = -6.2088;
    private double currentLon = 106.8456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initViews();
        initData();
        setupRecyclerView();
        setupClickListeners();
        checkLocationPermission();

        // Load initial weather data
        loadWeatherData();
    }

    private void initViews() {
        tvLocation = findViewById(R.id.tv_location);
        tvCurrentDate = findViewById(R.id.tv_current_date);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvFeelsLike = findViewById(R.id.tv_feels_like);
        tvWeatherDescription = findViewById(R.id.tv_weather_description);
        tvHighLowTemp = findViewById(R.id.tv_high_low_temp);
        tvWindSpeed = findViewById(R.id.tv_wind_speed);
        tvHumidity = findViewById(R.id.tv_humidity);
        tvRainChance = findViewById(R.id.tv_rain_chance);
        tvSunrise = findViewById(R.id.tv_sunrise);
        tvVisibility = findViewById(R.id.tv_visibility);
        ivWeatherIcon = findViewById(R.id.iv_weather_icon);
        ivRefresh = findViewById(R.id.iv_refresh);
        ivMenu = findViewById(R.id.iv_menu);
        rvHourlyForecast = findViewById(R.id.rv_hourly_forecast);
        swipeRefresh = findViewById(R.id.swipe_refresh);
    }

    private void initData() {
        weatherApiService = new WeatherApiService();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        hourlyData = new ArrayList<>();

        // Set current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());
        tvCurrentDate.setText("Today, " + dateFormat.format(new Date()));
    }

    private void setupRecyclerView() {
        hourlyAdapter = new HourlyForecastAdapter(hourlyData);
        rvHourlyForecast.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvHourlyForecast.setAdapter(hourlyAdapter);
    }

    private void setupClickListeners() {
        // Refresh button with animation
        ivRefresh.setOnClickListener(v -> {
            animateRefreshIcon();
            loadWeatherData();
        });

        // Swipe to refresh
        swipeRefresh.setOnRefreshListener(this::loadWeatherData);

        // Menu button with options
        ivMenu.setOnClickListener(v -> showMenuOptions());

        // Weekly forecast card
        findViewById(R.id.cv_weekly_forecast).setOnClickListener(v -> {
            Intent intent = new Intent(this, WeeklyForecastActivity.class);
            intent.putExtra("lat", currentLat);
            intent.putExtra("lon", currentLon);
            intent.putExtra("city", currentCity);
            startActivity(intent);
        });

        // Location click to change city
        tvLocation.setOnClickListener(v -> showLocationDialog());

        // Weather icon click for details
        ivWeatherIcon.setOnClickListener(v -> showWeatherDetails());

        // Add click listeners for other cards/sections
        setupAdditionalClickListeners();
    }

    private void setupAdditionalClickListeners() {
        // Wind speed card
        findViewById(R.id.card_wind).setOnClickListener(v -> {
            showDetailDialog("Wind Information",
                    "Current wind speed: " + tvWindSpeed.getText() + "\n" +
                            "Wind affects how the temperature feels on your skin.");
        });

        // Humidity card
        findViewById(R.id.card_humidity).setOnClickListener(v -> {
            showDetailDialog("Humidity Information",
                    "Current humidity: " + tvHumidity.getText() + "\n" +
                            "Humidity affects comfort level and how hot it feels.");
        });

        // Rain chance card
        findViewById(R.id.card_rain).setOnClickListener(v -> {
            showDetailDialog("Rain Forecast",
                    "Chance of rain: " + tvRainChance.getText() + "\n" +
                            "Based on current weather conditions and forecast models.");
        });

        // Sunrise card
        findViewById(R.id.card_sunrise).setOnClickListener(v -> {
            showDetailDialog("Sun Information",
                    "Sunrise: " + tvSunrise.getText() + "\n" +
                            "Plan your day with accurate sunrise and sunset times.");
        });

        // Visibility card
        findViewById(R.id.card_visibility).setOnClickListener(v -> {
            showDetailDialog("Visibility Information",
                    "Current visibility: " + tvVisibility.getText() + "\n" +
                            "Visibility affects driving conditions and outdoor activities.");
        });
    }

    private void animateRefreshIcon() {
        RotateAnimation rotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        ivRefresh.startAnimation(rotate);
    }

    private void showMenuOptions() {
        String[] options = {"Change Location", "Settings", "About", "Share App"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Menu Options")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            showLocationDialog();
                            break;
                        case 1:
                            showSettingsDialog();
                            break;
                        case 2:
                            showAboutDialog();
                            break;
                        case 3:
                            shareApp();
                            break;
                    }
                });
        builder.show();
    }

    private void showLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Location");

        final EditText input = new EditText(this);
        input.setHint("Enter city name (e.g., Jakarta, London)");
        builder.setView(input);

        builder.setPositiveButton("Search", (dialog, which) -> {
            String cityName = input.getText().toString().trim();
            if (!cityName.isEmpty()) {
                searchCityWeather(cityName);
            }
        });

        builder.setNegativeButton("Use Current Location", (dialog, which) -> {
            getCurrentLocation();
        });

        builder.setNeutralButton("Cancel", null);
        builder.show();
    }

    private void showWeatherDetails() {
        // Show detailed weather information
        showDetailDialog("Weather Details",
                "Current Conditions:\n" +
                        "Temperature: " + tvTemperature.getText() + "\n" +
                        "Feels like: " + tvFeelsLike.getText().toString().replace("Feels like ", "") + "\n" +
                        "Condition: " + tvWeatherDescription.getText() + "\n" +
                        "High/Low: " + tvHighLowTemp.getText());
    }

    private void showDetailDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void showSettingsDialog() {
        String[] settings = {"Temperature Unit (°C/°F)", "Wind Speed Unit", "Time Format", "Auto Refresh"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Settings")
                .setItems(settings, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            Toast.makeText(this, "Temperature unit settings", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(this, "Wind speed unit settings", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(this, "Time format settings", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(this, "Auto refresh settings", Toast.LENGTH_SHORT).show();
                            break;
                    }
                });
        builder.show();
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About Weather App")
                .setMessage("Weather App v1.0\n\n" +
                        "Get accurate weather information for your location.\n\n" +
                        "Features:\n" +
                        "• Current weather conditions\n" +
                        "• Hourly forecast\n" +
                        "• Weekly forecast\n" +
                        "• Location-based weather\n" +
                        "• Multiple cities support\n\n" +
                        "Powered by OpenWeatherMap API")
                .setPositiveButton("OK", null)
                .show();
    }

    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Weather App");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "Check out this amazing weather app! Get accurate weather forecasts for any location.");
        startActivity(Intent.createChooser(shareIntent, "Share Weather App"));
    }

    private void searchCityWeather(String cityName) {
        // For simplicity, using geocoding or you can implement a city search API
        // Here's a basic implementation with some major cities
        switch (cityName.toLowerCase()) {
            case "jakarta":
                currentLat = -6.2088;
                currentLon = 106.8456;
                currentCity = "Jakarta";
                break;
            case "london":
                currentLat = 51.5074;
                currentLon = -0.1278;
                currentCity = "London";
                break;
            case "new york":
                currentLat = 40.7128;
                currentLon = -74.0060;
                currentCity = "New York";
                break;
            case "tokyo":
                currentLat = 35.6762;
                currentLon = 139.6503;
                currentCity = "Tokyo";
                break;
            case "sydney":
                currentLat = -33.8688;
                currentLon = 151.2093;
                currentCity = "Sydney";
                break;
            case "paris":
                currentLat = 48.8566;
                currentLon = 2.3522;
                currentCity = "Paris";
                break;
            default:
                Toast.makeText(this, "City not found. Using current location.", Toast.LENGTH_SHORT).show();
                getCurrentLocation();
                return;
        }

        Toast.makeText(this, "Loading weather for " + currentCity, Toast.LENGTH_SHORT).show();
        loadWeatherData();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentLat = location.getLatitude();
                        currentLon = location.getLongitude();
                        Log.d(TAG, "Location: " + currentLat + ", " + currentLon);
                        loadWeatherData();
                    } else {
                        Log.d(TAG, "Using default location: Jakarta");
                        loadWeatherData();
                    }
                });
    }

    private void loadWeatherData() {
        swipeRefresh.setRefreshing(true);

        weatherApiService.getCurrentWeather(currentLat, currentLon, new WeatherApiService.WeatherCallback() {
            @Override
            public void onSuccess(WeatherData weatherData) {
                runOnUiThread(() -> {
                    updateUI(weatherData);
                    swipeRefresh.setRefreshing(false);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(WeatherDetailActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);
                    Log.e(TAG, "Weather API Error: " + error);
                });
            }
        });

        // Load hourly forecast
        loadHourlyForecast();
    }

    private void loadHourlyForecast() {
        weatherApiService.getHourlyForecast(currentLat, currentLon, new WeatherApiService.HourlyForecastCallback() {
            @Override
            public void onSuccess(List<HourlyWeatherData> hourlyForecast) {
                runOnUiThread(() -> {
                    hourlyData.clear();
                    hourlyData.addAll(hourlyForecast);
                    hourlyAdapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Hourly forecast error: " + error);
            }
        });
    }

    private void updateUI(WeatherData weather) {
        tvLocation.setText(weather.getCityName() + ", " + weather.getCountryCode());
        tvTemperature.setText(Math.round(weather.getTemperature()) + "°");
        tvFeelsLike.setText("Feels like " + Math.round(weather.getFeelsLike()) + "°");
        tvWeatherDescription.setText(weather.getDescription());
        tvHighLowTemp.setText("H: " + Math.round(weather.getTempMax()) + "°  L: " + Math.round(weather.getTempMin()) + "°");
        tvWindSpeed.setText(Math.round(weather.getWindSpeed()) + " km/h");
        tvHumidity.setText(weather.getHumidity() + "%");
        tvRainChance.setText(weather.getRainChance() + "%");
        tvSunrise.setText(weather.getSunrise());
        tvVisibility.setText(Math.round(weather.getVisibility() / 1000.0) + " km");

        // Update current city name
        currentCity = weather.getCityName();

        // Set weather icon based on weather condition
        setWeatherIcon(weather.getWeatherMain());
    }

    private void setWeatherIcon(String weatherMain) {
        // You can add more conditions and icons as needed
        switch (weatherMain.toLowerCase()) {
            case "clear":
                ivWeatherIcon.setImageResource(R.drawable.cuaca);
                break;
            case "rain":
            case "drizzle":
                ivWeatherIcon.setImageResource(R.drawable.hujan);
                break;
            case "clouds":
                ivWeatherIcon.setImageResource(R.drawable.cuaca);
                break;
            case "thunderstorm":
                ivWeatherIcon.setImageResource(R.drawable.hujan);
                break;
            default:
                ivWeatherIcon.setImageResource(R.drawable.cuaca);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied. Using default location.", Toast.LENGTH_SHORT).show();
                loadWeatherData();
            }
        }
    }
}