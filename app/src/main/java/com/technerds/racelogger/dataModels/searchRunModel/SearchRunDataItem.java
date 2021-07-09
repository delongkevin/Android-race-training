package com.technerds.racelogger.dataModels.searchRunModel;

import com.google.gson.annotations.SerializedName;

public class SearchRunDataItem {

	@SerializedName("street_address")
	private String streetAddress;

	@SerializedName("country")
	private String country;

	@SerializedName("distance")
	private String distance;
	
	@SerializedName("user_distance")
	private String user_distance;

	@SerializedName("city")
	private String city;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("training_route")
	private String trainingRoute;

	@SerializedName("temperature_type")
	private String temperatureType;

	@SerializedName("park_name")
	private String parkName;

	@SerializedName("zip_code")
	private String zipCode;

	@SerializedName("unit")
	private String unit;

	@SerializedName("course_type")
	private String courseType;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("weather")
	private String weather;

	@SerializedName("id")
	private int id;

	@SerializedName("state")
	private String state;

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

	public void setDistance(String distance){
		this.distance = distance;
	}

	public String getDistance(){
		return distance;
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

	public void setTrainingRoute(String trainingRoute){
		this.trainingRoute = trainingRoute;
	}

	public String getTrainingRoute(){
		return trainingRoute;
	}

	public void setTemperatureType(String temperatureType){
		this.temperatureType = temperatureType;
	}

	public String getTemperatureType(){
		return temperatureType;
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

	public void setUnit(String unit){
		this.unit = unit;
	}

	public String getUnit(){
		return unit;
	}

	public void setCourseType(String courseType){
		this.courseType = courseType;
	}

	public String getCourseType(){
		return courseType;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setWeather(String weather){
		this.weather = weather;
	}

	public String getWeather(){
		return weather;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}
	
	public String getUser_distance() {
		return user_distance;
	}
	
	public void setUser_distance(String user_distance) {
		this.user_distance = user_distance;
	}
	
	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"street_address = '" + streetAddress + '\'' + 
			",country = '" + country + '\'' + 
			",distance = '" + distance + '\'' + 
			",city = '" + city + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",training_route = '" + trainingRoute + '\'' + 
			",temperature_type = '" + temperatureType + '\'' + 
			",park_name = '" + parkName + '\'' + 
			",zip_code = '" + zipCode + '\'' + 
			",unit = '" + unit + '\'' + 
			",course_type = '" + courseType + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",weather = '" + weather + '\'' + 
			",id = '" + id + '\'' + 
			",state = '" + state + '\'' + 
			"}";
		}
}