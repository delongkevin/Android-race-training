package com.technerds.racelogger.dataModels.dropDownModels;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("course_type")
	private List<CourseTypeItem> courseType;

	@SerializedName("distance")
	private List<DistanceItem> distance;

	@SerializedName("weather")
	private List<WeatherItem> weather;

	@SerializedName("temperature_C")
	private List<TemperatureCItem> temperatureC;

	@SerializedName("temperature_F")
	private List<TemperatureFItem> temperatureF;

	@SerializedName("shoes")
	private List<ShoesItem> shoes;

	@SerializedName("terrain")
	private List<TerrainItem> terrain;

	public void setCourseType(List<CourseTypeItem> courseType){
		this.courseType = courseType;
	}

	public List<CourseTypeItem> getCourseType(){
		return courseType;
	}

	public void setDistance(List<DistanceItem> distance){
		this.distance = distance;
	}

	public List<DistanceItem> getDistance(){
		return distance;
	}

	public void setWeather(List<WeatherItem> weather){
		this.weather = weather;
	}

	public List<WeatherItem> getWeather(){
		return weather;
	}

	public void setTemperatureC(List<TemperatureCItem> temperatureC){
		this.temperatureC = temperatureC;
	}

	public List<TemperatureCItem> getTemperatureC(){
		return temperatureC;
	}

	public void setTemperatureF(List<TemperatureFItem> temperatureF){
		this.temperatureF = temperatureF;
	}

	public List<TemperatureFItem> getTemperatureF(){
		return temperatureF;
	}

	public void setShoes(List<ShoesItem> shoes){
		this.shoes = shoes;
	}

	public List<ShoesItem> getShoes(){
		return shoes;
	}

	public void setTerrain(List<TerrainItem> terrain){
		this.terrain = terrain;
	}

	public List<TerrainItem> getTerrain(){
		return terrain;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"course_type = '" + courseType + '\'' + 
			",distance = '" + distance + '\'' + 
			",weather = '" + weather + '\'' + 
			",temperature_C = '" + temperatureC + '\'' + 
			",temperature_F = '" + temperatureF + '\'' + 
			",shoes = '" + shoes + '\'' + 
			",terrain = '" + terrain + '\'' + 
			"}";
		}
}