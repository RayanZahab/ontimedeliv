package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BranchesActivity extends Activity {

	MyCustomAdapter dataAdapter = null;
	ArrayList<Branch> branches;
	ArrayList<Item> branchesItem;	
	ProgressDialog Dialog ;
	int shopId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_branches);
		Bundle extras = getIntent().getExtras();
		shopId=extras.getInt("shopId");
		Dialog = new ProgressDialog(BranchesActivity.this);
		Dialog.setCancelable(false);
		getBranches();

	}

	public void getBranches() {
		String serverURL = new myURL().getURL("branches", "shops", shopId, 30);		
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
	    	menu.clearHeader();  
	        menu.add(0, v.getId(), 0, "Delete");
	        menu.add(0, v.getId(), 0, "Edit");
	    }
	public boolean onContextItemSelected(MenuItem item) {  
        if(item.getTitle()=="Delete"){Delete(item.getItemId());}
        else if(item.getTitle()=="Edit"){Edit(item.getItemId());}
        else {return false;}  
    return true;  
    }  
      
    public void Delete(int id){  
        Toast.makeText(this, "Delete called", Toast.LENGTH_SHORT).show();  
    }
    public void Edit(int id){  
    	LayoutInflater li = LayoutInflater
				.from(getApplicationContext());
		View promptsView = li.inflate(R.layout.prompt_cancel, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BranchesActivity.this);

		alertDialogBuilder.setView(promptsView);
		final TextView title = (TextView) promptsView.findViewById(R.id.textView1);
		title.setText("Branch Name");
		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.editText1);	
		userInput.setHint("Name");
		alertDialogBuilder
		.setCancelable(false)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Toast.makeText(getApplicationContext(),
						userInput.getText(), Toast.LENGTH_LONG).show();
			}
		})
		.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();  
    } 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.branches, menu);
		return true;
	}
	@Override
	public void onBackPressed()
	{
	     Intent i = new Intent(BranchesActivity.this, NavigationActivity.class);
	     startActivity(i);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(this, AddBranchActivity.class);
		intent.putExtra("shopId", 37);
		startActivity(intent);
		
		return super.onOptionsItemSelected(item);
	}

}
