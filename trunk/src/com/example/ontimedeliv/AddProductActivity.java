package com.example.ontimedeliv;

import java.util.ArrayList;
import java.util.List;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class AddProductActivity extends Activity {
	Spinner units;
	Button upload;
	int RESULT_LOAD_IMAGE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_product);
		
		addItemsOndelivery();
		
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
		

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	     
	    if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
	        Uri selectedImage = data.getData();
	        String[] filePathColumn = { MediaStore.Images.Media.DATA };
	
	        Cursor cursor = getContentResolver().query(selectedImage,
	                filePathColumn, null, null, null);
	        cursor.moveToFirst();
	
	        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	        String picturePath = cursor.getString(columnIndex);
	        cursor.close();
	        Toast.makeText(getApplicationContext(), picturePath,
					Toast.LENGTH_LONG).show();
	         
	        
	     
	    }
	 
	 
	}
	public void addItemsOndelivery() {
		units = (Spinner) findViewById(R.id.units);
		List<String> list = new ArrayList<String>();
		list.add("Kg");
		list.add("g");
		list.add("Litre");
		list.add("mLitre");
		list.add("item");
		list.add("Dozen");
		list.add("half dozen");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		units.setAdapter(dataAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_product, menu);
		return true;
	}

}
