package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class OldOrdersActivity extends Activity {
	MyCustomAdapter dataAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_old_orders);
		displayListView();
	}
	
	private void displayListView() {

		ArrayList<Item> oldorders = new ArrayList<Item>();

		Item _Item = new Item("Tripoli, Abu Samra", true);
		oldorders.add(_Item);
		_Item = new Item("Tripoli, ma3rad street", false);
		oldorders.add(_Item);
		_Item = new Item("Beirut, salim slem", false);
		oldorders.add(_Item);
		_Item = new Item("jbeil", true);
		oldorders.add(_Item);
		

		// create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this, R.layout.row_oldorder, oldorders);
		ListView listView = (ListView) findViewById(R.id.list);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// When clicked, Navigate to the selected item
				Item navitem = (Item) parent.getItemAtPosition(position);
				String title = navitem.getTitle();
				Intent i = new Intent(getBaseContext(), OldOrdersInfoActivity.class);
					startActivity(i);	
				}		
			
		
		});

	
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.old_orders, menu);
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
			TextView address;
			TextView numbofitems;
			TextView totalamount;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;

			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null) {

				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = vi.inflate(R.layout.row_oldorder, null);

				holder = new ViewHolder();
				holder.address = (TextView) convertView
						.findViewById(R.id.useraddress);
				holder.numbofitems = (TextView) convertView
						.findViewById(R.id.numbofitems);
				holder.totalamount = (TextView) convertView
						.findViewById(R.id.totalamount);
				
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Item orderitem = navList.get(position);

			
			holder.address.setText(orderitem.getAddress());
			holder.address.setTag(orderitem);

			holder.numbofitems.setText(orderitem.getTitle());
			holder.numbofitems.setTag(orderitem);

			holder.totalamount.setText(orderitem.getTitle());
			holder.totalamount.setTag(orderitem);

			return convertView;
		}
	}

}
