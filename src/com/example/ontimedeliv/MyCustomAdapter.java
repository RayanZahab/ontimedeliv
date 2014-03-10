package com.example.ontimedeliv;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class MyCustomAdapter extends ArrayAdapter<Item> {

	private ArrayList<Item> currentList;
	Context context;

	public MyCustomAdapter(Context context, int textViewResourceId,
			ArrayList<Item> navList) {
		super(context, textViewResourceId, navList);
		this.currentList = new ArrayList<Item>();
		this.currentList.addAll(navList);
		this.context = context;
	}

	public ArrayList<Item> getCurrentList() {
		return currentList;
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

			LayoutInflater vi = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

		Item navitem = currentList.get(position);

		holder.name.setText(navitem.getTitle());

		holder.name.setTag(navitem);

		return convertView;
	}
}