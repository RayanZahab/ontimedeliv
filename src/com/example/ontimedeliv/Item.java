package com.example.ontimedeliv;

import android.graphics.Bitmap;

public class Item {
	private Bitmap image;
	private String title;
	boolean selected = false;
	String code = null;
	String address;
	boolean isNew = false;
	private int id, img;
	private int quantity;
	private double price;

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

	public Item(int id, Bitmap image, String title) {
		super();
		this.image = image;
		this.title = title;
		this.id = id;
	}

	public Item(int id, Bitmap image, String title, boolean selected) {
		super();
		this.image = image;
		this.title = title;
		this.id = id;
		this.selected = selected;
	}

	public Item(String title) {
		super();
		this.title = title;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
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

}