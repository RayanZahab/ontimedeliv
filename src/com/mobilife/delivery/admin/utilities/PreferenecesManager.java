package com.mobilife.delivery.admin.utilities;

import com.mobilife.delivery.admin.model.User;

import android.app.Activity;
import android.content.SharedPreferences;

public class PreferenecesManager {

	private static PreferenecesManager instance;
	
	private PreferenecesManager(){
	}
	
	public static PreferenecesManager getInstance() {
	      if(instance == null) {
	         instance = new PreferenecesManager();
	      }
	      return instance;
	 }
	
	public User getUserFromPreferences(Activity activity){
		SharedPreferences sharedPreferences = activity.getSharedPreferences("PREFS_NAME", 0);
		String  name = sharedPreferences.getString("name", "");
		String  phone = sharedPreferences.getString("phone", "");
		int     id = sharedPreferences.getInt("id", 0);
		int     branchId = sharedPreferences.getInt("branchId", 0);
		boolean isAdmin = sharedPreferences.getBoolean("admin",false);
		boolean isPreparer = sharedPreferences.getBoolean("preparer", false);
		boolean isDelivery = sharedPreferences.getBoolean("delivery",false);
		boolean isSuperAdmin = sharedPreferences.getBoolean("superadmin",false);
		String  token = sharedPreferences.getString("token", "");
		User    user  = new User(id,name,phone,token,branchId,isAdmin,isPreparer,isDelivery,isSuperAdmin);
		return user;
	}
}
