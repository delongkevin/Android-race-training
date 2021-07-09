package com.technerds.racelogger.dataModels.raceDetailModel;

import com.google.gson.annotations.SerializedName;

public class RaceTerrainItem{

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("race_id")
	private String raceId;

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

	public void setRaceId(String raceId){
		this.raceId = raceId;
	}

	public String getRaceId(){
		return raceId;
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
			"RaceTerrainItem{" + 
			"updated_at = '" + updatedAt + '\'' + 
			",race_id = '" + raceId + '\'' + 
			",name = '" + name + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}