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
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {

		Object sp1 = arg0.getSelectedItem();
		if(sp1 instanceof Country)
		{
			
			Log.d("ray", "Ray get cities");
			getCities(((Country) sp1).getId());
		}
		else if(sp1 instanceof City)
		{
			Log.d("ray", "Ray get areas");
			getAreas(((City) sp1).getId());
		}
		Toast.makeText(this, "class: "+sp1.getClass().getName(), Toast.LENGTH_SHORT).show();

		//getCities(sp1.getId());
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	public void getCountries() {
		String serverURL = "http://enigmatic-springs-5176.herokuapp.com/api/v1/countries?limit=30";
		ProgressDialog Dialog = new ProgressDialog(SelectionActivity.this);

		new MyJs(Dialog, "setCountries", SelectionActivity.this, "GET")
				.execute(serverURL);
	}

	public void setCountries(String s) {
		// TextView uiUpdate = (TextView) findViewById(R.id.out);
		countries = new APIManager().getCountries(s);
		ArrayAdapter<Country> counrytAdapter = new ArrayAdapter<Country>(this,
				android.R.layout.simple_spinner_item, countries);
		counrytAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		counrytAdapter.notifyDataSetChanged();
		s2.setAdapter(counrytAdapter);
		s2.setOnItemSelectedListener(this);

	}
	
	public void getCities(int CountryId) {
		String serverURL = "http://enigmatic-springs-5176.herokuapp.com/api/v1/countries/"
				+ CountryId + "/cities";
		ProgressDialog Dialog = new ProgressDialog(SelectionActivity.this);

		new MyJs(Dialog, "setCities", SelectionActivity.this, "GET")
				.execute(serverURL);
	}

	public void setCities(String s) {
		ArrayList<City> cities = new APIManager().getCitiesByCountry(s);
		ArrayAdapter<City> dataAdapter = new ArrayAdapter<City>(this,
				android.R.layout.simple_spinner_item, cities);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dataAdapter.notifyDataSetChanged();
		s3.setAdapter(dataAdapter);
		s3.setOnItemSelectedListener(this);

	}
	
	public void getAreas(int CountryId) {
		String serverURL = "http://enigmatic-springs-5176.herokuapp.com/api/v1/cities/"+ CountryId + "/areas";		
		ProgressDialog Dialog = new ProgressDialog(SelectionActivity.this);

		new MyJs(Dialog, "setAreas", SelectionActivity.this, "GET")
				.execute(serverURL);
	}

	public void setAreas(String s) {
		ArrayList<Area> areas = new APIManager().getAreasByCity(s);
		ArrayAdapter<Area> dataAdapter = new ArrayAdapter<Area>(this,
				android.R.layout.simple_spinner_item, areas);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dataAdapter.notifyDataSetChanged();
		s4.setAdapter(dataAdapter);

	}

	public void getBusinesses(){
		String serverURL = "http://enigmatic-springs-5176.herokuapp.com/api/v1/businesses";
		ProgressDialog Dialog = new ProgressDialog(SelectionActivity.this);

		new MyJs(Dialog, "setBusiness", SelectionActivity.this, "GET")
				.execute(serverURL);
	}

	public void setBusiness(String s) {
		// TextView uiUpdate = (TextView) findViewById(R.id.out);
		business = new APIManager().getBusinesses(s);
		
		ArrayAdapter<Business> busAdapter = new ArrayAdapter<Business>(this,
				android.R.layout.simple_spinner_item, business);
		busAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		busAdapter.notifyDataSetChanged();
		s1.setAdapter(busAdapter);
	}
}
