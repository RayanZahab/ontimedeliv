package com.mobilife.delivery.admin.utilities;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobilife.delivery.admin.DeliveryAdminApplication;
import com.mobilife.delivery.admin.R;
import com.mobilife.delivery.admin.view.activity.NavigationActivity;
import com.mobilife.delivery.admin.view.activity.OrdersActivity;

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

	public void bkToNav(Activity a, String msg) {

		boolean admin = DeliveryAdminApplication.isAdmin(a);
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
		if (DeliveryAdminApplication.getToken(a) != null)
			a.startActivity(i);
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
