package com.example.ontimedeliv;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OrdersAdapter extends ArrayAdapter<Item> {

	private ArrayList<Item> orderList;
	Context context;
	View convertView;

	public OrdersAdapter(Context context, int textViewResourceId, ArrayList<Item> navList) {
		super(context, textViewResourceId, navList);
		this.orderList = new ArrayList<Item>();
		this.orderList.addAll(navList);
		this.context = context;				 
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

			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
			this.convertView=convertView;
		}

		Item orderitem = orderList.get(position);

		holder.address.setText(orderitem.getTitle());
		holder.address.setTag(orderitem);

		holder.numbofitems.setText(orderitem.getQuantity()+" Items");
		holder.numbofitems.setTag(orderitem);

		holder.totalamount.setText(orderitem.getPrice()+" $$");
		holder.totalamount.setTag(orderitem);

		return convertView;
	}
	 public void getListViewSize(ListView myListView) {
	        ListAdapter myListAdapter = myListView.getAdapter();
	        
	        if (myListAdapter == null) {
	            //do nothing return null
	            return;
	        }
	        Log.d("ray","ray Number :"+ myListAdapter.getCount());
	        //set listAdapter in loop for getting final size
	        int totalHeight = 0;
	        for (int size = 0; 
	        		size < 
	        		myListAdapter.getCount(); 
	        		size++) {
	            View listItem = myListAdapter.getView(
	            		size, 
	            		this.convertView, 
	            		myListView);
	            //listItem.measure(0, 0);
	            totalHeight += listItem.getMeasuredHeight()*33;
	        }
	      //setting listview item in adapter
	        ViewGroup.LayoutParams params = myListView.getLayoutParams();
	        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
	        myListView.setLayoutParams(params);
	    
	    }
}
