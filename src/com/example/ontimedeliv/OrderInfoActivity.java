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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_info);

		addItemsOndelivery();
		addItemsOnpreparer();
		addItemsOnStatus();

		Dialog = new ProgressDialog(this);
		Dialog.setCancelable(false);
		Bundle extras = getIntent().getExtras();
		if (getIntent().hasExtra("orderId")) {
			try {
				orderId = Integer
						.parseInt((String) extras.getString("orderId"));
				getCurrentOrder(orderId);
				Button submit = (Button) findViewById(R.id.submit);
				submit.setText("Update");
			} catch (Exception e) {

			}
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
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Toast.makeText(getApplicationContext(),
								userInput.getText(), Toast.LENGTH_LONG).show();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	// add items into spinner dynamically
	public void addItemsOndelivery() {
		deliv = (Spinner) findViewById(R.id.delivery_Spinner);
		List<String> list = new ArrayList<String>();
		list.add("deliv 1");
		list.add("deliv 2");
		list.add("deliv 3");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		deliv.setAdapter(dataAdapter);
	}

	public void addItemsOnpreparer() {
		prep = (Spinner) findViewById(R.id.preparer_spinner);
		List<String> list = new ArrayList<String>();
		list.add("prep 1");
		list.add("prep 2");
		list.add("prep 3");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prep.setAdapter(dataAdapter);
	}

	public void addItemsOnStatus() {
		status = (Spinner) findViewById(R.id.order_status);
		List<String> list = new ArrayList<String>();
		list.add("Prepared");
		list.add("Delivered");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		status.setAdapter(dataAdapter);
	}

	public void getCurrentOrder(int orderId) {
		String serverURL = new myURL(null, "orders", orderId, 30).getURL();
		new MyJs(Dialog, "setOrderInfo", this, "GET").execute(serverURL);
	}

	public void setOrderInfo(String s, String error) {
		Order currentOrder = new APIManager().getOrder(s);
		ArrayList<OrderItem> orderitem = currentOrder.getOrderItems();
		ArrayList<Item> items = new ArrayList<Item>();
		Item _Item;
		double total = 0;
		TextView totalTxt = (TextView) findViewById(R.id.total);

		for (int i = 0; i < orderitem.size(); i++) {
			_Item = new Item(orderitem.get(i).getId(),
					orderitem.get(i).toString(),
					orderitem.get(i).getQuantity(),
					orderitem.get(i).getUnitPrice());
			items.add(_Item);

			total = total + orderitem.get(i).getTotalPrice();
			dataAdapter = new OrderInfoAdapter(OrderInfoActivity.this,
					R.layout.row_order_info, items);
			dataAdapter.setTotal(totalTxt);

			ListView listView = (ListView) findViewById(R.id.listView);

			listView.setAdapter(dataAdapter);
			Helper.getListViewSize(listView);
		}

		totalTxt.setText(total + "");
		TextView customerName = (TextView) findViewById(R.id.customerName);
		customerName.append(" " + currentOrder.getCustomer().toString());
		TextView customerAdd = (TextView) findViewById(R.id.customerAdd);
		customerAdd
				.append(" This is add"/* currentOrder.getAddress().toString() */);
	}

}
