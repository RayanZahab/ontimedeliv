package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
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
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ProductsActivity extends Activity {

	CheckboxAdapter dataAdapter = null;
	int categoryId, branchId, shopId;
	ArrayList<Product> products;
	ArrayList<Item> productItems;
	String url;
	DialogInterface dialog;
	ProgressDialog Dialog;
	ArrayList<Integer> selectedIds = new ArrayList<Integer>();
	ArrayList<Integer> unselectedIds = new ArrayList<Integer>();
	Activate myProd;

	@Override
	public void onCreate(Bundle savedInstancecat) {
		super.onCreate(savedInstancecat);
		Dialog = new ProgressDialog(this);
		Dialog.setCancelable(false);
		setContentView(R.layout.activity_product);
		if (getIntent().hasExtra("categoryId")) {
			Bundle extras = getIntent().getExtras();
			try {
				categoryId = Integer.parseInt((String) extras
						.getString("categoryId"));
				branchId = Integer.parseInt((String) extras
						.getString("branchId"));
				shopId = Integer.parseInt((String) extras.getString("shopId"));
				Log.d("ray", "ray branch:" + categoryId);

				url = new myURL("items",
						"branches/" + branchId + "/categories", categoryId, 30)
						.getURL();
				Toast.makeText(getApplicationContext(),
						"Selected: " + branchId, Toast.LENGTH_SHORT).show();
			} catch (Exception e) {

			}
		}
		// Generate list View from ArrayList
		getProducts();
		//checkButtonClick();

	}

	public DialogInterface getDialog() {
		return this.dialog;
	}

	public void setDialog(DialogInterface dialog) {
		this.dialog = dialog;
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(ProductsActivity.this, CategoriesActivity.class);
		i.putExtra("shopId", "" + shopId);
		i.putExtra("branchId", "" + branchId);
		i.putExtra("categoryId", "" + categoryId);
		startActivity(i);
	}
	public void submit(View v) {

		StringBuffer responseText = new StringBuffer();
		responseText.append("Selected Products are...\n");

		ArrayList<Item> stateList = dataAdapter.getCurrentList();

		for (int i = 0; i < stateList.size(); i++) {
			Item cat = stateList.get(i);
			if (cat.isSelected()) {
				selectedIds.add(cat.getId());
			} else {
				unselectedIds.add(cat.getId());
			}
		}
		myProd = new Activate("items",unselectedIds);

		Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG)
				.show();

		String serverURL = new myURL("deactivate_items", "branches",
				branchId, 0).getURL();
		new MyJs(Dialog, "afterDeactivate", this, "PUT", (Object) myProd, true)
				.execute(serverURL);
	}
	public void afterDeactivate(String s,String error) {
		Toast.makeText(getApplicationContext(), "DeActivate : " + s,
				Toast.LENGTH_SHORT).show();
		myProd = new Activate("items",selectedIds);
		String serverURL = new myURL("activate_items", "branches",
				branchId, 0).getURL();

		new MyJs(Dialog, "afterActivate", this, "PUT", (Object) myProd)
				.execute(serverURL);
	}

	public void afterActivate(String s,String error) {
		Toast.makeText(getApplicationContext(), "Activate : " + s,
				Toast.LENGTH_SHORT).show();
	}
	private void checkButtonClick() {

		Button myButton = (Button) findViewById(R.id.submit);

		myButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				StringBuffer responseText = new StringBuffer();

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
		new MyJs(Dialog, "setProducts", this, "GET").execute(serverURL);
	}

	public void setProducts(String s,String error) {
		Bitmap picture = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.user);
		products = new APIManager().getItemsByCategoryAndBranch(s);
		productItems = new ArrayList<Item>();

		for (int i = 0; i < products.size(); i++) {
			productItems.add(new Item(products.get(i).getId(), picture,
					products.get(i).toString(), products.get(i).isAvailable()));

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
				Intent intent = new Intent(ProductsActivity.this,
						ProductInfoActivity.class);
				intent.putExtra("shopId", "" + shopId);
				intent.putExtra("branchId", "" + branchId);
				intent.putExtra("categoryId", "" + categoryId);
				intent.putExtra("productId", ""
						+ productItems.get(position).getId());
				startActivity(intent);
			}

		});

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.categories, menu);
		SharedMenu.onCreateOptionsMenu(menu, getApplicationContext());
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this, ProductInfoActivity.class);
		intent.putExtra("shopId", "" + shopId);
		intent.putExtra("branchId", "" + branchId);
		intent.putExtra("categoryId", "" + categoryId);
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}

	public void addCategory(String categoryName) {
		String serverURL = new myURL("categories", null, 0, 0).getURL();
		Category newCategory = new Category(0, categoryName, true, 0);
		new MyJs(Dialog, "afterCreation", this, "POST", (Object) newCategory)
				.execute(serverURL);
	}

	public void afterCreation(String s,String error) {
		Intent i = new Intent(this, CategoriesActivity.class);
		startActivity(i);
	}
}
