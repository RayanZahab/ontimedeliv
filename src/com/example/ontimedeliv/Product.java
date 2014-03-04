package com.example.ontimedeliv;

public class Product {
	private int id,shop_id;
	private String description,price,name;
	private Photo photo;
	private Category category;
	private Unit unit;
	private boolean available;
	public Product(int id, String price,String name, String description, Photo photo, Category category, Unit unit, boolean is_available,int shop_id)
	{
		this.id=id;
		this.price=price;
		this.name=name;
		this.description=description;
		this.photo=photo;
		this.category=category;
		this.unit=unit;
		this.setAvailable(is_available);
		this.setShop_id(shop_id);
		
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public Photo getPhoto() {
		return photo;
	}
	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	public Unit getUnit() {
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}	
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public String toString()
	{
		return this.name+ " - "+this.price + "\n"+this.description;
	}
	public int getShop_id() {
		return shop_id;
	}
	public void setShop_id(int shop_id) {
		this.shop_id = shop_id;
	}

}
