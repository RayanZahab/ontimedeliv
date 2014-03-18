package com.example.ontimedeliv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class SharedMenu extends Activity {

	public static final int ABOUT = 1000;
	public static final int settings = 1001;
	public static final int LogOut   = 1002;
	public static Context context;
	public static Menu menu;

	public SharedMenu(Menu menu, Context ctx) {
		this.menu = menu;
		this.context = ctx;
	}

	public static void onCreateOptionsMenu(Menu menu, Context ctx) {
	    menu.add(Menu.NONE, ABOUT, Menu.NONE,
	             ctx.getString(R.string.About));
	    menu.add(Menu.NONE, settings, Menu.NONE,
	             ctx.getString(R.string.settings));
	    menu.add(Menu.NONE, LogOut, Menu.NONE,
	             ctx.getString(R.string.Logout));
	    
	    context = ctx;
	  }
	 
	  public static boolean onOptionsItemSelected(MenuItem item, Activity caller) {
	    Intent intent;
	    switch (item.getItemId()) {
	      case SharedMenu.ABOUT:
	    	  Toast msg = Toast.makeText(
	    			  context,
	    			  "Developped by ArrayFusion", Toast.LENGTH_LONG);
	    	  msg.show();
	        return true;
	      case SharedMenu.settings:
	        intent = new Intent(caller, UserProfileActivity.class);
	        caller.startActivity(intent);
	        return true;
	      case SharedMenu.LogOut:
	    	  	SharedPreferences sharedPref = context.getSharedPreferences("PREFS_NAME", 0);
	    	  	SharedPreferences.Editor editor = sharedPref.edit();
		        editor.clear();
		        editor.commit();
		        intent = new Intent(caller, SelectLanguageActivity		.class);
		        caller.startActivity(intent);
		        return true;  
	      default:
	        return false;
	    }
	  }


}
