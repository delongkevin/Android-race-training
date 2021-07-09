package com.technerds.racelogger.dataModels.graphs.graphByMonth;

import com.google.gson.annotations.SerializedName;

public class GraphByMonthDataItem {

	@SerializedName("distance_mile")
	private String distanceMile;

	@SerializedName("distance_km")
	private String distanceKm;

	@SerializedName("month")
	private String month;

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

	public void setMonth(String month){
		this.month = month;
	}

	public String getMonth(){
		return month;
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
			",month = '" + month + '\'' + 
			",count = '" + count + '\'' + 
			"}";
		}
}