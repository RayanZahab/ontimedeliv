package com.ontimedeliv;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class UsersActivity extends Activity {
	MyCustomAdapter dataAdapter;
	ArrayList<Item> usersItem = new ArrayList<Item>();
	ArrayList<User> users = new ArrayList<User>();
	boolean empty = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		ontimedeliv.clear("listing");
		getUsers();

	}

	public void getUsers() {
		String serverURL = new myURL("users", null, 0, 30).getURL();
		RZHelper p = new RZHelper(serverURL,this,"setUsers",false);
		p.get();
	}

	public void setUsers(String s, String error) {

		users = new APIManager().getUsers(s);
		int icon = 0;
		ListView listView = (ListView) findViewById(R.id.Userslist);
		if (users.size() == 0) {
			usersItem.add(new Item(0, R.drawable.user,
					getString(R.string.empty_list)));
			empty = true;
		} else {
			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).isIs_admin()) {
					icon = R.drawable.admin;
				} else if (users.get(i).isIs_preparer()) {
					icon = R.drawable.preparer;
				} else if (users.get(i).isIs_delivery()) {
					icon = R.drawable.delivery;
				} else if ((users.get(i).isIs_delivery())
						&& (users.get(i).isIs_preparer())) {
					icon = R.drawable.delivery;
				} else
					icon = R.drawable.user;
				usersItem.add(new Item(users.get(i).getId(), icon, users.get(i)
						.toString()));
			}
			registerForContextMenu(listView);
		}
		// create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this, R.layout.row_users, usersItem);
		SharedMenu.adapter = dataAdapter;
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!empty) {
					Intent i;
					try {
						i = new Intent(getBaseContext(), Class
								.forName(getPackageName() + "."
										+ "UserInfoActivity"));
						i.putExtra("id", "" + usersItem.get(position).getId());
						startActivity(i);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.users, menu);
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
		Intent i = new Intent(UsersActivity.this, NavigationActivity.class);
		startActivity(i);
		}
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.clearHeader();
		menu.add(0, v.getId(), 0, "Delete");
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.delete:
			Delete((int) info.id);
			break;
		default:
			break;

		}
		return true;
	}

	public void Delete(final int position) {
		final int catId = (usersItem.get(position)).getId();
		new AlertDialog.Builder(this)
				.setTitle(R.string.deletethisuser)
				.setIcon(R.drawable.users)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								String serverURL = new myURL(null, "users",
										catId, 0).getURL();
								RZHelper p = new RZHelper(serverURL,UsersActivity.this,"afterDelete",false);
								p.delete();
								usersItem.remove(position);
							}
						}).setNegativeButton(android.R.string.no, null).show();
	}

	public void afterDelete(String s, String error) {
		backToActivity(UsersActivity.class);
	}

	public void backToActivity(Class activity) {
		Intent i = new Intent(UsersActivity.this, activity);
		startActivity(i);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (SharedMenu.onOptionsItemSelected(item, this) == false) {
			Intent intent = new Intent(this, UserInfoActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
