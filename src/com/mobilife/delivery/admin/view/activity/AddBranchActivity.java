package com.mobilife.delivery.admin.view.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobilife.delivery.admin.DeliveryAdminApplication;
import com.mobilife.delivery.admin.R;
import com.mobilife.delivery.admin.ValidationError;
import com.mobilife.delivery.admin.adapter.ExpandableListAdapter;
import com.mobilife.delivery.admin.model.Area;
import com.mobilife.delivery.admin.model.Branch;
import com.mobilife.delivery.admin.model.City;
import com.mobilife.delivery.admin.model.Country;
import com.mobilife.delivery.admin.model.OpenHours;
import com.mobilife.delivery.admin.model.Shop;
import com.mobilife.delivery.admin.utilities.APIManager;
import com.mobilife.delivery.admin.utilities.GlobalM;
import com.mobilife.delivery.admin.utilities.Helper;
import com.mobilife.delivery.admin.utilities.RZHelper;
import com.mobilife.delivery.admin.utilities.myURL;

public class AddBranchActivity extends Activity implements
		OnItemSelectedListener {

	Button from, to;
	int fmHour, fmMinute, tHour, tMinute, shopId, branchId = 0;
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
	String openMethod = "update_opening_hours";
	String serverURL = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_branch);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		shopId = DeliveryAdminApplication.getShopId(this);
		countrySp = (Spinner) findViewById(R.id.countriesSP);
		citySp = (Spinner) findViewById(R.id.citiesSP);
		areasSp = (Spinner) findViewById(R.id.areasSP);

		Dialog = new ProgressDialog(this);
		Dialog.setCancelable(false);
		DeliveryAdminApplication.clear("branch");
		branchId = DeliveryAdminApplication.getBranchId(this);
		DeliveryAdminApplication.clear("listing");
		if (branchId != 0) { // old branch (edit)
			getCurrentBranch(branchId);
			Button submit = (Button) findViewById(R.id.submit);
			serverURL = new myURL(null, "branches", branchId, 0).getURL();
			submit.setText(getString(R.string.update));
		} else { // new branch
			getCountries();
//			populateExp(null);
			
			serverURL = new myURL("branches", null, 0, 0).getURL();
			openMethod = "opening_hours";
		}

	}

	private void populateExp(Branch b) {
		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		expListView.setVisibility(View.VISIBLE);
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
				new Helper().getListViewSize(expListView);
			}
		});

		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
				new Helper(100).getListViewSize(expListView);

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
								+ listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		});
	}

	private void getCurrentBranch(int branchId) {
		String url = new myURL(null, "branches", branchId, 1).getURL();
		// to get only one branch with id=branchId 
		url+="&only=true";
		
		RZHelper p;
		if (branchId != 0 && DeliveryAdminApplication.getCountries() == null)
			p = new RZHelper(url, this, "setBranchInfo", true, false);
		else
			p = new RZHelper(url, this, "setBranchInfo", true);
		p.get();
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
		EditText delivery_charge = ((EditText) findViewById(R.id.deliverycharge));
		EditText min_order = ((EditText) findViewById(R.id.minorder));

		delivery_charge.setText(currentBranch.getDelivery_charge());
		min_order.setText(currentBranch.getMin_amount());
		name.setText(currentBranch.getName());
		desc.setText(currentBranch.getDescription());
		address.setText(currentBranch.getAddress());
		estimation.setText(currentBranch.getEstimation_time());
		getActionBar().setTitle(currentBranch.getName());
		getCountries();
	}

	public void addBranch(View v) {

		areasSp = (Spinner) findViewById(R.id.areasSP);
		int selectedArea = 0;
		if (areasSp.getSelectedItem() != null)
			selectedArea = ((Area) areasSp.getSelectedItem()).getId();
		String name = ((EditText) findViewById(R.id.editTextAddName)).getText()
				.toString();
		String desc = ((EditText) findViewById(R.id.addDesc)).getText()
				.toString();
		String address = ((EditText) findViewById(R.id.editTextAddress))
				.getText().toString();
		String estimation = ((EditText) findViewById(R.id.estimation))
				.getText().toString();
		String deliverycharge = ((EditText) findViewById(R.id.deliverycharge))
				.getText().toString();
		String minorder = ((EditText) findViewById(R.id.minorder))
				.getText().toString();

		currentBranch = new Branch(branchId, name, desc,
				new Area(selectedArea), address, new Shop(shopId), estimation);
		currentBranch.setDelivery_charge(deliverycharge);
		currentBranch.setMin_amount(minorder);	
		if (branchId != 0)  
			currentBranch.setTosFroms(listAdapter.froms, listAdapter.tos, listAdapter.openDays);
		ValidationError valid = currentBranch.validate();

		if (valid.isValid(this)) {
			RZHelper p;
			if (branchId == 0) { // new branch
				p = new RZHelper(serverURL, this, "backToSelection", true);
				p.post(currentBranch);
			} else { // edit old branch
				p = new RZHelper(serverURL, this, "openHours", true, false);
				p.put(currentBranch);
			}
		}

	}

	public void openHours(String s, String error) {
		if (branchId == 0)
			branchId = new APIManager().getBranchId(s);
		if (error == null) {
			String ourl = new myURL(openMethod, "branches", branchId, 0).getURL();
			RZHelper p = new RZHelper(ourl, this, "backToSelection", false,	true);
			p.put(new OpenHours(currentBranch));
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
		SharedMenuActivity.onCreateOptionsMenu(this, menu, getApplicationContext());
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (SharedMenuActivity.onOptionsItemSelected(item, this) == false) {
			// handle local menu items here or leave blank
		}
		return super.onOptionsItemSelected(item);
	}

	public void getCountriesFromDB() {
		String serverURL = new myURL(null, "countries", "get_all_cities_areas",
				0).getURL();
		RZHelper p;
		if (branchId != 0)
			p = new RZHelper(serverURL, this, "setCountries", false, true);
		else
			p = new RZHelper(serverURL, this, "setCountries", false);
		p.get();
	}

	public void setCountries(String s, String error) {
		countries = new APIManager().getCountries(s);
		DeliveryAdminApplication.setCountries(countries);
		updateList("country");
	}

	public void getCountries() {
		countries = DeliveryAdminApplication.getCountries();
		if (DeliveryAdminApplication.getCountries() == null)
			getCountriesFromDB();
		else
			updateList("country");
	}

	public void getCities(int CountryId) {
		cities = countries.get(CountryId).getCities();
		updateList("city");
	}

	public void getAreas(int CityId) {
		areas = cities.get(CityId).getAreas();
		updateList("area");
	}

	public void updateList(String type) {
		if (type.equals("country")) {
			ArrayAdapter<Country> counrytAdapter = new ArrayAdapter<Country>(
					this, android.R.layout.simple_spinner_item, countries);
			counrytAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			counrytAdapter.notifyDataSetChanged();
			areasSp.setAdapter(null);
			countrySp.setAdapter(counrytAdapter);
			countrySp.setOnItemSelectedListener(this);
			if (branchId != 0)
				glob.setSelected(countrySp, counrytAdapter, new Country(
						currentBranch.getArea().getCountry_id()));
		} else if (type.equals("city")) {
			ArrayAdapter<City> cityAdapter = new ArrayAdapter<City>(this,
					android.R.layout.simple_spinner_item, cities);
			cityAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			cityAdapter.notifyDataSetChanged();
			areasSp.setAdapter(null);
			citySp.setAdapter(cityAdapter);
			citySp.setOnItemSelectedListener(this);
			if (branchId != 0)
				glob.setSelected(citySp, cityAdapter, new City(currentBranch
						.getArea().getCity_id()));
		} else if (type.equals("area")) {
			ArrayAdapter<Area> areaAdapter = new ArrayAdapter<Area>(this,
					android.R.layout.simple_spinner_item, areas);
			areaAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			areaAdapter.notifyDataSetChanged();
			areasSp.setAdapter(areaAdapter);
			areasSp.setOnItemSelectedListener(this);
			if (branchId != 0)
				glob.setSelected(areasSp, areaAdapter, currentBranch.getArea());
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Object sp1 = arg0.getSelectedItem();
		if (sp1 instanceof Country) {
			getCities(position);
		} else if (sp1 instanceof City) {
			getAreas(position);
		}

	}

}
