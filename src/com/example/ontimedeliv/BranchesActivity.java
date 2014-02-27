package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BranchesActivity extends Activity {

	MyCustomAdapter dataAdapter = null;
	ArrayList<Branch> branches;
	ArrayList<Item> branchesItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_branches);
		getBranches();

	}

	public void getBranches() {
		String serverURL = "http://enigmatic-springs-5176.herokuapp.com/api/v1/shops/37/branches";
		ProgressDialog Dialog = new ProgressDialog(this);

		new MyJs(Dialog, "setBranches", this, "GET").execute(serverURL);
	}
 
	public void setBranches(String s) {
		Bitmap picture = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.user);
		branches = new APIManager().getBranchesByShop(s);
		branchesItem = new ArrayList<Item>();

		for (int i = 0; i < branches.size(); i++) {
			branchesItem.add(new Item(branches.get(i).getId(), picture, branches.get(i)
					.toString()));
		}
		// create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this, R.layout.branches_list, branchesItem);
		ListView listView = (ListView) findViewById(R.id.list);
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
					i.putExtra("branchId", ""+branchesItem.get(position).getId());
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
		getMenuInflater().inflate(R.menu.branches, menu);
		return true;
	}

}
