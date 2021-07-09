package com.technerds.racelogger.dataModels.dashboardModel;

import com.google.gson.annotations.SerializedName;

public class NextRace{

	@SerializedName("date")
	private String date;

	@SerializedName("street_address")
	private String streetAddress;

	@SerializedName("notes")
	private String notes;

	@SerializedName("distance")
	private double distance;

	@SerializedName("id")
	private int id;

	@SerializedName("rae_name")
	private String raeName;

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

	public void setNotes(String notes){
		this.notes = notes;
	}

	public String getNotes(){
		return notes;
	}

	public void setDistance(double distance){
		this.distance = distance;
	}

	public double getDistance(){
		return distance;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setRaeName(String raeName){
		this.raeName = raeName;
	}

	public String getRaeName(){
		return raeName;
	}

	@Override
 	public String toString(){
		return 
			"NextRace{" + 
			"date = '" + date + '\'' + 
			",street_address = '" + streetAddress + '\'' + 
			",notes = '" + notes + '\'' + 
			",distance = '" + distance + '\'' + 
			",id = '" + id + '\'' + 
			",rae_name = '" + raeName + '\'' + 
			"}";
		}
}