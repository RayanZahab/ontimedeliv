package com.example.ontimedeliv;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class OrderInfoActivity extends Activity {
	Spinner prep, deliv, status;
	Button cancel;
	OrdersAdapter dataAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_info);

		addItemsOndelivery();
		addItemsOnpreparer();
		addItemsOnStatus();
		displayListView();

		cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				
				LayoutInflater li = LayoutInflater
						.from(getApplicationContext());
				View promptsView = li.inflate(R.layout.prompt_cancel, null);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OrderInfoActivity.this);

				// set prompts.xml to alertdialog builder
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

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

			}
		});

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
		dataAdapter = new OrdersAdapter(this, R.layout.row_order_info,
				orderitem);
		ListView listView = (ListView) findViewById(R.id.orderlist);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		/*
		 * listView.setOnItemClickListener(new OnItemClickListener() { // set
		 * dialog message
		 * alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new
		 * DialogInterface.OnClickListener() { public void
		 * onClick(DialogInterface dialog, int id) {
		 * Toast.makeText(getApplicationContext(), userInput.getText(),
		 * Toast.LENGTH_LONG) .show(); dialog.cancel(); }
		 * 
		 * public void onItemClick(AdapterView<?> parent, View view, int
		 * position, long id) { // When clicked, Navigate to the selected item
		 * Item navitem = (Item) parent.getItemAtPosition(position); String
		 * title = navitem.getTitle(); Intent i; try { i = new
		 * Intent(getBaseContext(), Class.forName(getPackageName() + "." + title
		 * + "Activity")); startActivity(i); } catch (ClassNotFoundException e)
		 * { // TODO Auto-generated catch block e.printStackTrace(); } }
		 * 
		 * });
		 */

	}

}
