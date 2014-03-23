package com.example.ontimedeliv;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
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

public class OrderInfoActivity extends Activity {
	Spinner prep, deliv, status;
	Button cancel;
	OrderInfoAdapter dataAdapter;
	int orderId;
	ProgressDialog Dialog;
	AlertDialog alertDialog;
	Order currentOrder;
	GlobalM glob = new GlobalM();
	ArrayList<OrderItem> orderitem;
	ArrayList<Item> SPitems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_info);

		Dialog = new ProgressDialog(this);
		Dialog.setCancelable(false);

		this.orderId = ((ontimedeliv) this.getApplication()).getOrderId();
		if (orderId != 0) {
			getCurrentOrder(orderId);
			Button submit = (Button) findViewById(R.id.submit);
			submit.setText("Update");
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

				Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						if (userInput.getText().toString() != null
								&& !userInput.getText().toString().isEmpty()) {
							Order order = new Order();
							order.setId(orderId);
							order.setCancel(true);
							String serverURL = new myURL("cancel", "orders", 6,
									0).getURL();
							new MyJs(Dialog, "cancelOrder",
									OrderInfoActivity.this,
									((ontimedeliv) OrderInfoActivity.this
											.getApplication()), "PUT",
									(Object) order).execute(serverURL);

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

		if (Error == null) {
			Toast.makeText(getApplicationContext(), R.string.ordercanceled,
					Toast.LENGTH_SHORT).show();
			alertDialog.dismiss();
		} else {
			alertDialog.dismiss();
		}
	}

	public void getPreparers() {
		String serverURL = new myURL(null, "users", "preparers", 30).getURL();
		new MyJs(Dialog, "serPreparers", this,
				((ontimedeliv) this.getApplication()), "GET")
				.execute(serverURL);
	}

	public void serPreparers(String s, String error) {
		ArrayList<User> userItems = new APIManager().getUsers(s);

		prep = (Spinner) findViewById(R.id.preparer_spinner);

		ArrayAdapter<User> dataAdapter = new ArrayAdapter<User>(this,
				android.R.layout.simple_spinner_item, userItems);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prep.setAdapter(dataAdapter);
		// glob.setSelected(deliv, dataAdapter, new
		// User(currentOrder.getPreparer().getId()));
	}

	public void getDelivery() {
		String serverURL = new myURL(null, "users", "deliverers", 30).getURL();
		new MyJs(Dialog, "setDeivery", this,
				((ontimedeliv) this.getApplication()), "GET")
				.execute(serverURL);
	}

	public void setDeivery(String s, String error) {
		ArrayList<User> userItems = new APIManager().getUsers(s);
		deliv = (Spinner) findViewById(R.id.delivery_Spinner);

		ArrayAdapter<User> dataAdapter = new ArrayAdapter<User>(this,
				android.R.layout.simple_spinner_item, userItems);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		deliv.setAdapter(dataAdapter);
		// glob.setSelected(deliv, dataAdapter, new
		// User(currentOrder.getDelivery().getId()));
	}

	public void addItemsOnStatus() {
		status = (Spinner) findViewById(R.id.order_status);
		List<String> list = new ArrayList<String>();
		list.add("Open");
		list.add("Prepared");
		list.add("Delivered");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		status.setAdapter(dataAdapter);
		if (list.indexOf(currentOrder.getStatus()) > -1)
			status.setSelection(list.indexOf(currentOrder.getStatus()));
	}

	public void getCurrentOrder(int orderId) {
		String serverURL = new myURL(null, "orders", orderId, 30).getURL();
		new MyJs(Dialog, "setOrderInfo", this,
				((ontimedeliv) this.getApplication()), "GET", true)
				.execute(serverURL);
	}

	public void setOrderInfo(String s, String error) {
		currentOrder = new APIManager().getOrder(s);
		getDelivery();
		getPreparers();
		addItemsOnStatus();
		orderitem = currentOrder.getOrderItems();
		SPitems = new ArrayList<Item>();
		Item _Item;
		double total = 0;
		TextView totalTxt = (TextView) findViewById(R.id.total);

		for (int i = 0; i < orderitem.size(); i++) {
			_Item = new Item(orderitem.get(i).getId(), orderitem.get(i)
					.toString(), orderitem.get(i).getQuantity(), orderitem.get(
					i).getUnitPrice());
			SPitems.add(_Item);
			total = total + orderitem.get(i).getTotalPrice();
		}
		dataAdapter = new OrderInfoAdapter(OrderInfoActivity.this,
				R.layout.row_order_info, SPitems, true);
		dataAdapter.setTotal(totalTxt);

		ListView listView = (ListView) findViewById(R.id.listView);

		listView.setAdapter(dataAdapter);
		Helper.getListViewSize(listView);
		totalTxt.setText(total + "");
		TextView customerName = (TextView) findViewById(R.id.customerName);
		customerName.append(" " + currentOrder.getCustomer().toString());
		TextView customerAdd = (TextView) findViewById(R.id.customerAdd);
		customerAdd
				.append(" This is add"/* currentOrder.getAddress().toString() */);
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

	public void submit(View view) {
		ListView listView = (ListView) findViewById(R.id.listView);
		ArrayList<OrderItem> newItems = new ArrayList<OrderItem>();
		OrderItem item;
		int quantity;
		View single;
		for (int i = 0; i < listView.getAdapter().getCount(); i++) {
			single = listView.getChildAt(i);
			quantity = Integer.parseInt(((EditText) single
					.findViewById(R.id.quantity)).getText().toString());
			item = new OrderItem();
			item.setQuantity(quantity);
			Log.d("rays","rays item"+
			 orderitem.get(i).getProduct().getId());
			item.setId(orderitem.get(i).getProduct().getId());
			newItems.add(item);
		}
		String serverURL = new myURL(null, "orders", orderId, 0).getURL();

		Order newOrder = new Order();
		newOrder.setId(currentOrder.getId()); 
		newOrder.setOrderItems(newItems);
		newOrder.setAddress_id(currentOrder.getAddress().getId());
		newOrder.setCustomer_id(currentOrder.getCustomer().getId());
		double total = Double.parseDouble(((TextView) findViewById(R.id.total))
				.getText().toString());
		newOrder.setTotal(total);
		new MyJs(Dialog, "updateStatus", this,
				((ontimedeliv) this.getApplication()), "PUT", newOrder)
				.execute(serverURL);
	}

	public void updateStatus(String s, String error) {
		Order newOrder = new Order();
		status = (Spinner) findViewById(R.id.order_status);
		newOrder.setStatus(status.getSelectedItem().toString());
		// newOrder.setCustomer_id();
		String serverURL = new myURL("change_status", "orders", orderId + "", 0)
				.getURL();
		new MyJs(Dialog, "done", this,((ontimedeliv)
		 this.getApplication()), "PUT",newOrder).execute(serverURL);
	}
	public void done(String s, String error)
	{
		Log.d("rays","ray done: "+s);
	}
}
