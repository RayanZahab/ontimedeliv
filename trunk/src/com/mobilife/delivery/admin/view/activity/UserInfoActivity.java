package com.mobilife.delivery.admin.view.activity;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import com.mobilife.delivery.admin.DeliveryAdminApplication;
import com.mobilife.delivery.admin.R;
import com.mobilife.delivery.admin.ValidationError;
import com.mobilife.delivery.admin.model.Branch;
import com.mobilife.delivery.admin.model.Country;
import com.mobilife.delivery.admin.model.Role;
import com.mobilife.delivery.admin.model.User;
import com.mobilife.delivery.admin.utilities.APIManager;
import com.mobilife.delivery.admin.utilities.Converter;
import com.mobilife.delivery.admin.utilities.GlobalM;
import com.mobilife.delivery.admin.utilities.PreferenecesManager;
import com.mobilife.delivery.admin.utilities.RZHelper;
import com.mobilife.delivery.admin.utilities.myURL;

public class UserInfoActivity extends Activity implements
		OnItemSelectedListener {

	Button addButton;
	EditText username, inputphone, code;
	CheckBox admin, preparer, delivery;
	Spinner branchesSP, countriesSP;
	User currentUser;
	int branchId, userId = 0;
	ArrayList<Branch> branches;
	GlobalM glob = new GlobalM();
	int shopId;
	ArrayList<Country> countries = new ArrayList<Country>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		ActionBar actionBar = getActionBar();
		shopId = DeliveryAdminApplication.getShopId(this);
		countriesSP = (Spinner) findViewById(R.id.countriesSP);
		countriesSP.setVisibility(View.INVISIBLE);
		code = (EditText) findViewById(R.id.countrycode);
		actionBar.setDisplayHomeAsUpEnabled(true);
		DeliveryAdminApplication.clear("user");
		if (getIntent().hasExtra("id")) {
			Bundle extras = getIntent().getExtras();
			try {
				userId = Converter.toInt((String) extras.getString("id"));
				getCurrentUser(userId);
				Button submit = (Button) findViewById(R.id.submit);
				submit.setText("Update");
			} catch (Exception e) {

			}
		} else {
			getBranches(true);
		}
	}

	private void getCountriesFromDB() {
		String serverURL = new myURL(null, "countries", "get_all_cities_areas",
				0).getURL();
		RZHelper p = new RZHelper(serverURL, this, "setCountries", false, true);
		p.get();
	}

	public void setCountries(String s, String error) {
		countries = new APIManager().getCountries(s);
		DeliveryAdminApplication.setCountries(countries);
		updateList();
	}

	private void getCountries() {
		countries = DeliveryAdminApplication.getCountries();
		if (DeliveryAdminApplication.getCountries() == null)
			getCountriesFromDB();
		else
			updateList();
	}

	private void updateList() {
		ArrayAdapter<Country> counrytAdapter = new ArrayAdapter<Country>(this,
				android.R.layout.simple_spinner_item, countries);
		counrytAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		counrytAdapter.notifyDataSetChanged();
		countriesSP.setAdapter(counrytAdapter);
		countriesSP.setOnItemSelectedListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_info, menu);
		SharedMenuActivity.onCreateOptionsMenu(this, menu, getApplicationContext());
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (SharedMenuActivity.onOptionsItemSelected(item, this) == false) {
			// handle local menu items here or leave blank
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		Object sp1 = arg0.getSelectedItem();
		if (sp1 instanceof Country) {
			Country currentCountry = (Country) arg0.getSelectedItem();
			String countryCode = GetCountryZipCode(currentCountry.getName());
			code.setText("" + countryCode);

		} else if (sp1 instanceof Branch) {
			branchesSP = (Spinner) findViewById(R.id.branchesSP);
			Branch branch = (Branch) arg0.getSelectedItem();
			this.branchId = branch.getId();

		}

	}

	private String GetCountryZipCode(String CountryID) {

		String CountryZipCode = "";

		String[] rl = this.getResources().getStringArray(R.array.CountryCodes);
		for (int i = 0; i < rl.length; i++) {
			String[] g = rl[i].split(",");
			if (g[1].trim().equals(CountryID.trim())) {
				CountryZipCode = g[0];
				break;
			}
		}
		return CountryZipCode;

	}

	public void getCurrentUser(int userId) {
		String url = new myURL(null, "users", userId, 1).getURL();
		String serverURL = url;
		RZHelper p = new RZHelper(serverURL, this, "setUserInfo", true, false);
		p.get();
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
		getActionBar().setTitle(currentUser.getName());
		getBranches(false);
	}

	public void getBranches(boolean first) {
		User user = PreferenecesManager.getInstance().getUserFromPreferences(this);
		String serverURL = new myURL(null, "branches", user.getBranch_id(), 0).getURL();
		//String serverURL = new myURL("branches", "shops", shopId, 30).getURL();
		RZHelper p = new RZHelper(serverURL, this, "setBranches", true);
		p.get();
	}

	public void setBranches(String s, String error) {
		this.branches = new APIManager().getBranches(s);
		branchesSP = (Spinner) findViewById(R.id.branchesSP);
		ArrayAdapter<Branch> branchAdapter = new ArrayAdapter<Branch>(this,
				android.R.layout.simple_spinner_item, branches);
		branchAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		branchAdapter.notifyDataSetChanged();
		branchesSP.setAdapter(branchAdapter);
		branchesSP.setOnItemSelectedListener(this);
		if (currentUser != null)
			glob.setSelected(branchesSP, branchAdapter,
					new Branch(currentUser.getBranch_id()));
//		getCountries();
	}

	public void addUser(View view) {
		username = (EditText) findViewById(R.id.nameinput);
		inputphone = (EditText) findViewById(R.id.inputphone);
		branchesSP = (Spinner) findViewById(R.id.branchesSP);
		admin = (CheckBox) findViewById(R.id.admin);
		preparer = (CheckBox) findViewById(R.id.preparer);
		delivery = (CheckBox) findViewById(R.id.delivery);
		code = (EditText) findViewById(R.id.countrycode);

		User user = null;
		String serverURL = "";
		String method = "POST";
		branchId = ((Branch) branchesSP.getSelectedItem()).getId();
		if (userId > 0) {
			serverURL = new myURL(null, "users", userId, 0).getURL();
			user = new User(userId, username.getText().toString(),// name
					null,// password
					inputphone.getText().toString(),// phone
					0,// is fired
					currentUser.getAddress(),// address
					branchId,// branch
					admin.isChecked(), preparer.isChecked(),
					delivery.isChecked()); // roles
			method = "PUT";
		} else {
			serverURL = new myURL("users", null, 0, 0).getURL();
			user = new User(0, username.getText().toString(), null, inputphone
					.getText().toString(), 0, null, branchId,
					admin.isChecked(), preparer.isChecked(),
					delivery.isChecked());

			user.setEncPassword(inputphone.getText().toString());
		}
		ValidationError valid = user.validate(false);
		if (valid.isValid(this)) {
			RZHelper p = new RZHelper(serverURL, this, "setRoles", true, false);
			if (method.equals("POST")) {
				p.post(user);
			} else {
				p.put(user);
			}
		}

	}

	public void setRoles(String s, String error) {
		int id = 0;
		if (currentUser != null)
			id = currentUser.getId();
		if (id < 1) {
			id = new APIManager().getUserId(s);
		}
		String makePreparerURL = new myURL("set_roles", "users", id, 0)
				.getURL();
		admin = (CheckBox) findViewById(R.id.admin);
		preparer = (CheckBox) findViewById(R.id.preparer);
		delivery = (CheckBox) findViewById(R.id.delivery);
		Role role = new Role();
		role.setPreparer(preparer.isChecked());
		role.setAdmin(admin.isChecked());
		role.setDelivery(delivery.isChecked());
		ValidationError valid = role.validate(false);
		if (valid.isValid(this)) {
			RZHelper p = new RZHelper(makePreparerURL, this, "afterRoles",
					false, true);
			p.post(role);
		}

	}

	public void afterRoles(String s, String error) {
		Intent i = new Intent(this, UsersActivity.class);
		startActivity(i);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(UserInfoActivity.this, UsersActivity.class);
		startActivity(i);
	}

}
