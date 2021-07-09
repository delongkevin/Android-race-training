package com.technerds.racelogger.dataModels.runDetailModel;

import com.google.gson.annotations.SerializedName;

public class UserShoe{
	

	@SerializedName("id")
	private int id;
	
	@SerializedName("user_run_id")
	private String user_run_id;
	
	@SerializedName("shoes_profile_id")
	private String shoes_profile_id;
	
	@SerializedName("created_at")
	private String created_at;
	
	@SerializedName("updated_at")
	private String updated_at;
	
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUser_run_id() {
		return user_run_id;
	}
	
	public void setUser_run_id(String user_run_id) {
		this.user_run_id = user_run_id;
	}
	
	public String getShoes_profile_id() {
		return shoes_profile_id;
	}
	
	public void setShoes_profile_id(String shoes_profile_id) {
		this.shoes_profile_id = shoes_profile_id;
	}
	
	public String getCreated_at() {
		return created_at;
	}
	
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	
	public String getUpdated_at() {
		return updated_at;
	}
	
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
}