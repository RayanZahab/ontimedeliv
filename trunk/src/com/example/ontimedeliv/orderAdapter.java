package com.example.ontimedeliv;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class orderAdapter extends ArrayAdapter<Item> {

	private ArrayList<Item> currentList;
	private Context context;

	public orderAdapter(Context context, int textViewResourceId, ArrayList<Item> currentList) {
		super(context, textViewResourceId, currentList);
		this.context = context;
		this.currentList = new ArrayList<Item>();
		this.currentList.addAll(currentList);
	}
	public ArrayList<Item> getCurrentList()
	{
		return currentList;
	}

	private class ViewHolder {
		CheckBox itemname;
		EditText quantity;
		TextView price;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		Log.v("ConvertView", String.valueOf(position));

		if (convertView == null) {

			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = vi.inflate(R.layout.row_order_info, null);

			holder = new ViewHolder();
			
			holder.itemname = (CheckBox) convertView.findViewById(R.id.itemname);
			holder.quantity = (EditText) convertView.findViewById(R.id.quantity);
			holder.price = (TextView) convertView.findViewById(R.id.price);

			convertView.setTag(holder);

			holder.itemname.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					CheckBox cb = (CheckBox) v;
					Item _cat = (Item) cb.getTag();

					Toast.makeText(
							context.getApplicationContext(),
							"Checkbox: " + cb.getText() + " -> "
									+ cb.isChecked(), Toast.LENGTH_LONG).show();

					_cat.setSelected(cb.isChecked());
				}
			});

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Item order = currentList.get(position);

		holder.itemname.setText(order.getTitle());
		holder.itemname.setChecked(order.isSelected());
		holder.itemname.setTag(order);

		return convertView;
	}

}
