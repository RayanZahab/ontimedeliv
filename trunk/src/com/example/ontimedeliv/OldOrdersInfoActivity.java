package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class OldOrdersInfoActivity extends Activity {
	oldorderAdapter dataAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_old_orders_info);
		displayListView();
	}
	
	private void displayListView() {

		ArrayList<Item> orderitem = new ArrayList<Item>();

		Item _Item = new Item("rez b7alib", 5, "10000L.L");
		orderitem.add(_Item);
		_Item = new Item("Nido", 1, "16000L.L");
		orderitem.add(_Item);
		_Item = new Item("khebez", 5, "5000L.L");
		orderitem.add(_Item);
		_Item = new Item("drink", 5, "10000L.L");
		orderitem.add(_Item);

		// create an ArrayAdaptar from the String Array
		dataAdapter = new oldorderAdapter(this, R.layout.row_old_order_info,
				orderitem);
		ListView listView = (ListView) findViewById(R.id.oldorderlist);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.old_orders_info, menu);
		return true;
	}

}
