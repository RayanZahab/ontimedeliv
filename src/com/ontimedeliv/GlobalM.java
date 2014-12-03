package com.ontimedeliv;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class GlobalM {

	public GlobalM() {

	}

	public void setSelected(Spinner sp, ArrayAdapter<?> list, Object o) {

		for (int position = list.getCount() - 1; position >= 0; position--) {
			if (list.getItem(position).equals(o)) {
				sp.setSelection(position);
				return;
			}
		}
	}

	public int getStatus(String status) {
		int return_id = 0;
		if (status.equals("assigned")) {
			return_id = R.string.assigned_orders;
		} else if (status.equals("closed")) {
			return_id = R.string.closed_orders;
		} else if (status.equals("opened")) {
			return_id = R.string.new_orders;
		} else if (status.equals("cancelled")) {
			return_id = R.string.canceled_orders;
		} else if (status.equals("prepared")) {
			return_id = R.string.prepared_orders;
		}
		return return_id;
	}

	public void bkToNav(Activity a, String msg) {

		boolean admin = ontimedeliv.isAdmin();
		Intent i;
		if (admin)
			i = new Intent(a, NavigationActivity.class);
		else
			i = new Intent(a, OrdersActivity.class);

		if (msg != null && !msg.isEmpty()) {
			Toast t = Toast.makeText(a.getApplicationContext(), msg,
					Toast.LENGTH_SHORT);
			t.setGravity(Gravity.TOP, 0, 0);
			t.show();
		}
		if (ontimedeliv.getToken() != null)
			a.startActivity(i);
	}

	public void goTo(Activity from, Class to, String msg) {

		Intent i;
		i = new Intent(from, to.getClass());

		if (msg != null && !msg.isEmpty()) {
			Toast t = Toast.makeText(from.getApplicationContext(), msg,
					Toast.LENGTH_SHORT);
			t.setGravity(Gravity.TOP, 0, 0);
			t.show();
		}
		if (ontimedeliv.getToken() != null)
			from.startActivity(i);
	}

	public String getago(String date) {
		try {
			long now = System.currentTimeMillis();
			long time = System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			TimeZone tz = TimeZone.getTimeZone("GMT");
			sdf.setTimeZone(tz);
			time = sdf.parse(date).getTime();

			return ""
					+ DateUtils.getRelativeTimeSpanString(time, now,
							DateUtils.SECOND_IN_MILLIS,
							DateUtils.FORMAT_ABBREV_ALL);
		} catch (Exception e) {
			return date;
		}
	}
}
