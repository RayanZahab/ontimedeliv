package com.example.ontimedeliv;

import android.graphics.Bitmap;

/**
 * 
 * @author manish.s
 *
 */

public class Item {
	Bitmap image;
	String title;
	boolean selected = false;
	String code = null;
	
	public Item(Bitmap image, String title) {
		super();
		this.image = image;
		this.title = title;
	}
	public Item (String title){
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
	

}
