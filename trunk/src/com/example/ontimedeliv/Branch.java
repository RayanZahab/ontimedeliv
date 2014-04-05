package com.example.ontimedeliv;

import android.util.Log;

public class Branch {
	private Integer id;
	private String name;
	private String description;
	private Area area;
	private String address;
	private String estimation_time;
	private int is_available;
	private Shop shop;
	private String longitude;
	private String latitude;
	private int open_hour;
	private int close_hour;

	public Branch(int id, String name, String description, Area area,
			String address, int is_available, Shop shop, String longitude,
			String latitude, int open_hour, int close_hour,
			String estimation_time) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.setArea(area);
		this.address = address;
		this.is_available = is_available;
		this.setShop(shop);
		this.longitude = longitude;
		this.latitude = latitude;
		this.open_hour = open_hour;
		this.close_hour = close_hour;
		this.estimation_time = estimation_time;
	}
	public ValidationError validate()
	{
		boolean valid = false;
		String msg="";
		if(this.name.isEmpty() || this.name.length()<4)
		{
			msg="Invalid name";
		}
		else if(this.address.isEmpty() || this.address.length()<4)
		{
			msg="Invalid address";
		}
		else if(this.area.getId()==0)
		{
			msg="Invalid area";
		}
		else if(this.estimation_time.isEmpty() || this.estimation_time.length()<2)
		{
			msg="Invalid estimation time";
		}
		else
		{
			valid =  true;
		}
		return new ValidationError(valid,msg);
	}
	public Branch(int id)
	{
		this.id = id;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getIs_available() {
		return is_available;
	}

	public void setIs_available(int is_available) {
		this.is_available = is_available;
	}

	public int getClose_hour() {
		return close_hour;
	}

	public void setClose_hour(int close_hour) {
		this.close_hour = close_hour;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public int getOpen_hour() {
		return open_hour;
	}

	public void setOpen_hour(int open_hour) {
		this.open_hour = open_hour;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getEstimation_time() {
		return estimation_time;
	}

	public void setEstimation_time(String estimation_time) {
		this.estimation_time = estimation_time;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public String toString() {
		return this.name + "\n" + this.area.toString() + ","
				+ this.address;
	}
	@Override
	public boolean equals(Object obj) {
		Branch c = (Branch) obj;
		Log.d("br","br : "+ this.id +"=="+ c.getId());
		if(this.id == c.getId())
			return true;
		return false;
	}
}
