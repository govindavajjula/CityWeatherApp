package com.city.weather.app.controller;

public class CityWeatherInfo {

    private String cityName;
    private float  temperature;

    public CityWeatherInfo(String cityName, float temperature) {
        this.cityName = cityName;
        this.temperature = temperature;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }
}
