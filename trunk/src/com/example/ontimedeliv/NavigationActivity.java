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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class NavigationActivity extends Activity {
	MyCustomAdapter dataAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		displayListView();

	}
	private void displayListView() {

			Bitmap picture = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
			//Bitmap albanajban = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
			//Bitmap drinks = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
			// Array list of Categories
			ArrayList<Item> categories = new ArrayList<Item>();

			Item _Item = new Item(0,picture,"Branches");
			categories.add(_Item);
			_Item = new Item(0,picture, "Categories");
			categories.add(_Item);
			_Item = new Item(0,picture, "Orders");
			categories.add(_Item);
			_Item = new Item(0,picture, "Users");
			categories.add(_Item);
			_Item = new Item(0,picture, "Selection");
			categories.add(_Item);
			

			// create an ArrayAdaptar from the String Array
			dataAdapter = new MyCustomAdapter(this, R.layout.categories_list, categories);
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
		getMenuInflater().inflate(R.menu.navigation, menu);
		return true;
	}

	class MyCustomAdapter extends ArrayAdapter<Item> {

		private ArrayList<Item> navList;

		public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Item> navList) {
			super(context, textViewResourceId, navList);
			this.navList = new ArrayList<Item>();
			this.navList.addAll(navList);
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

			Item navitem = navList.get(position);

			
			holder.name.setText(navitem.getTitle());

			holder.name.setTag(navitem);

			return convertView;
		}
	}
}
