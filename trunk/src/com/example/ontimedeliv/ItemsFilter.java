package com.example.ontimedeliv;

import java.util.ArrayList;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;

public class ItemsFilter extends Filter {
	public ArrayList<Item> currentList , tmpList;
	 ArrayAdapter<Item> adapter;
	public ItemsFilter( ArrayAdapter<Item> adp, ArrayList<Item> cl , ArrayList<Item> tlist){
		this.currentList = cl;
		tmpList=tlist;
		adapter =adp;
		
		
	}
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
					Log.d("=>","search: "+prefixString);
					newItems.add(item);
				}else if(newItems.indexOf(item)>-1)
					newItems.remove(item);
					
			}
			// Set and return
			results.values = newItems;
			results.count = newItems.size();
		}
		return results;
	}
	@Override
	@SuppressWarnings("unchecked")
	protected void publishResults(CharSequence prefix, FilterResults results) {
		// noinspection unchecked
		tmpList =null; 
		tmpList =(ArrayList<Item>) results.values;
		
		Log.d("ray", "results : " + tmpList.size());
		// Let the adapter know about the updated list
		if (tmpList.size() > 0) {
			if(adapter instanceof MyCustomAdapter)
			{
				((MyCustomAdapter) adapter).tmpList = tmpList;
				((MyCustomAdapter) adapter).currentList = currentList;
			}
			else if (adapter instanceof CheckboxAdapter)
			{
				((CheckboxAdapter) adapter).tmpList = tmpList;
				((CheckboxAdapter) adapter).currentList = currentList;
			}
			adapter.notifyDataSetChanged();
		} else {
			tmpList = new ArrayList<Item>();
			Item i = new Item("No items found");
			i.setEmpty(true);
			tmpList.add(i);
			
			if(adapter instanceof MyCustomAdapter)
			{
				((MyCustomAdapter) adapter).tmpList = tmpList;
				((MyCustomAdapter) adapter).currentList = currentList;
			}
			else if (adapter instanceof CheckboxAdapter)
			{
				((CheckboxAdapter) adapter).tmpList = tmpList;
				((CheckboxAdapter) adapter).currentList = currentList;
			}
			adapter.notifyDataSetInvalidated();
		}
	}

}
