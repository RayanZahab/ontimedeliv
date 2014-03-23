package com.example.ontimedeliv;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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
	public void bkToNav(Activity a)
	{
		Intent i = new Intent(a, NavigationActivity.class);
		Toast.makeText(a.getApplicationContext(),"No net Connection",
				Toast.LENGTH_SHORT).show();
		a.startActivity(i);
	}
}
