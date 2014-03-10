package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class UsersActivity extends Activity {
	MyCustomAdapter dataAdapter;
	ArrayList<Item> usersItem = new ArrayList<Item>();
	ArrayList<User> users = new ArrayList<User>();
	ProgressDialog Dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users);
		Dialog = new ProgressDialog(this);
		Dialog.setCancelable(false);
		getUsers();

	}

	public void getUsers() {
		String serverURL = new myURL().getURL("users", null, 0, 30);
		new MyJs(Dialog, "setUsers", this, "GET").execute(serverURL);
	}

	public void setUsers(String s) {
		Bitmap picture = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.user);
		users = new APIManager().getUsers(s);

		for (int i = 0; i < users.size(); i++) {
			usersItem.add(new Item(users.get(i).getId(), picture, users.get(i)
					.toString()));
		}
		// create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this, R.layout.row_users, usersItem);
		ListView listView = (ListView) findViewById(R.id.Userslist);
		registerForContextMenu(listView);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, Navigate to the selected item
				Toast.makeText(getApplicationContext(),
						"Selected" + usersItem.get(position).getId(),
						Toast.LENGTH_SHORT).show();
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

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.users, menu);
		SharedMenu.onCreateOptionsMenu(menu, getApplicationContext());
		return true;
	}
	@Override
	public void onBackPressed()
	{
	     Intent i = new Intent(UsersActivity.this, NavigationActivity.class);
	     startActivity(i);
	}
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
	    super.onCreateContextMenu(menu, v, menuInfo);  
	        menu.clearHeader(); 
	        menu.add(0, v.getId(), 0, "Delete"); 
	    }
	public boolean onContextItemSelected(MenuItem item) {  
        if(item.getTitle()=="Delete"){Delete(item.getItemId());} 
        else {return false;}  
    return true;  
    }  
      
    public void Delete(int id){  
        Toast.makeText(this, "Delete called", Toast.LENGTH_SHORT).show();  
    } 
	public boolean onOptionsItemSelected(MenuItem item) {
		if(SharedMenu.onOptionsItemSelected(item, this) == false) {
		Intent intent = new Intent(this, UserInfoActivity.class);
		startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
