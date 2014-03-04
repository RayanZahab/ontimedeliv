package com.example.ontimedeliv;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddProductActivity extends Activity {
	Spinner unitsSP;
	ArrayList<Unit> units;
	int categoryId, branchId,shopId;
	Button upload;
	int RESULT_LOAD_IMAGE = 1;
	String picturePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_product);
		if ( getIntent().hasExtra("categoryId")) {
			Bundle extras = getIntent().getExtras();
			try {
				categoryId = Integer.parseInt((String) extras.getString("categoryId"));
				branchId = Integer.parseInt((String) extras.getString("branchId"));
				shopId = Integer.parseInt((String) extras.getString("shopId"));
				Log.d("ray", "ray branch:" + categoryId);							
			} catch (Exception e) {

			}
		}
		getUnits();
		
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
		
	public void addProduct(View view)
	{
		unitsSP = (Spinner) findViewById(R.id.units);
		TextView name = (TextView) findViewById(R.id.productName);
		TextView desc = (TextView) findViewById(R.id.description);
		TextView price = (TextView) findViewById(R.id.price);
		String name_str=name.getText().toString();
		String desc_str = desc.getText().toString();
		String price_str = price.getText().toString();
		Product p=new Product(0,price_str,name_str,desc_str,new Photo(0,picturePath,""),new Category(categoryId,"",true,0),(Unit)unitsSP.getSelectedItem(), true,shopId);
		addProduct(p);
	}
	public void addProduct(Product p) {
		String serverURL = "http://enigmatic-springs-5176.herokuapp.com/api/v1/items";//new myURL().getURL("items", null, 0, 0);
		ProgressDialog Dialog = new ProgressDialog(this);
		
		new MyJs(Dialog, "afterCreation", this, "Upload", (Object) p).execute(serverURL);
	}
	public void afterCreation(String s){
		Intent i = new Intent(this, ProductActivity.class);
		i.putExtra("categoryId", ""+categoryId);
		i.putExtra("branchId", ""+ branchId);
		i.putExtra("shopId", ""+ shopId);
		startActivity(i);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	     
	    if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
	        Uri selectedImage = data.getData();
	        String[] filePathColumn = { MediaStore.Images.Media.DATA };
	
	        Cursor cursor = getContentResolver().query(selectedImage,
	                filePathColumn, null, null, null);
	        cursor.moveToFirst();
	
	        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	        picturePath = cursor.getString(columnIndex);
	        cursor.close();
	        Toast.makeText(getApplicationContext(), picturePath,
					Toast.LENGTH_LONG).show();	         	        	    
	    }	 
	}
	

	public void setUnits(String s) {
		unitsSP = (Spinner) findViewById(R.id.units);
		units = new APIManager().getUnits(s);
		ArrayAdapter<Unit> dataAdapter = new ArrayAdapter<Unit>(this,
				android.R.layout.simple_spinner_item, units);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		unitsSP.setAdapter(dataAdapter);
	}
	public void getUnits() {
		//getUnits
		String serverURL = new myURL().getURL("units", null,0, 30);
		ProgressDialog Dialog = new ProgressDialog(this);

		new MyJs(Dialog, "setUnits", this, "GET").execute(serverURL);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_product, menu);
		return true;
	}

}
