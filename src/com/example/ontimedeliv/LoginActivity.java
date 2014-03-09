package com.example.ontimedeliv;

import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class LoginActivity extends Activity implements OnItemSelectedListener {

	private EditText username;
	private EditText password;
	int counter = 3;
	private Spinner language;
	boolean isChecked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		language = (Spinner) findViewById(R.id.languageSP);
		language.setBackgroundResource(R.drawable.myspinner);
		CheckBox keeplog = (CheckBox) findViewById(R.id.keeploggedin);
		//language.setOnItemSelectedListener(this);
		
		keeplog.setOnCheckedChangeListener(new OnCheckedChangeListener() {

		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
		        SharedPreferences.Editor editor = settings.edit();
		        editor.putBoolean("isChecked", isChecked);
		        editor.commit();
		    }
		});
		
		SharedPreferences settings1 = getSharedPreferences("PREFS_NAME", 0);
		isChecked = settings1.getBoolean("isChecked", false);
		if (isChecked) {
		    Intent i = new Intent(LoginActivity.this, NavigationActivity.class);
		    startActivity(i);}
	}
	

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		String lang = language.getSelectedItem().toString();
		if (!lang.equals("English")) {
			Locale locale = new Locale("fr");
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config,
					getBaseContext().getResources().getDisplayMetrics());
			setContentView(R.layout.activity_login);
		}
		Toast.makeText(getApplicationContext(), "Selected: " + lang,
				Toast.LENGTH_SHORT).show();
	}

	public void login(View view) {
		if (username.getText().toString().equals("admin")
				&& password.getText().toString().equals("admin")) {
			Intent i = new Intent(this, NavigationActivity.class);
			i.putExtra("shopId", 37);
			startActivity(i);
		} else {
			Toast.makeText(getApplicationContext(), "Wrong Credentials",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

}
