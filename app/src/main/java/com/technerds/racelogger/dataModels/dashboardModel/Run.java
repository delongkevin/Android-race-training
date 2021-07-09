package com.technerds.racelogger.dataModels.dashboardModel;

import com.google.gson.annotations.SerializedName;

public class Run{

	@SerializedName("current_week_km")
	private double currentWeekKm;

	@SerializedName("today_miles")
	private double todayMiles;

	@SerializedName("current_month_km")
	private double currentMonthKm;

	@SerializedName("carrer_miles")
	private double carrerMiles;

	@SerializedName("current_month_miles")
	private double currentMonthMiles;

	@SerializedName("carrer_km")
	private double carrerKm;

	@SerializedName("current_year_miles")
	private double currentYearMiles;

	@SerializedName("total_run")
	private double totalRun;

	@SerializedName("current_week_miles")
	private double currentWeekMiles;

	public void setCurrentWeekKm(double currentWeekKm){
		this.currentWeekKm = currentWeekKm;
	}

	public double getCurrentWeekKm(){
		return currentWeekKm;
	}

	public void setTodayMiles(double todayMiles){
		this.todayMiles = todayMiles;
	}

	public double getTodayMiles(){
		return todayMiles;
	}

	public void setCurrentMonthKm(double currentMonthKm){
		this.currentMonthKm = currentMonthKm;
	}

	public double getCurrentMonthKm(){
		return currentMonthKm;
	}

	public void setCarrerMiles(double carrerMiles){
		this.carrerMiles = carrerMiles;
	}

	public double getCarrerMiles(){
		return carrerMiles;
	}

	public void setCurrentMonthMiles(double currentMonthMiles){
		this.currentMonthMiles = currentMonthMiles;
	}

	public double getCurrentMonthMiles(){
		return currentMonthMiles;
	}

	public void setCarrerKm(double carrerKm){
		this.carrerKm = carrerKm;
	}

	public double getCarrerKm(){
		return carrerKm;
	}

	public void setCurrentYearMiles(double currentYearMiles){
		this.currentYearMiles = currentYearMiles;
	}

	public double getCurrentYearMiles(){
		return currentYearMiles;
	}

	public void setTotalRun(double totalRun){
		this.totalRun = totalRun;
	}

	public double getTotalRun(){
		return totalRun;
	}

	public void setCurrentWeekMiles(double currentWeekMiles){
		this.currentWeekMiles = currentWeekMiles;
	}

	public double getCurrentWeekMiles(){
		return currentWeekMiles;
	}

	@Override
 	public String toString(){
		return 
			"Run{" + 
			"current_week_km = '" + currentWeekKm + '\'' + 
			",today_miles = '" + todayMiles + '\'' + 
			",current_month_km = '" + currentMonthKm + '\'' + 
			",carrer_miles = '" + carrerMiles + '\'' + 
			",current_month_miles = '" + currentMonthMiles + '\'' + 
			",carrer_km = '" + carrerKm + '\'' + 
			",current_year_miles = '" + currentYearMiles + '\'' + 
			",total_run = '" + totalRun + '\'' + 
			",current_week_miles = '" + currentWeekMiles + '\'' + 
			"}";
		}
}