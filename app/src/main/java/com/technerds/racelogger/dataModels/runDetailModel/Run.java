package com.technerds.racelogger.dataModels.runDetailModel;

import com.google.gson.annotations.SerializedName;

public class Run{

	@SerializedName("street_address")
	private String streetAddress;

	@SerializedName("country")
	private String country;

	

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("city")
	private String city;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("training_route")
	private String trainingRoute;

	@SerializedName("state")
	private String state;

	@SerializedName("park_name")
	private String parkName;

	@SerializedName("zip_code")
	private String zipCode;
	
	
	public void setStreetAddress(String streetAddress){
		this.streetAddress = streetAddress;
	}

	public String getStreetAddress(){
		return streetAddress;
	}

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}
	

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
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

	public void setTrainingRoute(String trainingRoute){
		this.trainingRoute = trainingRoute;
	}

	public String getTrainingRoute(){
		return trainingRoute;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setParkName(String parkName){
		this.parkName = parkName;
	}

	public String getParkName(){
		return parkName;
	}

	public void setZipCode(String zipCode){
		this.zipCode = zipCode;
	}

	public String getZipCode(){
		return zipCode;
	}
	
	
	@Override
 	public String toString(){
		return 
			"Run{" + 
			"street_address = '" + streetAddress + '\'' + 
			",country = '" + country + '\'' +
			",updated_at = '" + updatedAt + '\'' + 
			",city = '" + city + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",training_route = '" + trainingRoute + '\'' + 
			",state = '" + state + '\'' + 
			",park_name = '" + parkName + '\'' + 
			",zip_code = '" + zipCode + '\'' + 
			"}";
		}
}