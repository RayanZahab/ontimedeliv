package com.ontimedeliv;

import java.util.Locale;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText username;
	private EditText password;
	private TextView forgotpassword, loginTxt;
	boolean isChecked = false;
	private CheckBox keeploggedin;
	private Button submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_login);

		username = (EditText) findViewById(R.id.user_name);
		password = (EditText) findViewById(R.id.password);
		forgotpassword = (TextView) findViewById(R.id.forgotpassword);
		loginTxt = (TextView) findViewById(R.id.login); 
		keeploggedin = (CheckBox) findViewById(R.id.keeploggedin);
		submit = (Button) findViewById(R.id.submit);
		SharedPreferences settings1 = getSharedPreferences("PREFS_NAME", 0);
		isChecked = settings1.getBoolean("isChecked", false);

		if (isChecked) {

			((ontimedeliv) this.getApplication()).setGlobals();
			Intent i = new Intent(this, NavigationActivity.class);

			startActivity(i);
		}

	}

	public void login(View view) {

		String serverURL = new myURL(null, "users", "login", 0).getURL();
		User user = new User(username.getText().toString(), null);
		user.setEncPassword(password.getText().toString());

		RZHelper p = new RZHelper(serverURL, this, "getLoggedIn", true);
		p.post(user);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi") public void changeLang(View view) {
		String lang_ab = "en";

		switch (view.getId()) {
		case R.id.english:
			lang_ab = "en";
			break;
		case R.id.arabic:
			lang_ab = "ar";
			break;
		}

		Locale locale = new Locale(lang_ab);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;

		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());

		SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("lang", lang_ab);
		editor.commit();

		
		int bgId = 0;
		ImageView img = (ImageView) findViewById(view.getId());
		switch (view.getId()) {
		case R.id.english:
			img.setImageResource(R.drawable.arlanguage);
			bgId = R.drawable.phonebg;
			img.setId(R.id.arabic);
			break;
		case R.id.arabic:
			img.setImageResource(R.drawable.enlanguage);
			img.setId(R.id.english);
			bgId = R.drawable.phonebgar;
			break;
		}
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			username.setBackgroundDrawable(getResources().getDrawable(bgId));

			//password.setBackgroundDrawable(getResources().getDrawable(R.drawable.passwordbgar));
		} else {
			username.setBackground(getResources().getDrawable(bgId));
			//password.setBackground(getResources().getDrawable(R.drawable.passwordbgar));
		}
		username.setText(null);
		username.setHint(getString(R.string.username));
		password.setHint(getString(R.string.password));
		forgotpassword.setText(getString(R.string.forgotpass));
		keeploggedin.setText(getString(R.string.keeploggedin));
		loginTxt.setText(getString(R.string.login));
		submit.setText(getString(R.string.enter));
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
			editor.putString("pass", password.getText().toString());
			editor.putString("phone", username.getText().toString());
			editor.putBoolean("admin", user.isIs_admin());
			editor.putBoolean("preparer", user.isIs_preparer());
			editor.putBoolean("delivery", user.isIs_delivery());
			editor.putInt("shopId", user.getShop_id());
			editor.putInt("branchId", user.getBranch_id());
			editor.putInt("id", user.getId());
			Log.d("login", user.getToken());
			editor.commit();

			((ontimedeliv) this.getApplication()).setGlobals();
			Intent i;
			if (user.isIs_admin())
				i = new Intent(this, NavigationActivity.class);
			else
				i = new Intent(this, OrdersActivity.class);
			startActivity(i);
		} else {
			Toast.makeText(getApplicationContext(), R.string.wrongcredentials,
					Toast.LENGTH_SHORT).show();
		}
	}

	public void forgotpassword(View view) {
		Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.exit)
				.setMessage(R.string.exitquest)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@SuppressLint("NewApi")
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								LoginActivity.this.finishAffinity();
							}
						}).setNegativeButton("No", null).show();
	}

}
