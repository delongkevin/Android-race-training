package com.technerds.racelogger.dataModels.raceDetailModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Race{

	@SerializedName("street_address")
	private String streetAddress;

	@SerializedName("country")
	private String country;

	@SerializedName("feel_meter_rating")
	private FeelMeterRating feelMeterRating;

	@SerializedName("distance")
	private double distance;

	@SerializedName("race_name")
	private String raceName;

	@SerializedName("city")
	private String city;

	@SerializedName("temperature_unit")
	private String temperatureUnit;

	@SerializedName("female_runners")
	private String femaleRunners;

	@SerializedName("race_terrain")
	private List<RaceTerrainItem> raceTerrain;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("park_name")
	private String parkName;

	@SerializedName("zip_code")
	private String zipCode;

	@SerializedName("toughness_rating")
	private ToughnessRating toughnessRating;

	@SerializedName("total_runners")
	private String totalRunners;

	@SerializedName("unit")
	private String unit;

	@SerializedName("course_type")
	private String courseType;

	@SerializedName("enjoyment_rating")
	private EnjoymentRating enjoymentRating;

	@SerializedName("male_runners")
	private String maleRunners;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("temperature")
	private String temperature;

	@SerializedName("weather")
	private String weather;

	@SerializedName("id")
	private int id;

	@SerializedName("state")
	private String state;

	@SerializedName("user_distance")
	private String userDistance;

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

	public void setFeelMeterRating(FeelMeterRating feelMeterRating){
		this.feelMeterRating = feelMeterRating;
	}

	public FeelMeterRating getFeelMeterRating(){
		return feelMeterRating;
	}

	public void setDistance(double distance){
		this.distance = distance;
	}

	public double getDistance(){
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

	public void setTemperatureUnit(String temperatureUnit){
		this.temperatureUnit = temperatureUnit;
	}

	public String getTemperatureUnit(){
		return temperatureUnit;
	}

	public void setFemaleRunners(String femaleRunners){
		this.femaleRunners = femaleRunners;
	}

	public String getFemaleRunners(){
		return femaleRunners;
	}

	public void setRaceTerrain(List<RaceTerrainItem> raceTerrain){
		this.raceTerrain = raceTerrain;
	}

	public List<RaceTerrainItem> getRaceTerrain(){
		return raceTerrain;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
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

	public void setToughnessRating(ToughnessRating toughnessRating){
		this.toughnessRating = toughnessRating;
	}

	public ToughnessRating getToughnessRating(){
		return toughnessRating;
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

	public void setEnjoymentRating(EnjoymentRating enjoymentRating){
		this.enjoymentRating = enjoymentRating;
	}

	public EnjoymentRating getEnjoymentRating(){
		return enjoymentRating;
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

	public void setTemperature(String temperature){
		this.temperature = temperature;
	}

	public String getTemperature(){
		return temperature;
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

	public void setUserDistance(String userDistance){
		this.userDistance = userDistance;
	}

	public String getUserDistance(){
		return userDistance;
	}

	@Override
 	public String toString(){
		return 
			"Race{" + 
			"street_address = '" + streetAddress + '\'' + 
			",country = '" + country + '\'' + 
			",feel_meter_rating = '" + feelMeterRating + '\'' + 
			",distance = '" + distance + '\'' + 
			",race_name = '" + raceName + '\'' + 
			",city = '" + city + '\'' + 
			",temperature_unit = '" + temperatureUnit + '\'' + 
			",female_runners = '" + femaleRunners + '\'' + 
			",race_terrain = '" + raceTerrain + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",park_name = '" + parkName + '\'' + 
			",zip_code = '" + zipCode + '\'' + 
			",toughness_rating = '" + toughnessRating + '\'' + 
			",total_runners = '" + totalRunners + '\'' + 
			",unit = '" + unit + '\'' + 
			",course_type = '" + courseType + '\'' + 
			",enjoyment_rating = '" + enjoymentRating + '\'' + 
			",male_runners = '" + maleRunners + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",temperature = '" + temperature + '\'' + 
			",weather = '" + weather + '\'' + 
			",id = '" + id + '\'' + 
			",state = '" + state + '\'' + 
			",user_distance = '" + userDistance + '\'' + 
			"}";
		}
}