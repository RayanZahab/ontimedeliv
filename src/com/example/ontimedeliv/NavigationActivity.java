package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
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

		Bitmap picture = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.ic_launcher);
		// Bitmap albanajban = BitmapFactory.decodeResource(this.getResources(),
		// R.drawable.ic_launcher);
		// Bitmap drinks = BitmapFactory.decodeResource(this.getResources(),
		// R.drawable.ic_launcher);
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

			_Item = new Item(0, picture, "Orders");
			categories.add(_Item);
		}
		if (isAdmin) 
		{

			_Item = new Item(0, picture, "Branches");
			categories.add(_Item);

			_Item = new Item(0, picture, "Users");
			categories.add(_Item);
			_Item = new Item(0, picture, "Selection");
			categories.add(_Item);
			_Item = new Item(0, picture, "OldOrders");
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
							.forName(getPackageName() + "." + title
									+ "Activity"));
					i.putExtra("shopId", 37);
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
		// TODO Auto-generated method stub
		super.onBackPressed();
		NavigationActivity.this.finishAffinity();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.navigation, menu);
		SharedMenu.onCreateOptionsMenu(menu, getApplicationContext());
		return true;
	}
}
