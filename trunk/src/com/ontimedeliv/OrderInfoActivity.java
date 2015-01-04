package com.ontimedeliv;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class OrderInfoActivity extends Activity {
	Spinner prep, deliv, status;
	Button cancel, submit;
	OrderInfoAdapter dataAdapter;
	int orderId;
	AlertDialog alertDialog;
	Order currentOrder;
	GlobalM glob = new GlobalM();
	ArrayList<OrderItem> orderitem;
	ArrayList<Item> SPitems;
	ListView listView;
	EditText notes;
	Boolean isAdmin = true, isPreparer = true, disabled = false;
	ArrayList<String> stat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_info);
		status = (Spinner) findViewById(R.id.order_status);
		prep = (Spinner) findViewById(R.id.preparer_spinner);
		deliv = (Spinner) findViewById(R.id.delivery_Spinner);
		listView = (ListView) findViewById(R.id.listView);
		notes = (EditText) findViewById(R.id.noteinput);
		stat = new ArrayList<String>();
		stat.add(0, "Opened");
		stat.add(1, "Prepared");
		stat.add(2, "Closed");
		isAdmin = ontimedeliv.isAdmin(this);
		isPreparer = ontimedeliv.isPrep(this);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		ontimedeliv.clear("order");
		this.orderId = ontimedeliv.getOrderId(this);

		if (orderId != 0) {
			getCurrentOrder(orderId);
			submit = (Button) findViewById(R.id.submit);
			submit.setText("Update");
		}

		String orderStatus = ontimedeliv.getOrderStatus(this);
		actionBar.setTitle(orderStatus);
		if (!isAdmin) {
			disable(false);
		} else {

			if (orderStatus.equals("closed") || orderStatus.equals("cancelled"))
				disable(true);
		}

	}

	public void cancel(View v) {
		LayoutInflater li = LayoutInflater.from(getApplicationContext());
		View promptsView = li.inflate(R.layout.prompt_cancel, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				OrderInfoActivity.this);

		alertDialogBuilder.setView(promptsView);

		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.editText1);
		alertDialogBuilder.setCancelable(false).setPositiveButton("OK", null)
				.setNegativeButton("Cancel", null);

		alertDialog = alertDialogBuilder.create();
		alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {

				Button cancel_btn = alertDialog
						.getButton(AlertDialog.BUTTON_NEGATIVE);
				cancel_btn.setBackgroundColor(getResources().getColor(
						R.color.turquoise));
				cancel_btn.setTextColor(getResources().getColor(
						R.color.textview));
				Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setBackgroundColor(getResources().getColor(R.color.turquoise));
				b.setTextColor(getResources().getColor(R.color.textview));
				b.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						if (userInput.getText().toString() != null
								&& !userInput.getText().toString().isEmpty()) {
							Order order = new Order();
							order.setId(orderId);
							order.setCancel(true);
							String serverURL = new myURL("cancel", "orders",
									orderId, 0).getURL();

							RZHelper p = new RZHelper(serverURL,
									OrderInfoActivity.this, "cancelOrder", true);
							p.put(order);

						} else {
							Toast.makeText(getApplicationContext(),
									R.string.cancelreason, Toast.LENGTH_SHORT)
									.show();
						}
					}
				});
			}
		});

		alertDialog.show();

	}

	public void cancelOrder(String s, String Error) {
		alertDialog.dismiss();
		if (Error == null) {
			glob.bkToNav(OrderInfoActivity.this,
					getString(R.string.ordercanceled));
		}
	}

	public void getPreparers() {
		String serverURL = new myURL(null, "users", "preparers", 30).getURL();
		RZHelper p = new RZHelper(serverURL, this, "setPreparers", false, true);
		p.get();
	}

	public void setPreparers(String s, String error) {
		User empty = new User(0, "Select");
		ArrayList<User> userItems = new ArrayList<User>();
		userItems.add(empty);
		userItems.addAll(new APIManager().getUsers(s));

		ArrayAdapter<User> dataAdapter = new ArrayAdapter<User>(this,
				android.R.layout.simple_spinner_item, userItems);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prep.setAdapter(dataAdapter);
		if (currentOrder.getPreparer() != null)
			glob.setSelected(prep, dataAdapter, currentOrder.getPreparer());
		else
			glob.setSelected(prep, dataAdapter, empty);
	}

	public void getDelivery() {
		String serverURL = new myURL(null, "users", "deliverers", 30).getURL();
		RZHelper p = new RZHelper(serverURL, this, "setDeivery", false, false);
		p.get();
	}

	public void setDeivery(String s, String error) {
		User empty = new User(0, "Select");
		ArrayList<User> userItems = new ArrayList<User>();
		userItems.add(empty);
		userItems.addAll(new APIManager().getUsers(s));

		ArrayAdapter<User> dataAdapter = new ArrayAdapter<User>(this,
				android.R.layout.simple_spinner_item, userItems);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		deliv.setAdapter(dataAdapter);
		getPreparers();

		if (currentOrder.getDelivery() != null)
			glob.setSelected(deliv, dataAdapter, currentOrder.getDelivery());
		else
			glob.setSelected(deliv, dataAdapter, empty);
	}

	public void addItemsOnStatus() {
		List<String> list = new ArrayList<String>();
		list.add(getString(R.string.open_status));
		list.add(getString(R.string.prepared_status));
		list.add(getString(R.string.delivered_status));

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		status.setAdapter(dataAdapter);
		if (list.indexOf(currentOrder.getStatus()) > -1)
			status.setSelection(list.indexOf(currentOrder.getStatus()));
	}

	public void getCurrentOrder(int orderId) {
		String serverURL = new myURL(null, "orders", orderId, 0).getURL();
		RZHelper p = new RZHelper(serverURL, this, "setOrderInfo", true, false);
		p.get();
	}

	public void setOrderInfo(String s, String error) {
		currentOrder = new APIManager().getOrder(s);
		getDelivery();
		addItemsOnStatus();
		orderitem = currentOrder.getOrderItems();
		SPitems = new ArrayList<Item>();
		Item _Item;
		double total = 0;
		TextView totalTxt = (TextView) findViewById(R.id.allTotal);

		for (int i = 0; i < orderitem.size(); i++) {
			_Item = new Item(orderitem.get(i).getId(), orderitem.get(i)
					.toString(), orderitem.get(i).getQuantity(), orderitem.get(
					i).getUnitPrice());
			SPitems.add(_Item);
			total = total + orderitem.get(i).getTotalPrice();
		}
		dataAdapter = new OrderInfoAdapter(OrderInfoActivity.this,
				R.layout.row_order_info, SPitems, disabled);
		dataAdapter.setTotal(totalTxt);

		listView.setAdapter(dataAdapter);
		new Helper().getListViewSize(listView);
		totalTxt.setText(total + "");
		TextView customerName = (TextView) findViewById(R.id.customerName);
		customerName.setText(" " + currentOrder.getCustomer().toString());
		TextView customerAdd = (TextView) findViewById(R.id.customerAdd);
		customerAdd.setText(currentOrder.getAddress().toString());

		notes.setText(currentOrder.getNote());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.order_info, menu);
		SharedMenu.onCreateOptionsMenu(this, menu, getApplicationContext());
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (SharedMenu.onOptionsItemSelected(item, this) == false) {
			// handle local menu items here or leave blank
		}
		return super.onOptionsItemSelected(item);
	}

	public void submit(View view) {
		if (isAdmin) {
			ArrayList<OrderItem> newItems = new ArrayList<OrderItem>();
			OrderItem item;
			int quantity;
			View single;
			for (int i = 0; i < listView.getAdapter().getCount(); i++) {
				single = listView.getChildAt(i);
				quantity = Converter.toInt(((TextView) single
						.findViewById(R.id.quantity)).getText().toString());
				item = new OrderItem();
				item.setQuantity(quantity);
				item.setId(orderitem.get(i).getProduct().getId());
				item.setProduct(new Product(orderitem.get(i).getProduct()
						.getId()));
				newItems.add(item);
			}
			String serverURL = new myURL(null, "orders", orderId, 0).getURL();

			Order newOrder = new Order();
			newOrder.setId(currentOrder.getId());
			newOrder.setOrderItems(newItems);
			newOrder.setAddress_id(currentOrder.getAddress().getId());
			newOrder.setCustomer_id(currentOrder.getCustomer().getId());
			double total = Double
					.parseDouble(((TextView) findViewById(R.id.allTotal))
							.getText().toString());
			newOrder.setTotal(total);
			if (!newOrder.equals(currentOrder)) {
				RZHelper p = new RZHelper(serverURL, OrderInfoActivity.this,
						"updateStatus", true);
				p.put(newOrder);
			} else
				updateStatus(null, null);
		} else
			assign();
	}

	public void assign() {
		Order newOrder = new Order();
		if (isPreparer)
			newOrder.setStatus(stat.get(1));
		else
			newOrder.setStatus(stat.get(2));
		String serverURL = new myURL("change_status", "orders", orderId + "", 0)
				.getURL();
		RZHelper p = new RZHelper(serverURL, OrderInfoActivity.this, "done",
				true);
		p.put(newOrder);
	}

	public void updateStatus(String s, String error) {
		Order newOrder = new Order();

		User preparer = ((User) prep.getSelectedItem());
		User delivery = ((User) deliv.getSelectedItem());
		newOrder.setPreparer(preparer);
		newOrder.setDelivery(delivery);
		newOrder.setNote(notes.getText().toString());
		newOrder.setStatus(stat.get(status.getSelectedItemPosition()));
		String serverURL = new myURL("assign", "orders", orderId + "", 0)
				.getURL();
		RZHelper p = new RZHelper(serverURL, OrderInfoActivity.this, "done",
				true);
		p.put(newOrder);
	}

	public void done(String s, String error) {
		glob.bkToNav(this, getString(R.string.order_updated));
	}

	public void disable(boolean closed) {
		status.setEnabled(false);
		status.setClickable(false);
		prep.setEnabled(false);
		prep.setClickable(false);
		deliv.setEnabled(false);
		deliv.setClickable(false);
		listView.setEnabled(false);
		listView.setClickable(false);
		notes.setEnabled(false);
		notes.setClickable(false);
		cancel = (Button) findViewById(R.id.cancel);
		ViewGroup layout = (ViewGroup) cancel.getParent();
		layout.removeView(cancel);
		if (isPreparer)
			submit.setText("Prepared");
		else if (!isAdmin)
			submit.setText("Delivered");
		if (closed) {
			((ViewGroup) submit.getParent()).removeView(submit);
		}
		disabled = true;
	}
}
