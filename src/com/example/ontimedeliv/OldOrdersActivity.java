package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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

		Item _Item = new Item(1,"Tripoli, Abu Samra",1,"200", true);
		oldorders.add(_Item);
		_Item = new Item(2,"Tripoli, ma3rad street",2,"500", false);
		oldorders.add(_Item);
		_Item = new Item(3,"Beirut, salim slem",3,"700", false);
		oldorders.add(_Item);
		_Item = new Item(4,"jbeil",5,"500", true);
		oldorders.add(_Item);
		

		// create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this, R.layout.row_oldorder, oldorders);
		ListView listView = (ListView) findViewById(R.id.list);
		registerForContextMenu(listView);
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
		SharedMenu.onCreateOptionsMenu(menu, getApplicationContext());
		return true;
	}
	 public boolean onOptionsItemSelected(MenuItem item) {
		    if(SharedMenu.onOptionsItemSelected(item, this) == false) {
		      
		    }
		    return super.onOptionsItemSelected(item);
		  }
	@Override
	public void onBackPressed()
	{
	     Intent i = new Intent(OldOrdersActivity.this, NavigationActivity.class);
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
