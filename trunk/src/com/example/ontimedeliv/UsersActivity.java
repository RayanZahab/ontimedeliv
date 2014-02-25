package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class UsersActivity extends Activity {
	MyCustomAdapter dataAdapter;
	
	ArrayList<User> users ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users);
		displayListView();

	}

	private void displayListView() {

		Bitmap picture = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.user);

		ArrayList<Item> users = new ArrayList<Item>();

		Item _Item = new Item(picture, "User 1");
		users.add(_Item);
		_Item = new Item(picture, "User 2");
		users.add(_Item);
		_Item = new Item(picture, "User 3");
		users.add(_Item);
		_Item = new Item(picture, "User 4");
		users.add(_Item);

		// create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this, R.layout.row_users, users);
		ListView listView = (ListView) findViewById(R.id.Userslist);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, Navigate to the selected item
				Intent i;
				try {
					i = new Intent(getBaseContext(), Class
							.forName(getPackageName() + "."
									+ "UserInfoActivity"));
					startActivity(i);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

	}

	public void getUsers() {
		String serverURL = "http://enigmatic-springs-5176.herokuapp.com/api/v1/users";
		ProgressDialog Dialog = new ProgressDialog(this);

		new MyJs(Dialog, "setUsers", this, "GET")
				.execute(serverURL);
	}

	public void setUsers(String s) {

		/*users = new APIManager().getUsers(s);
		ArrayAdapter<User> counrytAdapter = new ArrayAdapter<Country>(this,
				android.R.layout.simple_spinner_item, users);
		counrytAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		counrytAdapter.notifyDataSetChanged();*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.users, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this, UserInfoActivity.class);
		startActivity(intent);

		return super.onOptionsItemSelected(item);
	}
}
