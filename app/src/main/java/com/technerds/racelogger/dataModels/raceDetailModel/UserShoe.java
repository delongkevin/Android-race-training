package com.technerds.racelogger.dataModels.raceDetailModel;

import com.google.gson.annotations.SerializedName;

public class UserShoe{

	@SerializedName("shoes_profile_id")
	private String shoesProfileId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("user_race_id")
	private String userRaceId;

	@SerializedName("shoe_profile")
	private ShoeProfile shoeProfile;

	public void setShoesProfileId(String shoesProfileId){
		this.shoesProfileId = shoesProfileId;
	}

	public String getShoesProfileId(){
		return shoesProfileId;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
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

	public void setUserRaceId(String userRaceId){
		this.userRaceId = userRaceId;
	}

	public String getUserRaceId(){
		return userRaceId;
	}

	public void setShoeProfile(ShoeProfile shoeProfile){
		this.shoeProfile = shoeProfile;
	}

	public ShoeProfile getShoeProfile(){
		return shoeProfile;
	}

	@Override
 	public String toString(){
		return 
			"UserShoe{" + 
			"shoes_profile_id = '" + shoesProfileId + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",user_race_id = '" + userRaceId + '\'' + 
			",shoe_profile = '" + shoeProfile + '\'' + 
			"}";
		}
}