package com.example.tisproject.Network;

public class WeatherData {
    // Detail cuaca
    private double temperature;
    private double feelsLike;
    private double tempMin;
    private double tempMax;
    private int humidity;
    private double pressure;
    private String weatherMain;
    private String description;
    private double windSpeed;
    private String cityName;
    private String countryCode;
    private int visibility;
    private String sunrise;
    private String sunset;
    private int rainChance;

    // Forecast harian
    private String day;
    private String condition;
    private int highTemp;
    private int lowTemp;
    private String rainChanceStr;

    public WeatherData(String mon, String sunny, int i, int i1, String s) {}

    // Getter dan Setter untuk detail cuaca
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public double getFeelsLike() { return feelsLike; }
    public void setFeelsLike(double feelsLike) { this.feelsLike = feelsLike; }

    public double getTempMin() { return tempMin; }
    public void setTempMin(double tempMin) { this.tempMin = tempMin; }

    public double getTempMax() { return tempMax; }
    public void setTempMax(double tempMax) { this.tempMax = tempMax; }

    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    public double getPressure() { return pressure; }
    public void setPressure(double pressure) { this.pressure = pressure; }

    public String getWeatherMain() { return weatherMain; }
    public void setWeatherMain(String weatherMain) { this.weatherMain = weatherMain; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }

    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public int getVisibility() { return visibility; }
    public void setVisibility(int visibility) { this.visibility = visibility; }

    public String getSunrise() { return sunrise; }
    public void setSunrise(String sunrise) { this.sunrise = sunrise; }

    public String getSunset() { return sunset; }
    public void setSunset(String sunset) { this.sunset = sunset; }

    public int getRainChance() { return rainChance; }
    public void setRainChance(int rainChance) { this.rainChance = rainChance; }

    // Getter dan Setter untuk forecast mingguan
    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public int getHighTemp() { return highTemp; }
    public void setHighTemp(int highTemp) { this.highTemp = highTemp; }

    public int getLowTemp() { return lowTemp; }
    public void setLowTemp(int lowTemp) { this.lowTemp = lowTemp; }

    public String getRainChanceStr() { return rainChanceStr; }
    public void setRainChanceStr(String rainChanceStr) { this.rainChanceStr = rainChanceStr; }
}
