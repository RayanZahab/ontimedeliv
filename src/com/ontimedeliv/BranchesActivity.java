package com.ontimedeliv;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem; 
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView;

public class BranchesActivity extends Activity {

	public MyCustomAdapter dataAdapter = null;
	ArrayList<Branch> branches; 
	ArrayList<Item> branchesItem;
	int shopId;
	boolean opened = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_branches);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		ontimedeliv.clear("listing");
		shopId = ontimedeliv.getShopId(this);
		Log.d("ray","shopid: "+shopId);
		getBranches();

	}

	public MyCustomAdapter getAdapter() {
		return dataAdapter;
		
	}

	public void getBranches() {
		String serverURL = new myURL("branches", "shops", shopId, 30).getURL();
		RZHelper p = new RZHelper(serverURL, this, "setBranches",false);
		p.get();
	}

	public void setBranches(String s, String error) {
		Log.d("ray","reply: "+s);
		branches = new APIManager().getBranchesByShop(s);
		branchesItem = new ArrayList<Item>();
		ListView listView = (ListView) findViewById(R.id.list);

		listView.setTextFilterEnabled(true);

		if (branches.size() == 0) {
			branchesItem.add(new Item(0, "", getString(R.string.empty_list)));
		} else if (branches.size() == 1) {
			ontimedeliv.setBranchId(branchesItem.get(0).getId());
			Intent i = new Intent(getBaseContext(), CategoriesActivity.class);
			startActivity(i);
			return;
		} else {
			for (int i = 1; i < branches.size(); i++) {
				branchesItem.add(new Item(branches.get(i).getId(), "", branches
						.get(i).displayName()));
			}
			registerForContextMenu(listView);
		}
		dataAdapter = new MyCustomAdapter(this, R.layout.categories_list,
				branchesItem);
		SharedMenu.adapter = dataAdapter;
		listView.setAdapter(dataAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (dataAdapter.tmpList.size() > 0) {
					Intent i;
					try {
						i = new Intent(getBaseContext(), Class
								.forName(getPackageName() + "."
										+ "CategoriesActivity"));
						ontimedeliv.setBranchId(dataAdapter.tmpList.get(position).getId());
						startActivity(i);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		getMenuInflater().inflate(R.menu.cat_context_menu, menu);
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
		final int branchId = dataAdapter.tmpList.get(position).getId();
		new AlertDialog.Builder(this)
				.setTitle("Delete this branch: " + dataAdapter.tmpList.get(position).toString() +" ?")
				.setIcon(R.drawable.branches)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								String serverURL = new myURL(null, "branches",
										branchId, 0).getURL();
								RZHelper p = new RZHelper(serverURL,
										BranchesActivity.this, "afterDelete",true);
								p.delete();
								branchesItem.remove(position);

							}
						}).setNegativeButton(android.R.string.no, null).show();
	}

	public void afterDelete(String s, String error) {
		dataAdapter.currentList = branchesItem;
		dataAdapter.tmpList = branchesItem;
		dataAdapter.notifyDataSetChanged();
	}

	public void Edit(Item item) {
		Intent i = new Intent(BranchesActivity.this, AddBranchActivity.class);
		ontimedeliv.setBranchId(item.getId());
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.branches, menu);
		SharedMenu.onCreateOptionsMenu(this, menu, getApplicationContext(),
				dataAdapter);
		return true;

	}

	@Override
	public void onBackPressed() {

		SearchView searchView = (SearchView) SharedMenu.menu.findItem(
				R.id.action_search).getActionView();

		if (!searchView.isIconified()) {
			searchView.setIconified(true);
		} else {
			Intent i = new Intent(BranchesActivity.this,
					NavigationActivity.class);
			startActivity(i);
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		if (SharedMenu.onOptionsItemSelected(item, this) == false) {
			// handle local menu items here or leave blank

			switch (item.getItemId()) {

			case R.id.action_search:
				opened = true;
				break;
			case R.id.add:
				Intent intent = new Intent(this, AddBranchActivity.class);
				startActivity(intent);
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	public void backToActivity(Class activity) {
		Intent i = new Intent(BranchesActivity.this, activity);
		startActivity(i);
	}

}
