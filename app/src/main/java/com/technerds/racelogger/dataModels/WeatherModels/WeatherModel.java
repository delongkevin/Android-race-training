package com.technerds.racelogger.dataModels.WeatherModels;

import java.util.ArrayList;

public class WeatherModel {
    private CoordModel coord;
    private ArrayList<WeatherDataModel> weather;
    private String base;
    private TemperatureModel main;
    private int visibility;
    private WindModel wind;
    private CloudsModel clouds;
    private long dt;
    private SysModel sys;
    private int timezone;
    private int id;
    private String name;
    private int cod;
    
    public WeatherModel() {
    }
    
    public CoordModel getCoord() {
        return coord;
    }
    
    public void setCoord(CoordModel coord) {
        this.coord = coord;
    }
    
    public ArrayList<WeatherDataModel> getWeather() {
        return weather;
    }
    
    public void setWeather(ArrayList<WeatherDataModel> weather) {
        this.weather = weather;
    }
    
    public String getBase() {
        return base;
    }
    
    public void setBase(String base) {
        this.base = base;
    }
    
    public TemperatureModel getMain() {
        return main;
    }
    
    public void setMain(TemperatureModel main) {
        this.main = main;
    }
    
    public int getVisibility() {
        return visibility;
    }
    
    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }
    
    public WindModel getWind() {
        return wind;
    }
    
    public void setWind(WindModel wind) {
        this.wind = wind;
    }
    
    public CloudsModel getClouds() {
        return clouds;
    }
    
    public void setClouds(CloudsModel clouds) {
        this.clouds = clouds;
    }
    
    public long getDt() {
        return dt;
    }
    
    public void setDt(long dt) {
        this.dt = dt;
    }
    
    public SysModel getSys() {
        return sys;
    }
    
    public void setSys(SysModel sys) {
        this.sys = sys;
    }
    
    public int getTimezone() {
        return timezone;
    }
    
    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getCod() {
        return cod;
    }
    
    public void setCod(int cod) {
        this.cod = cod;
    }
}
