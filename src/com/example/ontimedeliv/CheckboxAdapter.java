package com.example.ontimedeliv;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class CheckboxAdapter extends ArrayAdapter<Item> {

	private ArrayList<Item> currentList;
	private Context context;

	public CheckboxAdapter(Context context, int textViewResourceId,

	ArrayList<Item> currentList) {
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
		CheckBox name;
		Bitmap picture;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		Log.v("ConvertView", String.valueOf(position));

		if (convertView == null) {

			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = vi.inflate(R.layout.category_info, null);

			holder = new ViewHolder();
			View v = convertView.findViewById(R.id.item_image);
			v.setDrawingCacheEnabled(true);

			v.buildDrawingCache();

			Bitmap picture = v.getDrawingCache();
			holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
			holder.picture = picture;

			convertView.setTag(holder);

			holder.name.setOnClickListener(new View.OnClickListener() {
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

		Item cat = currentList.get(position);

		holder.name.setText(cat.getTitle());
		holder.name.setChecked(cat.isSelected());

		holder.name.setTag(cat);

		return convertView;
	}

}
