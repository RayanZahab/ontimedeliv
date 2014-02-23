package com.example.ontimedeliv;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class SelectionActivity extends Activity implements
		OnItemSelectedListener {
	Spinner s1, s2, s3;
	ArrayList<Country> countries = new ArrayList<Country>();
	ArrayList<City> cities = new ArrayList<City>();
	ArrayList<Business> business = new ArrayList<Business>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selection);
		s1 = (Spinner) findViewById(R.id.countriesSP);
		s2 = (Spinner) findViewById(R.id.citiesSP);
		s3 = (Spinner) findViewById(R.id.businessSP);
		
		s1.setOnItemSelectedListener(this);
		countries.add(new Country(1, "lebanon"));
		countries.add(new Country(2, "Syria"));
		countries.add(new Country(3, "USA"));

		cities.add(new City(1, 1, "Tripoli"));
		cities.add(new City(2, 1, "Beirut"));
		cities.add(new City(3, 2, "Sham"));
		cities.add(new City(4, 2, "Homs"));
		cities.add(new City(5, 3, "Dallas"));
		
		business.add(new Business(1,"SuperMarket"));
		business.add(new Business(2,"Clothes"));

		ArrayAdapter<Country> counrytAdapter = new ArrayAdapter<Country>(this,
				android.R.layout.simple_spinner_item, countries);
		counrytAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		counrytAdapter.notifyDataSetChanged();
		s1.setAdapter(counrytAdapter);

		ArrayAdapter<Business> busAdapter = new ArrayAdapter<Business>(this,
				android.R.layout.simple_spinner_item, business);
		busAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		busAdapter.notifyDataSetChanged();
		s3.setAdapter(busAdapter);

	}
	public void select(View view) {
		
		Toast.makeText(getApplicationContext(), "You selected: "+s1.getSelectedItem().toString() +"-"+
				s2.getSelectedItem().toString()+" - "+s3.getSelectedItem().toString(),
				Toast.LENGTH_SHORT).show();
		Intent i = new Intent(this, NavigationActivity.class);
		startActivity(i);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

		Country sp1 = (Country) s1.getSelectedItem();
		Toast.makeText(this, s1.getId() + ">" + sp1, Toast.LENGTH_SHORT).show();

		List<City> list = sp1.getCities(cities);

		ArrayAdapter<City> dataAdapter = new ArrayAdapter<City>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dataAdapter.notifyDataSetChanged();
		s2.setAdapter(dataAdapter);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

}
