package com.example.ontimedeliv;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class UserInfoActivity extends Activity {

	Button addButton;
	EditText username, inputphone;
	CheckBox admin, preparer, delivery;
	Spinner branchesSP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_info, menu);
		return true;
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
				inputphone.getText().toString(),0,new Address(0,"", "", "","", "", "","", 0, "","", "", ""),0);
		String serverURL = new myURL().getURL("users", null, 0, 30);;
		ProgressDialog Dialog = new ProgressDialog(this);
		new MyJs(Dialog, "backToSelection", this, "POST", (Object) newUser)
				.execute(serverURL);
	}
	public void backToSelection(String s)
	{
		Intent i = new Intent(this, UsersActivity.class);
		startActivity(i);
	}

}
