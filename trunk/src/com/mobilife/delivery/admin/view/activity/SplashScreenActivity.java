package com.mobilife.delivery.admin.view.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.mobilife.delivery.admin.DeliveryAdminApplication;
import com.mobilife.delivery.admin.R;
import com.mobilife.delivery.admin.model.Country;
import com.mobilife.delivery.admin.model.Unit;
import com.mobilife.delivery.admin.utilities.APIManager;
import com.mobilife.delivery.admin.utilities.RZHelper;
import com.mobilife.delivery.admin.utilities.myURL;

public class SplashScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();

		setContentView(R.layout.activity_splash_screen);
		// getCountries();
		timer();
	}

	public void timer() {
		new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

			@Override
			public void run() {

				Intent i = new Intent(SplashScreenActivity.this,
						LoginActivity.class);

				startActivity(i);
				finish();
			}
		}, 2000);
	}

	public void getCountries() {
		String serverURL = new myURL(null, "countries", "get_all_cities_areas",
				0).getURL();
		RZHelper p = new RZHelper(serverURL, this, "getUnits", false);
		p.get();
	}

	public void move(String s, String error) {
		ArrayList<Unit> units = new APIManager().getUnits(s);
		DeliveryAdminApplication.setUnits(units);
		Intent i;
		i = new Intent(SplashScreenActivity.this, LoginActivity.class);
		startActivity(i);
	}

	public void getUnits(String s, String error) {
		ArrayList<Country> countries = new APIManager().getCountries(s);
		DeliveryAdminApplication.setCountries(countries);

		String serverURL = new myURL("units", null, 0, 30).getURL();

		RZHelper p = new RZHelper(serverURL, this, "move", true);
		p.get();
	}

}