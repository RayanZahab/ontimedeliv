package com.example.ontimedeliv;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class NavigationActivity extends Activity {
	MyCustomAdapter dataAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		((ontimedeliv) NavigationActivity.this.getApplication())
				.clear("listing");
	}

	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.exit)
				.setMessage(R.string.exitquest)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								NavigationActivity.this.finishAffinity();
							}
						}).setNegativeButton(android.R.string.no, null).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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

	public void select(View v) {

		Intent i;
		String method = "", status = null;
		switch (v.getId()) {
		case R.id.orders:
			method = "Orders";
			status = "opened";
			break;

		case R.id.closed:
			method = "Orders";
			status = "closed";
			break;

		case R.id.users:
			method = "Users";
			break;

		case R.id.branches:
			method = "Branches";
			break;

		case R.id.canceled:
			method = "Orders";
			status = "cancelled";
			break;

		case R.id.assigned:
			method = "Orders";
			status = "assigned";
			break;
		}

		try {
			i = new Intent(getBaseContext(), Class.forName(getPackageName()
					+ "." + method + "Activity"));
			if(status!=null)
			{
				((ontimedeliv) NavigationActivity.this.getApplication()).setOrderStatus(status);
				if(!status.equals("new")&& !status.equals("assigned"))
				{
					i.putExtra("old", true);
				}
			}
			startActivity(i);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}
