package com.example.ontimedeliv;

import java.util.ArrayList;

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

import com.example.ontimedeliv.PullToRefreshListView.OnRefreshListener;
 
public class OrdersActivity extends Activity {
	OrdersAdapter dataAdapter;
	ArrayList<Order> morders;
	ArrayList<Item> orderItems = new ArrayList<Item>();
	boolean old = false, admin = true, isPreparer = false;
	String status;
	PullToRefreshListView lvTweets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orders);
		ActionBar actionBar = getActionBar();

		((ontimedeliv) this.getApplication()).clear("orders");
		if (getIntent().hasExtra("old")
				&& getIntent().getBooleanExtra("old", false)) {
			old = getIntent().getBooleanExtra("old", false);
		}
		admin = ((ontimedeliv) OrdersActivity.this.getApplication()).isAdmin();
		isPreparer = ((ontimedeliv) this.getApplication()).isPrep();
		if (admin) {
			actionBar.setDisplayHomeAsUpEnabled(true); 
		} else if (isPreparer) {
			((ontimedeliv) OrdersActivity.this.getApplication())
					.setOrderStatus("assigned");
		} else {
			((ontimedeliv) OrdersActivity.this.getApplication())
					.setOrderStatus("prepared");
		}
		status = ((ontimedeliv) OrdersActivity.this.getApplication())
				.getOrderStatus();
		int status_id = new GlobalM().getStatus(status);
		actionBar.setTitle(getString(status_id));
		getOrders();
	}

	public void getOrders() {
		String serverURL;
		serverURL = new myURL(null, "orders", status, 30).getURL();
		//new MyJs("setOrders", this, ((ontimedeliv) this.getApplication()),
			//	"GET").execute(serverURL);
		RZHelper p = new RZHelper(serverURL,this,"setOrders");
		p.get();
	}

	public void fetchTimelineAsync(int page) {
		Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_SHORT)
				.show();
		getOrders();
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
						.getTotal(), morders.get(i).isNewCustomer());
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
					if (orderItems.get(position).isNew) {
						i = new Intent(getBaseContext(), BlockUser.class);
					} else {
						i = new Intent(getBaseContext(),
								OrderInfoActivity.class);
					}
					((ontimedeliv) OrdersActivity.this.getApplication())
							.setOrderId(orderItems.get(position).getId());
					startActivity(i);
				}
			}
		});
		lvTweets.onRefreshComplete();
	}

	@Override
	public void onBackPressed() {
		new GlobalM().bkToNav(OrdersActivity.this, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.orders, menu);
		SharedMenu.onCreateOptionsMenu(this, menu, getApplicationContext());
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (SharedMenu.onOptionsItemSelected(item, this) == false) {
			// handle local menu items here or leave blank
		}
		return super.onOptionsItemSelected(item);
	}

}