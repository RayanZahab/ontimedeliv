package com.example.ontimedeliv;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BranchesActivity extends Activity {
	
	MyCustomAdapter dataAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_branches);
		displayListView();

	}
		private void displayListView() {

			Bitmap picture = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
			//Bitmap albanajban = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
			//Bitmap drinks = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
			// Array list of Categories
			ArrayList<Item> branches = new ArrayList<Item>();

			Item _Item = new Item(picture,"abu Samra");
			branches.add(_Item);
			_Item = new Item(picture, "3azmi");
			branches.add(_Item); 
			_Item = new Item(picture, "Kura");
			branches.add(_Item);
			_Item = new Item(picture, "Beirut");
			branches.add(_Item);
			

			// create an ArrayAdaptar from the String Array
			dataAdapter = new MyCustomAdapter(this, R.layout.branches_list, branches);
			ListView listView = (ListView) findViewById(R.id.list);
			// Assign adapter to ListView
			listView.setAdapter(dataAdapter);

			listView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// When clicked, Navigate to the selected item
					Item navitem = (Item) parent.getItemAtPosition(position);
					String title = navitem.getTitle();
					Intent i;
					try {
						i = new Intent(getBaseContext(), Class.forName(getPackageName() + "." + title + "Activity"));
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

	class MyCustomAdapter extends ArrayAdapter<Item> {

		private ArrayList<Item> branchList;

		public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Item> navList) {
			super(context, textViewResourceId, navList);
			this.branchList = new ArrayList<Item>();
			this.branchList.addAll(navList);
		}

		class ViewHolder {
			TextView name;
			Bitmap picture;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;

			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null) {

				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = vi.inflate(R.layout.categories_list, null);

				holder = new ViewHolder();
				View v = convertView.findViewById(R.id.picture);
				v.setDrawingCacheEnabled(true);
				
				v.buildDrawingCache();
				
				Bitmap picture = v.getDrawingCache();
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.picture = picture;

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Item navitem = branchList.get(position);

			
			holder.name.setText(navitem.getTitle());

			holder.name.setTag(navitem);

			return convertView;
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
	    Intent intent = new Intent(this, AddBranchActivity.class);
	    startActivity(intent);
	
	    return super.onOptionsItemSelected(item);
	}

}
