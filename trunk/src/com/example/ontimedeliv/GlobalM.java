package com.example.ontimedeliv;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class GlobalM {

	public GlobalM() {

	}

	public void setSelected(Spinner sp, ArrayAdapter<?> list, Object o) {
		
		for (int position = 0; position < list.getCount(); position++) {
			Log.d("glob","glob: "+ list.getItem(position).equals(o));
			if (list.getItem(position).equals(o)) {
				sp.setSelection(position);
				return;
			}
		}

	}
}
