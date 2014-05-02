package com.example.ontimedeliv;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

class MyCustomAdapter extends ArrayAdapter<Item> implements Filterable {

	private ArrayList<Item> currentList, tmpList;
	Context context;
	private ItemsFilter mFilter;
	private final Object mLock = new Object();

	public MyCustomAdapter(Context context, int textViewResourceId,
			ArrayList<Item> navList) {
		super(context, textViewResourceId, navList);
		this.tmpList = new ArrayList<Item>();
		this.tmpList.addAll(navList);
		this.context = context;
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

	class ViewHolder {
		TextView name;
		ImageView picture;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {

			LayoutInflater vi = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = vi.inflate(R.layout.categories_list, null);

			holder = new ViewHolder();

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Item navitem = tmpList.get(position);
		// holder.picture =navitem.getImg();
		// holder.picture ;
		holder.name = (TextView) convertView.findViewById(R.id.name);
		holder.name.setText(navitem.getTitle());

		holder.picture = (ImageView) convertView.findViewById(R.id.picture);
		holder.picture.setImageResource(navitem.getImg());

		holder.name.setTag(navitem);

		return convertView;
	}

	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ItemsFilter();
		}
		return mFilter;
	}

	private class ItemsFilter extends Filter {
		protected FilterResults performFiltering(CharSequence prefix) {
			// Initiate our results object
			FilterResults results = new FilterResults();
			
			// No prefix is sent to filter by so we're going to send back the
			// original array
			if (currentList == null) {
				currentList = tmpList; // saves the original data in mOriginalValues
            }
			if (prefix == null || prefix.length() == 0) {
				Log.d("ray","empty pref: "+prefix);
					results.values = currentList;
					results.count = currentList.size();
			} else {
				Log.d("ray","rays pref: "+prefix);
				// Compare lower case strings
				String prefixString = prefix.toString().toLowerCase();
				// Local to here so we're not changing actual array
				final ArrayList<Item> items = currentList;
				
				final int count = items.size();
				final ArrayList<Item> newItems = new ArrayList<Item>(count);
				for (int i = 0; i < count; i++) {
					final Item item = items.get(i);
					final String itemName = item.toString().toLowerCase();
					Log.d("ray","rays to str : "+itemName);
					// First match against the whole, non-splitted value
					if (itemName.contains(prefixString)) {
						Log.d("=>","cont");
						newItems.add(item);
					}
				}
				// Set and return
				results.values = newItems;
				results.count = newItems.size();
			}
			return results;
		}

		@SuppressWarnings("unchecked")
		protected void publishResults(CharSequence prefix, FilterResults results) {
			// noinspection unchecked
			tmpList =null; tmpList =(ArrayList<Item>) results.values;
			
			Log.d("ray","results : "+tmpList.size());
			// Let the adapter know about the updated list
			if (tmpList.size() > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}
}
