package com.technerds.racelogger.dataModels.shoeModels.getShoeProfiles;

import com.google.gson.annotations.SerializedName;

public class ShoeProfileDataItem {
    
    
    @SerializedName("id")
    private int id;
    
    @SerializedName("shoe_name")
    private String shoeName;
    
    @SerializedName("shoe_profile")
    private String shoeProfile;
    
    @SerializedName("model")
    private String model;
    
    @SerializedName("size")
    private String size;
    
    @SerializedName("width")
    private String width;

//	@SerializedName("cost")
//	private String cost;
    
    @SerializedName("date_purchased")
    private String datePurchased;
    
    @SerializedName("comfort_rating")
    private String comfortRating;
    
    //	@SerializedName("rating")
//	private String rating;
    
    @SerializedName("user_id")
    private String userId;
    
    @SerializedName("status")
    private String status;
    
    
    @SerializedName("created_at")
    private String createdAt;
    
    
    @SerializedName("updated_at")
    private String updatedAt;
    
    @SerializedName("shoe_mileage")
    private String shoeMileage;
    
    
    public void setShoeMileage(String shoeMileage) {
        this.shoeMileage = shoeMileage;
    }
    
    public String getShoeMileage() {
        return shoeMileage;
    }
    
    public void setComfortRating(String comfortRating) {
        this.comfortRating = comfortRating;
    }
    
    public String getComfortRating() {
        return comfortRating;
    }
    
    public void setDatePurchased(String datePurchased) {
        this.datePurchased = datePurchased;
    }
    
    public String getDatePurchased() {
        return datePurchased;
    }
    
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setShoeName(String shoeName) {
        this.shoeName = shoeName;
    }
    
    public String getShoeName() {
        return shoeName;
    }
    
    public void setSize(String size) {
        this.size = size;
    }
    
    public String getSize() {
        return size;
    }
    
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setWidth(String width) {
        this.width = width;
    }
    
    public String getWidth() {
        return width;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public void setShoeProfile(String shoeProfile) {
        this.shoeProfile = shoeProfile;
    }
    
    public String getShoeProfile() {
        return shoeProfile;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getStatus() {
        return status;
    }
    
    @Override
    public String toString() {
        return
                "DataItem{" +
                        ",shoe_mileage = '" + shoeMileage + '\'' +
                        ",comfort_rating = '" + comfortRating + '\'' +
                        ",date_purchased = '" + datePurchased + '\'' +
                        ",created_at = '" + createdAt + '\'' +
                        ",shoe_name = '" + shoeName + '\'' +
                        ",size = '" + size + '\'' +
                        ",updated_at = '" + updatedAt + '\'' +
                        ",user_id = '" + userId + '\'' +
                        ",width = '" + width + '\'' +
                        ",model = '" + model + '\'' +
                        ",id = '" + id + '\'' +
                        ",shoe_profile = '" + shoeProfile + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}