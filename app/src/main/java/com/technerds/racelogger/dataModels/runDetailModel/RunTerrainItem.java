package com.technerds.racelogger.dataModels.runDetailModel;

import com.google.gson.annotations.SerializedName;

public class RunTerrainItem{

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_run_id")
	private String userRunId;

	@SerializedName("name")
	private String name;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUserRunId(String userRunId){
		this.userRunId = userRunId;
	}

	public String getUserRunId(){
		return userRunId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"RunTerrainItem{" + 
			"updated_at = '" + updatedAt + '\'' + 
			",user_run_id = '" + userRunId + '\'' + 
			",name = '" + name + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}