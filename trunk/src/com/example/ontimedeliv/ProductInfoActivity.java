package com.example.ontimedeliv;

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
	Button upload;
	int RESULT_LOAD_IMAGE = 1;
	String picturePath, picName;
	Product currentProduct = null;
	Photo uploaded;
	TextView name ;
	TextView desc ;
	TextView price ;
	Spinner unitsSP ;
	GlobalM glob = new GlobalM();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_product);
		name = (TextView) findViewById(R.id.productName);
		desc = (TextView) findViewById(R.id.description);
		price = (TextView) findViewById(R.id.price);
		unitsSP = (Spinner) findViewById(R.id.units);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		((ontimedeliv) this.getApplication()).clear("product");
		branchId = ((ontimedeliv) this.getApplication()).getBranchId();
		categoryId = ((ontimedeliv) this.getApplication()).getCategoryId();
		productId = ((ontimedeliv) this.getApplication()).getProductId();
		shopId = ((ontimedeliv) this.getApplication()).getShopId();
		if (productId != 0) {
			getProduct(productId);
		} else {
			getUnits(true);
		}

		upload = (Button) findViewById(R.id.uploadimage);
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
		new MyJs("setProduct", this, ((ontimedeliv) this.getApplication()),
				"GET", true, false).execute(serverURL);
	}

	public void setProduct(String s, String error) {
		currentProduct = new APIManager().getItemsByCategoryAndBranch(s).get(0);

		name.setText(currentProduct.getName());
		desc.setText(currentProduct.getDescription());
		price.setText("" + currentProduct.getPrice());

		new ImageTask((ImageView) findViewById(R.id.preview),ProductInfoActivity.this)
				.execute(currentProduct.getPhoto().getUrl());

		getUnits(false);
	}

	public void addProduct(View view) {

		String name_str = name.getText().toString();
		String desc_str = desc.getText().toString();
		int price_val = 0;
		if (!price.getText().toString().isEmpty())
			price_val = Integer.parseInt(price.getText().toString());
		Product p = new Product(0, price_val, name_str, desc_str, uploaded,
				new Category(categoryId), (Unit) unitsSP.getSelectedItem(),
				true, shopId);
		ValidationError v = p.validate();
		if (v.isValid(this)) {
			addProduct(p);
		}
	}

	public void addProduct(Product p) {
		String serverURL;
		if (currentProduct == null) {
			serverURL = new myURL("items", null, 0, 0).getURL();
		} else {
			serverURL = new myURL(null, "items", currentProduct.getId(), 0)
					.getURL();
		}

		new MyJs("afterCreation", this, ((ontimedeliv) this.getApplication()),
				"Upload", (Object) p).execute(serverURL);
	}

	public void afterCreation(String s, String error) {

		Intent i = new Intent(this, ProductsActivity.class);
		((ontimedeliv) ProductInfoActivity.this.getApplication())
				.setProductId(0);
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
			Log.d("rays","path"+picturePath+ "->"+picName);
			uploaded = new Photo(picturePath, picName);
			cursor.close();
			new ImageTask((ImageView) findViewById(R.id.preview),ProductInfoActivity.this)
			.execute(picturePath);
		}
	}

	public void setUnits(String s, String error) {
		units = new APIManager().getUnits(s);
		ArrayAdapter<Unit> dataAdapter = new ArrayAdapter<Unit>(this,
				android.R.layout.simple_spinner_item, units);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		unitsSP.setAdapter(dataAdapter);

		if (currentProduct != null) {
			Log.d("ray","selecting"+currentProduct.getUnit().getId());
			glob.setSelected(unitsSP, dataAdapter, currentProduct.getUnit());
		}
	}

	public void getUnits(boolean first) {
		// getUnits
		String serverURL = new myURL("units", null, 0, 30).getURL();
		new MyJs("setUnits", this, ((ontimedeliv) this.getApplication()),
				"GET", first, true).execute(serverURL);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.navigation, menu);
		SharedMenu.onCreateOptionsMenu(this,menu, getApplicationContext());
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
		((ontimedeliv) ProductInfoActivity.this.getApplication())
				.setProductId(0);
		Intent i = new Intent(ProductInfoActivity.this, ProductsActivity.class);
		startActivity(i);
	}

}
