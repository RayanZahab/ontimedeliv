package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
		registerForContextMenu(listView);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,int position, long id) { 
				// When clicked, Navigate to the selected item
				Intent i = new Intent(getBaseContext(), OrderInfoActivity.class);
				startActivity(i);
			}

		});

	}
	@Override
	public void onBackPressed()
	{
	     Intent i = new Intent(OrdersActivity.this, NavigationActivity.class);
	     startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.orders, menu);
		return true;
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
					RelativeLayout main = (RelativeLayout) convertView
							.findViewById(R.id.roworder);
					main.setBackgroundColor(Color.parseColor("#FF9999"));
				} 

				

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