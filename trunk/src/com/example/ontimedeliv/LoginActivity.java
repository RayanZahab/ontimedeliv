package com.example.ontimedeliv;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class LoginActivity extends Activity {

	private EditText username;
	private EditText password;
	int counter = 3;
	boolean isChecked = false;
	ProgressDialog Dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);

		Dialog = new ProgressDialog(LoginActivity.this);
		Dialog.setCancelable(false);
		SharedPreferences settings1 = getSharedPreferences("PREFS_NAME", 0);
		isChecked = settings1.getBoolean("isChecked", false);
		
		
		String token = settings1.getString("token", null);
		Log.d("ray", "ray login token: " + token);
		if (isChecked) {
			Intent i = new Intent(LoginActivity.this, NavigationActivity.class);
			startActivity(i);
		}			
	}	

	public void login(View view) {

		String serverURL = new myURL(null, "users", "login", 0).getURL();
		User user = new User(username.getText().toString(), password.getText()
				.toString());
		new MyJs(Dialog, "getLoggedIn", this, "POST", (Object) user)
				.execute(serverURL);

	}

	public void getLoggedIn(String s, String error) {
		if (error == null) {
			User user = new APIManager().getLogedInUser(s);
			CheckBox keeplog = (CheckBox) findViewById(R.id.keeploggedin);
			SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
			SharedPreferences.Editor editor = settings.edit();

			editor.putBoolean("isChecked", keeplog.isChecked());
			editor.putString("token", user.getToken());
			Log.d("ray", "ray token: " + user.getToken());
			editor.putBoolean("admin", user.isIs_admin());
			editor.putBoolean("preparer", user.isIs_preparer());
			editor.putBoolean("delivery", user.isIs_delivery());
			editor.putInt("id", user.getId());

			editor.commit();

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
		SharedMenu.onCreateOptionsMenu(menu, getApplicationContext());
		return true;
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Exit")
				.setMessage("Are you sure you want to exit?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								LoginActivity.this.finishAffinity();
							}
						}).setNegativeButton("No", null).show();
	}


}
