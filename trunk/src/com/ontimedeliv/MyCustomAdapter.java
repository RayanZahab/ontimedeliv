package com.ontimedeliv;

import java.util.ArrayList;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

class MyCustomAdapter extends ArrayAdapter<Item> implements Filterable {

	public ArrayList<Item> currentList, tmpList;
	Context context;
	private ItemsFilter mFilter;
	private int layout = R.layout.categories_list;
	public boolean empty;

	public MyCustomAdapter(Context context, int textViewResourceId,
			ArrayList<Item> navList) {
		super(context, textViewResourceId, navList);
		if (textViewResourceId != 0)
			layout = textViewResourceId;
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

			convertView = vi.inflate(layout, null);

			holder = new ViewHolder();

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Item navitem = tmpList.get(position);
		holder.name = (TextView) convertView.findViewById(R.id.name);
		holder.name.setText(Html.fromHtml(navitem.getTitle()));

		holder.picture = (ImageView) convertView.findViewById(R.id.picture);
		holder.picture.setImageResource(navitem.getImg());
		if (navitem.isEmpty()) {
			holder.name.setText(context.getResources().getString(
					R.string.no_items));
			if (holder.picture != null)
				holder.picture.setVisibility(View.GONE);
		} else if (position == 0) {
			if (holder.picture != null
					&& holder.picture.getVisibility() == View.GONE)
				holder.picture.setVisibility(View.VISIBLE);
		}
		holder.name.setTag(navitem);

		return convertView;
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

	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ItemsFilter(this, currentList, tmpList);
		}
		return mFilter;
	}

}
