package com.example.ontimedeliv;

public class Order {
	private int id;
	private int customer_id;
	private int status_id;
	private String total;
	private int count;
	private int address_id;
	private Customer customer;

	public Order(int id, int customer_id, int status_id, String total,
			int count, int address_id) {
		this.setId(id);
		this.setCustomer_id(customer_id);
		this.setStatus_id(status_id);
		this.setTotal(total);
		this.setCount(count);
		this.setAddress_id(address_id);
	}

	public Order(int id, Customer customer, String total, int count) {
		this.id = id;
		this.customer = customer;
		this.total = total;
		this.count = count;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}

	public int getStatus_id() {
		return status_id;
	}

	public void setStatus_id(int status_id) {
		this.status_id = status_id;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getAddress_id() {
		return address_id;
	}

	public void setAddress_id(int address_id) {
		this.address_id = address_id;
	}

	public String toString() {
		return this.customer.getName();
	}
}
