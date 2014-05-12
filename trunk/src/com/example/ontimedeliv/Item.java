package com.example.ontimedeliv;

import android.graphics.Bitmap;

public class Item {
	private String image;
	private String title,date;
	boolean selected = false;
	String code = null;
	String address;
	boolean isNew = false;
	private int id, img;
	private int quantity;
	private double price;
	private String method,orderStatus;
	
	public Item(){}
	
	public Item(int id,String title, Integer quantity, double price) {
		super();
		this.id= id;
		this.title = title;
		this.setQuantity(quantity);
		this.setPrice(price);
	}
	public Item(int id, int img, String title) {
		super();
		this.setImg(img);
		this.title = title;
		this.id = id;
	}

	public Item(int id, String title, Integer quantity, double price, boolean status) {
		super();
		this.title = title;
		this.isNew = status;
		this.id = id;
		this.setQuantity(quantity);
		this.setPrice(price);
	}

	public Item(int id, String image, String title) {
		super();
		this.image = image;
		this.title = title;
		this.id = id;
	}

	public Item(int id, String image, String title, boolean selected) {
		super();
		this.image = image;
		this.title = title;
		this.id = id;
		this.selected = selected;
	}

	public Item(int id, int price, String title, boolean selected) {
		super();
		this.price = price;
		this.title = title;
		this.id = id;
		this.selected = selected;
	}

	public Item(String title) {
		super();
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public boolean getStatus() {
		return isNew;
	}

	public String getAddress() {
		return address;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setName(String name) {
		this.title = name;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	public int getImg() {
		return img;
	}
	public void setImg(int img) {
		this.img = img;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	public String toString()
	{
		return this.title;
	}

}
