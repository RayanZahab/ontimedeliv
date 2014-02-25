package com.example.ontimedeliv;

public class Address {
	private Integer id;
	private String country;
	private String city;
	private String area;
	private String building;
	private String floor;
	private String street;
	private String details;
	private Integer customer_id;
	private String longitude;
	private String latitude;

	public Address(int id, String country, String city, String area,
			String building, String floor, String street, String details,
			int customer_id, String longitude, String latitude) {
		this.id = id;
		this.country = country;
		this.city = city;
		this.area = area;
		this.building = building;
		this.floor = floor;
		this.street = street;
		this.details = details;
		this.customer_id = customer_id;
		this.longitude = longitude;
		this.latitude = latitude;

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Integer getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(Integer customer_id) {
		this.customer_id = customer_id;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setAddress_long(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

}
