package com.mobilife.delivery.admin.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.mobilife.delivery.admin.DeliveryAdminApplication;
import com.mobilife.delivery.admin.R;
import com.mobilife.delivery.admin.model.Customer;
import com.mobilife.delivery.admin.model.Order;
import com.mobilife.delivery.admin.utilities.APIManager;
import com.mobilife.delivery.admin.utilities.RZHelper;
import com.mobilife.delivery.admin.utilities.myURL;

public class BlockUserActivity extends Activity {

	Customer currentCustomer;
	int orderId;
	Order currentOrder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_block_user);
		this.orderId = DeliveryAdminApplication.getOrderId(this);
		getCurrentCustomer(orderId);
		// ActionBar actionBar = getActionBar();
		// actionBar.setDisplayHomeAsUpEnabled(true);
	}

	public void getCurrentCustomer(int orderId) {
		String serverURL = new myURL(null, "orders", orderId, 0).getURL();
		RZHelper p = new RZHelper(serverURL, this, "setCustomerInfo", false);
		p.get();
	}

	public void setCustomerInfo(String s, String error) {
		currentOrder = new APIManager().getOrder(s);
		currentCustomer = currentOrder.getCustomer();
		TextView customerName = (TextView) findViewById(R.id.customerName);
		customerName.setText(" " + currentCustomer.toString());
		TextView customerPhone = (TextView) findViewById(R.id.customerPhone);
		customerPhone.setText(" " + currentCustomer.getMobile());
		TextView customerAdd = (TextView) findViewById(R.id.customerAdd);
		customerAdd.setText(currentOrder.getAddress().toString());
	}

	public void block(View v) {
		String serverURL;
		serverURL = new myURL("deny", "customers", currentCustomer.getId(), 0)
				.getURL();
		RZHelper p = new RZHelper(serverURL, this, "back", false);
		p.post(currentCustomer);
	}

	public void back(String s, String error) {
		// Intent i = new Intent(getBaseContext(), OrderInfoActivity.class);
		// startActivity(i);
		DeliveryAdminApplication.setOrderId(0);
		Intent i = new Intent(getBaseContext(), OrdersActivity.class);
		startActivity(i);
	}

	public void cancel(View v) {
		DeliveryAdminApplication.setOrderId(0);
		onBackPressed();
	}

	public void allow(View v) {
		/*
		 * String serverURL; serverURL = new myURL("allow", "customers",
		 * currentCustomer.getId(), 0) .getURL(); RZHelper p = new
		 * RZHelper(serverURL, this, "gotoOrder", false);
		 * p.post(currentCustomer);
		 */
		gotoOrder(v);
	}

	public void gotoOrder(View v) {
		Intent i = new Intent(getBaseContext(), OrderInfoActivity.class);
		startActivity(i);
		// ontimedeliv.setOrderId(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.block_user, menu);
		SharedMenuActivity.onCreateOptionsMenu(this, menu, getApplicationContext());
		return true;
	}

}
