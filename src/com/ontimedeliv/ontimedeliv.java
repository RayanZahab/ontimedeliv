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
	private static ArrayList<Country> countries;
	private static ArrayList<Unit> units;
	static SharedPreferences settings;
	static SharedPreferences.Editor editor;

	public ontimedeliv() {

		// defaultUEH = Thread.getDefaultUncaughtExceptionHandler();

		// setup handler for uncaught exception
		// Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);
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
			settings = getSharedPreferences("PREFS_NAME", 0);
			settings.edit().clear().commit();
			System.exit(2);

			// re-throw critical exception further to the os (important)
			defaultUEH.uncaughtException(thread, ex);

		}
	};

	public void setGlobals() {
		settings = getSharedPreferences("PREFS_NAME", 0);
		editor = settings.edit();

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

	public static void setSetting(Context ctx) {
		if (settings == null) {
			settings = ctx.getSharedPreferences("PREFS_NAME", 0);
			editor = settings.edit();
		}
	}

	public static ArrayList<Country> getCountries() {
		return countries;
	}

	public static void setCountries(ArrayList<Country> mycountries) {
		countries = mycountries;
	}

	public static String getToken(Context context) {
		if (token == null) {
			setSetting(context);
			token = settings.getString("token", null);
		}
		return token;
	}

	public void setToken(String mytoken) {
		token = mytoken;
		editor.putString("token", token);
		editor.commit();
	}

	public static int getShopId(Context context) {
		if (shopId == 0) {
			setSetting(context);
			shopId = settings.getInt("shopId", 0);
		}
		return shopId;
	}

	public void setShopId(int myshopId) {
		shopId = myshopId;
		editor.putInt("shopId", myshopId);
		editor.commit();
	}

	public static int getBranchId(Context context) {
		if (shopId == 0) {
			setSetting(context);
			branchId = settings.getInt("branchId", 0);
		}
		return branchId;
	}

	public static void setBranchId(int mybranchId) {
		branchId = mybranchId;
		editor.putInt("branchId", mybranchId);
		editor.commit();
	}

	public static boolean isDelivery(Context context) {
		if (shopId == 0) {
			setSetting(context);
			delivery = settings.getBoolean("delivery", false);
		}
		return delivery;
	}

	public void setDelivery(boolean mydelivery) {
		delivery = mydelivery;
		editor.putBoolean("delivery", delivery);
		editor.commit();
	}

	public static boolean isAdmin(Context context) {
		if (shopId == 0) {
			setSetting(context);
			admin = settings.getBoolean("admin", false);
		}
		return admin;
	}

	public static void setAdmin(boolean myadmin) {
		admin = myadmin;
		editor.putBoolean("admin", admin);
		editor.commit();
	}

	public static boolean isPrep(Context context) {
		if (shopId == 0) {
			setSetting(context);
			prep = settings.getBoolean("preparer", false);
		}
		return prep;
	}

	public void setPrep(boolean myprep) {
		prep = myprep;
		editor.putBoolean("preparer", prep);
		editor.commit();
	}

	public boolean isKeepme(Context context) {
		if (shopId == 0) {
			setSetting(context);
			prep = settings.getBoolean("isChecked", false);
		}
		return keepme;
	}

	public void setKeepme(boolean mykeepme) {
		keepme = mykeepme;
		editor.putBoolean("isChecked", keepme);
		editor.commit();
	}

	public static int getProductId(Context context) {
		if (shopId == 0) {
			setSetting(context);
			productId = settings.getInt("productId", 0);
		}
		return productId;
	}

	public static void setProductId(int myproductId) {
		productId = myproductId;
		editor.putInt("productId", productId);
		editor.commit();
	}

	public static int getCategoryId(Context context) {
		if (shopId == 0) {
			setSetting(context);
			productId = settings.getInt("categoryId", 0);
		}
		return categoryId;
	}

	public static void setCategoryId(int mycategoryId) {
		categoryId = mycategoryId;
		editor.putInt("productId", productId);
		editor.commit();
	}

	public static int getOrderId(Context context) {
		if (shopId == 0) {
			setSetting(context);
			orderId = settings.getInt("orderId", 0);
		}
		return orderId;
	}

	public static void setOrderId(int myorderId) {
		orderId = myorderId;
		editor.putInt("orderId", orderId);
		editor.commit();
	}

	public static String getOrderStatus(Context context) {
		if (shopId == 0) {
			setSetting(context);
			orderStatus = settings.getString("orderStatus", null);
		}
		return orderStatus;
	}

	public static void setOrderStatus(String myorderStatus) {
		orderStatus = myorderStatus;
		editor.putString("orderStatus", orderStatus);
		editor.commit();
	}

	public int getUserId(Context context) {
		if (shopId == 0) {
			setSetting(context);
			userId = settings.getInt("userId", 0);
		}
		return userId;
	}

	public void setUserId(int myuserId) {
		userId = myuserId;
		editor.putInt("userId", userId);
		editor.commit();
	}

	public TransparentProgressDialog getLoader() {
		return loader;
	}

	public void setLoader(TransparentProgressDialog loader) {
		this.loader = loader;
	}

	public static ArrayList<Unit> getUnits() {
		return units;
	}

	public static void setUnits(ArrayList<Unit> units) {
		ontimedeliv.units = units;
	}
}
