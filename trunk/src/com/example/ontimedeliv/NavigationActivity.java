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
		((ontimedeliv) NavigationActivity.this.getApplication())
				.clear("listing");
		displayListView();
	}

	private void displayListView() {
		ArrayList<Item> categories = new ArrayList<Item>();
		boolean isAdmin = ((ontimedeliv) NavigationActivity.this
				.getApplication()).isAdmin();
		boolean isPreparer = ((ontimedeliv) NavigationActivity.this
				.getApplication()).isPrep();
		boolean isDelivery = ((ontimedeliv) NavigationActivity.this
				.getApplication()).isDelivery();
		Item _Item;

		if (isPreparer || isDelivery || isAdmin) {

			_Item = new Item(0, R.drawable.ic_launcher, "New Orders");
			_Item.setMethod("Orders");
			_Item.setOrderStatus("opened");
			categories.add(_Item);
		}
		if (isAdmin) {

			_Item = new Item(1, R.drawable.branches, "Branches");
			_Item.setMethod("Branches");
			categories.add(_Item);
			_Item = new Item(2, R.drawable.users, "Users");
			_Item.setMethod("Users");
			categories.add(_Item);
			/* _Item = new Item(2, R.drawable.ic_launcher, "Selection"); */
			_Item = new Item(3, R.drawable.ic_launcher, "Assigned Orders");
			_Item.setMethod("Orders");
			_Item.setOrderStatus("assigned");
			categories.add(_Item);
			_Item = new Item(4, R.drawable.ic_launcher, "Prepared Orders");
			_Item.setMethod("Orders");
			_Item.setOrderStatus("prepared");
			categories.add(_Item);
			_Item = new Item(5, R.drawable.ic_launcher, "Canceled Orders");
			_Item.setMethod("Orders");
			_Item.setOrderStatus("cancelled");
			categories.add(_Item);
			_Item = new Item(6, R.drawable.ic_launcher, "Closed Orders");
			_Item.setMethod("Orders");
			_Item.setOrderStatus("closed");
			categories.add(_Item);
		}

		dataAdapter = new MyCustomAdapter(this, R.layout.categories_list,
				categories);
		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Item navitem = (Item) parent.getItemAtPosition(position);
				Intent i;
				try {
					i = new Intent(getBaseContext(), Class
							.forName(getPackageName() + "."
									+ navitem.getMethod() + "Activity"));
					if (navitem.getId() == 5 || navitem.getId() == 6) {
						i.putExtra("old", true);
					}
					if (navitem.getId() != 1 && navitem.getId() != 2) {
						((ontimedeliv) NavigationActivity.this.getApplication())
								.setOrderStatus(navitem.getOrderStatus());
					}
					startActivity(i);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.exit)
				.setMessage(R.string.exitquest)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								NavigationActivity.this.finishAffinity();
							}
						}).setNegativeButton(android.R.string.no, null).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
