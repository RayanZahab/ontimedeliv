package com.example.ontimedeliv;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class SelectionActivity extends Activity implements
		OnItemSelectedListener {
	Spinner s1, s2, s3, s4;
	ArrayList<Country> countries = new ArrayList<Country>();
	ArrayList<City> cities = new ArrayList<City>();
	ArrayList<Business> business = new ArrayList<Business>();
	ArrayList<Area> areas = new ArrayList<Area>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selection);
		s1 = (Spinner) findViewById(R.id.businessSP);
		s2 = (Spinner) findViewById(R.id.countriesSP);
		s3 = (Spinner) findViewById(R.id.citiesSP);
		s4 = (Spinner) findViewById(R.id.areasSP);
		getBusinesses();
		getCountries();
	}

	public void select(View view) {

		Toast.makeText(
				getApplicationContext(),
				"You selected: " + s1.getSelectedItem().toString() + "-"
						+ s2.getSelectedItem().toString() + " - "
						+ s3.getSelectedItem().toString(), Toast.LENGTH_SHORT)
				.show();
		Intent i = new Intent(this, NavigationActivity.class);
		startActivity(i);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

		Object sp1 = arg0.getSelectedItem();
		if (sp1 instanceof Country) {
			getCities(((Country) sp1).getId());
		} else if (sp1 instanceof City) {
			getAreas(((City) sp1).getId());
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		Log.d("ray", "ray nothing");
	}

	public void getCountries() {
		String serverURL = new myURL("countries", null, 0, 30).getURL();
		new MyJs("setCountries", SelectionActivity.this, ((ontimedeliv) SelectionActivity.this.getApplication()),"GET")
				.execute(serverURL);
	}

	public void setCountries(String s,String error) {

		countries = new APIManager().getCountries(s);
		ArrayAdapter<Country> counrytAdapter = new ArrayAdapter<Country>(this,
				android.R.layout.simple_spinner_item, countries);
		counrytAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		counrytAdapter.notifyDataSetChanged();
		s4.setAdapter(null);
		s2.setAdapter(counrytAdapter);
		s2.setOnItemSelectedListener(this);
	}

	public void getCities(int CountryId) {
		String serverURL = new myURL("cities", "countries", CountryId, 30)
				.getURL();

		new MyJs("setCities", SelectionActivity.this,((ontimedeliv) SelectionActivity.this.getApplication()), "GET")
				.execute(serverURL);
	}

	public void setCities(String s,String error) {
		cities = new APIManager().getCitiesByCountry(s);
		ArrayAdapter<City> cityAdapter = new ArrayAdapter<City>(this,
				android.R.layout.simple_spinner_item, cities);
		cityAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cityAdapter.notifyDataSetChanged();
		s4.setAdapter(null);
		s3.setAdapter(cityAdapter);
		s3.setOnItemSelectedListener(this);

	}

	public void getAreas(int CityId) {
		String serverURL = new myURL("areas", "cities", CityId, 30).getURL();
		new MyJs("setAreas", SelectionActivity.this,((ontimedeliv) SelectionActivity.this.getApplication()), "GET")
				.execute(serverURL);
	}

	public void setAreas(String s,String error) {
		areas = new APIManager().getAreasByCity(s);
		ArrayAdapter<Area> areaAdapter = new ArrayAdapter<Area>(this,
				android.R.layout.simple_spinner_item, areas);
		areaAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		areaAdapter.notifyDataSetChanged();
		s4.setAdapter(areaAdapter);
		s4.setOnItemSelectedListener(this);

	}

	public void getBusinesses() {
		String serverURL = new myURL("businesses", null, 0, 30).getURL();

		new MyJs("setBusiness", SelectionActivity.this,((ontimedeliv) SelectionActivity.this.getApplication()), "GET")
				.execute(serverURL);
	}

	public void setBusiness(String s,String error) {

		business = new APIManager().getBusinesses(s);

		ArrayAdapter<Business> busAdapter = new ArrayAdapter<Business>(this,
				android.R.layout.simple_spinner_item, business);
		busAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		busAdapter.notifyDataSetChanged();
		s1.setAdapter(busAdapter);
	}

	public void add(View v) {
		Intent i = new Intent(this, AddLocationDetails.class);

		switch (v.getId()) {
		case R.id.addCountry:
			i.putExtra("type", "Country");
			break;
		case R.id.addCity:
			i.putExtra("type", "City");
			break;
		case R.id.addArea:
			i.putExtra("type", "Area");
			break;
		case R.id.addBusiness:
			i.putExtra("type", "Business");
			break;
		}
		startActivity(i);

	}
}
