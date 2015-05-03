package com.mobilife.delivery.admin.view.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.mobilife.delivery.admin.DeliveryAdminApplication;
import com.mobilife.delivery.admin.R;
import com.mobilife.delivery.admin.adapter.OrdersAdapter;
import com.mobilife.delivery.admin.model.Item;
import com.mobilife.delivery.admin.model.Order;
import com.mobilife.delivery.admin.model.OrderStatus;
import com.mobilife.delivery.admin.utilities.APIManager;
import com.mobilife.delivery.admin.utilities.GlobalM;
import com.mobilife.delivery.admin.utilities.PreferenecesManager;
import com.mobilife.delivery.admin.utilities.RZHelper;
import com.mobilife.delivery.admin.utilities.myURL;
import com.mobilife.delivery.admin.view.customcomponent.PullToRefreshListView;
import com.mobilife.delivery.admin.view.customcomponent.PullToRefreshListView.OnRefreshListener;

@SuppressLint("NewApi")
public class OrdersActivity extends Activity {
	OrdersAdapter dataAdapter;
	ArrayList<Order> morders;
	ArrayList<Item> orderItems = new ArrayList<Item>();
	boolean old = false, admin = true, isPreparer = false;
	String status;
	PullToRefreshListView lvTweets;
	GlobalM glob = new GlobalM();
	private boolean isSuperAdmin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orders);
		ActionBar actionBar = getActionBar();

		DeliveryAdminApplication.clear("orders");
		if (getIntent().hasExtra("old")&& getIntent().getBooleanExtra("old", false)) {
			old = getIntent().getBooleanExtra("old", false);
		}
		admin = DeliveryAdminApplication.isAdmin(this);
		isPreparer = DeliveryAdminApplication.isPrep(this);
		isSuperAdmin = PreferenecesManager.getInstance().getUserFromPreferences(this).isSuperAdmin();
		if (admin || isSuperAdmin) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		} else if (isPreparer) {
			DeliveryAdminApplication.setOrderStatus(OrderStatus.Assigned.name());
			actionBar.setDisplayHomeAsUpEnabled(false);
		} else {
			DeliveryAdminApplication.setOrderStatus(OrderStatus.Prepared.name());
			actionBar.setDisplayHomeAsUpEnabled(false);
		}
		status = DeliveryAdminApplication.getOrderStatus(this);
		if(status!=null){
			int status_id = glob.getStatus(status);
			actionBar.setTitle(getString(status_id));
		}
		getOrders();
	}

	public void getOrders() {
		String serverURL;
		serverURL = new myURL(null, "orders", status, 30).getURL();
		RZHelper p = new RZHelper(serverURL, this, "setOrders", true);
		p.get();
	}

	public void fetchTimelineAsync(int page) {
		getOrders();
		Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT).show();
	}

	public void setOrders(String s, String error) {
		orderItems = new ArrayList<Item>();
		morders = new APIManager().getOrders(s);
		lvTweets = (PullToRefreshListView) findViewById(R.id.list);

		lvTweets.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				lvTweets.setAdapter(null);
				fetchTimelineAsync(0);

			}
		});
		if (morders.size() == 0) {
			orderItems.add(new Item(0, getString(R.string.empty_list), 0, 0,
					false));
		} else {
			for (int i = 0; i < morders.size(); i++) {
				Item itm = new Item(morders.get(i).getId(), morders.get(i)
						.toString(), morders.get(i).getCount(), morders.get(i)
						.getTotal(), old ? false : morders.get(i)
						.isNewCustomer());
				itm.setDate(morders.get(i).getDate());
				orderItems.add(itm);
			}
		}

		dataAdapter = new OrdersAdapter(OrdersActivity.this,
				R.layout.row_order, orderItems);
		lvTweets.setAdapter(dataAdapter);
		if (morders.size() == 0) {
			dataAdapter.empty = true;
		}
		lvTweets.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (morders.size() > 0) {
					Intent i;

					if (morders.get(position).isNewCustomer()) {
						i = new Intent(getBaseContext(), BlockUserActivity.class);
					} else {
						i = new Intent(getBaseContext(),
								OrderInfoActivity.class);
					}
					DeliveryAdminApplication.setOrderId(morders.get(position).getId());
					startActivity(i);
				}
			}
		});
		lvTweets.onRefreshComplete();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.orders, menu);
		SharedMenuActivity.onCreateOptionsMenu(this, menu, getApplicationContext());
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (SharedMenuActivity.onOptionsItemSelected(item, this) == false) {
			// handle local menu items here or leave blank
		}
		return super.onOptionsItemSelected(item);
	}

}