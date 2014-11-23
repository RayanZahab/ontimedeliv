package com.example.ontimedeliv;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CheckboxAdapter extends ArrayAdapter<Item>  implements Filterable {

	public ArrayList<Item> currentList, tmpList;
	private ItemsFilter mFilter;
	private ArrayList<Item> selectedList = new ArrayList<Item>();
	private ArrayList<Item> unselectedList = new ArrayList<Item>();
	private Context context;
	boolean icon;
	public boolean empty =false;
	
	public CheckboxAdapter(Context context, int textViewResourceId,

	ArrayList<Item> currentList, boolean icon) {
		super(context, textViewResourceId, currentList);
		this.context = context;
		this.tmpList = new ArrayList<Item>();
		this.tmpList.addAll(currentList);
		this.icon = icon;
	}

	public CheckboxAdapter(Context context, int textViewResourceId,

	ArrayList<Item> currentList) {
		super(context, textViewResourceId, currentList);
		this.context = context;
		this.tmpList = new ArrayList<Item>();
		this.tmpList.addAll(currentList);
		this.icon = true;
	}

	public ArrayList<Item> getCurrentList() {
		return currentList;
	}
	@Override
	public int getCount() {
		return tmpList.size();
	}

	@Override
	public Item getItem(int position) {
		return tmpList.get(position);
	}

	@Override
	public int getPosition(Item item) {
		return tmpList.indexOf(item);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	private class ViewHolder {
		CheckBox name;
		TextView chTxt;
		String picture;
		TextView price;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Item cat = tmpList.get(position);
		if (convertView == null) {

			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder = new ViewHolder();
			if(!empty)
			{
				if (this.icon) {
					convertView = vi.inflate(R.layout.category_info, null);
					ImageTask img = new ImageTask((ImageView) convertView.findViewById(R.id.item_image),context);
					img.isCat =true;
					String image_name = (cat.getImage()).replace(" ", "_") + ".png";
					img.execute(image_name);
				} else
				{
					convertView = vi.inflate(R.layout.product_info, null);
					holder.price = (TextView) convertView.findViewById(R.id.price);
					holder.price.setText(cat.getPrice()+context.getString(R.string.lira));
				}
				holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
				holder.chTxt = (TextView) convertView.findViewById(R.id.checkBox1_txt);
			}
			else
			{
				convertView = vi.inflate(R.layout.categories_list, null);
				holder.price = (TextView) convertView.findViewById(R.id.name);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(!empty)
		{
			holder.chTxt.setText(cat.getTitle());
			holder.name.setChecked(cat.isSelected());
			if(cat.isSelected())
				selectedList.add(cat);
			else
				unselectedList.add(cat);
			holder.chTxt.setTag(cat);
			setLis(cat,holder);
			
		}
		else
		{
			holder.price.setText(cat.getTitle());
		}
		return convertView;
	}
	public void setLis(final Item cat,ViewHolder holder)
	{
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
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ItemsFilter(this, currentList, tmpList);
		}
		return mFilter;
	}


}
