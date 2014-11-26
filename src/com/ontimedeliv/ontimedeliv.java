package com.ontimedeliv;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class ontimedeliv extends Application {
	private String token, orderStatus;
	private int shopId = 0, branchId = 0, categoryId = 0, productId = 0,
			orderId = 0, userId = 0;
	private boolean admin, prep, delivery, keepme;
	public TransparentProgressDialog loader;
	public TextProgressDialog txtDialog;
	private UncaughtExceptionHandler defaultUEH;
	private ArrayList<Country> countries ;
	SharedPreferences mySettings;
	private static ontimedeliv mInstance= null;
	
	public ontimedeliv(){
		mInstance=this;
		mySettings = getSharedPreferences("PREFS_NAME", 0);
	}

    public static synchronized ontimedeliv getInstance(){
    	if(null == mInstance){
    		mInstance = new ontimedeliv();
    		//defaultUEH = Thread.getDefaultUncaughtExceptionHandler();

    		// setup handler for uncaught exception
    		//Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);
    	}
    	return mInstance;
    }
	public ArrayList<Country> getCountries() {
		return countries;
	}
	public void setCountries(ArrayList<Country> countries) {
		this.countries = countries;
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

	// handler listener
	private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			// here I do logging of exception to a db
			PendingIntent myActivity = PendingIntent.getActivity(
					getBaseContext(), 192837, new Intent(getBaseContext(),
							LoginActivity.class), PendingIntent.FLAG_ONE_SHOT);

			AlarmManager alarmManager;
			alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 15000,
					myActivity);
			mySettings.edit().clear().commit();
			System.exit(2);

			// re-throw critical exception further to the os (important)
			defaultUEH.uncaughtException(thread, ex);

		}
	};

	public void setGlobals() {

		
		this.token = mySettings.getString("token", null);
		this.shopId = mySettings.getInt("shopId", 0);
		this.admin = mySettings.getBoolean("admin", false);
		this.prep = mySettings.getBoolean("preparer", false);
		this.delivery = mySettings.getBoolean("delivery", false);
		this.keepme = mySettings.getBoolean("isChecked", false);
	}

	public void clear(String current) {
		if (current.equals("listing")) {
			branchId = 0;
			categoryId = 0;
			productId = 0;
			orderId = 0;
			orderStatus = null;
			userId = 0;
		} else if (current.equals("categories") || current.equals("branch")) {
			categoryId = 0;
			productId = 0;
			orderId = 0;
			orderStatus = null;
			userId = 0;
		} else if (current.equals("products")) {
			productId = 0;
			orderId = 0;
			orderStatus = null;
			userId = 0;
		} else if (current.equals("product")) {
			orderId = 0;
			orderStatus = null;
			userId = 0;
		} else if (current.equals("branch")) {
			categoryId = 0;
			productId = 0;
			orderId = 0;
			orderStatus = null;
		} else if (current.equals("order")) {
			branchId = 0;
			categoryId = 0;
			productId = 0;
			userId = 0;
		} else if (current.equals("orders")) {
			branchId = 0;
			categoryId = 0;
			productId = 0;
			orderId = 0;
			userId = 0;
		} else if (current.equals("user")) {
			branchId = 0;
			categoryId = 0;
			productId = 0;
			orderId = 0;
			orderStatus = null;
		}
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public boolean isDelivery() {
		return delivery;
	}

	public void setDelivery(boolean delivery) {
		this.delivery = delivery;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isPrep() {
		return prep;
	}

	public void setPrep(boolean prep) {
		this.prep = prep;
	}

	public boolean isKeepme() {
		return keepme;
	}

	public void setKeepme(boolean keepme) {
		this.keepme = keepme;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public TransparentProgressDialog getLoader() {
		return loader;
	}

	public void setLoader(TransparentProgressDialog loader) {
		this.loader = loader;
	}
}
