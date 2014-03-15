package com.example.ontimedeliv;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OrderInfoAdapter extends ArrayAdapter<Item> {

	private ArrayList<Item> orderList;
	Context context;
	View convertView;
	int adapterView;

	public OrderInfoAdapter(Context context, int adapterView,
			ArrayList<Item> navList) {
		super(context, adapterView, navList);
		this.orderList = new ArrayList<Item>();
		this.orderList.addAll(navList);
		this.context = context;
		this.adapterView = adapterView;
	}

	class ViewHolder {
		CheckBox itemname;
		TextView price;
		NumberPicker quantity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		Log.v("ConvertView", String.valueOf(position));

		if (convertView == null) {

			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = vi.inflate(adapterView, null);

			holder = new ViewHolder();
			if (this.orderList.get(position).isNew) {
				RelativeLayout main = (RelativeLayout) convertView
						.findViewById(R.id.roworder);
				main.setBackgroundColor(Color.parseColor("#FF9999"));
			}

			holder.quantity = (NumberPicker) convertView
					.findViewById(R.id.quantity);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.itemname = (CheckBox) convertView
					.findViewById(R.id.itemname);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
			this.convertView = convertView;
		}

		Item orderitem = orderList.get(position);

		holder.price.setText(orderitem.getTitle());
		holder.price.setTag(orderitem);

		holder.quantity.setValue(3);
		holder.quantity.setTag(orderitem);
		holder.quantity.setMaxValue(10);
		holder.quantity.setMinValue(1);

		return convertView;
	}

	public void getListViewSize(ListView myListView) {
		ListAdapter myListAdapter = myListView.getAdapter();

		if (myListAdapter == null) {
			// do nothing return null
			return;
		}
		Log.d("ray", "ray Number :" + myListAdapter.getCount());
		// set listAdapter in loop for getting final size
		int totalHeight = 0;
		for (int size = 0; size < myListAdapter.getCount(); size++) {
			View listItem = myListAdapter.getView(size, this.convertView,
					myListView);
			// listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight() * 33;
		}
		// setting listview item in adapter
		ViewGroup.LayoutParams params = myListView.getLayoutParams();
		params.height = totalHeight
				+ (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
		myListView.setLayoutParams(params);

	}
}
