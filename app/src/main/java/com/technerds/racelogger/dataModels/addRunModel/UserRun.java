package com.technerds.racelogger.dataModels.addRunModel;

import com.google.gson.annotations.SerializedName;

public class UserRun {
	
	@SerializedName("hour")
	private int hour;
	
	@SerializedName("minute")
	private int minute;
	
	@SerializedName("second")
	private int second;
	
	@SerializedName("date")
	private String date;
	
	@SerializedName("notes")
	private String notes;
	
	@SerializedName("course_type")
	private String course_type;
	
	@SerializedName("temperature")
	private String temperature;
	
	@SerializedName("temperature_unit")
	private String temperature_unit;
	
	@SerializedName("weather")
	private String weather;
	
	@SerializedName("distance")
	private double distance;
	
	@SerializedName("unit")
	private String unit;
	
	
	
	public void setHour(int hour){
		this.hour = hour;
	}
	
	public int getHour(){
		return hour;
	}
	
	public void setMinute(int minute){
		this.minute = minute;
	}
	
	public int getMinute(){
		return minute;
	}
	
	public void setSecond(int second){
		this.second = second;
	}
	
	public int getSecond(){
		return second;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public String getCourse_type() {
		return course_type;
	}
	
	public void setCourse_type(String course_type) {
		this.course_type = course_type;
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
	
	public String getWeather() {
		return weather;
	}
	
	public void setWeather(String weather) {
		this.weather = weather;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
}
