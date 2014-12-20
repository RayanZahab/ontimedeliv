package com.ontimedeliv;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class ProductInfoActivity extends Activity {
	ArrayList<Unit> units;
	int categoryId, branchId, shopId, productId;
	View upload;
	int RESULT_LOAD_IMAGE = 1;
	String picturePath, picName;
	Product currentProduct = null;
	Photo uploaded;
	TextView name;
	TextView desc;
	TextView price;
	Spinner unitsSP;
	GlobalM glob = new GlobalM();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		productId = ontimedeliv.getProductId(this);
		
		setContentView(R.layout.activity_add_product);
		name = (TextView) findViewById(R.id.productName);
		desc = (TextView) findViewById(R.id.description);
		price = (TextView) findViewById(R.id.price);
		unitsSP = (Spinner) findViewById(R.id.units);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		if (productId == 0) {
			//ActionBar actionBar = getActionBar();
			//actionBar.setDisplayHomeAsUpEnabled(true);
		}
		//setTheme(android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		
		ontimedeliv.clear("product");
		branchId = ontimedeliv.getBranchId(this);
		categoryId = ontimedeliv.getCategoryId(this);
		shopId = ontimedeliv.getShopId(this);
		if (productId != 0) {
			getProduct(productId);
		} else {
			getUnits(true);
		}

		upload = findViewById(R.id.preview);
		upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
	}

	public void getProduct(int id) {
		String serverURL = new myURL(null, "items", id, 1).getURL();
		RZHelper p = new RZHelper(serverURL, this, "setProduct", true);
		p.get();
	}

	public void setProduct(String s, String error) {
		currentProduct = new APIManager().getItemsByCategoryAndBranch(s).get(0);
		String n;
		try {
			n = new String((currentProduct.getName()).getBytes("iso-8859-1"),
					"UTF-8");

			name.setText(n);
			desc.setText(currentProduct.getDescription());
			price.setText("" + currentProduct.getPrice());
			//getActionBar().setTitle(currentProduct.getName());
			new ImageTask((ImageView) findViewById(R.id.preview),
					ProductInfoActivity.this).execute(currentProduct.getPhoto()
					.getUrl());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getUnits(false);
	}

	public void addProduct(View view) {

		String name_str = name.getText().toString();
		String desc_str = desc.getText().toString();
		int price_val = 0;
		if (!price.getText().toString().isEmpty())
			price_val = Converter.toInt(price.getText().toString());
		Product p = new Product(0, price_val, name_str, desc_str, uploaded,
				new Category(categoryId), (Unit) unitsSP.getSelectedItem(),
				true, shopId);
		ValidationError v = p.validate();
		if (v.isValid(this)) {
			addProduct(p);
		}
	}

	public void addProduct(Product prod) {
		String serverURL;
		if (currentProduct == null) {
			serverURL = new myURL("items", null, 0, 0).getURL();
		} else {
			serverURL = new myURL(null, "items", currentProduct.getId(), 0)
					.getURL();
		}

		new MyJs("afterCreation", this, ((ontimedeliv) this.getApplication()),
				"Upload", (Object) prod).execute(serverURL);
	}

	public void afterCreation(String s, String error) {

		Intent i = new Intent(this, ProductsActivity.class);
		ontimedeliv.setProductId(0);
		startActivity(i);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			picName = cursor.getString(columnIndex);
			uploaded = new Photo(picturePath, picName);
			cursor.close();
			if (uploaded.validate().isValid(this)) {
				new ImageTask((ImageView) findViewById(R.id.preview),
						ProductInfoActivity.this).execute(picturePath);
			}
		}
	}

	public void getUnits(boolean first) 
	{
		units = ontimedeliv.getUnits();
		if (units == null)
			getUnitsFromDB();
		else
			populateUnits();
	}

	public void getUnitsFromDB() 
	{
		String serverURL = new myURL("units", null, 0, 30).getURL();
		RZHelper p = new RZHelper(serverURL, this, "setUnits", false);
		p.get();
	}

	public void setUnits(String s, String error) {
		units = new APIManager().getUnits(s);		
		populateUnits();
	}
	public void populateUnits()
	{
		ArrayAdapter<Unit> dataAdapter = new ArrayAdapter<Unit>(this,
				android.R.layout.simple_spinner_item, units);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		unitsSP.setAdapter(dataAdapter);

		if (currentProduct != null) {
			Log.d("ray", "selecting" + currentProduct.getUnit().getId());
			glob.setSelected(unitsSP, dataAdapter, currentProduct.getUnit());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.navigation, menu);
		SharedMenu.onCreateOptionsMenu(this, menu, getApplicationContext());
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (SharedMenu.onOptionsItemSelected(item, this) == false) {
			// handle local menu items here or leave blank
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		ontimedeliv.setProductId(0);
		Intent i = new Intent(ProductInfoActivity.this, ProductsActivity.class);
		//super.onBackPressed();
		startActivity(i);
	}

}
