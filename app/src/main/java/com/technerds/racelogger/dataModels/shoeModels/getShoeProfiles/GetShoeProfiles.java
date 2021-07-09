package com.technerds.racelogger.dataModels.shoeModels.getShoeProfiles;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetShoeProfiles{

	@SerializedName("data")
	private List<ShoeProfileDataItem> data;

	@SerializedName("message")
	private String message;

	@SerializedName("error")
	private String error;

	@SerializedName("status")
	private int status;

	public void setData(List<ShoeProfileDataItem> data){
		this.data = data;
	}

	public List<ShoeProfileDataItem> getData(){
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
			"GetShoeProfiles{" + 
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",error = '" + error + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}