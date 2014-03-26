package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class ProductsActivity extends Activity {

	CheckboxAdapter dataAdapter = null;
	int categoryId, branchId, shopId;
	ArrayList<Product> products;
	ArrayList<Item> productItems;
	String url;
	DialogInterface dialog;
	ArrayList<Integer> selectedIds = new ArrayList<Integer>();
	ArrayList<Integer> unselectedIds = new ArrayList<Integer>();
	Activate myProd;

	@Override
	public void onCreate(Bundle savedInstancecat) {
		super.onCreate(savedInstancecat);
		setContentView(R.layout.activity_product);
		ActionBar actionBar = getActionBar();		 
        actionBar.setDisplayHomeAsUpEnabled(true);
        ((ontimedeliv) this.getApplication()).clear("products");
		categoryId = ((ontimedeliv) this.getApplication()).getCategoryId();
		if (categoryId!=0) {
			branchId = ((ontimedeliv) this.getApplication()).getBranchId();
			shopId = ((ontimedeliv) this.getApplication()).getShopId();

			url = new myURL("items",
					"branches/" + branchId + "/categories", categoryId, 30)
					.getURL();
			
			getProducts();
		}
		else 
		{
			//go back to categories page!
		}

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
		((ontimedeliv) ProductsActivity.this.getApplication()).setCategoryId(0);
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
		new MyJs("afterDeactivate", this,((ontimedeliv) this.getApplication()), "PUT", (Object) myProd, true,false)
				.execute(serverURL);
	}
	public void afterDeactivate(String s,String error) {
		Toast.makeText(getApplicationContext(), "DeActivate : " + s,
				Toast.LENGTH_SHORT).show();
		myProd = new Activate("items",selectedIds);
		String serverURL = new myURL("activate_items", "branches",
				branchId, 0).getURL();

		new MyJs("afterActivate", this ,((ontimedeliv) this.getApplication()), "PUT", (Object) myProd,false,true)
				.execute(serverURL);
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.cat_context_menu, menu);
	}
	
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.edit:
			Edit(productItems.get((int) info.id));
			break;
		case R.id.delete:
			Delete(productItems.get((int) info.id).getId());
			break;
		default:
			break;

		}
		return true;
	}

	public void Delete(final int branchId) {

		new AlertDialog.Builder(this)
				.setTitle(R.string.deletethisprod)
				.setIcon(R.drawable.branches)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								String serverURL = new myURL(null, "branches",branchId, 0).getURL();
								new MyJs("afterDelete",
								ProductsActivity.this,((ontimedeliv) ProductsActivity.this.getApplication()), "DELETE",true,true)
								.execute(serverURL);
							}
						}).setNegativeButton(android.R.string.no, null).show();
	}

	public void afterDelete(String s, String error) {
		backToActivity(BranchesActivity.class);
	}
	
	public void backToActivity(Class activity) {
		Intent i = new Intent(ProductsActivity.this, activity);
		startActivity(i);
	}

	public void Edit(Item item) {
		Intent i = new Intent(ProductsActivity.this, ProductInfoActivity.class);
		((ontimedeliv) this.getApplication()).setProductId(item.getId());
		startActivity(i);
	}

	public void afterActivate(String s,String error) {
		Toast.makeText(getApplicationContext(), R.string.activate + s,
				Toast.LENGTH_SHORT).show();
	}

	public void getProducts() {
		String serverURL = this.url;
		new MyJs("setProducts", this,((ontimedeliv) this.getApplication()), "GET").execute(serverURL);
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
		registerForContextMenu(listView);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, Navigate to the selected item
				Toast.makeText(getApplicationContext(),
						R.string.selected + productItems.get(position).getId(),
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(ProductsActivity.this,
						ProductInfoActivity.class);
				((ontimedeliv) ProductsActivity.this.getApplication()).setProductId(productItems.get(position).getId());
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
		((ontimedeliv) ProductsActivity.this.getApplication()).setProductId(0);
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}
}
