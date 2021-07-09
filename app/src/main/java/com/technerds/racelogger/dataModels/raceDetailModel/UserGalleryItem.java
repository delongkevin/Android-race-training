package com.technerds.racelogger.dataModels.raceDetailModel;

import com.google.gson.annotations.SerializedName;

public class UserGalleryItem {
    @SerializedName("image")
    private String image;
    
    @SerializedName("updated_at")
    private String updatedAt;
    
    @SerializedName("created_at")
    private String createdAt;
    
    @SerializedName("id")
    private int id;
    
    @SerializedName("user_race_id")
    private String userRaceId;
    
    @SerializedName("url")
    private String url;
    
    public void setImage(String image){
        this.image = image;
    }
    
    public String getImage(){
        return image;
    }
    
    public void setUpdatedAt(String updatedAt){
        this.updatedAt = updatedAt;
    }
    
    public String getUpdatedAt(){
        return updatedAt;
    }
    
    public void setCreatedAt(String createdAt){
        this.createdAt = createdAt;
    }
    
    public String getCreatedAt(){
        return createdAt;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public int getId(){
        return id;
    }
    
    public void setUserRaceId(String userRaceId){
        this.userRaceId = userRaceId;
    }
    
    public String getUserRaceId(){
        return userRaceId;
    }
    
    public void setUrl(String url){
        this.url = url;
    }
    
    public String getUrl(){
        return url;
    }
    
    @Override
    public String toString(){
        return
                "UserGalleryItem{" +
                        "image = '" + image + '\'' +
                        ",updated_at = '" + updatedAt + '\'' +
                        ",created_at = '" + createdAt + '\'' +
                        ",id = '" + id + '\'' +
                        ",user_race_id = '" + userRaceId + '\'' +
                        ",url = '" + url + '\'' +
                        "}";
    }
    
}
