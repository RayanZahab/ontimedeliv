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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobilife.delivery.admin.DeliveryAdminApplication;
import com.mobilife.delivery.admin.R;
import com.mobilife.delivery.admin.ValidationError;
import com.mobilife.delivery.admin.model.Branch;
import com.mobilife.delivery.admin.model.Role;
import com.mobilife.delivery.admin.model.User;
import com.mobilife.delivery.admin.utilities.APIManager;
import com.mobilife.delivery.admin.utilities.Converter;
import com.mobilife.delivery.admin.utilities.GlobalM;
import com.mobilife.delivery.admin.utilities.PreferenecesManager;
import com.mobilife.delivery.admin.utilities.RZHelper;
import com.mobilife.delivery.admin.utilities.myURL;

public class UserInfoActivity extends Activity implements OnItemSelectedListener {

	Button addButton;
	EditText username, inputphone;
	Spinner branchesSP;
	User currentUser;
	int branchId, userId = 0;
	ArrayList<Branch> branches;
	GlobalM glob = new GlobalM();
	private EditText password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(PreferenecesManager.getInstance().getUserFromPreferences(this).isSuperAdmin())
			setContentView(R.layout.activity_super_user_info);
		else
			setContentView(R.layout.activity_user_info);

		ActionBar actionBar = getActionBar();
		
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
		if (sp1 instanceof Branch) {
			branchesSP = (Spinner) findViewById(R.id.branchesSP);
			Branch branch = (Branch) arg0.getSelectedItem();
			this.branchId = branch.getId();
		}

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
		password = (EditText)findViewById(R.id.inputPassword);
		username.setText(currentUser.getName());
		inputphone.setText(currentUser.getPhone());
		password.setText(currentUser.getPassword());
		getActionBar().setTitle(currentUser.getName());
		getBranches(false);
	}

	public void getBranches(boolean first) {
		User user = PreferenecesManager.getInstance().getUserFromPreferences(this);
		String serverURL = new myURL(null, "branches", user.getBranch_id(), 0).getURL();
		RZHelper p = new RZHelper(serverURL, this, "setBranches", true);
		p.get();
	}

	public void setBranches(String s, String error) {
		this.branches = new APIManager().getBranches(s);
		branchesSP = (Spinner) findViewById(R.id.branchesSP);
		ArrayAdapter<Branch> branchAdapter = new ArrayAdapter<Branch>(this, android.R.layout.simple_spinner_item, branches);
		branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		branchAdapter.notifyDataSetChanged();
		branchesSP.setAdapter(branchAdapter);
		branchesSP.setOnItemSelectedListener(this);
		if (currentUser != null)
			glob.setSelected(branchesSP, branchAdapter,new Branch(currentUser.getBranch_id()));
	}

	public void addUser(View view) {
		password = (EditText)findViewById(R.id.inputPassword);
		username = (EditText) findViewById(R.id.nameinput);
		inputphone = (EditText) findViewById(R.id.inputphone);
		User user = null;
		String serverURL = "";
		String method = "POST";
		branchId = _getBranchId();
		if (userId > 0) {
			serverURL = new myURL(null, "users", userId, 0).getURL();
			user = new User(userId, username.getText().toString(),null,//pass
					inputphone.getText().toString(),// phone
					0,// is fired
					currentUser.getAddress(),// address
					branchId,// branch
					true, false,
					false); // roles
			user.setEncPassword(password.getText().toString());
			method = "PUT";
		}else{
			serverURL = new myURL("users", null, 0, 0).getURL();
			user = new User(0, username.getText().toString(), null, inputphone
					.getText().toString(), 0, null, branchId,true, false,false);
			user.setPassword(password.getText().toString());
		}
		ValidationError valid = user.validate(false);
		if (valid.isValid(this)) {
			RZHelper p = new RZHelper(serverURL, this, "setRoles", true);
			if (method.equals("POST")) {
				p.post(user);
			} else {
				p.put(user);
			}
		}

	}

	private int _getBranchId() {
		if(PreferenecesManager.getInstance().getUserFromPreferences(this).isSuperAdmin()){
			branchesSP = (Spinner) findViewById(R.id.branchesSP);
			return ((Branch) branchesSP.getSelectedItem()).getId();
		}else{
			return PreferenecesManager.getInstance().getUserFromPreferences(this).getBranch_id();
		}
	}


	public void setRoles(String s, String error) {
		if(error==null){
			int id = 0;
			if (currentUser != null)
				id = currentUser.getId();
			if (id < 1) {
				id = new APIManager().getUserId(s);
			}
			String makePreparerURL = new myURL("set_roles", "users", id, 0).getURL();
			Role role = new Role();
			role.setPreparer(false);
			role.setAdmin(true);
			role.setDelivery(false);
			ValidationError valid = role.validate(false);
			if (valid.isValid(this)) {
				RZHelper p = new RZHelper(makePreparerURL, this, "afterRoles",true);
				p.post(role);
			}
		}else{
			Toast.makeText(getApplicationContext(),R.string.problemInUserCreate, Toast.LENGTH_SHORT).show();
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
