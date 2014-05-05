package com.example.ontimedeliv;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.view.Menu;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText username;
	private EditText password;
	boolean isChecked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ReadCSV.main(null);

		username = (EditText) findViewById(R.id.user_name);
		password = (EditText) findViewById(R.id.password);

		SharedPreferences settings1 = getSharedPreferences("PREFS_NAME", 0);
		isChecked = settings1.getBoolean("isChecked", false);

		if (isChecked) {

			((ontimedeliv) this.getApplication()).setGlobals();
			new TextProgressDialog(this,"logging in ").showProg();		
		}
	}

	public void login(View view) {

		String serverURL = new myURL(null, "users", "login", 0).getURL();
		User user = new User(username.getText().toString(), null);
		user.setEncPassword(password.getText().toString());
		MyJs mjs = new MyJs("getLoggedIn", this,
				((ontimedeliv) this.getApplication()), "POST", (Object) user);
		mjs.execute(serverURL);

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
			editor.putInt("shopId", 6);
			editor.putInt("branchId", user.getBranch_id());
			editor.putInt("id", user.getId());

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

class MyTask extends AsyncTask<String, String, String> {
	private Context context;
	private ProgressDialog progressDialog;

	MyTask(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(context);
		progressDialog.show();
	}

	@Override
	protected String doInBackground(String... params) {
		// Do your loading here
		return "finish";
	}

	@Override
	protected void onPostExecute(String result) {
		progressDialog.dismiss();
		// Start other Activity or do whatever you want
	}
}
