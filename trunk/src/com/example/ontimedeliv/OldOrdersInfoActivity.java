package com.example.ontimedeliv;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class OldOrdersInfoActivity extends Activity {
	Spinner prep, deliv, status;
	Button cancel;
	OrderInfoAdapter dataAdapter;
	int orderId;
	MyJs mjs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_old_orders_info);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		((ontimedeliv) this.getApplication()).clear("order");
		this.orderId = ((ontimedeliv) this.getApplication()).getOrderId();
		if (orderId != 0) {
			getCurrentOrder(orderId);
		}

	}

	public void getPreparers() {
		String serverURL = new myURL(null, "users", "preparers", 30).getURL();
		mjs = new MyJs("serPreparers", this,
				((ontimedeliv) this.getApplication()), "GET",false,true);
		mjs.execute(serverURL);
	}

	public void serPreparers(String s, String error) {
		ArrayList<User> userItems = new APIManager().getUsers(s);

		prep = (Spinner) findViewById(R.id.preparer_spinner);

		ArrayAdapter<User> dataAdapter = new ArrayAdapter<User>(this,
				android.R.layout.simple_spinner_item, userItems);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prep.setAdapter(dataAdapter);
	}

	public void getDelivery() {
		String serverURL = new myURL(null, "users", "deliverers", 30).getURL();
		new MyJs("setDeivery", this, ((ontimedeliv) this.getApplication()),
				"GET",false,false).execute(serverURL);
	}

	public void setDeivery(String s, String error) {
		ArrayList<User> userItems = new APIManager().getUsers(s);

		deliv = (Spinner) findViewById(R.id.delivery_Spinner);

		ArrayAdapter<User> dataAdapter = new ArrayAdapter<User>(this,
				android.R.layout.simple_spinner_item, userItems);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		deliv.setAdapter(dataAdapter);
		getPreparers();
	}

	public void getCurrentOrder(int orderId) {
		String serverURL = new myURL(null, "orders", orderId, 30).getURL();
		new MyJs("setOrderInfo", this, ((ontimedeliv) this.getApplication()),
				"GET",true,false).execute(serverURL);
	}

	public void setOrderInfo(String s, String error) {
		Order currentOrder = new APIManager().getOrder(s);
		ArrayList<OrderItem> orderitem = currentOrder.getOrderItems();
		ArrayList<Item> items = new ArrayList<Item>();
		Item _Item;
		double total = 0;
		TextView totalTxt = (TextView) findViewById(R.id.total);
		ListView listView = (ListView) findViewById(R.id.listView);
		for (int i = 0; i < orderitem.size(); i++) {
			_Item = new Item(orderitem.get(i).getId(), orderitem.get(i)
					.toString(), orderitem.get(i).getQuantity(), orderitem.get(
					i).getUnitPrice());
			items.add(_Item);
			total = total + orderitem.get(i).getTotalPrice();
		}

		dataAdapter = new OrderInfoAdapter(OldOrdersInfoActivity.this,
				R.layout.row_old_order_info, items, true);
		dataAdapter.setTotal(totalTxt);

		listView.setAdapter(dataAdapter);
		Helper.getListViewSize(listView);

		totalTxt.setText(total + "");
		TextView customerName = (TextView) findViewById(R.id.customerName);
		customerName.append(" " + currentOrder.getCustomer().toString());
		TextView customerAdd = (TextView) findViewById(R.id.customerAdd);
		customerAdd
				.append(" This is add"/* currentOrder.getAddress().toString() */);
		getDelivery();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order_info, menu);
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
