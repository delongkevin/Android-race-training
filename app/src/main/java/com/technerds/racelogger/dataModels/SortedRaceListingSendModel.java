package com.technerds.racelogger.dataModels;

public class SortedRaceListingSendModel {
    private String token;
    private int page;
    private String race_name;
    private String park_name;
    private String date;
    private String city;
    private String state;
    private String country;
    private String zip_code;
    private String finish_place;
    private String age_group;
    private String distance;
    private String unit;
    
    public SortedRaceListingSendModel() {
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public int getPage() {
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    public String getRace_name() {
        return race_name;
    }
    
    public void setRace_name(String race_name) {
        this.race_name = race_name;
    }
    
    public String getPark_name() {
        return park_name;
    }
    
    public void setPark_name(String park_name) {
        this.park_name = park_name;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getZip_code() {
        return zip_code;
    }
    
    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }
    
    public String getFinish_place() {
        return finish_place;
    }
    
    public void setFinish_place(String finish_place) {
        this.finish_place = finish_place;
    }
    
    public String getAge_group() {
        return age_group;
    }
    
    public void setAge_group(String age_group) {
        this.age_group = age_group;
    }
    
    public String getDistance() {
        return distance;
    }
    
    public void setDistance(String distance) {
        this.distance = distance;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
