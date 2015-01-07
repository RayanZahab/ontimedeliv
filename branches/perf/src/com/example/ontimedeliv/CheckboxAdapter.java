package com.example.ontimedeliv;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CheckboxAdapter extends ArrayAdapter<Item> {

	private ArrayList<Item> currentList;
	private ArrayList<Item> selectedList = new ArrayList<Item>();
	private ArrayList<Item> unselectedList = new ArrayList<Item>();
	private Context context;
	boolean icon;

	public CheckboxAdapter(Context context, int textViewResourceId,

	ArrayList<Item> currentList, boolean icon) {
		super(context, textViewResourceId, currentList);
		this.context = context;
		this.currentList = new ArrayList<Item>();
		this.currentList.addAll(currentList);
		this.icon = icon;
	}

	public CheckboxAdapter(Context context, int textViewResourceId,

	ArrayList<Item> currentList) {
		super(context, textViewResourceId, currentList);
		this.context = context;
		this.currentList = new ArrayList<Item>();
		this.currentList.addAll(currentList);
		this.icon = true;
	}

	public ArrayList<Item> getCurrentList() {
		return currentList;
	}

	private class ViewHolder {
		CheckBox name;
		Bitmap picture;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {

			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder = new ViewHolder();
			if (this.icon) {
				convertView = vi.inflate(R.layout.category_info, null);
				View v = convertView.findViewById(R.id.item_image);
				v.setDrawingCacheEnabled(true);
				v.buildDrawingCache();
				Bitmap picture = v.getDrawingCache();
				holder.picture = picture;
			} else
				convertView = vi.inflate(R.layout.product_info, null);
			holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final Item cat = currentList.get(position);
		holder.name.setText(cat.getTitle());
		holder.name.setChecked(cat.isSelected());
		if(cat.isSelected())
			selectedList.add(cat);
		else
			unselectedList.add(cat);
		holder.name.setTag(cat);
		holder.name.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					if(unselectedList.indexOf(cat)>=0)
						unselectedList.remove(cat);
					if(selectedList.indexOf(cat)<0)
						selectedList.add(cat);
				} else {
					if(selectedList.indexOf(cat)>=0)
						selectedList.remove(cat);
					if(unselectedList.indexOf(cat)<0)
						unselectedList.add(cat);
				}
			}
		});
		return convertView;
	}

	public ArrayList<Integer> getSelectedList() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for(int i =0;i<selectedList.size();i++)
			ids.add(selectedList.get(i).getId());
		return ids;
	}

	public void setSelectedList(ArrayList<Item> selectedList) {
		this.selectedList = selectedList;
	}

	public ArrayList<Integer> getUnselectedList() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for(int i =0;i<unselectedList.size();i++)
			ids.add(unselectedList.get(i).getId());
		return ids;
	}

	public void setUnselectedList(ArrayList<Item> unselectedList) {
		this.unselectedList = unselectedList;
	}

}