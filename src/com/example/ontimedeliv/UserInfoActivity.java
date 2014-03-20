package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class UserInfoActivity extends Activity implements
		OnItemSelectedListener {

	Button addButton;
	EditText username, inputphone;
	CheckBox admin, preparer, delivery;
	Spinner branchesSP;
	User currentUser;
	int branchId, userId = 0;
	ArrayList<Branch> branches;
	ProgressDialog Dialog;
	GlobalM glob= new GlobalM();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		Dialog = new ProgressDialog(UserInfoActivity.this);
		Dialog.setCancelable(false);
		if (getIntent().hasExtra("id")) {
			Bundle extras = getIntent().getExtras();
			try {
				userId = Integer.parseInt((String) extras.getString("id"));
				getCurrentUser(userId);
				Button submit = (Button) findViewById(R.id.submit);
				submit.setText("Update");
			} catch (Exception e) {

			}
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_info, menu);
		SharedMenu.onCreateOptionsMenu(menu, getApplicationContext());
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (SharedMenu.onOptionsItemSelected(item, this) == false) {
			// handle local menu items here or leave blank
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		branchesSP = (Spinner) findViewById(R.id.branchesSP);
		Branch branch = (Branch) arg0.getSelectedItem();
		this.branchId = branch.getId();

	}

	public void getCurrentUser(int userId) {
		String url = new myURL(null, "users", userId, 1).getURL();
		String serverURL = url;
		Log.d("rays", "ray url" + url);
		new MyJs(Dialog, "setUserInfo", this, "GET", true).execute(serverURL);
	}

	public void setUserInfo(String s, String error) {

		currentUser = (new APIManager().getUsers(s)).get(0);

		username = (EditText) findViewById(R.id.nameinput);
		inputphone = (EditText) findViewById(R.id.inputphone);
		branchesSP = (Spinner) findViewById(R.id.branchesSP);
		admin = (CheckBox) findViewById(R.id.admin);
		preparer = (CheckBox) findViewById(R.id.preparer);
		delivery = (CheckBox) findViewById(R.id.delivery);

		username.setText(currentUser.getName());
		inputphone.setText(currentUser.getPhone());

		admin.setChecked(currentUser.isIs_admin());
		preparer.setChecked(currentUser.isIs_preparer());
		delivery.setChecked(currentUser.isIs_delivery());
		getBranches();
	}

	public void getBranches() {
		String serverURL = new myURL("branches", "shops", 37, 30).getURL();
		new MyJs(Dialog, "setBranches", this, "GET").execute(serverURL);
	}

	public void setBranches(String s, String error) {
		this.branches = new APIManager().getBranchesByShop(s);
		branchesSP = (Spinner) findViewById(R.id.branchesSP);
		ArrayAdapter<Branch> branchAdapter = new ArrayAdapter<Branch>(this,
				android.R.layout.simple_spinner_item, branches);
		branchAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		branchAdapter.notifyDataSetChanged();
		branchesSP.setAdapter(branchAdapter);
		branchesSP.setOnItemSelectedListener(this);
		glob.setSelected(branchesSP, branchAdapter, new Branch(currentUser.getBranch_id()));

	}

	public void addUser(View view) {
		username = (EditText) findViewById(R.id.nameinput);
		inputphone = (EditText) findViewById(R.id.inputphone);
		branchesSP = (Spinner) findViewById(R.id.branchesSP);
		admin = (CheckBox) findViewById(R.id.admin);
		preparer = (CheckBox) findViewById(R.id.preparer);
		delivery = (CheckBox) findViewById(R.id.delivery);
		User user = null;
		String serverURL = "";
		String method = "POST";
		if (userId > 0) {
			serverURL = new myURL(null, "users", userId, 0).getURL();
			user = new User(0, username.getText().toString(),// name
					inputphone.getText().toString(),// password
					inputphone.getText().toString(),// phone
					inputphone.getText().toString(),// mobile
					0,// is fired
					null,// address
					branchId,// branch
					admin.isChecked(), preparer.isChecked(),
					delivery.isChecked()); // roles
			method = "PUT";
		} else {
			serverURL = new myURL("users", null, 0, 0).getURL();
			user = new User(0, username.getText().toString(),  "", inputphone.getText().toString(),
					inputphone.getText().toString(), 0, null, branchId,
					admin.isChecked(), preparer.isChecked(),
					delivery.isChecked());

		}
		new MyJs(Dialog, "setRoles", this, method, (Object) user, true)
				.execute(serverURL);

	}

	public void setRoles(String s, String error) {
		int id = userId;

		if (id < 1) {
			id = new APIManager().getUserId(s);
		} else {
			String makePreparerURL = new myURL("set_roles", "users", id, 0)
					.getURL();
			admin = (CheckBox) findViewById(R.id.admin);
			preparer = (CheckBox) findViewById(R.id.preparer);
			delivery = (CheckBox) findViewById(R.id.delivery);
			Role role = new Role();
			role.setPreparer(preparer.isChecked());
			role.setAdmin(admin.isChecked());
			role.setDelivery(delivery.isChecked());
			new MyJs(Dialog, "afterRoles", this, "POST", (Object) role)
					.execute(makePreparerURL);
		}
	}

	public void afterRoles(String s, String error) {
		Intent i = new Intent(this, UsersActivity.class);
		startActivity(i);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

}
