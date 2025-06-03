
package com.example.tisproject.Model;

import android.util.Log;

import com.example.tisproject.Network.WeatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherApiService {

    private static final String TAG = "WeatherApiService";
    private static final String API_KEY = "fe4c29d61b0dfd07029d8ffa9f5a9072";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    private OkHttpClient client;

    public interface WeatherCallback {
        void onSuccess(WeatherData weatherData);
        void onError(String error);
    }

    // Add this missing interface
    public interface HourlyForecastCallback {
        void onSuccess(List<HourlyWeatherData> hourlyForecast);
        void onError(String error);
    }

    public WeatherApiService() {
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public void getCurrentWeather(double lat, double lon, WeatherCallback callback) {
        String url = BASE_URL + "weather?lat=" + lat + "&lon=" + lon + "&appid=" + API_KEY + "&units=metric";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "API call failed", e);
                callback.onError("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError("HTTP error: " + response.code());
                    return;
                }

                String jsonData = response.body().string();
                try {
                    WeatherData weatherData = parseWeatherData(jsonData);
                    callback.onSuccess(weatherData);
                } catch (JSONException e) {
                    Log.e(TAG, "JSON parsing error", e);
                    callback.onError("Data parsing error: " + e.getMessage());
                }
            }
        });
    }

    // Add this missing method
    public void getHourlyForecast(double lat, double lon, HourlyForecastCallback callback) {
        String url = BASE_URL + "forecast?lat=" + lat + "&lon=" + lon + "&appid=" + API_KEY + "&units=metric";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Hourly forecast API call failed", e);
                callback.onError("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError("HTTP error: " + response.code());
                    return;
                }

                String jsonData = response.body().string();
                try {
                    List<HourlyWeatherData> hourlyData = parseHourlyForecastData(jsonData);
                    callback.onSuccess(hourlyData);
                } catch (JSONException e) {
                    Log.e(TAG, "JSON parsing error", e);
                    callback.onError("Data parsing error: " + e.getMessage());
                }
            }
        });
    }

    private WeatherData parseWeatherData(String jsonData) throws JSONException {
        JSONObject json = new JSONObject(jsonData);

        JSONObject main = json.getJSONObject("main");
        double temperature = main.getDouble("temp");
        double feelsLike = main.optDouble("feels_like", temperature);
        double tempMin = main.optDouble("temp_min", temperature);
        double tempMax = main.optDouble("temp_max", temperature);
        int humidity = main.optInt("humidity", 0);
        double pressure = main.optDouble("pressure", 0);

        JSONArray weatherArray = json.getJSONArray("weather");
        JSONObject weather = weatherArray.getJSONObject(0);
        String weatherMain = weather.getString("main");
        String description = weather.getString("description");

        JSONObject wind = json.optJSONObject("wind");
        double windSpeed = 0;
        if (wind != null) {
            windSpeed = wind.optDouble("speed", 0) * 3.6; // m/s to km/h
        }

        String cityName = json.getString("name");
        JSONObject sys = json.getJSONObject("sys");
        String countryCode = sys.getString("country");

        int visibility = json.optInt("visibility", 0);

        long sunriseTimestamp = sys.getLong("sunrise");
        long sunsetTimestamp = sys.getLong("sunset");

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String sunrise = timeFormat.format(new Date(sunriseTimestamp * 1000));
        String sunset = timeFormat.format(new Date(sunsetTimestamp * 1000));

        WeatherData weatherData = new WeatherData("Mon", "Sunny", 33, 24, "10%");
        weatherData.setTemperature(temperature);
        weatherData.setFeelsLike(feelsLike);
        weatherData.setTempMin(tempMin);
        weatherData.setTempMax(tempMax);
        weatherData.setHumidity(humidity);
        weatherData.setPressure(pressure);
        weatherData.setWeatherMain(weatherMain);
        weatherData.setDescription(capitalizeFirstLetter(description));
        weatherData.setWindSpeed(windSpeed);
        weatherData.setCityName(cityName);
        weatherData.setCountryCode(countryCode);
        weatherData.setVisibility(visibility);
        weatherData.setSunrise(sunrise);
        weatherData.setSunset(sunset);
        weatherData.setRainChance(calculateRainChance(weatherMain)); // Add rain chance calculation

        return weatherData;
    }

    private List<HourlyWeatherData> parseHourlyForecastData(String jsonData) throws JSONException {
        List<HourlyWeatherData> hourlyList = new ArrayList<>();
        JSONObject json = new JSONObject(jsonData);
        JSONArray list = json.getJSONArray("list");

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // Get next 24 hours (8 items, each 3 hours apart)
        int itemsToShow = Math.min(8, list.length());

        for (int i = 0; i < itemsToShow; i++) {
            JSONObject item = list.getJSONObject(i);

            long timestamp = item.getLong("dt");
            String time = timeFormat.format(new Date(timestamp * 1000));

            JSONObject main = item.getJSONObject("main");
            int temperature = (int) Math.round(main.getDouble("temp"));

            JSONArray weatherArray = item.getJSONArray("weather");
            JSONObject weather = weatherArray.getJSONObject(0);
            String weatherMain = weather.getString("main");
            String weatherIcon = weather.getString("icon");

            int rainChance = calculateRainChance(weatherMain);

            HourlyWeatherData hourlyData = new HourlyWeatherData(time, temperature, rainChance, weatherIcon);
            hourlyList.add(hourlyData);
        }

        return hourlyList;
    }

    private int calculateRainChance(String weatherMain) {
        switch (weatherMain.toLowerCase()) {
            case "rain":
            case "drizzle":
                return 80;
            case "thunderstorm":
                return 90;
            case "clouds":
                return 30;
            case "clear":
                return 5;
            default:
                return 10;
        }
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
