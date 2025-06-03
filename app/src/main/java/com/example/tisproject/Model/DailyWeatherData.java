package com.example.tisproject.Model;

public class DailyWeatherData {
    private String day;
    private String condition;
    private int highTemp;
    private int lowTemp;
    private int rainChance;
    private String weatherIcon;

    public DailyWeatherData(String day, String condition, int highTemp, int lowTemp, int rainChance, String weatherIcon) {
        this.day = day;
        this.condition = condition;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
        this.rainChance = rainChance;
        this.weatherIcon = weatherIcon;
    }

    // Getters
    public String getDay() { return day; }
    public String getCondition() { return condition; }
    public int getHighTemp() { return highTemp; }
    public int getLowTemp() { return lowTemp; }
    public int getRainChance() { return rainChance; }
    public String getWeatherIcon() { return weatherIcon; }

    // Setters
    public void setDay(String day) { this.day = day; }
    public void setCondition(String condition) { this.condition = condition; }
    public void setHighTemp(int highTemp) { this.highTemp = highTemp; }
    public void setLowTemp(int lowTemp) { this.lowTemp = lowTemp; }
    public void setRainChance(int rainChance) { this.rainChance = rainChance; }
    public void setWeatherIcon(String weatherIcon) { this.weatherIcon = weatherIcon; }
}