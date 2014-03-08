package com.example.ontimedeliv;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CategoriesActivity extends Activity {

	CheckboxAdapter dataAdapter = null;
	int branchId, shopId;
	ArrayList<Category> categories;
	ArrayList<Item> categoryItems;
	String url = new myURL().getURL("categories", null, 0, 30);
	DialogInterface dialog;
	ArrayList<Integer> selectedIds =new ArrayList<Integer>();
	ArrayList<Integer> unselectedIds =new ArrayList<Integer>();
	Activate myCat;
	ProgressDialog Dialog;
	@Override
	public void onCreate(Bundle savedInstancecat) {
		super.onCreate(savedInstancecat);
		setContentView(R.layout.activity_categories);
		this.Dialog = new ProgressDialog(CategoriesActivity.this);
		if (getIntent().hasExtra("branchId")) {
			Bundle extras = getIntent().getExtras();
			try {
				this.branchId = Integer.parseInt((String) extras
						.getString("branchId"));
				this.shopId = Integer.parseInt((String) extras
						.getString("shopId"));
				url = new myURL()
						.getURL("categories", "branches", branchId, 30);

			} catch (Exception e) {

			}
		}
		getCategories();

	}

	public DialogInterface getDialog() {
		return this.dialog;
	}

	public void setDialog(DialogInterface dialog) {
		this.dialog = dialog;
	}

	public void submit(View v) {

		StringBuffer responseText = new StringBuffer();
		responseText.append("Selected Categories are...\n");

		ArrayList<Item> stateList = dataAdapter.getCurrentList();
		
		
		for (int i = 0; i < stateList.size(); i++) {
			Item cat = stateList.get(i);
			if (cat.isSelected()) {
				responseText.append("\n" + cat.getTitle()+"->"+cat.getId());
				selectedIds.add( cat.getId());
			}else{
				unselectedIds.add(cat.getId());
			}
		}
		myCat= new Activate(selectedIds,unselectedIds);

		Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG)
				.show();
		
		String serverURL =new myURL().getURL("deactivate_categories", "branches", branchId, 0);		
		

		new MyJs(Dialog, "afterDeactivate", this, "PUT",(Object)myCat).execute(serverURL);
	}
	public void afterDeactivate(String s)
	{
		Toast.makeText(getApplicationContext(),
				"DeActivate : " + s, Toast.LENGTH_SHORT).show();
		String serverURL =new myURL().getURL("activate_categories", "branches", branchId, 0);
		
		new MyJs(Dialog, "afterActivate", this, "PUT",(Object)myCat).execute(serverURL);
	}
	public void afterActivate(String s )
	{
		Toast.makeText(getApplicationContext(),
				"Activate : " + s, Toast.LENGTH_SHORT).show();			
	}

	public void getCategories() {
		String serverURL = this.url;

		new MyJs(Dialog, "setCategories", this, "GET").execute(serverURL);
	}

	public void setCategories(String s) {
		Bitmap picture = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.user);
		categories = new APIManager().getCategoriesByBranch(s);
		categoryItems = new ArrayList<Item>();

		for (int i = 0; i < categories.size(); i++) {
			categoryItems
					.add(new Item(categories.get(i).getId(), picture,
							categories.get(i).toString(), categories.get(i)
									.isActive()));
		}
		// create an ArrayAdaptar from the String Array
		dataAdapter = new CheckboxAdapter(this, R.layout.category_info,
				categoryItems);
		ListView listView = (ListView) findViewById(R.id.categorylist);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, Navigate to the selected item
				Toast.makeText(getApplicationContext(),
						"Selected: " + branchId, Toast.LENGTH_SHORT).show();
				Intent i = new Intent(getBaseContext(), ProductActivity.class);
				i.putExtra("categoryId", ""
						+ categoryItems.get(position).getId());
				i.putExtra("branchId", "" + branchId);
				i.putExtra("shopId", "" + shopId);
				startActivity(i);
			}

		});

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.categories, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
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
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						setDialog(dialog);
						Toast.makeText(getApplicationContext(),
								userInput.getText(), Toast.LENGTH_LONG).show();
						addCategory(userInput.getText().toString(), shopId);
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

		return super.onOptionsItemSelected(item);
	}

	public void addCategory(String categoryName, int shopId) {
		String serverURL = new myURL().getURL("categories", null, 0, 0);
		Category newCategory = new Category(0, categoryName, true, shopId);
		new MyJs(Dialog, "afterCreation", this, "POST", (Object) newCategory)
				.execute(serverURL);
	}

	public void afterCreation(String s) {
		Intent i = new Intent(this, CategoriesActivity.class);
		i.putExtra("branchId", "" + branchId);

		i.putExtra("shopId", "" + shopId);
		startActivity(i);
	}
}
