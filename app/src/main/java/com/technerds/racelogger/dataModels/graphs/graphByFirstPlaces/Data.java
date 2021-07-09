package com.technerds.racelogger.dataModels.graphs.graphByFirstPlaces;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("race")
	private List<FirstPlacesDataItem> race;

	public void setRace(List<FirstPlacesDataItem> race){
		this.race = race;
	}

	public List<FirstPlacesDataItem> getRace(){
		return race;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"race = '" + race + '\'' + 
			"}";
		}
}