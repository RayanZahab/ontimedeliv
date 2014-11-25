package com.ontimedeliv;

import java.util.ArrayList;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OldOdersAdapter extends ArrayAdapter<Item> {

	private ArrayList<Item> currentList;
	private Context context;

	public OldOdersAdapter(Context context, int textViewResourceId,
			ArrayList<Item> currentList) {
		super(context, textViewResourceId, currentList);
		this.context = context;
		this.currentList = new ArrayList<Item>();
		this.currentList.addAll(currentList);
	}

	public ArrayList<Item> getCurrentList() {
		return currentList;
	}

	private class ViewHolder {
		TextView itemname;
		TextView quantity;
		TextView price;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		Log.v("ConvertView", String.valueOf(position));

		if (convertView == null) {

			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = vi.inflate(R.layout.row_old_order_info, null);

			holder = new ViewHolder();

			holder.itemname = (TextView) convertView
					.findViewById(R.id.itemname);
			holder.quantity = (TextView) convertView
					.findViewById(R.id.quantity);
			holder.price = (TextView) convertView.findViewById(R.id.price);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Item order = currentList.get(position);

		holder.itemname.setText(order.getTitle());
		holder.quantity.setText(order.getTitle());
		holder.price.setText(order.getTitle());

		return convertView;
	}

}
