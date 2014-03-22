package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class BranchesActivity extends Activity {

	MyCustomAdapter dataAdapter = null;
	ArrayList<Branch> branches;
	ArrayList<Item> branchesItem;
	ProgressDialog Dialog;
	int shopId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_branches);
		Bundle extras = getIntent().getExtras();
		shopId = extras.getInt("shopId");
		Dialog = new ProgressDialog(BranchesActivity.this);
		Dialog.setCancelable(false);
		getBranches();

	}

	public void getBranches() {
		String serverURL = new myURL("branches", "shops", shopId, 30).getURL();
		new MyJs(Dialog, "setBranches", this,((ontimedeliv) this.getApplication()), "GET").execute(serverURL);
	}

	public void setBranches(String s, String error) {
		Bitmap picture = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.user);
		branches = new APIManager().getBranchesByShop(s);
		branchesItem = new ArrayList<Item>();

		for (int i = 0; i < branches.size(); i++) {
			branchesItem.add(new Item(branches.get(i).getId(), picture,
					branches.get(i).toString()));
		}
		// create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this, R.layout.branches_list,
				branchesItem);
		ListView listView = (ListView) findViewById(R.id.list);

		registerForContextMenu(listView);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, Navigate to the selected item
				Toast.makeText(getApplicationContext(),
						R.string.selected + branchesItem.get(position).getId(),
						Toast.LENGTH_SHORT).show();
				Intent i;
				try {
					i = new Intent(getBaseContext(), Class
							.forName(getPackageName() + "."
									+ "CategoriesActivity"));
					i.putExtra("branchId", ""
							+ branchesItem.get(position).getId());

					i.putExtra("shopId", "" + shopId);
					startActivity(i);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

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
			Edit(branchesItem.get((int) info.id));
			break;
		case R.id.delete:
			Delete(branchesItem.get((int) info.id).getId());
			break;
		default:
			break;

		}
		return true;
	}

	public void Delete(final int branchId) {

		new AlertDialog.Builder(this)
				.setTitle("Delete this branch?")
				.setIcon(R.drawable.branches)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								String serverURL = new myURL(null, "branches",
										branchId, 0).getURL();
								new MyJs(Dialog, "afterDelete",
										BranchesActivity.this,((ontimedeliv) BranchesActivity.this.getApplication()), "DELETE")
										.execute(serverURL);
							}
						}).setNegativeButton(android.R.string.no, null).show();
	}

	public void afterDelete(String s, String error) {
		backToActivity(BranchesActivity.class);
	}

	public void Edit(Item item) {
		Intent i = new Intent(BranchesActivity.this, AddBranchActivity.class);
		Toast.makeText(this, R.string.editing + item.getId(), Toast.LENGTH_SHORT)
				.show();

		i.putExtra("id", "" + item.getId());
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.branches, menu);
		SharedMenu.onCreateOptionsMenu(menu, getApplicationContext());
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		return true;
	}

	@Override
	public void onBackPressed() {
		backToActivity(NavigationActivity.class);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (SharedMenu.onOptionsItemSelected(item, this) == false) {
			Intent intent = new Intent(this, AddBranchActivity.class);
			intent.putExtra("shopId", 37);
			startActivity(intent);
		}

		return super.onOptionsItemSelected(item);
	}

	public void backToActivity(Class activity) {
		Intent i = new Intent(BranchesActivity.this, activity);
		i.putExtra("shopId", shopId);
		startActivity(i);
	}

}
