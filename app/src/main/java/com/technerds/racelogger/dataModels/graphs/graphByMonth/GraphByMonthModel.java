package com.technerds.racelogger.dataModels.graphs.graphByMonth;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GraphByMonthModel{

	@SerializedName("data")
	private List<GraphByMonthDataItem> data;

	@SerializedName("message")
	private String message;

	@SerializedName("error")
	private String error;

	@SerializedName("status")
	private int status;

	public void setData(List<GraphByMonthDataItem> data){
		this.data = data;
	}

	public List<GraphByMonthDataItem> getData(){
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
			"GraphByMonthModel{" + 
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",error = '" + error + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}