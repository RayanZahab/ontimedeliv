package com.ontimedeliv;



import android.R.color;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

public class SharedMenu extends Activity {

	public static final int ABOUT = 1000;
	public static final int settings = 1001;
	public static final int LogOut = 1002;
	public static Context context;
	public static Menu menu;
	public static Activity activity;
	public static ArrayAdapter<Item> adapter;

	public SharedMenu(Menu menu, Context ctx) {
		SharedMenu.menu = menu;
		SharedMenu.context = ctx;
	}

	public static void onCreateOptionsMenu(Activity a, Menu menu, Context ctx) {
		menu.add(Menu.NONE, ABOUT, Menu.NONE, ctx.getString(R.string.About));
		menu.add(Menu.NONE, settings, Menu.NONE,
				ctx.getString(R.string.settings));
		menu.add(Menu.NONE, LogOut, Menu.NONE, ctx.getString(R.string.Logout));
		context = ctx;
		activity = a;
	}

	public static void onCreateOptionsMenu(Activity a, Menu menu, Context ctx,ArrayAdapter<Item> adpt) {
		
		onCreateOptionsMenu(a,  menu, ctx);
		context = ctx;
		activity = a;
		adapter = adpt;
		setSearch(a, menu);
	}	
	public static void setSearch(Activity a,Menu mymenu)
	{
		menu = mymenu;
		SearchManager searchManager = (SearchManager) a.getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) mymenu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(a.getComponentName()));
		
		int  searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText inputSearch = (EditText) searchView.findViewById(searchPlateId);
        

		inputSearch.setHintTextColor(searchView.getContext().getResources().getColor(color.darker_gray));
		inputSearch.setTextColor(searchView.getContext().getResources().getColor(color.darker_gray));
		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				adapter.getFilter().filter(cs);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	public static boolean onOptionsItemSelected(MenuItem item, Activity caller) {
		Intent intent;
		switch (item.getItemId()) {
		case SharedMenu.ABOUT:

			Toast msg = null;
			try {
				msg = Toast.makeText(context,"version" + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName,
						Toast.LENGTH_LONG);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			msg.show();
			return true;
		case SharedMenu.settings:
			intent = new Intent(caller, UserProfileActivity.class);
			caller.startActivity(intent);
			return true;
		case SharedMenu.LogOut:			
			SharedPreferences.Editor editor = ontimedeliv.getInstance().mySettings.edit();
			editor.clear();
			editor.commit();
			intent = new Intent(caller, SelectLanguageActivity.class);
			caller.startActivity(intent);
			return true;
		default:
			return false;
		}
	}

}
