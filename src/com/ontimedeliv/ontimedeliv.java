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
	private static String token;
	private static String orderStatus;
	private static int shopId = 0, branchId = 0, categoryId = 0, productId = 0,
			orderId = 0, userId = 0;
	private static boolean admin;
	private static boolean prep;
	private static boolean delivery;
	private static boolean keepme;
	public TransparentProgressDialog loader;
	public static TextProgressDialog txtDialog;
	private UncaughtExceptionHandler defaultUEH;
	private static ArrayList<Country> countries ;
	
	
	public ontimedeliv() {
		
		//defaultUEH = Thread.getDefaultUncaughtExceptionHandler();

		// setup handler for uncaught exception
		//Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);
	}
	public static ArrayList<Country> getCountries() {
		return countries;
	}
	public static void setCountries(ArrayList<Country> mycountries) {
		countries = mycountries;
	}
	public static String getToken() {
		return token;
	}

	public void setToken(String mytoken) {
		token = mytoken;
	}

	public static int getShopId() {
		return shopId;
	}

	public void setShopId(int myshopId) {
		shopId = myshopId;
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
			SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
			settings.edit().clear().commit();
			System.exit(2);

			// re-throw critical exception further to the os (important)
			defaultUEH.uncaughtException(thread, ex);

		}
	};

	public void setGlobals() {		
		SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
		token = settings.getString("token", null);
		shopId = settings.getInt("shopId", 0);
		admin = settings.getBoolean("admin", false);
		prep = settings.getBoolean("preparer", false);
		delivery = settings.getBoolean("delivery", false);
		keepme = settings.getBoolean("isChecked", false);
	}

	public static void clear(String current) {
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

	public static int getBranchId() {
		return branchId;
	}

	public static void setBranchId(int mybranchId) {
		branchId = mybranchId;
	}

	public static boolean isDelivery() {
		return delivery;
	}

	public void setDelivery(boolean mydelivery) {
		delivery = mydelivery;
	}

	public static boolean isAdmin() {
		return admin;
	}

	public static void setAdmin(boolean myadmin) {
		admin = myadmin;
	}

	public static boolean isPrep() {
		return prep;
	}

	public void setPrep(boolean myprep) {
		prep = myprep;
	}

	public boolean isKeepme() {
		return keepme;
	}

	public void setKeepme(boolean mykeepme) {
		keepme = mykeepme;
	}

	public int getProductId() {
		return productId;
	}

	public static void setProductId(int myproductId) {
		productId = myproductId;
	}

	public static int getCategoryId() {
		return categoryId;
	}

	public static void setCategoryId(int mycategoryId) {
		categoryId = mycategoryId;
	}

	public static int getOrderId() {
		return orderId;
	}

	public static void setOrderId(int myorderId) {
		orderId = myorderId;
	}

	public static String getOrderStatus() {
		return orderStatus;
	}

	public static void setOrderStatus(String myorderStatus) {
		orderStatus = myorderStatus;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int myuserId) {
		userId = myuserId;
	}

	public TransparentProgressDialog getLoader() {
		return loader;
	}

	public void setLoader(TransparentProgressDialog loader) {
		this.loader = loader;
	}
}
