package com.technerds.racelogger.dataModels.graphs.getRaceParticipantsModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("street_address")
	private String streetAddress;

	@SerializedName("country")
	private String country;

	@SerializedName("race_name")
	private String raceName;

	@SerializedName("city")
	private String city;

	@SerializedName("id")
	private int id;

	@SerializedName("state")
	private String state;

	@SerializedName("park_name")
	private String parkName;

	@SerializedName("user_race")
	private List<UserRaceItem> userRace;

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

	public void setParkName(String parkName){
		this.parkName = parkName;
	}

	public String getParkName(){
		return parkName;
	}

	public void setUserRace(List<UserRaceItem> userRace){
		this.userRace = userRace;
	}

	public List<UserRaceItem> getUserRace(){
		return userRace;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"street_address = '" + streetAddress + '\'' + 
			",country = '" + country + '\'' + 
			",race_name = '" + raceName + '\'' + 
			",city = '" + city + '\'' + 
			",id = '" + id + '\'' + 
			",state = '" + state + '\'' + 
			",park_name = '" + parkName + '\'' + 
			",user_race = '" + userRace + '\'' + 
			"}";
		}
}