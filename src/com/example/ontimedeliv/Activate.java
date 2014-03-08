package com.example.ontimedeliv;

import java.util.ArrayList;

public class Activate {

	private ArrayList<Integer> active;
	private ArrayList<Integer> inactive;

	public Activate(ArrayList<Integer> toActivate, ArrayList<Integer> toDeActivate) {
		this.setActive(toActivate);
		this.setUnactive(toDeActivate);
	}

	public ArrayList<Integer> getActive() {
		return active;
	}

	public void setActive(ArrayList<Integer> active) {
		this.active = active;
	}

	public ArrayList<Integer> getUnactive() {
		return inactive;
	}

	public void setUnactive(ArrayList<Integer> unactive) {
		this.inactive = unactive;
	}

}
