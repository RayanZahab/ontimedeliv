package com.example.ontimedeliv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class AddBranchActivity extends Activity implements
		OnItemSelectedListener {

	Button from, to;
	int fmHour, fmMinute, tHour, tMinute, shopId, branchId;
	ArrayList<Country> countries = new ArrayList<Country>();
	ArrayList<City> cities = new ArrayList<City>();
	ArrayList<Area> areas = new ArrayList<Area>();
	Spinner countrySp, citySp, areasSp;
	ProgressDialog Dialog;
	Branch currentBranch;
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	GlobalM glob = new GlobalM();
	boolean click = false;
	String method = "PUT";
	String openMethod = "update_opening_hours";
	String serverURL = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_branch);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		shopId = ((ontimedeliv) this.getApplication()).getShopId();
		countrySp = (Spinner) findViewById(R.id.countriesSP);
		citySp = (Spinner) findViewById(R.id.citiesSP);
		areasSp = (Spinner) findViewById(R.id.areasSP);
		countrySp.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				click = true;
				return false;
			}
		});
		Dialog = new ProgressDialog(this);
		Dialog.setCancelable(false);
		((ontimedeliv) this.getApplication()).clear("branch");
		branchId = ((ontimedeliv) this.getApplication()).getBranchId();
		if (branchId != 0) {
			getCurrentBranch(branchId);
			Button submit = (Button) findViewById(R.id.submit);
			serverURL = new myURL(null, "branches", branchId, 0).getURL();
			submit.setText("Update");
		} else {
			getCountries(true);
			populateExp(null);
			serverURL = new myURL("branches", null, 0, 0).getURL();
			method = "POST";
			openMethod = "opening_hours";
		}

	}

	public void populateExp(Branch b) {
		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		prepareListData();
		if (b != null) {
			OpenHours oh = b.getOpenHours();
			listAdapter = new ExpandableListAdapter(this, listDataHeader,
					listDataChild, oh);
		} else {
			listAdapter = new ExpandableListAdapter(this, listDataHeader,
					listDataChild);
		}
		expListView.setAdapter(listAdapter);
		expListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return false;
			}
		});

		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				Toast.makeText(getApplicationContext(),
						listDataHeader.get(groupPosition) + " Expanded",
						Toast.LENGTH_SHORT).show();
				new Helper(900).getListViewSize(expListView);
			}
		});

		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
				Toast.makeText(getApplicationContext(),
						listDataHeader.get(groupPosition) + " Collapsed",
						Toast.LENGTH_SHORT).show();
				new Helper(60).getListViewSize(expListView);

			}
		});

		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Toast.makeText(
						getApplicationContext(),
						listDataHeader.get(groupPosition)
								+ " : "
								+ listDataChild.get(
										listDataHeader.get(groupPosition)).get(
										childPosition), Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		});
	}

	public void getCurrentBranch(int branchId) {
		String url = new myURL(null, "branches", branchId, 1).getURL();
		String serverURL = url;
		new MyJs("setBranchInfo", this, ((ontimedeliv) this.getApplication()),
				"GET", true, false).execute(serverURL);

	}

	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();
		listDataHeader.add(getString(R.string.opening_hours));
		List<String> openhour = new ArrayList<String>();
		openhour.add(getString(R.string.monday));
		openhour.add(getString(R.string.tuseday));
		openhour.add(getString(R.string.wednesday));
		openhour.add(getString(R.string.thursday));
		openhour.add(getString(R.string.friday));
		openhour.add(getString(R.string.saturday));
		openhour.add(getString(R.string.sunday));

		listDataChild.put(listDataHeader.get(0), openhour);
	}

	public void setBranchInfo(String s, String error) {
		currentBranch = new APIManager().getBranch(s);
		populateExp(currentBranch);
		countrySp = (Spinner) findViewById(R.id.countriesSP);
		citySp = (Spinner) findViewById(R.id.citiesSP);
		areasSp = (Spinner) findViewById(R.id.areasSP);
		EditText name = ((EditText) findViewById(R.id.editTextAddName));
		EditText desc = ((EditText) findViewById(R.id.addDesc));
		EditText address = ((EditText) findViewById(R.id.editTextAddress));
		EditText estimation = ((EditText) findViewById(R.id.estimation));

		name.setText(currentBranch.getName());
		desc.setText(currentBranch.getDescription());
		address.setText(currentBranch.getAddress());
		estimation.setText(currentBranch.getEstimation_time());
		getCountries(false);
	}

	public void addBranch(View v) {

		areasSp = (Spinner) findViewById(R.id.areasSP);
		int selectedArea = ((Area) areasSp.getSelectedItem()).getId();
		String name = ((EditText) findViewById(R.id.editTextAddName)).getText()
				.toString();
		String desc = ((EditText) findViewById(R.id.addDesc)).getText()
				.toString();
		String address = ((EditText) findViewById(R.id.editTextAddress))
				.getText().toString();
		String estimation = ((EditText) findViewById(R.id.estimation))
				.getText().toString();

		currentBranch = new Branch(branchId, name, desc,
				new Area(selectedArea), address, new Shop(shopId), estimation);
		currentBranch.setTosFroms(listAdapter.froms, listAdapter.tos,
				listAdapter.openDays);
		ValidationError valid = currentBranch.validate();

		if (valid.isValid(this)) {
			new MyJs("openHours", this, ((ontimedeliv) this.getApplication()),
					method, (Object) currentBranch).execute(serverURL);
		}

	}

	public void openHours(String s, String error) {
		if (branchId == 0)
			branchId = new APIManager().getBranchId(s);
		if (error == null) {
			String ourl = new myURL(openMethod, "branches", branchId, 0)
					.getURL();
			new MyJs("backToSelection", this,
					((ontimedeliv) this.getApplication()), method,
					(Object) new OpenHours(currentBranch)).execute(ourl);
		}
	}

	public void backToSelection(String s, String error) {
		Intent intent = new Intent(this, BranchesActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_branch, menu);
		SharedMenu.onCreateOptionsMenu(menu, getApplicationContext());
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (SharedMenu.onOptionsItemSelected(item, this) == false) {
			// handle local menu items here or leave blank
		}
		return super.onOptionsItemSelected(item);
	}

	public void getCountries(boolean first) {
		String serverURL = new myURL("countries", null, 0, 30).getURL();
		MyJs mjs = new MyJs("setCountries", AddBranchActivity.this,
				((ontimedeliv) this.getApplication()), "GET", first, true);
		mjs.execute(serverURL);
	}

	public void setCountries(String s, String error) {
		click = false;
		countries = new APIManager().getCountries(s);
		ArrayAdapter<Country> counrytAdapter = new ArrayAdapter<Country>(this,
				android.R.layout.simple_spinner_item, countries);
		counrytAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		counrytAdapter.notifyDataSetChanged();
		areasSp.setAdapter(null);
		countrySp.setAdapter(counrytAdapter);
		countrySp.setOnItemSelectedListener(this);
		if (currentBranch != null
				&& currentBranch.getArea().getCountry_id() != 0)
		{
			glob.setSelected(countrySp, counrytAdapter, new Country(
					currentBranch.getArea().getCountry_id()));
		}
		
	}

	public void getCities(int CountryId) {
		String serverURL = new myURL("cities", "countries", CountryId, 30)
				.getURL();
		new MyJs("setCities", AddBranchActivity.this,
				((ontimedeliv) this.getApplication()), "GET", click, true)
				.execute(serverURL);
		click = false;
	}

	public void setCities(String s, String error) {
		cities = new APIManager().getCitiesByCountry(s);
		ArrayAdapter<City> cityAdapter = new ArrayAdapter<City>(this,
				android.R.layout.simple_spinner_item, cities);
		cityAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cityAdapter.notifyDataSetChanged();
		areasSp.setAdapter(null);
		citySp.setAdapter(cityAdapter);
		citySp.setOnItemSelectedListener(this);
		if (currentBranch != null && currentBranch.getArea().getCity_id() != 0)
			glob.setSelected(citySp, cityAdapter, new City(currentBranch
					.getArea().getCity_id()));
	}

	public void getAreas(int CityId) {
		String serverURL = new myURL("areas", "cities", CityId, 30).getURL();
		MyJs mjs = new MyJs("setAreas", AddBranchActivity.this,
				((ontimedeliv) this.getApplication()), "GET", click, true);
		click = false;
		mjs.execute(serverURL);
	}

	public void setAreas(String s, String error) {
		areas = new APIManager().getAreasByCity(s);
		ArrayAdapter<Area> areaAdapter = new ArrayAdapter<Area>(this,
				android.R.layout.simple_spinner_item, areas);
		areaAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		areaAdapter.notifyDataSetChanged();
		areasSp.setAdapter(areaAdapter);
		areasSp.setOnItemSelectedListener(this);
		if (currentBranch != null && currentBranch.getArea() != null)
			glob.setSelected(areasSp, areaAdapter, new Area(currentBranch
					.getArea().getId()));
		click=true;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		Object sp1 = arg0.getSelectedItem();
		if (sp1 instanceof Country) {
			if (((Country) sp1).getId() != 0)
				getCities(((Country) sp1).getId());
		} else if (sp1 instanceof City) {
			if (((City) sp1).getId() != 0)
				getAreas(((City) sp1).getId());
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		Log.d("ray", "ray nothing");
	}

}
