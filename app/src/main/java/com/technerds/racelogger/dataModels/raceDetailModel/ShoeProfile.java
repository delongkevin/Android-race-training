package com.technerds.racelogger.dataModels.raceDetailModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShoeProfile{

	@SerializedName("cost")
	private String cost;

	@SerializedName("comfort_rating")
	private String comfortRating;

	@SerializedName("date_purchased")
	private String datePurchased;

	@SerializedName("rating")
	private String rating;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("shoe_name")
	private String shoeName;

	@SerializedName("size")
	private String size;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("width")
	private String width;

	@SerializedName("model")
	private String model;

	@SerializedName("id")
	private int id;

	@SerializedName("shoe_profile")
	private String shoeProfile;

	@SerializedName("status")
	private String status;
	
	@SerializedName("shoe_mileage")
	private List<ShoeMileageMini> shoe_mileage;
	
	public List<ShoeMileageMini> getShoe_mileage() {
		return shoe_mileage;
	}
	
	public void setShoe_mileage(List<ShoeMileageMini> shoe_mileage) {
		this.shoe_mileage = shoe_mileage;
	}
	
	public void setCost(String cost){
		this.cost = cost;
	}

	public String getCost(){
		return cost;
	}

	public void setComfortRating(String comfortRating){
		this.comfortRating = comfortRating;
	}

	public String getComfortRating(){
		return comfortRating;
	}

	public void setDatePurchased(String datePurchased){
		this.datePurchased = datePurchased;
	}

	public String getDatePurchased(){
		return datePurchased;
	}

	public void setRating(String rating){
		this.rating = rating;
	}

	public String getRating(){
		return rating;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setShoeName(String shoeName){
		this.shoeName = shoeName;
	}

	public String getShoeName(){
		return shoeName;
	}

	public void setSize(String size){
		this.size = size;
	}

	public String getSize(){
		return size;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setWidth(String width){
		this.width = width;
	}

	public String getWidth(){
		return width;
	}

	public void setModel(String model){
		this.model = model;
	}

	public String getModel(){
		return model;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setShoeProfile(String shoeProfile){
		this.shoeProfile = shoeProfile;
	}

	public String getShoeProfile(){
		return shoeProfile;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"ShoeProfile{" + 
			"cost = '" + cost + '\'' + 
			",comfort_rating = '" + comfortRating + '\'' + 
			",date_purchased = '" + datePurchased + '\'' + 
			",rating = '" + rating + '\'' + 
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