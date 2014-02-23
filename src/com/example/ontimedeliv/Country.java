package com.example.ontimedeliv;

import java.util.ArrayList;

public class Country {
	private Integer id;
	private String name;
	
	public Country(Integer id, String name)
	{
		this.setId(id);
		this.setName(name);
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return id + " - " + name;
	}
	public ArrayList<City> getCities(ArrayList<City> cities)
	{
		ArrayList<City> currentCities = new ArrayList<City>();
		for(int i =0;i<cities.size();i++)
		{
			if(cities.get(i).getCountry_id()==id)
			{
				currentCities.add(cities.get(i));
			}				
		}
		return currentCities;		
	}

}
