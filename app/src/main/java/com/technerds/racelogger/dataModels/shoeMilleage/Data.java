package com.technerds.racelogger.dataModels.shoeMilleage;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("unit")
	private String unit;

	@SerializedName("shoe_mileage")
	private String shoe_mileage;

	public void setUnit(String unit){
		this.unit = unit;
	}

	public String getUnit(){
		return unit;
	}

	public void setShoeMileage(String shoeMileage){
		this.shoe_mileage = shoeMileage;
	}

	public String getShoeMileage(){
		return shoe_mileage;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"unit = '" + unit + '\'' + 
			",shoe_mileage = '" + shoe_mileage + '\'' +
			"}";
		}
}