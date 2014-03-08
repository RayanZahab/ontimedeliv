package com.example.ontimedeliv;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddBranchActivity extends Activity  implements
OnItemSelectedListener {

	Button from, to;
	int fmHour, fmMinute, tHour, tMinute, shopId;
	ArrayList<Country> countries = new ArrayList<Country>();
	ArrayList<City> cities = new ArrayList<City>();
	ArrayList<Area> areas = new ArrayList<Area>();
	Spinner countrySp, citySp,areasSp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_branch);
		Bundle extras = getIntent().getExtras();
		shopId=extras.getInt("shopId");
		countrySp = (Spinner) findViewById(R.id.countriesSP);
		citySp = (Spinner) findViewById(R.id.citiesSP);
		areasSp = (Spinner) findViewById(R.id.areasSP);
		
		
		getCountries();
		from = (Button) findViewById(R.id.fromBtnn);
		to = (Button) findViewById(R.id.toBtn);
	}
	public void from(View view)
	{
		TimePickerDialog tpd = new TimePickerDialog(
				AddBranchActivity.this,
				new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view,
							int hourOfDay, int minute) {
						from.setText(hourOfDay + ":" + minute);
					}
				}, fmHour, fmMinute, false);
		tpd.show();
	}
	public void to(View view)
	{
		TimePickerDialog tpd = new TimePickerDialog(
				AddBranchActivity.this,
				new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view,
							int hourOfDay, int minute) {
						to.setText(hourOfDay + ":" + minute);
					}
				}, tHour, tMinute, false);
		tpd.show();
	}
	public void addBranch(View v)
	{
		areasSp = (Spinner) findViewById(R.id.areasSP);
		int selectedArea =((Area)areasSp.getSelectedItem()).getId();
		String name=((EditText) findViewById(R.id.editTextAddName) ).getText().toString();
		String desc=((EditText) findViewById(R.id.addDesc) ).getText().toString();
		String address=((EditText) findViewById(R.id.editTextAddress) ).getText().toString();
		String estimation=((EditText) findViewById(R.id.estimation)).getText().toString();
		String serverURL = new myURL().getURL("branches", null, 0, 30);
		ProgressDialog Dialog = new ProgressDialog(this);
		Branch newBranch = new Branch(0,name, desc, 
				new Area(selectedArea), address, 1, new Shop(shopId), "0", "0", 0, 0, estimation);
		new MyJs(Dialog, "backToSelection", this, "POST", (Object) newBranch)
				.execute(serverURL);
	}
	public void backToSelection(String s)
	{
		Intent intent = new Intent(this, BranchesActivity.class);
		intent.putExtra("shopId", 37);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_branch, menu);
		return true;
	}
	
	public void getCountries() {
		String serverURL = new myURL().getURL("countries", null, 0, 30);;
		ProgressDialog Dialog = new ProgressDialog(AddBranchActivity.this);

		new MyJs(Dialog, "setCountries", AddBranchActivity.this, "GET")
				.execute(serverURL);
	}

	public void setCountries(String s) {

		countries = new APIManager().getCountries(s);
		ArrayAdapter<Country> counrytAdapter = new ArrayAdapter<Country>(this,
				android.R.layout.simple_spinner_item, countries);
		counrytAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		counrytAdapter.notifyDataSetChanged();
		areasSp.setAdapter(null);
		countrySp.setAdapter(counrytAdapter);
		countrySp.setOnItemSelectedListener(this);
	}

	public void getCities(int CountryId) {
		String serverURL = new myURL().getURL("cities", "countries", CountryId, 30);
		ProgressDialog Dialog = new ProgressDialog(AddBranchActivity.this);

		new MyJs(Dialog, "setCities", AddBranchActivity.this, "GET")
				.execute(serverURL);
	}

	public void setCities(String s) {
		cities = new APIManager().getCitiesByCountry(s);
		ArrayAdapter<City> cityAdapter = new ArrayAdapter<City>(this,
				android.R.layout.simple_spinner_item, cities);
		cityAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cityAdapter.notifyDataSetChanged();
		areasSp.setAdapter(null);
		citySp.setAdapter(cityAdapter);
		citySp.setOnItemSelectedListener(this);

	}

	public void getAreas(int CityId) {
		String serverURL = new myURL().getURL("areas", "cities", CityId, 30);
		ProgressDialog Dialog = new ProgressDialog(AddBranchActivity.this);

		new MyJs(Dialog, "setAreas", AddBranchActivity.this, "GET")
				.execute(serverURL);
	}

	public void setAreas(String s) {
		areas = new APIManager().getAreasByCity(s);
		ArrayAdapter<Area> areaAdapter = new ArrayAdapter<Area>(this,
				android.R.layout.simple_spinner_item, areas);
		areaAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		areaAdapter.notifyDataSetChanged();
		areasSp.setAdapter(areaAdapter);
		areasSp.setOnItemSelectedListener(this);

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

}
