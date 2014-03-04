package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ProductActivity extends Activity {

	CheckboxAdapter dataAdapter = null;
	int categoryId, branchId,shopId;
	ArrayList<Product> products;
	ArrayList<Item> productItems;
	String url = new myURL().getURL("products", null, 0, 30);
	DialogInterface dialog;

	@Override
	public void onCreate(Bundle savedInstancecat) {
		super.onCreate(savedInstancecat);
		setContentView(R.layout.activity_product);
		if ( getIntent().hasExtra("categoryId")) {
			Bundle extras = getIntent().getExtras();
			try {
				categoryId = Integer.parseInt((String) extras.getString("categoryId"));
				branchId = Integer.parseInt((String) extras.getString("branchId"));
				shopId = Integer.parseInt((String) extras.getString("shopId"));
				Log.d("ray", "ray branch:" + categoryId);
				
				url =  new myURL().getURL("items", "branches/"+branchId+"/categories", categoryId,30);
				Toast.makeText(getApplicationContext(),
						"Selected: " + branchId,
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) {

			}
		}
		// Generate list View from ArrayList
		getProducts();
		checkButtonClick();

	}

	public DialogInterface getDialog() {
		return this.dialog;
	}

	public void setDialog(DialogInterface dialog) {
		this.dialog = dialog;
	}

	private void checkButtonClick() {

		Button myButton = (Button) findViewById(R.id.submit);

		myButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				StringBuffer responseText = new StringBuffer();
				responseText.append("Selected Product are...\n");

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

	public void getProducts() {
		String serverURL = this.url;
		Log.d("rays", "ray url" + this.url);
		ProgressDialog Dialog = new ProgressDialog(this);

		new MyJs(Dialog, "setProducts", this, "GET").execute(serverURL);
	}

	public void setProducts(String s) {
		Bitmap picture = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.user);
		products = new APIManager().getItemsByCategoryAndBranch(s);
		productItems = new ArrayList<Item>();

		for (int i = 0; i < products.size(); i++) {
			productItems.add(new Item(products.get(i).getId(), picture,
					products.get(i).toString(),products.get(i).isAvailable()));
			
		}
		// create an ArrayAdaptar from the String Array
		dataAdapter = new CheckboxAdapter(this, R.layout.category_info,
				productItems);
		ListView listView = (ListView) findViewById(R.id.categorylist);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, Navigate to the selected item
				Toast.makeText(getApplicationContext(),
						"Selected" + productItems.get(position).getId(),
						Toast.LENGTH_SHORT).show();
				/*
				 * Intent i; try { i = new Intent(getBaseContext(), Class
				 * .forName(getPackageName() + "." + "CategoriesActivity"));
				 * i.putExtra("branchId", productItems.get(position).getId());
				 * startActivity(i); } catch (ClassNotFoundException e) { //
				 * TODO Auto-generated catch block e.printStackTrace(); }
				 */
			}

		});

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.categories, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this, AddProductActivity.class);
		intent.putExtra("shopId", ""+ shopId);
		intent.putExtra("branchId", ""+ branchId);
		intent.putExtra("categoryId", ""+ categoryId);
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}

	public void addCategory(String categoryName) {
		String serverURL = new myURL().getURL("categories", null, 0, 0);
		ProgressDialog Dialog = new ProgressDialog(this);
		Category newCategory = new Category(0, categoryName,true,0);
		new MyJs(Dialog, "afterCreation", this, "POST", (Object) newCategory)
				.execute(serverURL);
	}

	public void afterCreation(String s) {
		Intent i = new Intent(this, CategoriesActivity.class);
		startActivity(i);
	}
}
