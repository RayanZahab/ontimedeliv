package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BranchesActivity extends Activity {

	MyCustomAdapter dataAdapter = null;
	ArrayList<Branch> branches;
	ArrayList<Item> branchesItem;	
	int shopId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_branches);
		Bundle extras = getIntent().getExtras();
		shopId=extras.getInt("shopId");
		getBranches();

	}

	public void getBranches() {
		String serverURL = new myURL().getURL("branches", "shops", shopId, 30);
		ProgressDialog Dialog = new ProgressDialog(this);

		new MyJs(Dialog, "setBranches", this, "GET").execute(serverURL);
	}

	public void setBranches(String s) {
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
						"Selected" + branchesItem.get(position).getId(),
						Toast.LENGTH_SHORT).show();
				Intent i;
				try {
					i = new Intent(getBaseContext(), Class
							.forName(getPackageName() + "."
									+ "CategoriesActivity"));
					i.putExtra("branchId", ""
							+ branchesItem.get(position).getId());

					i.putExtra("shopId", ""+ shopId);
					startActivity(i);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

	}
	
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
	    super.onCreateContextMenu(menu, v, menuInfo);  
	        menu.setHeaderTitle("Context Menu");  
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.branches, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this, AddBranchActivity.class);
		intent.putExtra("shopId", 37);
		startActivity(intent);

		return super.onOptionsItemSelected(item);
	}

}
