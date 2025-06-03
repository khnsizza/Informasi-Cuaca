
package com.example.tisproject.Model;

public class HourlyWeatherData {
    private String time;
    private int temperature;
    private int rainChance;
    private String weatherIcon;

    public HourlyWeatherData(String time, int temperature, int rainChance, String weatherIcon) {
        this.time = time;
        this.temperature = temperature;
        this.rainChance = rainChance;
        this.weatherIcon = weatherIcon;
    }

    // Getters
    public String getTime() { return time; }
    public int getTemperature() { return temperature; }
    public int getRainChance() { return rainChance; }
    public String getWeatherIcon() { return weatherIcon; }

    // Setters
    public void setTime(String time) { this.time = time; }
    public void setTemperature(int temperature) { this.temperature = temperature; }
    public void setRainChance(int rainChance) { this.rainChance = rainChance; }
    public void setWeatherIcon(String weatherIcon) { this.weatherIcon = weatherIcon; }
}
