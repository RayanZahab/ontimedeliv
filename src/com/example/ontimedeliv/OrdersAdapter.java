package com.example.ontimedeliv;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OrdersAdapter extends ArrayAdapter<Item> {

	private ArrayList<Item> orderList;
	Context context;

	public OrdersAdapter(Context context, int textViewResourceId,
			ArrayList<Item> navList) {
		super(context, textViewResourceId, navList);
		this.orderList = new ArrayList<Item>();
		this.orderList.addAll(navList);
		this.context=context;
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
