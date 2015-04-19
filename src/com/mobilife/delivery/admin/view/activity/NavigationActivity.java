package com.mobilife.delivery.admin.view.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mobilife.delivery.admin.DeliveryAdminApplication;
import com.mobilife.delivery.admin.R;
import com.mobilife.delivery.admin.adapter.MyCustomAdapter;
import com.mobilife.delivery.admin.model.Area;
import com.mobilife.delivery.admin.model.City;
import com.mobilife.delivery.admin.model.Country;
import com.mobilife.delivery.admin.model.OrderStatus;

@SuppressLint("NewApi")
public class NavigationActivity extends Activity {
	MyCustomAdapter dataAdapter;
	int i, countryP, cityP, areaP;
	ArrayList<Country> countries = new ArrayList<Country>();
	ArrayList<City> cities = new ArrayList<City>();
	ArrayList<Area> areas = new ArrayList<Area>();
	int CityId;
	boolean last = false;
	String currentName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		getActionBar().hide();
		DeliveryAdminApplication.clear("listing");
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.exit)
				.setMessage(R.string.exitquest)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								NavigationActivity.this.finishAffinity();
							}
						}).setNegativeButton(android.R.string.no, null).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.navigation, menu);
		SharedMenuActivity.onCreateOptionsMenu(this, menu, getApplicationContext());
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (SharedMenuActivity.onOptionsItemSelected(item, this) == false) {
			// handle local menu items here or leave blank
		}
		return super.onOptionsItemSelected(item);
	}

	public void select(View v) throws NameNotFoundException {

		Intent i;
		String method = "", status = null;
		switch (v.getId()) {
		case R.id.orders:
			method = "Orders";
			status = "opened";
			break;

		case R.id.closed:
			method = "Orders";
			status = "closed";
			break;

		case R.id.users:
			method = "Users";
			break;

		case R.id.branches:
			method = "Branches";
			break;

		case R.id.canceled:
			method = "Orders";
			status = "cancelled";
			break;

		case R.id.assigned:
			method = "Orders";
			status = "assigned";
			break;
		case R.id.about:
			status = null;
			method = null;
			Toast.makeText(
					getApplicationContext(),
					"version"
							+ getPackageManager().getPackageInfo(
									getPackageName(), 0).versionName,
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.settings:
			method = "UserProfile";
			status = null;
			break;
		}

		try {
			if (method != null) {
				i = new Intent(getBaseContext(), Class.forName(getPackageName()+ ".view.activity." + method + "Activity"));
				if (status != null) {
					DeliveryAdminApplication.setOrderStatus(status);
					if (status != null && !status.equals(OrderStatus.Opened.name())
							&& !status.equals(OrderStatus.Assigned.name())) {
						i.putExtra("old", true);
					}
				}
				startActivity(i);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}
