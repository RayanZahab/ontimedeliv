package com.mobilife.delivery.admin.view.activity;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilife.delivery.admin.DeliveryAdminApplication;
import com.mobilife.delivery.admin.R;
import com.mobilife.delivery.admin.model.User;
import com.mobilife.delivery.admin.utilities.APIManager;
import com.mobilife.delivery.admin.utilities.RZHelper;
import com.mobilife.delivery.admin.utilities.myURL;

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
		String lang = settings1.getString("lang", null);
		if (lang != null) {
			if (lang.equals("en")) {
				changeLangById(R.id.english);
			} else {
				changeLangById(R.id.arabic);
			}
		}
		if (isChecked) {

			((DeliveryAdminApplication) this.getApplication()).setGlobals();
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

	public void changeLang(View view) {
		int viewId = view.getId();
		changeLangById(viewId);
	}

	@SuppressLint("NewApi")
	public void changeLangById(int viewId) {
		String lang_ab = "en";
		switch (viewId) {
		case R.id.english:
			lang_ab = "en";
			break;
		case R.id.arabic:
			lang_ab = "ar";
			break;
		}
		username = (EditText) findViewById(R.id.user_name);
		password = (EditText) findViewById(R.id.password);
		forgotpassword = (TextView) findViewById(R.id.forgotpassword);
		loginTxt = (TextView) findViewById(R.id.login);
		keeploggedin = (CheckBox) findViewById(R.id.keeploggedin);
		submit = (Button) findViewById(R.id.submit);

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
		ImageView img = (ImageView) findViewById(viewId);
		switch (viewId) {
		case R.id.english:
			if (img != null) {
				img.setImageResource(R.drawable.arlanguage);
				img.setId(R.id.arabic);
			}
			bgId = R.drawable.phonebg;
			break;
		case R.id.arabic:
			img = (ImageView) findViewById(R.id.arabic);
			img.setImageResource(R.drawable.enlanguage);
			img.setId(R.id.english);
			bgId = R.drawable.phonebgar;
			break;
		}
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			username.setBackgroundDrawable(getResources().getDrawable(bgId));

			// password.setBackgroundDrawable(getResources().getDrawable(R.drawable.passwordbgar));
		} else {
			username.setBackground(getResources().getDrawable(bgId));
			// password.setBackground(getResources().getDrawable(R.drawable.passwordbgar));
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
			if (s != null) {
				User user = new APIManager().getLogedInUser(s);
				if (user != null) {
					CheckBox keeplog = (CheckBox) findViewById(R.id.keeploggedin);
					SharedPreferences settings = getSharedPreferences(
							"PREFS_NAME", 0);
					SharedPreferences.Editor editor = settings.edit();

					editor.putBoolean("isChecked", keeplog.isChecked());
					editor.putString("token", user.getToken());
					editor.putString("name", user.getName());
					editor.putString("pass", password.getText().toString());
					editor.putString("phone", username.getText().toString());
					editor.putBoolean("admin", user.isIs_admin());
					editor.putBoolean("preparer", user.isIs_preparer());
					editor.putBoolean("delivery", user.isIs_delivery());
					editor.putBoolean("superadmin", user.isSuperAdmin());
					editor.putInt("shopId", user.getShop_id());
					editor.putInt("branchId", user.getBranch_id());
					editor.putInt("id", user.getId());
					Log.d("login", user.getToken());
					editor.commit();

					((DeliveryAdminApplication) this.getApplication())
							.setGlobals();
					Intent i;
					if (user.isIs_admin() || user.isSuperAdmin())
						i = new Intent(this, NavigationActivity.class);
					else
						i = new Intent(this, OrdersActivity.class);
					startActivity(i);
				} else {
					Toast.makeText(getApplicationContext(), R.string.no_net, Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), R.string.no_net, Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), R.string.wrongcredentials, Toast.LENGTH_LONG).show();
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
				.setPositiveButton(getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							@SuppressLint("NewApi")
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								LoginActivity.this.finishAffinity();
							}
						}).setNegativeButton(getString(R.string.no), null).show();
	}

}
