package com.technerds.racelogger.dataModels.graphs.getRaceParticipantsModel;

import com.google.gson.annotations.SerializedName;

public class UserRaceItem{

	@SerializedName("date")
	private String date;

	@SerializedName("hour")
	private String hour;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("finish_place")
	private String finishPlace;

	@SerializedName("age_group")
	private String ageGroup;

	@SerializedName("race_id")
	private String raceId;

	@SerializedName("id")
	private int id;

	@SerializedName("user")
	private User user;

	@SerializedName("minute")
	private String minute;

	@SerializedName("second")
	private String second;

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setHour(String hour){
		this.hour = hour;
	}

	public String getHour(){
		return hour;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setFinishPlace(String finishPlace){
		this.finishPlace = finishPlace;
	}

	public String getFinishPlace(){
		return finishPlace;
	}

	public void setAgeGroup(String ageGroup){
		this.ageGroup = ageGroup;
	}

	public String getAgeGroup(){
		return ageGroup;
	}

	public void setRaceId(String raceId){
		this.raceId = raceId;
	}

	public String getRaceId(){
		return raceId;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
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
			"UserRaceItem{" + 
			"date = '" + date + '\'' + 
			",hour = '" + hour + '\'' + 
			",user_id = '" + userId + '\'' + 
			",finish_place = '" + finishPlace + '\'' + 
			",age_group = '" + ageGroup + '\'' + 
			",race_id = '" + raceId + '\'' + 
			",id = '" + id + '\'' + 
			",user = '" + user + '\'' + 
			",minute = '" + minute + '\'' + 
			",second = '" + second + '\'' + 
			"}";
		}
}