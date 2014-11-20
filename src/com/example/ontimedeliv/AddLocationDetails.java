package com.example.ontimedeliv;

import java.util.ArrayList;

import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
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
				Toast.makeText(getApplicationContext(),
						R.string.invalednameentered, Toast.LENGTH_SHORT).show();
			}
		} else if (type.equals("City")) {
			Country selectedCountry = (Country) countrySp.getSelectedItem();

			Toast.makeText(getApplicationContext(),
					R.string.youselected + selectedCountry.getId(),
					Toast.LENGTH_SHORT).show();
			addCity(locationName.getText().toString(), selectedCountry.getId());
		} else if (type.equals("Area")) {
			City selectedCity = (City) citySp.getSelectedItem();
			Toast.makeText(getApplicationContext(),
					R.string.areaselected + selectedCity.getId(),
					Toast.LENGTH_SHORT).show();
			addArea(locationName.getText().toString(), selectedCity.getId());
		} else if (type.equals("Business")) {
			if (locationName.getText().toString() != null
					&& locationName.getText().length() >= 3) {
				addBusiness(locationName.getText().toString());

			} else {
				Toast.makeText(getApplicationContext(),
						R.string.invalednameentered, Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void addCountry(String countryName) {
		String serverURL = new myURL("countries", null, 0, 30).getURL();
		//http://107.170.86.46/api/v1/countries
		Country newCountry = new Country(0, countryName);
		//new MyJs("backToSelection", this,
			//	((ontimedeliv) this.getApplication()), "POST",
				//(Object) newCountry).execute(serverURL);
		JSONObject params = (new APIManager())
				.objToCreate((Object) newCountry);
		RZHelper p = new RZHelper(serverURL,this,"backToSelection");
		p.async_post(params);
	}

	public void addBusiness(String businessName) {
		String serverURL = new myURL("businesses", null, 0, 30).getURL();
		Business newBusiness = new Business(0, businessName);
		//new MyJs("backToSelection", this,
			//	((ontimedeliv) this.getApplication()), "POST",
			//	(Object) newBusiness).execute(serverURL);
		JSONObject params = (new APIManager())
				.objToCreate((Object) newBusiness);
		RZHelper p = new RZHelper(serverURL,this,"backToSelection");
		p.async_post(params);
	}

	public void addCity(String cityName, int countryId) {
		String serverURL = new myURL("cities", null, 0, 30).getURL();
		City newCity = new City(0, countryId, cityName);
		//new MyJs("backToSelection", this,
		//		((ontimedeliv) this.getApplication()), "POST", (Object) newCity)
			//	.execute(serverURL);
		
		JSONObject params = (new APIManager())
				.objToCreate((Object) newCity);
		RZHelper p = new RZHelper(serverURL,this,"backToSelection");
		p.async_post(params);
	}

	public void addArea(String areaName, int cityId) {
		String serverURL = new myURL("areas", null, 0, 30).getURL();
		Area newArea = new Area(0, cityId, areaName);
		//new MyJs("backToSelection", this,
		//		((ontimedeliv) this.getApplication()), "POST", (Object) newArea)
		//		.execute(serverURL);
		
		JSONObject params = (new APIManager())
				.objToCreate((Object) newArea);
		RZHelper p = new RZHelper(serverURL,this,"backToSelection");
		p.async_post(params);
	}

	public void backToSelection(String s, String error) {
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

		//new MyJs("setCountries", this, ((ontimedeliv) this.getApplication()),
		//		"GET").execute(serverURL);
		RZHelper p = new RZHelper(serverURL,this,"setCountries");
		p.async_get();
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
				.getURL();
		//new MyJs("setCities", this, ((ontimedeliv) this.getApplication()),
		//		"GET").execute(serverURL);
		RZHelper p = new RZHelper(serverURL,this,"setCities");
		p.async_get();
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
