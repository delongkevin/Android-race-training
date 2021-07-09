package com.technerds.racelogger.dataModels.graphs.graphByYear;

import com.google.gson.annotations.SerializedName;

public class GraphByYearDataItem {

	@SerializedName("distance_mile")
	private String distanceMile;

	@SerializedName("distance_km")
	private String distanceKm;

	@SerializedName("year")
	private String year;

	@SerializedName("count")
	private String count;

	public void setDistanceMile(String distanceMile){
		this.distanceMile = distanceMile;
	}

	public String getDistanceMile(){
		return distanceMile;
	}

	public void setDistanceKm(String distanceKm){
		this.distanceKm = distanceKm;
	}

	public String getDistanceKm(){
		return distanceKm;
	}

	public void setYear(String year){
		this.year = year;
	}

	public String getYear(){
		return year;
	}

	public void setCount(String count){
		this.count = count;
	}

	public String getCount(){
		return count;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"distance_mile = '" + distanceMile + '\'' + 
			",distance_km = '" + distanceKm + '\'' + 
			",year = '" + year + '\'' + 
			",count = '" + count + '\'' + 
			"}";
		}
}