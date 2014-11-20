package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
		((ontimedeliv) NavigationActivity.this.getApplication())
				.clear("listing");
		if (((ontimedeliv) NavigationActivity.this.getApplication())
				.getCountries() == null)
			getCountries();
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
		SharedMenu.onCreateOptionsMenu(this, menu, getApplicationContext());
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (SharedMenu.onOptionsItemSelected(item, this) == false) {
			// handle local menu items here or leave blank
		}
		return super.onOptionsItemSelected(item);
	}

	public void getCountries() {
		String serverURL = new myURL("countries", null, 0, 30).getURL();
		Log.d("ray", "count: " + serverURL);
		//MyJs mjs = new MyJs("setCountries", this,
			//	((ontimedeliv) getApplication()), "GET", true, false);
		//mjs.execute(serverURL);
		RZHelper p = new RZHelper(serverURL,this,"setCountries");
		p.get();
	}

	public void setCountries(String s, String error) {
		countries = new APIManager().getCountries(s);
		if (countries.size() > 1) {
			for (int j = 1; j < countries.size(); j++) {
				Log.d("ray", "Country: " + j + "->" + countries.get(j).getId());
				currentName = countries.get(j).getName();
				getCities(j);
			}
		}
	}

	public void getCities(int position) {
		countryP = position;
		int countryId = countries.get(position).getId();
		Log.d("ray", "City for: " + position + "->" + countryId);
		String serverURL = new myURL("cities", "countries", countryId, 30)
				.getURL();
		//new MyJs("setCities", this, ((ontimedeliv) getApplication()), "GET",
			//	false, false).execute(serverURL);
		RZHelper p = new RZHelper(serverURL,this,"setCities");
		p.get();
	}

	public void setCities(String s, String error) {
		cities = new APIManager().getCitiesByCountry(s);
		String currentCountry = currentName;
		if (cities.size() > 1) {
			for (int j = 1; j < cities.size(); j++) {
				if (j == cities.size() - 1)
					last = true;
				currentName = currentCountry + "," + cities.get(j).getName();
				getAreas(j);
			}
		}
	}

	public void getAreas(int position) {
		cityP = position;
		CityId = cities.get(position).getId();
		String serverURL = new myURL("areas", "cities", CityId, 30).getURL();
		//MyJs mjs = new MyJs("setAreas", this,
			//	((ontimedeliv) this.getApplication()), "GET", false, false);
		//mjs.execute(serverURL);
		RZHelper p = new RZHelper(serverURL,this,"setAreas");
		p.get();
	}

	public void setAreas(String s, String error) {
		areas = new APIManager().getAreasByCity(s);
		String currentCity = currentName;
		if (areas.size() > 1) {
			for (int j = 1; j < cities.size(); j++) {
				currentName = currentCity + "," + areas.get(j).getName();
				areas.get(j).setDisplayName("lala: "+currentName);
				Log.d("ray", "Area : " + areas.get(j).getDisplayName());
			}
		}
		cities.get(cityP).setAreas(areas);
		countries.get(countryP).setCities(cities);
		if (last) {
			((ontimedeliv) this.getApplication()).setCountries(countries);
			//((ontimedeliv) this.getApplication()).loader.dismiss();
			last = false;
		}
	}

	public void select(View v) {

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
			Toast.makeText(getApplicationContext(),
					"Developed With Passion", Toast.LENGTH_SHORT).show();
			break;
		case R.id.settings:
			method = "UserProfile";
			status = null;
			break;
		}

		try {
			if (method != null) {
				i = new Intent(getBaseContext(), Class.forName(getPackageName()
						+ "." + method + "Activity"));
				if (status != null) {
					((ontimedeliv) NavigationActivity.this.getApplication())
							.setOrderStatus(status);
					if (status != null && !status.equals("opened")
							&& !status.equals("assigned")) {
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
