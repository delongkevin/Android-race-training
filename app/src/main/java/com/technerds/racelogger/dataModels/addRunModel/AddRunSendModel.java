package com.technerds.racelogger.dataModels.addRunModel;

import com.google.gson.annotations.SerializedName;
import com.technerds.racelogger.dataModels.addRaceModel.UserRace;
import com.technerds.racelogger.dataModels.addRaceModel.UserShoes;

import java.util.List;

public class AddRunSendModel {
	
	@SerializedName("token")
	private String token;
	
	@SerializedName("training_route")
	private String training_route;
	
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
	
	
	
	@SerializedName("enjoyment_rating")
	private int enjoyment_rating;
	
	@SerializedName("feel_meter_rating")
	private int feel_meter_rating;
	
	@SerializedName("toughness_rating")
	private int toughness_rating;
	
	
	
	@SerializedName("user_run")
	private UserRun user_run;
	
	@SerializedName("shoes_profile_id")
	private int shoes_profile_id;
	
	@SerializedName("run_terrain")
	private List<String> run_terrain;
	
	@SerializedName("user_gallery")
	private List<String> userGallery;
	
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getTraining_route() {
		return training_route;
	}
	
	public void setTraining_route(String training_route) {
		this.training_route = training_route;
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
	
	
	
	public List<String> getUserGallery() {
		return userGallery;
	}
	
	public void setUserGallery(List<String> userGallery) {
		this.userGallery = userGallery;
	}
	
	public List<String> getRun_terrain() {
		return run_terrain;
	}
	
	public void setRun_terrain(List<String> race_terrain) {
		this.run_terrain = race_terrain;
	}
	
	public int getShoes_profile_id() {
		return shoes_profile_id;
	}
	
	public void setShoes_profile_id(int shoes_profile_id) {
		this.shoes_profile_id = shoes_profile_id;
	}
	
	public UserRun getUser_run() {
		return user_run;
	}
	
	public void setUser_run(UserRun user_run) {
		this.user_run = user_run;
	}
	
	@Override
	public String toString(){
		return
				"AddRaceSendModel{" +
						",user_gallery = '" + userGallery + '\'' +
						",address = '" +street_address+ '\'' +
						",city = '" + city + '\'' +
						",race_terrain = '" + run_terrain + '\'' +
						",token = '" + token + '\'' +
						"}";
	}
}
