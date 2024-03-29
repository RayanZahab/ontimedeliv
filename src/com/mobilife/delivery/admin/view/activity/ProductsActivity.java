package com.mobilife.delivery.admin.view.activity;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.mobilife.delivery.admin.DeliveryAdminApplication;
import com.mobilife.delivery.admin.R;
import com.mobilife.delivery.admin.adapter.CheckboxAdapter;
import com.mobilife.delivery.admin.model.Item;
import com.mobilife.delivery.admin.model.Product;
import com.mobilife.delivery.admin.utilities.APIManager;
import com.mobilife.delivery.admin.utilities.Activate;
import com.mobilife.delivery.admin.utilities.RZHelper;
import com.mobilife.delivery.admin.utilities.myURL;

public class ProductsActivity extends Activity {

	CheckboxAdapter dataAdapter = null;
	int categoryId, branchId, shopId;
	ArrayList<Product> products;
	ArrayList<Item> productItems;
	String url, serverURL;
	DialogInterface dialog;
	ArrayList<Integer> selectedIds = new ArrayList<Integer>();
	ArrayList<Integer> unselectedIds = new ArrayList<Integer>();
	static Activate myProd;
	static RZHelper activate;
	static RZHelper deactivate;

	@Override
	public void onCreate(Bundle savedInstancecat) {
		super.onCreate(savedInstancecat);
		setContentView(R.layout.activity_product);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		DeliveryAdminApplication.clear("products");
		categoryId = DeliveryAdminApplication.getCategoryId(this);
		if (categoryId != 0) {
			branchId = DeliveryAdminApplication.getBranchId(this);
			shopId = DeliveryAdminApplication.getShopId(this);

			url = new myURL("items", "branches/" + branchId + "/categories",
					categoryId, 300).getURL();

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
		SearchView searchView = (SearchView) SharedMenuActivity.menu.findItem(
				R.id.action_search).getActionView();

		if (!searchView.isIconified()) {
			searchView.setIconified(true);
		} else {
			Intent i = new Intent(ProductsActivity.this,
					CategoriesActivity.class);
			DeliveryAdminApplication.setCategoryId(0);
			startActivity(i);
		}
	}

	public void afterDeactivate(String s, String error) {
		selectedIds = dataAdapter.getSelectedList();
		if (selectedIds.size() > 0) {
			myProd = new Activate("items", selectedIds);

			String serverURL = new myURL("activate_items", "branches",
					branchId, 0).getURL();

			RZHelper p = new RZHelper(serverURL, this, "afterActivate", false);
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
			Edit(dataAdapter.tmpList.get((int) info.id));
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
		final int productId = dataAdapter.tmpList.get(position).getId();
		new AlertDialog.Builder(this)
				.setTitle(R.string.deletethisprod)
				.setIcon(R.drawable.branches)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								String serverURL = new myURL(null, "items",
										productId, 0).getURL();
								RZHelper p = new RZHelper(serverURL,
										ProductsActivity.this, "afterDelete",
										false);
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

	public void Edit(Item item) {
		Intent i = new Intent(ProductsActivity.this, ProductInfoActivity.class);
		DeliveryAdminApplication.setProductId(item.getId());
		startActivity(i);
	}

	public void getProducts() {
		String serverURL = this.url;
		RZHelper p = new RZHelper(serverURL, this, "setProducts", true);
		p.get();
	}

	public void setProducts(String s, String error) {
		Boolean empty = false;

		ListView listView = (ListView) findViewById(R.id.categorylist);
		products = new APIManager().getItemsByCategoryAndBranch(s);
		productItems = new ArrayList<Item>();
		if (products.size() == 0) {
			Item i = new Item("empty");
			i.setEmpty(true);
			productItems.add(i);
			empty = true;
		} else {
			for (int i = 0; i < products.size(); i++) {
				productItems.add(new Item(products.get(i).getId(), products
						.get(i).getPrice(), products.get(i).toString(),
						products.get(i).isAvailable()));
			}
			registerForContextMenu(listView);
		}
		dataAdapter = new CheckboxAdapter(this, R.layout.product_info, productItems, false);
		dataAdapter.empty = empty;

		SharedMenuActivity.adapter = dataAdapter;
		listView.setAdapter(dataAdapter);

		serverURL = new myURL("activate_items", "branches", branchId, 0).getURL();
		activate = new RZHelper(serverURL, this, "afterActivate", false);

		serverURL = new myURL("deactivate_items", "branches", branchId, 0).getURL();
		deactivate = new RZHelper(serverURL, this, "afterActivate", false);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (dataAdapter.tmpList.size() > 0) {
					Intent intent = new Intent(ProductsActivity.this,
							ProductInfoActivity.class);
					DeliveryAdminApplication.setProductId(dataAdapter.tmpList.get(position)
							.getId());
					startActivity(intent);
				}
			}

		});
	}

	public static void activate(ArrayList<Integer> selectedIds) {
		if (selectedIds.size() > 0) {
			myProd = new Activate("items", selectedIds);
			activate.put(myProd);
		}
	}

	public static void deActivate(ArrayList<Integer> unselectedIds) {
		if (unselectedIds.size() > 0) {
			myProd = new Activate("items", unselectedIds);
			deactivate.put(myProd);
		}
	}

	public void afterActivate(String s, String error) {
		Toast.makeText(getApplicationContext(), "DONE", Toast.LENGTH_SHORT)
				.show();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.categories, menu);
		SharedMenuActivity.onCreateOptionsMenu(this, menu, getApplicationContext(),
				dataAdapter);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		if (SharedMenuActivity.onOptionsItemSelected(item, this) == false) {
			if (item.getItemId() == R.id.add) {
				// handle local menu items here or leave blank
				Intent intent = new Intent(this, ProductInfoActivity.class);
				DeliveryAdminApplication.setProductId(0);
				startActivity(intent);
			}
		}
		return super.onOptionsItemSelected(item);
	}
}
