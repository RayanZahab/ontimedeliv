package com.ontimedeliv;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class CheckboxAdapter extends ArrayAdapter<Item> implements Filterable {

	public ArrayList<Item> currentList, tmpList;
	private ItemsFilter mFilter;
	private ArrayList<Item> selectedList = new ArrayList<Item>();
	private ArrayList<Item> unselectedList = new ArrayList<Item>();
	private Context context;
	boolean icon;
	public boolean empty = false,isNew = true;

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
		ToggleButton name;
		TextView chTxt;
		String picture;
		TextView price;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Item cat = tmpList.get(position);
		Log.d("ray", "gettingView : " + position+ "->"+cat.isEmpty());
		
		if (convertView == null) {

			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder = new ViewHolder();
			if (empty || (position==0 && cat.isEmpty())) {
				convertView = vi.inflate(R.layout.categories_list, null);
				holder.price = (TextView) convertView.findViewById(R.id.name);
				holder.price.setText(cat.getTitle());
				convertView.findViewById(R.id.picture).setVisibility(View.GONE);
			}
			else
			{
				if (this.icon) {
					convertView = vi.inflate(R.layout.category_info, null);
					
					String image_name = (cat.getImage()).replace(" ", "_")
							+ ".png";
					new RZHelper((ImageView) convertView
							.findViewById(R.id.item_image), image_name,(Activity) context);
				} else {
					convertView = vi.inflate(R.layout.product_info, null);
					holder.price = (TextView) convertView
							.findViewById(R.id.price);
					holder.price.setText(cat.getPrice()
							+ context.getString(R.string.lira));
				}
				holder.name = (ToggleButton) convertView
					.findViewById(R.id.toggleButton);
				holder.chTxt = (TextView) convertView
						.findViewById(R.id.checkBox1_txt);
				setList(cat, holder);
			}
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			if(position==0 && cat.isEmpty()) {				
				holder.price = (TextView) convertView.findViewById(R.id.name);
				holder.price.setText(cat.getTitle());
				convertView.findViewById(R.id.picture).setVisibility(View.GONE);
			}
		}
		if (!empty)
		{
			holder.chTxt.setText(cat.getTitle());
			holder.name.setTag(position);
			
			holder.name.setChecked(cat.isSelected());
		
			if (cat.isSelected())
				if (selectedList.indexOf(cat) < 0)
					selectedList.add(cat);
			else
				if (unselectedList.indexOf(cat) < 0)
					unselectedList.add(cat);
		}
		return convertView;
	}

	public void setList(final Item cat, ViewHolder holder) {
					
		holder.name.setOnClickListener(new OnClickListener()
		{
		    @Override
		    public void onClick(View v)
		    {
		    	boolean isChecked = ((ToggleButton) v).isChecked();
		    	int getPosition = (Integer) v.getTag();  // Here we get the position that we have set for the checkbox using setTag.
				tmpList.get(getPosition).setSelected(isChecked); // Set the value of checkbox to maintain its state.
				if (isChecked) {
					selectedList.clear();
					selectedList.add(cat);
					ArrayList<Integer> ids = new ArrayList<Integer>();
					ids.add(cat.getId());
					Log.d("ray","activate: "+cat.getId());
					if(icon)
						CategoriesActivity.activate(ids);
					else
						ProductsActivity.activate(ids);
				} else {
					unselectedList.clear();
					unselectedList.add(cat);
					ArrayList<Integer> ids = new ArrayList<Integer>();
					ids.add(cat.getId());
					Log.d("ray","deactivate: "+cat.getId());
					if(icon)
						CategoriesActivity.deActivate(ids);
					else
						ProductsActivity.deActivate(ids);
				}
		    }
		});
	}

	public ArrayList<Integer> getSelectedList() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < selectedList.size(); i++)
			ids.add(selectedList.get(i).getId());
		return ids;
	}

	public void setSelectedList(ArrayList<Item> selectedList) {
		this.selectedList = selectedList;
	}

	public ArrayList<Integer> getUnselectedList() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < unselectedList.size(); i++)
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

	@Override
	public boolean areAllItemsEnabled() {

		return false;

	}

	@Override
	public boolean isEnabled(int position) {
		if (tmpList.get(position).isEmpty())
			return false;
		return true;
	}

}
