package com.technerds.racelogger.dataModels.raceListingModel;

import com.google.gson.annotations.SerializedName;

public class RaceListingModel{

	@SerializedName("data")
	private Data data;

	@SerializedName("message")
	private String message;

	@SerializedName("error")
	private String error;

	@SerializedName("status")
	private int status;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
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
			"RaceListingModel{" + 
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",error = '" + error + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}