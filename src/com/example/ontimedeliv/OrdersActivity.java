package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OrdersActivity extends Activity {
	OrdersAdapter dataAdapter;
	ProgressDialog Dialog;
	ArrayList<Order> morders;
	ArrayList<Item> orderItems = new ArrayList<Item>();
	boolean old = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent().hasExtra("old")
				&& getIntent().getBooleanExtra("old", false)) {
			old = getIntent().getBooleanExtra("old", false);
			setContentView(R.layout.activity_old_orders);
		} else {
			setContentView(R.layout.activity_orders);
		}
		this.Dialog = new ProgressDialog(OrdersActivity.this);
		getOrders();
	}

	public void getOrders() {
		String serverURL;
		if (getIntent().hasExtra("old")
				&& getIntent().getBooleanExtra("old", false)) {
			serverURL = new myURL(null, "orders", "cancelled", 30).getURL();
		} else {
			serverURL = new myURL(null, "orders", "opened", 30).getURL();
		}
		new MyJs(Dialog, "setOrders", this, "GET").execute(serverURL);
	}

	public void setOrders(String s, String error) {

		morders = new APIManager().getOrders(s);
		for (int i = 0; i < morders.size(); i++) {
			orderItems.add(new Item(morders.get(i).getId(), morders.get(i)
					.toString(), morders.get(i).getCount(), morders.get(i)
					.getTotal(), false));
		}
		dataAdapter = new OrdersAdapter(OrdersActivity.this,
				R.layout.row_order, orderItems);

		ListView listView = (ListView) findViewById(R.id.list);
		registerForContextMenu(listView);

		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i;
				Log.d("ray","rayclik: "+old);
				if (old) {
					i = new Intent(getBaseContext(),
							OldOrdersInfoActivity.class);
				} else {
					i = new Intent(getBaseContext(), OrderInfoActivity.class);
				}
				i.putExtra("orderId", "" + orderItems.get(position).getId());
				startActivity(i);
			}
		});
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(OrdersActivity.this, NavigationActivity.class);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.orders, menu);
		SharedMenu.onCreateOptionsMenu(menu, getApplicationContext());
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (SharedMenu.onOptionsItemSelected(item, this) == false) {
			// handle local menu items here or leave blank
		}
		return super.onOptionsItemSelected(item);
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.clearHeader();
		menu.add(0, v.getId(), 0, "Delete");
	}

	public boolean onContextItemSelected(MenuItem item) {
		if (item.getTitle() == "Delete") {
			Delete(item.getItemId());
		} else {
			return false;
		}
		return true;
	}

	public void Delete(int id) {
		Toast.makeText(this, "Delete called", Toast.LENGTH_SHORT).show();
	}

}