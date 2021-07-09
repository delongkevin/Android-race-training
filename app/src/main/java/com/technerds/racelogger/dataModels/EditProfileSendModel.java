package com.technerds.racelogger.dataModels;

public class EditProfileSendModel {
    private String token;
    private String first_name;
    private String last_name;
    private String gender;
    private double latitude;
    private double longitude;
    private String fcm_token;
    private String address;
    private String dob;
    private String time_zone;
    private String country;
    private String state;
    private String city;
    private String profile_image;
    private String zip_code;
    private String weight_unit;
    private double weight;
    private String start_running;

    public EditProfileSendModel() {
    
    }
    
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getFirst_name() {
        return first_name;
    }
    
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    
    public String getLast_name() {
        return last_name;
    }
    
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    public String getFcm_token() {
        return fcm_token;
    }
    
    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getDob() {
        return dob;
    }
    
    public void setDob(String dob) {
        this.dob = dob;
    }
    
    public String getTime_zone() {
        return time_zone;
    }
    
    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getProfile_image() {
        return profile_image;
    }
    
    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
    
    public String getZip_code() {
        return zip_code;
    }
    
    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }
    
    public String getWeight_unit() {
        return weight_unit;
    }
    
    public void setWeight_unit(String weight_unit) {
        this.weight_unit = weight_unit;
    }
    
    public double getWeight() {
        return weight;
    }
    
    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    public String getStart_running() {
        return start_running;
    }
    
    public void setStart_running(String start_running) {
        this.start_running = start_running;
    }
}
