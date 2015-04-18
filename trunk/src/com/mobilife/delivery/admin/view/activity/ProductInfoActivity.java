package com.mobilife.delivery.admin.view.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobilife.delivery.admin.DeliveryAdminApplication;
import com.mobilife.delivery.admin.R;
import com.mobilife.delivery.admin.ValidationError;
import com.mobilife.delivery.admin.model.Category;
import com.mobilife.delivery.admin.model.Photo;
import com.mobilife.delivery.admin.model.Product;
import com.mobilife.delivery.admin.model.Unit;
import com.mobilife.delivery.admin.utilities.APIManager;
import com.mobilife.delivery.admin.utilities.Converter;
import com.mobilife.delivery.admin.utilities.GlobalM;
import com.mobilife.delivery.admin.utilities.MyJs;
import com.mobilife.delivery.admin.utilities.RZHelper;
import com.mobilife.delivery.admin.utilities.myURL;

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
		productId = DeliveryAdminApplication.getProductId(this);

		setContentView(R.layout.activity_add_product);
		name = (TextView) findViewById(R.id.productName);
		desc = (TextView) findViewById(R.id.description);
		price = (TextView) findViewById(R.id.price);
		unitsSP = (Spinner) findViewById(R.id.units);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		if (productId == 0) {
			// ActionBar actionBar = getActionBar();
			// actionBar.setDisplayHomeAsUpEnabled(true);
		}
		// setTheme(android.R.style.Theme_Holo_Light_Dialog_NoActionBar);

		DeliveryAdminApplication.clear("product");
		branchId = DeliveryAdminApplication.getBranchId(this);
		categoryId = DeliveryAdminApplication.getCategoryId(this);
		shopId = DeliveryAdminApplication.getShopId(this);
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
		RZHelper p = new RZHelper(serverURL, this, "setProduct", true, false);
		p.get();
	}

	public void setProduct(String s, String error) {
		currentProduct = new APIManager().getItemsByCategoryAndBranch(s).get(0);
		name.setText(currentProduct.getName());
		desc.setText(currentProduct.getDescription());
		price.setText("" + currentProduct.getPrice());
		// getActionBar().setTitle(currentProduct.getName());
		new RZHelper((ImageView) findViewById(R.id.preview), currentProduct
				.getPhoto().getUrl(), ProductInfoActivity.this);
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
		p.setBranchId(DeliveryAdminApplication.getBranchId(getApplicationContext()));
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

		new MyJs("afterCreation", this, ((DeliveryAdminApplication) this.getApplication()),
				"Upload", (Object) prod).execute(serverURL);

	}

	public void afterCreation(String s, String error) {

		Intent i = new Intent(this, ProductsActivity.class);
		DeliveryAdminApplication.setProductId(0);
		startActivity(i);
	}

	public void getUnits(boolean first) {
		units = DeliveryAdminApplication.getUnits();
		if (units == null)
			getUnitsFromDB();
		else
			populateUnits();
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
				// new RZHelper((ImageView) findViewById(R.id.preview),
				// picturePath, ProductInfoActivity.this);
				Bitmap myBitmap = BitmapFactory.decodeFile(picturePath);

				ImageView myImage = (ImageView) findViewById(R.id.preview);

				myImage.setImageBitmap(myBitmap);

			}
		}
	}

	public void getUnitsFromDB() {
		String serverURL = new myURL("units", null, 0, 30).getURL();
		RZHelper p = new RZHelper(serverURL, this, "setUnits", false, true);
		p.get();
	}

	public void setUnits(String s, String error) {
		units = new APIManager().getUnits(s);
		populateUnits();
	}

	public void populateUnits() {
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
		SharedMenuActivity.onCreateOptionsMenu(this, menu, getApplicationContext());
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (SharedMenuActivity.onOptionsItemSelected(item, this) == false) {
			// handle local menu items here or leave blank
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		DeliveryAdminApplication.setProductId(0);
		// Intent i = new Intent(ProductInfoActivity.this,
		// ProductsActivity.class);
		super.onBackPressed();
		// startActivity(i);
	}

}
