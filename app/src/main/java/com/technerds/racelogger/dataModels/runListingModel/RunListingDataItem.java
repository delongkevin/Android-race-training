package com.technerds.racelogger.dataModels.runListingModel;

import com.google.gson.annotations.SerializedName;

public class RunListingDataItem {

	@SerializedName("date")
	private String date;

	@SerializedName("street_address")
	private String streetAddress;

	@SerializedName("unit")
	private String unit;

	@SerializedName("run_id")
	private int runId;

	@SerializedName("distance")
	private String distance;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("id")
	private int id;

	@SerializedName("training_route")
	private String trainingRoute;

	@SerializedName("park_name")
	private String parkName;
	
	@SerializedName("user_distance")
	private String user_distance;

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setStreetAddress(String streetAddress){
		this.streetAddress = streetAddress;
	}

	public String getStreetAddress(){
		return streetAddress;
	}

	public void setUnit(String unit){
		this.unit = unit;
	}

	public String getUnit(){
		return unit;
	}

	public void setRunId(int runId){
		this.runId = runId;
	}

	public int getRunId(){
		return runId;
	}

	public void setDistance(String distance){
		this.distance = distance;
	}

	public String getDistance(){
		return distance;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
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

	public void setParkName(String parkName){
		this.parkName = parkName;
	}

	public String getParkName(){
		return parkName;
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
			"date = '" + date + '\'' + 
			",street_address = '" + streetAddress + '\'' + 
			",unit = '" + unit + '\'' + 
			",run_id = '" + runId + '\'' + 
			",distance = '" + distance + '\'' + 
			",user_id = '" + userId + '\'' + 
			",id = '" + id + '\'' + 
			",training_route = '" + trainingRoute + '\'' + 
			",park_name = '" + parkName + '\'' + 
			"}";
		}
}