package com.technerds.racelogger.dataModels.dashboardModel;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("next_race")
	private NextRace nextRace;

	@SerializedName("race")
	private Race race;

	@SerializedName("run")
	private Run run;

	public void setNextRace(NextRace nextRace){
		this.nextRace = nextRace;
	}

	public NextRace getNextRace(){
		return nextRace;
	}

	public void setRace(Race race){
		this.race = race;
	}

	public Race getRace(){
		return race;
	}

	public void setRun(Run run){
		this.run = run;
	}

	public Run getRun(){
		return run;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"next_race = '" + nextRace + '\'' + 
			",race = '" + race + '\'' + 
			",run = '" + run + '\'' + 
			"}";
		}
}