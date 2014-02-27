package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CategoriesActivity extends Activity {

	CheckboxAdapter dataAdapter = null;
	int branchId ; 
	ArrayList<Category> categories;
	ArrayList<Item> categoryItems;
	String url = "http://enigmatic-springs-5176.herokuapp.com/api/v1/categories";
	@Override
	public void onCreate(Bundle savedInstancecat) {
		super.onCreate(savedInstancecat);
		setContentView(R.layout.activity_categories);
		if(getIntent().hasExtra("branchId"))
		{
			Bundle extras = getIntent().getExtras();
			branchId = Integer.parseInt( (String) extras.getString("branchId") );
			Log.d("ray","ray branch:"+branchId);
			url = "http://enigmatic-springs-5176.herokuapp.com/api/v1/branches/"+branchId+"/categories";
		}
		// Generate list View from ArrayList
		getCategories();
		checkButtonClick();

	}
	

	private void displayListView() {

		Bitmap picture = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
		
		// Array list of Categories
		ArrayList<Item> categories = new ArrayList<Item>();

		/*Item _Item = new Item(0,picture,"Lou7um");
		categories.add(_Item);
		_Item = new Item(0,picture, "Alban&Ajban");
		categories.add(_Item);
		_Item = new Item(0,picture, "Drinks");
		categories.add(_Item);*/
		
		categoryItems = new ArrayList<Item>();
		for (int i = 0; i < categories.size(); i++) {
			categoryItems.add(new Item(categories.get(i).getId(), picture, categories.get(i)
					.toString()));
		}

		// create an ArrayAdaptar from the String Array
		dataAdapter = new CheckboxAdapter(this, R.layout.category_info, categoryItems);
		ListView listView = (ListView) findViewById(R.id.categorylist);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				Item cat = (Item) parent.getItemAtPosition(position);
				Toast.makeText(getApplicationContext(),
						"Clicked on : " + cat.getTitle(), Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	
	private void checkButtonClick() {

		Button myButton = (Button) findViewById(R.id.submit);

		myButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				StringBuffer responseText = new StringBuffer();
				responseText.append("Selected Categories are...\n");
		
				ArrayList<Item> stateList = dataAdapter.getCurrentList();
		
				for (int i = 0; i < stateList.size(); i++) {
					Item cat = stateList.get(i);
		
					if (cat.isSelected()) {
						responseText.append("\n" + cat.getTitle());
					}
				}
		
				Toast.makeText(getApplicationContext(), responseText,
						Toast.LENGTH_LONG).show();

			}
		});
	}
	public void getCategories() {
		String serverURL = this.url;
		Log.d("rays","ray url"+this.url);
		ProgressDialog Dialog = new ProgressDialog(this);

		new MyJs(Dialog, "setCategories", this, "GET").execute(serverURL);
	}
 
	public void setCategories(String s) {
		Bitmap picture = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.user);
		categories = new APIManager().getCategoriesByBranch(s);
		categoryItems = new ArrayList<Item>();

		for (int i = 0; i < categories.size(); i++) {
			categoryItems.add(new Item(categories.get(i).getId(), picture, categories.get(i)
					.toString()));
		}
		// create an ArrayAdaptar from the String Array
		dataAdapter = new CheckboxAdapter(this, R.layout.category_info, categoryItems);
		ListView listView = (ListView) findViewById(R.id.categorylist);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, Navigate to the selected item
				Toast.makeText(getApplicationContext(),
						"Selected" + categoryItems.get(position).getId(),
						Toast.LENGTH_SHORT).show();
				Intent i;
				try {
					i = new Intent(getBaseContext(), Class
							.forName(getPackageName() + "."
									+ "CategoriesActivity"));
					i.putExtra("branchId", categoryItems.get(position).getId());
					startActivity(i);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.categories, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		
		final EditText result = null;
		
		// get prompts.xml view
		LayoutInflater li = LayoutInflater.from(this);
		View promptsView = li.inflate(R.layout.prompt_addcategory, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.editTextDialogUserInput);

		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								// get user input and set it to result
								// edit text
								//result.setText(userInput.getText());
								Toast.makeText(getApplicationContext(), userInput.getText(),
										Toast.LENGTH_LONG).show();
								dialog.cancel();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	
	    return super.onOptionsItemSelected(item);
	}
}
