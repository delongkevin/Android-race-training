package com.technerds.racelogger.dataModels.addRaceModel;

import com.google.gson.annotations.SerializedName;

public class UserRace{

	private int hour;
	
	private int minute;
	
	private int second;
	
	private int finish_place;
	
	private int finish_group_place;

	private int age_group;

	private int age_group_runner;
	
	private String date;
	
	private String notes;
	
	
	public int getHour() {
		return hour;
	}
	
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public void setMinute(int minute) {
		this.minute = minute;
	}
	
	public int getSecond() {
		return second;
	}
	
	public void setSecond(int second) {
		this.second = second;
	}
	
	public int getFinish_place() {
		return finish_place;
	}
	
	public void setFinish_place(int finish_place) {
		this.finish_place = finish_place;
	}
	
	public int getFinish_group_place() {
		return finish_group_place;
	}
	
	public void setFinish_group_place(int finish_group_place) {
		this.finish_group_place = finish_group_place;
	}
	
	public int getAge_group() {
		return age_group;
	}
	
	public void setAge_group(int age_group) {
		this.age_group = age_group;
	}
	
	public int getAge_group_runner() {
		return age_group_runner;
	}
	
	public void setAge_group_runner(int age_group_runner) {
		this.age_group_runner = age_group_runner;
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
}