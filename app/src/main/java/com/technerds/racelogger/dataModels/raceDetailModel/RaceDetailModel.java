package com.technerds.racelogger.dataModels.raceDetailModel;

import com.google.gson.annotations.SerializedName;

public class RaceDetailModel{

	@SerializedName("data")
	private RaceData raceData;

	@SerializedName("message")
	private String message;

	@SerializedName("error")
	private String error;

	@SerializedName("status")
	private int status;

	public void setRaceData(RaceData raceData){
		this.raceData = raceData;
	}

	public RaceData getRaceData(){
		return raceData;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setError(String error){
		this.error = error;
	}

	public String getError(){
		return error;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"RaceDetailModel{" + 
			"data = '" + raceData + '\'' +
			",message = '" + message + '\'' + 
			",error = '" + error + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}