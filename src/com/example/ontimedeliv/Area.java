package com.example.ontimedeliv;

public class Area {
	private Integer id;
	private String name;
	private Integer city_id;

	public Area(int id)
	{
		this.id=id;
	}
	public Area(int id, int city_id,String name) {
		this.id = id;
		this.name = name;
		this.city_id = city_id;
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

	public Integer getCity_id() {
		return city_id;
	}

	public void setCity_id(Integer city_id) {
		this.city_id = city_id;
	}

	public String toString() {
		return id + " - " + city_id + " - " + name;
	}
}
