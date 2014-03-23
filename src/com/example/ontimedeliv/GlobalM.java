package com.example.ontimedeliv;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class GlobalM {

	public GlobalM() {

	}

	public void setSelected(Spinner sp, ArrayAdapter<?> list, Object o) {

		for (int position = 0; position < list.getCount(); position++) {
			if (list.getItem(position).equals(o)) {
				sp.setSelection(position);
				return;
			}
		}

	}

	public void bkToNav(Activity a) {

		Intent i = new Intent(a, NavigationActivity.class);
		Toast t = Toast.makeText(a.getApplicationContext(), R.string.no_net,
				Toast.LENGTH_SHORT);
		t.setGravity(Gravity.TOP, 0, 0);
		t.show();
		if (((ontimedeliv) a.getApplication()).getToken() != null)
			a.startActivity(i);
	}
}
