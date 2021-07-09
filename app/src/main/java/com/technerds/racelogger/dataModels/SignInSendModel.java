package com.technerds.racelogger.dataModels;

public class SignInSendModel {
    private String email;
    private String password;
    private double latitude;
    private double longitude;
    private String fcm_token;
    private String login_type;
    private String time_zone;
    
    public SignInSendModel() {
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
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
    
    public String getLogin_type() {
        return login_type;
    }
    
    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }
    
    public String getTime_zone() {
        return time_zone;
    }
    
    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }
}
