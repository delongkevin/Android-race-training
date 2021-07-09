package com.technerds.racelogger.dataModels.shoeModels;

public class AddShoeSendModel {
    private String token;
    private String shoe_profile;
    private String shoe_name;
    private String comfort_rating;
    private String date_purchased;
    private String model;
    private String size;
    private String width;
    private String cost;
    private String rating;
    
    public AddShoeSendModel() {
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getShoe_profile() {
        return shoe_profile;
    }
    
    public void setShoe_profile(String shoe_profile) {
        this.shoe_profile = shoe_profile;
    }
    
    public String getShoe_name() {
        return shoe_name;
    }
    
    public void setShoe_name(String shoe_name) {
        this.shoe_name = shoe_name;
    }
    
    public String getComfort_rating() {
        return comfort_rating;
    }
    
    public void setComfort_rating(String comfort_rating) {
        this.comfort_rating = comfort_rating;
    }
    
    public String getDate_purchased() {
        return date_purchased;
    }
    
    public void setDate_purchased(String date_purchased) {
        this.date_purchased = date_purchased;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getSize() {
        return size;
    }
    
    public void setSize(String size) {
        this.size = size;
    }
    
    public String getWidth() {
        return width;
    }
    
    public void setWidth(String width) {
        this.width = width;
    }
    
    public String getCost() {
        return cost;
    }
    
    public void setCost(String cost) {
        this.cost = cost;
    }
    
    public String getRating() {
        return rating;
    }
    
    public void setRating(String rating) {
        this.rating = rating;
    }
}
