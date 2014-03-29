package com.example.ontimedeliv;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UserProfileActivity extends Activity {
	EditText nameTxt ;
	EditText pass ;
	EditText pass2 ;
	Spinner langSp ;
	CheckBox keep ;
	
	String name ,phone;// = settings1.getString("name", "");
	String lang ;//= settings1.getString("lang", null);
	int id ;//= settings1.getInt("id", 0);
	int branchId ;//= settings1.getInt("id", 0);

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		
		nameTxt = (EditText) findViewById(R.id.user_name);
		pass = (EditText) findViewById(R.id.password);
		pass2 = (EditText) findViewById(R.id.password2);
		langSp = (Spinner) findViewById(R.id.languageSP);
		keep = (CheckBox) findViewById(R.id.keeploggedin);
		
		SharedPreferences settings1 = getSharedPreferences("PREFS_NAME", 0);
		boolean isChecked = settings1.getBoolean("isChecked", false);
		
		name = settings1.getString("name", "");
		phone = settings1.getString("phone", "");
		lang = settings1.getString("lang", null);
		id = settings1.getInt("id", 0);
		branchId = settings1.getInt("branchId", 0);
		
		List<String> list = new ArrayList<String>();
		list.add(getString(R.string.arabic));
		list.add(getString(R.string.english));
		if (lang.equals("en")) {
			lang = getString(R.string.english);
		} else {
			lang = getString(R.string.arabic);
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		langSp.setAdapter(dataAdapter);

		nameTxt.setText(name);
		keep.setChecked(isChecked);
		new GlobalM().setSelected(langSp, dataAdapter, lang);
	}
	public void updateInfo(View view)
	{
		if(!pass.getText().toString().equals(pass2.getText().toString()))
		{

			Toast.makeText(getApplicationContext(), R.string.wrongcredentials,
					Toast.LENGTH_SHORT).show();
		}
		else
		{
			Log.d("ray","ray pass: "+pass.getText().toString());
			String serverURL = new myURL(null, "users", id, 0).getURL();
			User user = new User( 
					nameTxt.getText().toString(),
					phone,
					pass.getText().toString(),
					branchId,
					0);
			String method = "PUT";
			new MyJs("done", this,((ontimedeliv) this.getApplication()), method, (Object) user,true,true)
			.execute(serverURL);
		}
	}
	public void done(String s,String error)
	{
		Toast.makeText(getApplicationContext(), "GOOD",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_profile, menu);
		
		return true;
	}
	

}
