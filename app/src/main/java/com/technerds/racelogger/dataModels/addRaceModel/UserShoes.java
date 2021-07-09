package com.technerds.racelogger.dataModels.addRaceModel;

import com.google.gson.annotations.SerializedName;

public class UserShoes{
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("model")
	private String model;
	
	@SerializedName("size")
	private String size;
	
	@SerializedName("width")
	private String width;
	
	@SerializedName("date_purchased")
	private String datePurchased;
	
	@SerializedName("comfort_rating")
	private int comfortRating;
	
	
	public UserShoes() {
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
	
	
	public String getDatePurchased() {
		return datePurchased;
	}
	
	public void setDatePurchased(String datePurchased) {
		this.datePurchased = datePurchased;
	}
	
	public int getComfortRating() {
		return comfortRating;
	}
	
	public void setComfortRating(int comfortRating) {
		this.comfortRating = comfortRating;
	}
	
	
	
	@Override
 	public String toString(){
		return 
			"UserShoes{" +
			",size = '" + size + '\'' +
			",comfort_rating = '" + comfortRating + '\'' + 
			",width = '" + width + '\'' + 
			",date_purchased = '" + datePurchased + '\'' + 
			",brand_name = '" + name + '\'' +
			",model = '" + model + '\'' +
			"}";
		}
}