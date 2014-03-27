package com.example.ontimedeliv;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ProductInfoActivity extends Activity {
	Spinner unitsSP;
	ArrayList<Unit> units;
	int categoryId, branchId, shopId, productId;
	Button upload;
	int RESULT_LOAD_IMAGE = 1;
	String picturePath,picName;
	Product currentProduct = null;
	Photo uploaded;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_product);
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
		new MyJs("setProduct", this,
				((ontimedeliv) this.getApplication()), "GET", true,false)
				.execute(serverURL);
	}

	public void setProduct(String s, String error) {
		currentProduct = new APIManager().getItemsByCategoryAndBranch(s).get(0);
		TextView name = (TextView) findViewById(R.id.productName);
		TextView desc = (TextView) findViewById(R.id.description);
		TextView price = (TextView) findViewById(R.id.price);
		name.setText(currentProduct.getName());
		desc.setText(currentProduct.getDescription());
		price.setText("" + currentProduct.getPrice());
		
		Bitmap bmp = currentProduct.getBmpPhoto();
		ImageView imageView =(ImageView) findViewById(R.id.preview);
		imageView.setImageBitmap(bmp);
		
		getUnits(false);
	}

	public void addProduct(View view) {
		unitsSP = (Spinner) findViewById(R.id.units);
		TextView name = (TextView) findViewById(R.id.productName);
		TextView desc = (TextView) findViewById(R.id.description);
		TextView price = (TextView) findViewById(R.id.price);
		String name_str = name.getText().toString();
		String desc_str = desc.getText().toString();
		int price_val = Integer.parseInt(price.getText().toString());
		Product p = new Product(0, price_val, name_str, desc_str, 
				uploaded, new Category(categoryId),
				(Unit) unitsSP.getSelectedItem(), true, shopId);
		addProduct(p);
	}

	public void addProduct(Product p) {
		String serverURL;
		if (currentProduct == null) {
			serverURL = new myURL("items", null, 0, 0).getURL();
		} else {
			serverURL = new myURL(null, "items", currentProduct.getId(), 0)
					.getURL();
		}
		new MyJs("afterCreation", this,
				((ontimedeliv) this.getApplication()), "Upload", (Object) p)
				.execute(serverURL);
	}

	public void afterCreation(String s, String error) {

		Intent i = new Intent(this, ProductsActivity.class);
		((ontimedeliv) ProductInfoActivity.this.getApplication()).setProductId(0);
		startActivity(i);

		Toast.makeText(getApplicationContext(), "created: " + s,
				Toast.LENGTH_LONG).show();
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
			uploaded = new Photo(picturePath,picName);
			cursor.close();
			Toast.makeText(getApplicationContext(), uploaded.getName(),
					Toast.LENGTH_LONG).show();
		}
	}

	public void setUnits(String s, String error) {
		unitsSP = (Spinner) findViewById(R.id.units);
		units = new APIManager().getUnits(s);
		ArrayAdapter<Unit> dataAdapter = new ArrayAdapter<Unit>(this,
				android.R.layout.simple_spinner_item, units);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		unitsSP.setAdapter(dataAdapter);

		if (currentProduct != null) {

			for (int position = 0; position < units.size(); position++) {
				Log.d("ray", "ray spin: " + units.get(position).getId()
						+ " == " + currentProduct.getUnit().getId());
				if (units.get(position).getId() == currentProduct.getUnit()
						.getId()) {

					unitsSP.setSelection(position);
					break;
				}
			}
		}
	}

	public void getUnits(boolean first) {
		// getUnits
		String serverURL = new myURL("units", null, 0, 30).getURL();
		new MyJs("setUnits", this,
				((ontimedeliv) this.getApplication()), "GET",first,true)
				.execute(serverURL);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_product, menu);
		return true;
	}
	@Override
	public void onBackPressed() {
		((ontimedeliv) ProductInfoActivity.this.getApplication()).setProductId(0);
		Intent i = new Intent(ProductInfoActivity.this, ProductsActivity.class);
		startActivity(i);
	}

}
