package com.example.ontimedeliv;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class UserInfoActivity extends Activity implements OnItemSelectedListener {

	Button addButton;
	EditText username, inputphone;
	CheckBox admin, preparer, delivery;
	Spinner branchesSP;
	User currentUser;
	int branchId;
	ArrayList<Branch> branches;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		int userId;
		if (getIntent().hasExtra("id")) {
			Bundle extras = getIntent().getExtras();
			try{
				userId = Integer.parseInt((String) extras.getString("id"));
				getCurrentUser(userId);	
			}
			catch(Exception e )
			{
				
			}
		}
		getBranches();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_info, menu);
		
		return true;
	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		branchesSP = (Spinner) findViewById(R.id.branchesSP);
		Branch branch = (Branch) arg0.getSelectedItem();
		this.branchId=branch.getId();

	}
	public void getCurrentUser(int userId)
	{
		String url = new myURL().getURL(null, "users", userId, 1);	
		String serverURL = url;
		Log.d("rays", "ray url" + url);
		ProgressDialog Dialog = new ProgressDialog(this);

		new MyJs(Dialog, "setUserInfo", this, "GET").execute(serverURL);
	}
	public void setUserInfo(String s) {
		currentUser = (new APIManager().getUsers(s)).get(0);
		
		username = (EditText) findViewById(R.id.nameinput);
		inputphone = (EditText) findViewById(R.id.inputphone);
		branchesSP = (Spinner) findViewById(R.id.branchesSP);
		admin = (CheckBox) findViewById(R.id.admin);
		preparer = (CheckBox) findViewById(R.id.preparer);
		delivery = (CheckBox) findViewById(R.id.delivery);
		
		username.setText(currentUser.getName());
		inputphone.setText(currentUser.getPhone());
		branchesSP.setSelection(currentUser.getBranch_id());
		//branches.indexOf(br)
		
	}
	public void getBranches() {
		String serverURL = new myURL().getURL("branches", "shops", 37, 30);
		ProgressDialog Dialog = new ProgressDialog(this);

		new MyJs(Dialog, "setBranches", this, "GET").execute(serverURL);
	}
	public void setBranches(String s) {
		this.branches = new APIManager().getBranchesByShop(s);
		branchesSP = (Spinner) findViewById(R.id.branchesSP);
		ArrayAdapter<Branch> counrytAdapter = new ArrayAdapter<Branch>(this,
				android.R.layout.simple_spinner_item, branches);
		counrytAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		counrytAdapter.notifyDataSetChanged();
		branchesSP.setAdapter(counrytAdapter);
		branchesSP.setOnItemSelectedListener(this);
	}
	
	public void addUser(View view)
	{
		username = (EditText) findViewById(R.id.nameinput);
		inputphone = (EditText) findViewById(R.id.inputphone);
		branchesSP = (Spinner) findViewById(R.id.branchesSP);
		admin = (CheckBox) findViewById(R.id.admin);
		preparer = (CheckBox) findViewById(R.id.preparer);
		delivery = (CheckBox) findViewById(R.id.delivery);
		User newUser =new User(0,username.getText().toString(),username.getText().toString(),"",inputphone.getText().toString(),
				inputphone.getText().toString(),0,new Address(0,"default", "default", "default","default", "default", "default","default", 0, "default","default", "default", "default"),
				branchId);
		String serverURL = new myURL().getURL("users", null, 0, 0);
		ProgressDialog Dialog = new ProgressDialog(this);
		new MyJs(Dialog, "makePreparer", this, "POST", (Object) newUser).execute(serverURL);
		
	}
	public void makePreparer(String s)
	{
		int id = new APIManager().getUserId(s);
		ProgressDialog Dialog = new ProgressDialog(this);
		String makePreparerURL = new myURL().getURL("preparer", "users",id, 0);
		Role role=new Role();
		role.setPreparer(1);
		new MyJs(Dialog, "afterPreparer", this, "POST", (Object) role).execute(makePreparerURL);
	}
	public void afterPreparer(String s)
	{
		delivery = (CheckBox) findViewById(R.id.delivery);
		if(delivery.isChecked())
		{
			//makeDelivery(s);
		}
		Intent i = new Intent(this, UsersActivity.class);
		startActivity(i);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
