package com.example.ontimedeliv;

import java.util.ArrayList;
import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Spinner;
import android.widget.Switch;

public class SelectLanguageActivity extends Activity {
	MyCustomAdapter dataAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_language);
		SharedPreferences settings1 = getSharedPreferences("PREFS_NAME", 0);
		String lang = settings1.getString("lang", null);
		if(lang!=null)
		{
			Intent i = new Intent(SelectLanguageActivity.this, LoginActivity.class);
			startActivity(i);
		}
	}
	public void select(View view){
		Spinner language = (Spinner) findViewById(R.id.languageSP);
		String lang = language.getSelectedItem().toString();
		Log.d("rays","ray changed"+lang);
		String lang_ab = "en";
		if (!lang.equals("English")) {
			lang_ab = "fr";
			
		} else {
			lang_ab = "en";
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
		Intent i = new Intent(SelectLanguageActivity.this, LoginActivity.class);
		startActivity(i);
	}

	@Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    	SelectLanguageActivity.this.finishAffinity();
                    }
                }).setNegativeButton("No", null).show();
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.navigation, menu);
		SharedMenu.onCreateOptionsMenu(menu, getApplicationContext());
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (SharedMenu.onOptionsItemSelected(item, this) == false) {
			// handle local menu items here or leave blank
		}
		return super.onOptionsItemSelected(item);
	}
}
