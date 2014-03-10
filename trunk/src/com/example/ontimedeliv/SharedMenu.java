package com.example.ontimedeliv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class SharedMenu extends Activity {
	
	public static final int ABOUT = 1000;
	public static final int navigation   = 1001;
	public static Context context;
	public static Menu menu;
	
	public SharedMenu(Menu menu , Context ctx)
	{
		this.menu=menu;
		this.context=ctx;
	}
	 
	public static void onCreateOptionsMenu(Menu menu, Context ctx) {
	    menu.add(Menu.NONE, navigation, Menu.NONE,
	             ctx.getString(R.string.navigation));
	    menu.add(Menu.NONE, ABOUT, Menu.NONE,
	             ctx.getString(R.string.About));
	    
	    context = ctx;
	  }
	 
	  public static boolean onOptionsItemSelected(MenuItem item, Activity caller) {
	    Intent intent;
	    switch (item.getItemId()) {
	      case SharedMenu.ABOUT:
	    	  Toast msg = Toast.makeText(
	    			  context,
	    			  "Developped by Rayan&Bachir", Toast.LENGTH_LONG);
	    	  msg.show();
	        return true;
	      case SharedMenu.navigation:
	        intent = new Intent(caller, NavigationActivity.class);
	        caller.startActivity(intent);
	        return true;
	      default:
	        return false;
	    }
	  }

}
