package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class NavigationActivity extends Activity {
	MyCustomAdapter dataAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		
		displayListView();
	}

	private void displayListView() {

		// Array list of Categories
		ArrayList<Item> categories = new ArrayList<Item>();
		// String role
		// if()
		SharedPreferences settings1 = getSharedPreferences("PREFS_NAME", 0);
		boolean isAdmin = settings1.getBoolean("admin", false);
		boolean isPreparer = settings1.getBoolean("preparer", false);
		boolean isDelivery = settings1.getBoolean("delivery", false);
		Item _Item;
		if (isPreparer || isDelivery || isAdmin) 
		{

			_Item = new Item(0, R.drawable.ic_launcher, "Orders");
			_Item.setMethod("Orders");
			categories.add(_Item);
		}
		if (isAdmin) 
		{

			_Item = new Item(0, R.drawable.branches , "Branches");
			_Item.setMethod("Branches");
			categories.add(_Item);
			
			_Item = new Item(1, R.drawable.users , "Users");
			_Item.setMethod("Users");
			categories.add(_Item);
			_Item = new Item(2, R.drawable.ic_launcher, "Selection");
			_Item.setMethod("Selection");
			categories.add(_Item);
			_Item = new Item(3, R.drawable.ic_launcher, "OldOrders");
			_Item.setMethod("Orders");
			categories.add(_Item);
		}

		// create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this, R.layout.categories_list,
				categories);
		ListView listView = (ListView) findViewById(R.id.list);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, Navigate to the selected item
				Item navitem = (Item) parent.getItemAtPosition(position);
				String title = navitem.getTitle();
				Intent i;
				try {
					i = new Intent(getBaseContext(), Class
							.forName(getPackageName() + "." + navitem.getMethod()
									+ "Activity"));
					i.putExtra("shopId", 37);
					if(navitem.getId()==3)
					{
						i.putExtra("old", true);
					}
					startActivity(i);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

	}

	@Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    	NavigationActivity.this.finishAffinity();
                    }
                }).setNegativeButton("No", null).show();
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.navigation, menu);
		SharedMenu.onCreateOptionsMenu(menu, getApplicationContext());
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (SharedMenu.onOptionsItemSelected(item, this) == false) {
			// handle local menu items here or leave blank
		}
		return super.onOptionsItemSelected(item);
	}
}
