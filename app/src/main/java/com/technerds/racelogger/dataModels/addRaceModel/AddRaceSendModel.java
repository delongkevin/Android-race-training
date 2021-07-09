package com.technerds.racelogger.dataModels.addRaceModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AddRaceSendModel{
	
	@SerializedName("token")
	private String token;
	
	@SerializedName("race_name")
	private String raceName;
	
	@SerializedName("park_name")
	private String park_name;
	
	@SerializedName("street_address")
	private String street_address;
	
	@SerializedName("city")
	private String city;
	
	@SerializedName("country")
	private String country;
	
	@SerializedName("state")
	private String state;
	
	@SerializedName("zip_code")
	private String zip_code;
	
	@SerializedName("distance")
	private double distance;
	
	@SerializedName("course_type")
	private String course_type;
	
	@SerializedName("temperature")
	private String temperature;
	
	@SerializedName("temperature_unit")
	private String temperature_unit;
	
	@SerializedName("weather")
	private String weather;
	
	@SerializedName("total_runners")
	private int total_runners;
	
	@SerializedName("male_runners")
	private int male_runners;
	
	@SerializedName("female_runners")
	private int female_runners;
	
	@SerializedName("enjoyment_rating")
	private int enjoyment_rating;
	
	@SerializedName("feel_meter_rating")
	private int feel_meter_rating;
	
	@SerializedName("toughness_rating")
	private int toughness_rating;
	
	@SerializedName("unit")
	private String unit;
	
	@SerializedName("user_race")
	private UserRace user_race;
	
	@SerializedName("shoes_profile_id")
	private int shoes_profile_id;
	
	@SerializedName("race_terrain")
	private List<String> race_terrain;
	
	@SerializedName("user_gallery")
	private List<String> userGallery;
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getRaceName() {
		return raceName;
	}
	
	public void setRaceName(String raceName) {
		this.raceName = raceName;
	}
	
	public String getPark_name() {
		return park_name;
	}
	
	public void setPark_name(String park_name) {
		this.park_name = park_name;
	}
	
	public String getStreet_address() {
		return street_address;
	}
	
	public void setStreet_address(String street_address) {
		this.street_address = street_address;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getZip_code() {
		return zip_code;
	}
	
	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public String getCourse_type() {
		return course_type;
	}
	
	public void setCourse_type(String course_type) {
		this.course_type = course_type;
	}
	
	public String getTemperature() {
		return temperature;
	}
	
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	
	public String getTemperature_unit() {
		return temperature_unit;
	}
	
	public void setTemperature_unit(String temperature_unit) {
		this.temperature_unit = temperature_unit;
	}
	
	public String getWeather() {
		return weather;
	}
	
	public void setWeather(String weather) {
		this.weather = weather;
	}
	
	public int getTotal_runners() {
		return total_runners;
	}
	
	public void setTotal_runners(int total_runners) {
		this.total_runners = total_runners;
	}
	
	public int getMale_runners() {
		return male_runners;
	}
	
	public void setMale_runners(int male_runners) {
		this.male_runners = male_runners;
	}
	
	public int getFemale_runners() {
		return female_runners;
	}
	
	public void setFemale_runners(int female_runners) {
		this.female_runners = female_runners;
	}
	
	public int getEnjoyment_rating() {
		return enjoyment_rating;
	}
	
	public void setEnjoyment_rating(int enjoyment_rating) {
		this.enjoyment_rating = enjoyment_rating;
	}
	
	public int getFeel_meter_rating() {
		return feel_meter_rating;
	}
	
	public void setFeel_meter_rating(int feel_meter_rating) {
		this.feel_meter_rating = feel_meter_rating;
	}
	
	public int getToughness_rating() {
		return toughness_rating;
	}
	
	public void setToughness_rating(int toughness_rating) {
		this.toughness_rating = toughness_rating;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public List<String> getUserGallery() {
		return userGallery;
	}
	
	public void setUserGallery(List<String> userGallery) {
		this.userGallery = userGallery;
	}
	
	public List<String> getRace_terrain() {
		return race_terrain;
	}
	
	public void setRace_terrain(List<String> race_terrain) {
		this.race_terrain = race_terrain;
	}
	
	public int getShoes_profile_id() {
		return shoes_profile_id;
	}
	
	public void setShoes_profile_id(int shoes_profile_id) {
		this.shoes_profile_id = shoes_profile_id;
	}
	
	public UserRace getUser_race() {
		return user_race;
	}
	
	public void setUser_race(UserRace user_race) {
		this.user_race = user_race;
	}
	
	
	@Override
 	public String toString(){
		return 
			"AddRaceSendModel{" +
			",unit = '" + unit + '\'' + 
			",user_gallery = '" + userGallery + '\'' + 
			",address = '" +street_address+ '\'' +
			",distance = '" + distance + '\'' + 
			",race_name = '" + raceName + '\'' + 
			",city = '" + city + '\'' +
			",race_terrain = '" + race_terrain + '\'' +
			",race_result = '" + user_race + '\'' +
			",token = '" + token + '\'' + 
			"}";
		}
}