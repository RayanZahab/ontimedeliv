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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;

import com.mobilife.delivery.admin.DeliveryAdminApplication;
import com.mobilife.delivery.admin.R;
import com.mobilife.delivery.admin.adapter.MyCustomAdapter;
import com.mobilife.delivery.admin.model.Branch;
import com.mobilife.delivery.admin.model.Item;
import com.mobilife.delivery.admin.model.User;
import com.mobilife.delivery.admin.utilities.APIManager;
import com.mobilife.delivery.admin.utilities.PreferenecesManager;
import com.mobilife.delivery.admin.utilities.RZHelper;
import com.mobilife.delivery.admin.utilities.myURL;

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
		DeliveryAdminApplication.clear("listing");
		shopId = DeliveryAdminApplication.getShopId(this);
		getBranches();

	}

	public MyCustomAdapter getAdapter() {
		return dataAdapter;

	}

	public void getBranches() {
		User user = PreferenecesManager.getInstance().getUserFromPreferences(this);
		String serverURL = new myURL(null, "branches", user.getBranch_id(), 0).getURL();
		//String serverURL = new myURL("branches", "shops", shopId, 30).getURL();
		RZHelper p = new RZHelper(serverURL, this, "setBranches", true);
		p.get();
	}

	public void setBranches(String s, String error) {
		if(PreferenecesManager.getInstance().getUserFromPreferences(this).isSuperAdmin()){
			branches = new APIManager().getBranches(s);
		}else{
			Branch branch= new APIManager().getBranch(s);
			branches = new ArrayList<Branch>();
			branches.add(branch);	
		}
		branchesItem = new ArrayList<Item>();
		ListView listView = (ListView) findViewById(R.id.list);

		listView.setTextFilterEnabled(true);

		if (branches.size() == 0) {
			branchesItem.add(new Item(0, "", getString(R.string.empty_list)));
		} /*else if (branches.size() == 1) {
			DeliveryAdminApplication.setBranchId(branchesItem.get(0).getId());
			Intent i = new Intent(getBaseContext(), CategoriesActivity.class);
			startActivity(i);
			return;
		}*/ else {
			for (int i = 0; i < branches.size(); i++) {
				Item myItem = new Item(branches.get(i).getId(), "", branches.get(i).displayName());
				myItem.setTime(branches.get(i).getEstimation_time());
				myItem.setCharge(branches.get(i).getDelivery_charge());
				myItem.setMinimum(branches.get(i).getMin_amount());
				branchesItem.add(myItem);
			}
			registerForContextMenu(listView);
		}
		dataAdapter = new MyCustomAdapter(this, R.layout.branches_list,
				branchesItem);
		dataAdapter.setType("branch");
		SharedMenuActivity.adapter = dataAdapter;
		listView.setAdapter(dataAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if (dataAdapter.tmpList.size() > 0) {
					Intent i;
					try {
						i = new Intent(getBaseContext(), Class.forName(getPackageName() + ".view.activity."+ "CategoriesActivity"));
						DeliveryAdminApplication.setBranchId(dataAdapter.tmpList.get(position).getId());
						startActivity(i);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		});

	}

	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		getMenuInflater().inflate(R.menu.cat_context_menu, menu);
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

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
				.setTitle(
						"Delete this branch: "
								+ dataAdapter.tmpList.get(position).toString()
								+ " ?")
				.setIcon(R.drawable.branches)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								String serverURL = new myURL(null, "branches",
										branchId, 0).getURL();
								RZHelper p = new RZHelper(serverURL,
										BranchesActivity.this, "afterDelete",
										false);
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
		DeliveryAdminApplication.setBranchId(item.getId());
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.branches, menu);
		
		// remove add branch option for non super user
		if(!PreferenecesManager.getInstance().getUserFromPreferences(this).isSuperAdmin())
			menu.removeItem(menu.findItem(R.id.add).getItemId());
		
		SharedMenuActivity.onCreateOptionsMenu(this, menu, getApplicationContext(),	dataAdapter);
		return true;

	}

	@Override
	public void onBackPressed() {

		SearchView searchView = (SearchView) SharedMenuActivity.menu.findItem(
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

		if (SharedMenuActivity.onOptionsItemSelected(item, this) == false) {
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

}
