package com.example.ontimedeliv;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Helper {
	private int height = 0;

	public Helper() {

	}

	public Helper(int height) {
		this.setHeight(height);
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void getListViewSize(ListView myListView) {
		if (height != 0) {
			ViewGroup.LayoutParams params = myListView.getLayoutParams();
			params.height = height;
			myListView.setLayoutParams(params);
			Log.i("height fix:", String.valueOf(height));
		} else {
			ListAdapter myListAdapter = myListView.getAdapter();
			if (myListAdapter == null) {
				// do nothing return null
				return;
			}
			// set listAdapter in loop for getting final size
			int totalHeight = 0;
			for (int size = 0; size < myListAdapter.getCount(); size++) {
				View listItem = myListAdapter.getView(size, null, myListView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
			// setting listview item in adapter
			ViewGroup.LayoutParams params = myListView.getLayoutParams();
			params.height = totalHeight
					+ (myListView.getDividerHeight() * (myListAdapter
							.getCount() - 1));
			myListView.setLayoutParams(params);
			// print height of adapter on log
			Log.i("height of listItem:", String.valueOf(totalHeight));
		}
	}
}