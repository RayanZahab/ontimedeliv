 package com.ontimedeliv;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class SplashScreenActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();

		setContentView(R.layout.activity_splash_screen);
		getCountries();
	}
	public void getCountries() {
		String serverURL = new myURL(null,"countries","get_all_cities_areas", 0).getURL();
		RZHelper p = new RZHelper(serverURL, this, "getUnits",true);
		p.setProgress(true);
		p.get();
	}
	public void move(String s,String error)
	{
		ArrayList<Unit> units = new APIManager().getUnits(s);
		ontimedeliv.setUnits(units);
		Intent i;
		i = new Intent(SplashScreenActivity.this, LoginActivity.class);				
		startActivity(i);
	}
	public void getUnits(String s,String error) {
		ArrayList<Country> countries = new APIManager().getCountries(s);
		ontimedeliv.setCountries(countries);
	
		String serverURL = new myURL("units", null, 0, 30).getURL();
		
		RZHelper p = new RZHelper(serverURL, this, "move",true);
		p.get();
	}

}