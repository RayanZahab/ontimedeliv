package com.example.ontimedeliv;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddCountry extends Activity {

	private EditText countryName;
	int counter = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_country);
		
	}

	public void add(View view) {
		countryName = (EditText) findViewById(R.id.countryName);
		if (countryName.getText().toString() != null) {
			addCountry();
			
		} else {
			Toast.makeText(getApplicationContext(), "Wrong Credentials",
					Toast.LENGTH_SHORT).show();
		}
	}
	public void addCountry() {
		String serverURL = "http://enigmatic-springs-5176.herokuapp.com/api/v1/countries";
		ProgressDialog Dialog = new ProgressDialog(this);
		Country newCountry= new Country(0,countryName.getText().toString());
		new MyJs(Dialog, "setCountries", this, "POST",(Object) newCountry)
				.execute(serverURL);
	}
	public void setCountries(String s) {
		Toast.makeText(getApplicationContext(), "Added"+s,
				Toast.LENGTH_SHORT).show();
		Intent i = new Intent(this, SelectionActivity.class);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
