package com.technerds.racelogger.dataModels.graphs.graphByFirstPlaces;

import com.google.gson.annotations.SerializedName;

public class FirstPlacesDataItem {

	@SerializedName("hours")
	private String hours;

	@SerializedName("race_name")
	private String raceName;

	@SerializedName("race_id")
	private int raceId;

	@SerializedName("minute")
	private String minute;

	@SerializedName("second")
	private String second;

	public void setHours(String hours){
		this.hours = hours;
	}

	public String getHours(){
		return hours;
	}

	public void setRaceName(String raceName){
		this.raceName = raceName;
	}

	public String getRaceName(){
		return raceName;
	}

	public void setRaceId(int raceId){
		this.raceId = raceId;
	}

	public int getRaceId(){
		return raceId;
	}

	public void setMinute(String minute){
		this.minute = minute;
	}

	public String getMinute(){
		return minute;
	}

	public void setSecond(String second){
		this.second = second;
	}

	public String getSecond(){
		return second;
	}

	@Override
 	public String toString(){
		return 
			"RaceItem{" + 
			"hours = '" + hours + '\'' + 
			",race_name = '" + raceName + '\'' + 
			",race_id = '" + raceId + '\'' + 
			",minute = '" + minute + '\'' + 
			",second = '" + second + '\'' + 
			"}";
		}
}