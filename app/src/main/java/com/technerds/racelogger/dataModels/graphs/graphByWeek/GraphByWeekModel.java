package com.technerds.racelogger.dataModels.graphs.graphByWeek;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GraphByWeekModel{

	@SerializedName("data")
	private List<GraphByWeekDataItem> data;

	@SerializedName("message")
	private String message;

	@SerializedName("error")
	private String error;

	@SerializedName("status")
	private int status;

	public void setData(List<GraphByWeekDataItem> data){
		this.data = data;
	}

	public List<GraphByWeekDataItem> getData(){
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
			"GraphByWeekModel{" + 
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",error = '" + error + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}