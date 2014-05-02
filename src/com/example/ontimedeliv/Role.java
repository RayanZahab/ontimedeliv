package com.example.ontimedeliv;

public class Role {
	private boolean admin, preparer, delivery;

	public Role() {
	}

	public boolean getAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean getPreparer() {
		return preparer;
	}

	public void setPreparer(boolean preparer) {
		this.preparer = preparer;
	}

	public boolean getDelivery() {
		return delivery;
	}

	public void setDelivery(boolean delivery) {
		this.delivery = delivery;
	}
	
	public ValidationError validate(boolean mine) {
		boolean valid = false;
		int msg = 0;
		if (!this.admin && !this.preparer && !this.delivery) {
			msg = R.string.select_role;
		} else
			valid = true;
		return new ValidationError(valid, msg);
	}

}
