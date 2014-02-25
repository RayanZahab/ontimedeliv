package com.example.ontimedeliv;

public class User {
	private int id;
	private String name;
	private String username;
	private String password;
	private String phone;
	private String mobile;
	private int is_fired;

	public User(int id, String name, String username, String password,
			String phone, String mobile, int is_fired) {

		this.setId(id);
		this.setName(name);
		this.setUsername(username);
		this.setPassword(password);
		this.setPhone(phone);
		this.setMobile(mobile);
		this.setIs_fired(is_fired);
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public int getIs_fired() {
		return is_fired;
	}

	public void setIs_fired(int is_fired) {
		this.is_fired = is_fired;
	}

}
