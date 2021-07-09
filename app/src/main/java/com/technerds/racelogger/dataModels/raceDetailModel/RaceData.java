package com.technerds.racelogger.dataModels.raceDetailModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RaceData {

	@SerializedName("date")
	private String date;

	@SerializedName("user_gallery")
	private List<UserGalleryItem> userGallery;

	@SerializedName("notes")
	private String notes;

	@SerializedName("user_shoe")
	private UserShoe userShoe;

	@SerializedName("race")
	private Race race;

	@SerializedName("finish_place")
	private String finishPlace;

	@SerializedName("age_group")
	private String ageGroup;

	@SerializedName("race_id")
	private String raceId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("age_group_runner")
	private String ageGroupRunner;

	@SerializedName("minute")
	private String minute;

	@SerializedName("second")
	private String second;

	@SerializedName("hour")
	private String hour;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("finish_group_place")
	private String finishGroupPlace;

	@SerializedName("id")
	private int id;

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setUserGallery(List<UserGalleryItem> userGallery){
		this.userGallery = userGallery;
	}

	public List<UserGalleryItem> getUserGallery(){
		return userGallery;
	}

	public void setNotes(String notes){
		this.notes = notes;
	}

	public String getNotes(){
		return notes;
	}

	public void setUserShoe(UserShoe userShoe){
		this.userShoe = userShoe;
	}

	public UserShoe getUserShoe(){
		return userShoe;
	}

	public void setRace(Race race){
		this.race = race;
	}

	public Race getRace(){
		return race;
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

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setAgeGroupRunner(String ageGroupRunner){
		this.ageGroupRunner = ageGroupRunner;
	}

	public String getAgeGroupRunner(){
		return ageGroupRunner;
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

	public void setHour(String hour){
		this.hour = hour;
	}

	public String getHour(){
		return hour;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setFinishGroupPlace(String finishGroupPlace){
		this.finishGroupPlace = finishGroupPlace;
	}

	public String getFinishGroupPlace(){
		return finishGroupPlace;
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
			"Data{" + 
			"date = '" + date + '\'' + 
			",user_gallery = '" + userGallery + '\'' + 
			",notes = '" + notes + '\'' + 
			",user_shoe = '" + userShoe + '\'' + 
			",race = '" + race + '\'' + 
			",finish_place = '" + finishPlace + '\'' + 
			",age_group = '" + ageGroup + '\'' + 
			",race_id = '" + raceId + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",age_group_runner = '" + ageGroupRunner + '\'' + 
			",minute = '" + minute + '\'' + 
			",second = '" + second + '\'' + 
			",hour = '" + hour + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",user_id = '" + userId + '\'' + 
			",finish_group_place = '" + finishGroupPlace + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}