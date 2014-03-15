package com.example.ontimedeliv;

import java.util.ArrayList;

public class Activate {

	private ArrayList<Integer> toUpdate;
	private String type;

	public Activate(String type, ArrayList<Integer> toUpdate) {
		this.setToUpdate(toUpdate);
		this.setType(type);
	}

	public ArrayList<Integer> getToUpdate() {
		return toUpdate;
	}

	public void setToUpdate(ArrayList<Integer> toUpdate) {
		this.toUpdate = toUpdate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
