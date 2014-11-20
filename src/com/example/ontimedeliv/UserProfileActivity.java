package com.example.ontimedeliv;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UserProfileActivity extends Activity {
	EditText nameTxt, usernameTxt, passTxt;
	EditText pass2;
	Spinner langSp;
	CheckBox keep;

	String name, phone, pass;
	String lang, lang_abv;
	int id;
	int branchId;
	User user;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);

		usernameTxt = (EditText) findViewById(R.id.user_name);
		nameTxt = (EditText) findViewById(R.id.name);
		passTxt = (EditText) findViewById(R.id.password);
		pass2 = (EditText) findViewById(R.id.password2);
		langSp = (Spinner) findViewById(R.id.languageSP);
		keep = (CheckBox) findViewById(R.id.keeploggedin);

		SharedPreferences settings1 = getSharedPreferences("PREFS_NAME", 0);
		boolean isChecked = settings1.getBoolean("isChecked", false);

		name = settings1.getString("name", "");
		pass = settings1.getString("pass", "");
		phone = settings1.getString("phone", "");
		lang = settings1.getString("lang", null);
		id = settings1.getInt("id", 0);
		branchId = settings1.getInt("branchId", 0);

		List<String> list = new ArrayList<String>();
		list.add(getString(R.string.arabic));
		list.add(getString(R.string.english));
		if (lang.equals("en")) {
			lang = getString(R.string.english);
			lang_abv = "en";
		} else {
			lang = getString(R.string.arabic);
			lang_abv = "ar";
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		langSp.setAdapter(dataAdapter);

		nameTxt.setText(name);
		usernameTxt.setText(phone);
		passTxt.setText(pass);
		pass2.setText(pass);
		keep.setChecked(isChecked);
		new GlobalM().setSelected(langSp, dataAdapter, lang);
	}

	public void updateInfo(View view) {
		if (!passTxt.getText().toString().equals(pass2.getText().toString())) {
			Toast.makeText(getApplicationContext(), R.string.wrongcredentials,
					Toast.LENGTH_SHORT).show();
		} else {
			String serverURL = new myURL(null, "users", id, 0).getURL();
			User user = new User(nameTxt.getText().toString(), phone, null, branchId, 0);
			user.setEncPassword(passTxt
					.getText().toString());
			String method = "PUT";
			ValidationError valid = user.validate(true);
			if(valid.isValid(this))
			{
				new MyJs("done", this, ((ontimedeliv) this.getApplication()),
						method, (Object) user, true, true).execute(serverURL);
			}
			
		}
	}

	public void done(String s, String error) {
		Toast.makeText(getApplicationContext(), "GOOD", Toast.LENGTH_SHORT)
				.show();
		String serverURL = new myURL(null, "users", "login", 0).getURL();
		User user = new User(phone, pass);
		user.setEncPassword(pass);
		//MyJs mjs = new MyJs("getLoggedIn", this,
			//	((ontimedeliv) this.getApplication()), "POST", (Object) user);
		//mjs.execute(serverURL);
		
		JSONObject params = (new APIManager())
				.objToCreate((Object) user);
		RZHelper p = new RZHelper(serverURL,this,"getLoggedIn");
		p.async_post(params);
	}

	public void getLoggedIn(String s, String error) {
		if (error == null) {
			User user = new APIManager().getLogedInUser(s);
			CheckBox keeplog = (CheckBox) findViewById(R.id.keeploggedin);
			SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
			SharedPreferences.Editor editor = settings.edit();

			editor.putBoolean("isChecked", keeplog.isChecked());
			editor.putString("token", user.getToken());
			editor.putString("name", user.getName());
			
			if (langSp.getSelectedItem().equals(getString(R.string.english))) {
				lang_abv = "en";
			} else {
				lang_abv = "ar";
			}

			editor.putString("lang", lang_abv);
			editor.putString("pass", pass);

			editor.commit();

			((ontimedeliv) this.getApplication()).setGlobals();
			Locale locale = new Locale(lang_abv);
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;

			getBaseContext().getResources().updateConfiguration(config,
					getBaseContext().getResources().getDisplayMetrics());
			new GlobalM().bkToNav(UserProfileActivity.this, null);
		} else {
			Toast.makeText(getApplicationContext(), R.string.wrongcredentials,
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_profile, menu);

		return true;
	}

	@Override
	public void onBackPressed() {
		new GlobalM().bkToNav(UserProfileActivity.this, null);
	}

}
