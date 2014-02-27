package com.example.ontimedeliv;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class OrderInfoActivity extends Activity {
	Spinner prep, deliv, status;
	Button cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_info);

		addItemsOndelivery();
		onCancelClick();
		addItemsOnpreparer();

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
		deliv.setAdapter(dataAdapter);
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
		deliv.setAdapter(dataAdapter);
	}

	private void onCancelClick() {

		cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				final EditText result = null;
				LayoutInflater li = LayoutInflater
						.from(getApplicationContext());
				View promptsView = li.inflate(R.layout.prompt_cancel, null);

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getApplicationContext());

				// set prompts_cancel.xml to alertdialog builder
				alertDialogBuilder.setView(promptsView);

				final EditText userInput = (EditText) promptsView
						.findViewById(R.id.editTextDialogUserInput);

				// set dialog message
				alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// get user input and set it to result
								// edit text
								result.setText(userInput.getText());
							}

						});
			}
		});

	}

}
