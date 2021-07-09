package com.technerds.racelogger.dataModels.graphs.graphByWeek;

import com.google.gson.annotations.SerializedName;

public class GraphByWeekDataItem {

	@SerializedName("distance_mile")
	private String distanceMile;

	@SerializedName("week")
	private int week;

	@SerializedName("distance_km")
	private String distanceKm;

	@SerializedName("count")
	private String count;

	public void setDistanceMile(String distanceMile){
		this.distanceMile = distanceMile;
	}

	public String getDistanceMile(){
		return distanceMile;
	}

	public void setWeek(int week){
		this.week = week;
	}

	public int getWeek(){
		return week;
	}

	public void setDistanceKm(String distanceKm){
		this.distanceKm = distanceKm;
	}

	public String getDistanceKm(){
		return distanceKm;
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
			",week = '" + week + '\'' + 
			",distance_km = '" + distanceKm + '\'' + 
			",count = '" + count + '\'' + 
			"}";
		}
}