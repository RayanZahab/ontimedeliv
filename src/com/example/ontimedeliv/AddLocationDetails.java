package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddLocationDetails extends Activity implements
		OnItemSelectedListener {

	private EditText locationName;
	int counter = 3;
	Spinner countrySp, citySp;
	ArrayList<Country> countries = new ArrayList<Country>();
	ArrayList<City> cities = new ArrayList<City>();
	String type = "Country";
	Button addButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_location_details);
		addButton = (Button) findViewById(R.id.add);
		TextView labelTxt = (TextView) findViewById(R.id.nameLabel);
		String label = "";
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			type = extras.getString("type");
			RelativeLayout layoutToHide;
			setTitle("Add " + type);
			addButton.setText("Add " + type);
			if (type.equals("Country")) {

				layoutToHide = (RelativeLayout) findViewById(R.id.addCityView);
				layoutToHide.setVisibility(View.GONE);

			} else if (type.equals("Business")) {
				label = getString(R.string.add_business);
				labelTxt.setText(label);

				layoutToHide = (RelativeLayout) findViewById(R.id.addCityView);
				layoutToHide.setVisibility(View.GONE);

			} else {

				countrySp = (Spinner) findViewById(R.id.countriesSP);
				if (type.equals("City")) {
					label = getString(R.string.add_city);
					labelTxt.setText(label);
					layoutToHide = (RelativeLayout) findViewById(R.id.addAreaView);
					layoutToHide.setVisibility(View.GONE);

				} else if (type.equals("Area")) {
					label = getString(R.string.add_area);
					labelTxt.setText(label);
					citySp = (Spinner) findViewById(R.id.citiesSP);
					label = getString(R.string.add_city);
					labelTxt.setText(label);
				}
				getCountries();
			}

		}

	}

	public void add(View view) {
		locationName = (EditText) findViewById(R.id.name);
		if (type.equals("Country")) {
			if (locationName.getText().toString() != null
					&& locationName.getText().length() >= 3) {
				addCountry(locationName.getText().toString());

			} else {
				Toast.makeText(getApplicationContext(), "Invalid Name Entered",
						Toast.LENGTH_SHORT).show();
			}
		} else if (type.equals("City")) {
			Country selectedCountry = (Country) countrySp.getSelectedItem();

			Toast.makeText(getApplicationContext(),
					"You selected: " + selectedCountry.getId(),
					Toast.LENGTH_SHORT).show();
			addCity(locationName.getText().toString(), selectedCountry.getId());
		} else if (type.equals("Area")) {
			City selectedCity = (City) citySp.getSelectedItem();
			Toast.makeText(getApplicationContext(),
					"Area selected: " + selectedCity.getId(),
					Toast.LENGTH_SHORT).show();
			addArea(locationName.getText().toString(), selectedCity.getId());
		} else if (type.equals("Business")) {
			if (locationName.getText().toString() != null
					&& locationName.getText().length() >= 3) {
				addBusiness(locationName.getText().toString());

			} else {
				Toast.makeText(getApplicationContext(), "Invalid Name Entered",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void addCountry(String countryName) {
		String serverURL = new myURL("countries", null, 0, 30).getURL();
		ProgressDialog Dialog = new ProgressDialog(this);
		Country newCountry = new Country(0, countryName);
		new MyJs(Dialog, "backToSelection", this, "POST", (Object) newCountry)
				.execute(serverURL);
	}

	public void addBusiness(String businessName) {
		String serverURL = new myURL("businesses", null, 0, 30).getURL();
		ProgressDialog Dialog = new ProgressDialog(this);
		Business newBusiness = new Business(0, businessName);
		new MyJs(Dialog, "backToSelection", this, "POST", (Object) newBusiness)
				.execute(serverURL);
		Activity currentActivity = AddLocationDetails.this;
	}

	public void addCity(String cityName, int countryId) {
		String serverURL = new myURL("cities", null, 0, 30).getURL();
		ProgressDialog Dialog = new ProgressDialog(this);
		City newCity = new City(0, countryId, cityName);
		new MyJs(Dialog, "backToSelection", this, "POST", (Object) newCity)
				.execute(serverURL);
	}

	public void addArea(String areaName, int cityId) {
		String serverURL = new myURL("areas", null, 0, 30).getURL();
		ProgressDialog Dialog = new ProgressDialog(this);
		Area newArea = new Area(0, cityId, areaName);
		new MyJs(Dialog, "backToSelection", this, "POST", (Object) newArea)
				.execute(serverURL);
	}

	public void backToSelection(String s,String error) {
		Intent i = new Intent(this, SelectionActivity.class);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void getCountries() {
		String serverURL = new myURL("countries", null, 0, 30).getURL();// "http://enigmatic-springs-5176.herokuapp.com/api/v1/countries?limit=30";
		ProgressDialog Dialog = new ProgressDialog(this);

		new MyJs(Dialog, "setCountries", this, "GET").execute(serverURL);
	}

	public void setCountries(String s) {

		countries = new APIManager().getCountries(s);
		ArrayAdapter<Country> counrytAdapter = new ArrayAdapter<Country>(this,
				android.R.layout.simple_spinner_item, countries);
		counrytAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		counrytAdapter.notifyDataSetChanged();
		// s4.setAdapter(null);
		countrySp.setAdapter(counrytAdapter);
		countrySp.setOnItemSelectedListener(this);
	}

	public void getCities(int CountryId) {
		String serverURL = new myURL("cities", "countries", CountryId, 30)
				.getURL();// "http://enigmatic-springs-5176.herokuapp.com/api/v1/countries/"
		// + CountryId + "/cities";
		ProgressDialog Dialog = new ProgressDialog(this);

		new MyJs(Dialog, "setCities", this, "GET").execute(serverURL);
	}

	public void setCities(String s) {
		cities = new APIManager().getCitiesByCountry(s);
		ArrayAdapter<City> cityAdapter = new ArrayAdapter<City>(this,
				android.R.layout.simple_spinner_item, cities);
		cityAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cityAdapter.notifyDataSetChanged();
		citySp.setAdapter(cityAdapter);
		citySp.setOnItemSelectedListener(this);

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

		Object sp1 = arg0.getSelectedItem();
		if (sp1 instanceof Country && type.equals("Area")) {
			getCities(((Country) sp1).getId());
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}