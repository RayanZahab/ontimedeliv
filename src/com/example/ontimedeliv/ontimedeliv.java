package com.example.ontimedeliv;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

public class ontimedeliv extends Application {
	private String token;
	private int shopId;

	public ontimedeliv() {
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public void setGlobals() {
		
		SharedPreferences settings1 = getSharedPreferences("PREFS_NAME", 0);
		this.token = settings1.getString("token", null);
		this.shopId = settings1.getInt("shopId", 0);
		Log.d("rays","global set: "+this.token +" - "+this.shopId);
	}
}
