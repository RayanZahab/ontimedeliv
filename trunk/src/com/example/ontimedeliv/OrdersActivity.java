package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class OrdersActivity extends Activity {
	OrdersAdapter dataAdapter;
	ArrayList<Order> morders;
	ArrayList<Item> orderItems = new ArrayList<Item>();
	boolean old = false;
	String status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orders);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		((ontimedeliv) this.getApplication()).clear("orders");
		if (getIntent().hasExtra("old")
				&& getIntent().getBooleanExtra("old", false)) {
			old = getIntent().getBooleanExtra("old", false);
		}
		status = ((ontimedeliv) OrdersActivity.this.getApplication())
				.getOrderStatus();

		getOrders();
	}

	public void getOrders() {
		String serverURL;
		serverURL = new myURL(null, "orders", status, 30).getURL();
		new MyJs("setOrders", this,
				((ontimedeliv) this.getApplication()), "GET")
				.execute(serverURL);
	}

	public void setOrders(String s, String error) {

		morders = new APIManager().getOrders(s);
		for (int i = 0; i < morders.size(); i++) {
			orderItems.add(new Item(morders.get(i).getId(), morders.get(i)
					.toString(), morders.get(i).getCount(), morders.get(i)
					.getTotal(), morders.get(i).isNewCustomer()));
		}

		dataAdapter = new OrdersAdapter(OrdersActivity.this,
				R.layout.row_order, orderItems);

		ListView listView = (ListView) findViewById(R.id.list);

		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i;
				if (old) {
					i = new Intent(getBaseContext(),
							OldOrdersInfoActivity.class);
				} else {
					i = new Intent(getBaseContext(), OrderInfoActivity.class);
				}
				((ontimedeliv) OrdersActivity.this.getApplication())
						.setOrderId(orderItems.get(position).getId());
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

}