package com.technerds.racelogger.dataModels.runDetailModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.technerds.racelogger.dataModels.raceDetailModel.UserGalleryItem;

public class Data{

	@SerializedName("date")
	private String date;

	@SerializedName("feel_meter_rating")
	private FeelMeterRating feelMeterRating;

	@SerializedName("user_gallery")
	private List<UserGalleryItem> userGallery;

	@SerializedName("run_id")
	private String runId;

	@SerializedName("notes")
	private String notes;

	@SerializedName("user_shoe")
	private UserShoe userShoe;

	@SerializedName("run_terrain")
	private List<RunTerrainItem> runTerrain;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("run")
	private Run run;
	
	@SerializedName("temperature")
	private String temperature;
	
	@SerializedName("temperature_unit")
	private String temperature_unit;

	@SerializedName("toughness_rating")
	private ToughnessRating toughnessRating;

	@SerializedName("minute")
	private String minute;

	@SerializedName("second")
	private String second;

	@SerializedName("course_type")
	private String courseType;

	@SerializedName("enjoyment_rating")
	private EnjoymentRating enjoymentRating;

	@SerializedName("hour")
	private String hour;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("weather")
	private String weather;

	@SerializedName("id")
	private int id;
	
	@SerializedName("unit")
	private String unit;
	
	@SerializedName("distance")
	private String distance;
	
	@SerializedName("user_distance")
	private String user_distance;

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setFeelMeterRating(FeelMeterRating feelMeterRating){
		this.feelMeterRating = feelMeterRating;
	}

	public FeelMeterRating getFeelMeterRating(){
		return feelMeterRating;
	}

	public void setUserGallery(List<UserGalleryItem> userGallery){
		this.userGallery = userGallery;
	}

	public List<UserGalleryItem> getUserGallery(){
		return userGallery;
	}

	public void setRunId(String runId){
		this.runId = runId;
	}

	public String getRunId(){
		return runId;
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

	public void setRunTerrain(List<RunTerrainItem> runTerrain){
		this.runTerrain = runTerrain;
	}

	public List<RunTerrainItem> getRunTerrain(){
		return runTerrain;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setRun(Run run){
		this.run = run;
	}

	public Run getRun(){
		return run;
	}
	
	public String getTemperature() {
		return temperature;
	}
	
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	
	public String getTemperature_unit() {
		return temperature_unit;
	}
	
	public void setTemperature_unit(String temperature_unit) {
		this.temperature_unit = temperature_unit;
	}
	
	public void setToughnessRating(ToughnessRating toughnessRating){
		this.toughnessRating = toughnessRating;
	}

	public ToughnessRating getToughnessRating(){
		return toughnessRating;
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

	public void setCourseType(String courseType){
		this.courseType = courseType;
	}

	public String getCourseType(){
		return courseType;
	}

	public void setEnjoymentRating(EnjoymentRating enjoymentRating){
		this.enjoymentRating = enjoymentRating;
	}

	public EnjoymentRating getEnjoymentRating(){
		return enjoymentRating;
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

	public void setWeather(String weather){
		this.weather = weather;
	}

	public String getWeather(){
		return weather;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String getDistance() {
		return distance;
	}
	
	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	public String getUser_distance() {
		return user_distance;
	}
	
	public void setUser_distance(String user_distance) {
		this.user_distance = user_distance;
	}
	
	@Override
 	public String toString(){
		return 
			"Data{" + 
			"date = '" + date + '\'' + 
			",feel_meter_rating = '" + feelMeterRating + '\'' + 
			",user_gallery = '" + userGallery + '\'' + 
			",run_id = '" + runId + '\'' + 
			",notes = '" + notes + '\'' + 
			",user_shoe = '" + userShoe + '\'' + 
			",run_terrain = '" + runTerrain + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",run = '" + run + '\'' +
			",toughness_rating = '" + toughnessRating + '\'' + 
			",minute = '" + minute + '\'' + 
			",second = '" + second + '\'' + 
			",course_type = '" + courseType + '\'' + 
			",enjoyment_rating = '" + enjoymentRating + '\'' + 
			",hour = '" + hour + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",user_id = '" + userId + '\'' + 
			",weather = '" + weather + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}