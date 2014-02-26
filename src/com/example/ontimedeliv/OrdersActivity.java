package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OrdersActivity extends Activity {
	MyCustomAdapter dataAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orders);
		displayListView();
	}

	private void displayListView() {

		// Array list of Categories
		ArrayList<Item> orders = new ArrayList<Item>();

		Item _Item = new Item("Tripoli, Abu Samra", true);
		orders.add(_Item);
		_Item = new Item("Tripoli, ma3rad street", false);
		orders.add(_Item);
		_Item = new Item("Beirut, salim slem", false);
		orders.add(_Item);
		_Item = new Item("jbeil", true);
		orders.add(_Item);

		// create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this, R.layout.row_order, orders);
		ListView listView = (ListView) findViewById(R.id.list);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		/*
		 * listView.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * public void onItemClick(AdapterView<?> parent, View view, int
		 * position, long id) { // When clicked, Navigate to the selected item
		 * Intent i; try { i = new Intent(getBaseContext(),
		 * Class.forName(getPackageName() + "." + title + "Activity"));
		 * startActivity(i); } catch (ClassNotFoundException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } }
		 * 
		 * });
		 */

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.orders, menu);
		return true;
	}

	class MyCustomAdapter extends ArrayAdapter<Item> {

		private ArrayList<Item> orderList;

		public MyCustomAdapter(Context context, int textViewResourceId,
				ArrayList<Item> navList) {
			super(context, textViewResourceId, navList);
			this.orderList = new ArrayList<Item>();
			this.orderList.addAll(navList);
		}

		class ViewHolder {
			TextView address;
			TextView numbofitems;
			TextView totalamount;
			Bitmap reject;
			Bitmap accept;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;

			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null) {

				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = vi.inflate(R.layout.row_order, null);

				holder = new ViewHolder();
				if (this.orderList.get(position).isNew) {
					RelativeLayout main = (RelativeLayout) convertView.findViewById(R.id.roworder);
					main.setBackgroundColor(Color.parseColor("#FF9999"));					
				}
				else
				{
					RelativeLayout my = (RelativeLayout) convertView
							.findViewById(R.id.newButtons);
					my.setVisibility(View.GONE);
				}

				View v = convertView.findViewById(R.id.accept);
				v.setDrawingCacheEnabled(true);
				v.buildDrawingCache();
				Bitmap accept = v.getDrawingCache();
				View p = convertView.findViewById(R.id.reject);
				p.setDrawingCacheEnabled(true);
				p.buildDrawingCache();
				Bitmap reject = p.getDrawingCache();

				holder.address = (TextView) convertView
						.findViewById(R.id.useraddress);
				holder.numbofitems = (TextView) convertView
						.findViewById(R.id.numbofitems);
				holder.totalamount = (TextView) convertView
						.findViewById(R.id.totalamount);
				holder.accept = accept;
				holder.reject = reject;

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Item orderitem = orderList.get(position);

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