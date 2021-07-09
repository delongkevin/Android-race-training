package com.technerds.racelogger.dataModels.searchRaceModel;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("street_address")
	private String streetAddress;

	@SerializedName("country")
	private String country;

	@SerializedName("notes")
	private Object notes;

	@SerializedName("distance")
	private String distance;
	
	@SerializedName("user_distance")
	private String user_distance;

	@SerializedName("race_name")
	private String raceName;

	@SerializedName("city")
	private String city;

	@SerializedName("female_runners")
	private String femaleRunners;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("temperature_type")
	private String temperatureType;

	@SerializedName("park_name")
	private String parkName;

	@SerializedName("zip_code")
	private String zipCode;

	@SerializedName("total_runners")
	private String totalRunners;

	@SerializedName("unit")
	private String unit;

	@SerializedName("course_type")
	private String courseType;

	@SerializedName("male_runners")
	private String maleRunners;

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

	public void setNotes(Object notes){
		this.notes = notes;
	}

	public Object getNotes(){
		return notes;
	}

	public void setDistance(String distance){
		this.distance = distance;
	}

	public String getDistance(){
		return distance;
	}

	public void setRaceName(String raceName){
		this.raceName = raceName;
	}

	public String getRaceName(){
		return raceName;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setFemaleRunners(String femaleRunners){
		this.femaleRunners = femaleRunners;
	}

	public String getFemaleRunners(){
		return femaleRunners;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
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

	public void setTotalRunners(String totalRunners){
		this.totalRunners = totalRunners;
	}

	public String getTotalRunners(){
		return totalRunners;
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

	public void setMaleRunners(String maleRunners){
		this.maleRunners = maleRunners;
	}

	public String getMaleRunners(){
		return maleRunners;
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
			",notes = '" + notes + '\'' + 
			",distance = '" + distance + '\'' + 
			",race_name = '" + raceName + '\'' + 
			",city = '" + city + '\'' + 
			",female_runners = '" + femaleRunners + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",temperature_type = '" + temperatureType + '\'' + 
			",park_name = '" + parkName + '\'' + 
			",zip_code = '" + zipCode + '\'' + 
			",total_runners = '" + totalRunners + '\'' + 
			",unit = '" + unit + '\'' + 
			",course_type = '" + courseType + '\'' + 
			",male_runners = '" + maleRunners + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",weather = '" + weather + '\'' + 
			",id = '" + id + '\'' + 
			",state = '" + state + '\'' + 
			"}";
		}
}