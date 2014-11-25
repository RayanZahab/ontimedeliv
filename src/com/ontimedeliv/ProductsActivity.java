package com.ontimedeliv;

import java.util.ArrayList;



import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
		if (categoryId != 0) {
			branchId = ((ontimedeliv) this.getApplication()).getBranchId();
			shopId = ((ontimedeliv) this.getApplication()).getShopId();

			url = new myURL("items", "branches/" + branchId + "/categories",
					categoryId, 30).getURL();

			getProducts();
		} else {
			// go back to categories page!
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
		SearchView searchView = (SearchView) SharedMenu.menu.findItem(
				R.id.action_search).getActionView();

		if (!searchView.isIconified()) {
			searchView.setIconified(true);
		} else {
			Intent i = new Intent(ProductsActivity.this,
					CategoriesActivity.class);
			((ontimedeliv) ProductsActivity.this.getApplication())
					.setCategoryId(0);
			startActivity(i);
		}
	}

	public void submit(View v) {
		unselectedIds = dataAdapter.getUnselectedList();
		if (unselectedIds.size() > 0) {
			myProd = new Activate("items", unselectedIds);

			String serverURL = new myURL("deactivate_items", "branches",
					branchId, 0).getURL();
			// new MyJs("afterDeactivate", this,
			// ((ontimedeliv) this.getApplication()), "PUT",
			// (Object) myProd, true, false).execute(serverURL);

			RZHelper p = new RZHelper(serverURL, this, "afterDeactivate");
			p.put(myProd);
		} else {
			afterDeactivate("", null);
		}

	}

	public void afterDeactivate(String s, String error) {
		selectedIds = dataAdapter.getSelectedList();
		if (selectedIds.size() > 0) {
			myProd = new Activate("items", selectedIds);

			String serverURL = new myURL("activate_items", "branches",
					branchId, 0).getURL();

			// new MyJs("afterActivate", this,
			// ((ontimedeliv) this.getApplication()), "PUT",
			// (Object) myProd, false, true).execute(serverURL);

			RZHelper p = new RZHelper(serverURL, this, "afterDeactivate");
			p.put(myProd);
		} else {
			afterActivate("DONE", null);
		}
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
			Delete((int) info.id);
			break;
		default:
			break;

		}
		return true;
	}

	public void Delete(final int position) {
		final int productId = productItems.get(position).getId();
		new AlertDialog.Builder(this)
				.setTitle(R.string.deletethisprod)
				.setIcon(R.drawable.branches)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								String serverURL = new myURL(null, "items",
										productId, 0).getURL();
								// new MyJs("afterDelete",
								// ProductsActivity.this,
								// ((ontimedeliv) ProductsActivity.this
								// .getApplication()), "DELETE",
								// true, true).execute(serverURL);

								RZHelper p = new RZHelper(serverURL,
										ProductsActivity.this, "afterDelete");
								p.delete();
								productItems.remove(position);
							}
						}).setNegativeButton(android.R.string.no, null).show();
	}

	public void afterDelete(String s, String error) {
		dataAdapter.currentList = productItems;
		dataAdapter.tmpList = productItems;
		dataAdapter.notifyDataSetChanged();
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

	public void afterActivate(String s, String error) {
		onBackPressed();
	}

	public void getProducts() {
		String serverURL = this.url;
		// new MyJs("setProducts", this, ((ontimedeliv) this.getApplication()),
		// "GET").execute(serverURL);
		RZHelper p = new RZHelper(serverURL, this, "setProducts");
		p.get();
	}

	public void setProducts(String s, String error) {
		Button submit = (Button) findViewById(R.id.submit);

		Bitmap picture = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.user);
		ListView listView = (ListView) findViewById(R.id.categorylist);
		products = new APIManager().getItemsByCategoryAndBranch(s);
		productItems = new ArrayList<Item>();
		if (products.size() == 0) {
			productItems.add(new Item(0, "", getString(R.string.empty_list)));
			submit.setEnabled(false);
		} else {
			for (int i = 0; i < products.size(); i++) {
				productItems.add(new Item(products.get(i).getId(), products
						.get(i).getPrice(), products.get(i).toString(),
						products.get(i).isAvailable()));
			}
			registerForContextMenu(listView);
		}
		dataAdapter = new CheckboxAdapter(this, R.layout.category_info,
				productItems, false);
		if (products.size() == 0) {
			dataAdapter.empty = true;
		}
		SharedMenu.adapter = dataAdapter;
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (products.size() > 0) {
					Intent intent = new Intent(ProductsActivity.this,
							ProductInfoActivity.class);
					((ontimedeliv) ProductsActivity.this.getApplication())
							.setProductId(productItems.get(position).getId());
					startActivity(intent);
				}
			}

		});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.categories, menu);
		SharedMenu.onCreateOptionsMenu(this, menu, getApplicationContext(),
				dataAdapter);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		if (SharedMenu.onOptionsItemSelected(item, this) == false) {
			if (item.getItemId() == R.id.add) {
				// handle local menu items here or leave blank
				Intent intent = new Intent(this, ProductInfoActivity.class);
				((ontimedeliv) ProductsActivity.this.getApplication())
						.setProductId(0);
				startActivity(intent);
			}
		}
		return super.onOptionsItemSelected(item);
	}
}
