package com.example.ontimedeliv;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CategoriesActivity extends Activity {

	MyCustomAdapter dataAdapter = null;

	@Override
	public void onCreate(Bundle savedInstancecat) {
		super.onCreate(savedInstancecat);
		setContentView(R.layout.activity_categories);

		// Generate list View from ArrayList
		displayListView();
		checkButtonClick();

	}

	private void displayListView() {

		Bitmap lou7um = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
		Bitmap albanajban = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
		Bitmap drinks = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
		// Array list of Categories
		ArrayList<Item> categories = new ArrayList<Item>();

		Item _Item = new Item(lou7um,"Lou7um");
		categories.add(_Item);
		_Item = new Item(albanajban, "Alban&Ajban");
		categories.add(_Item);
		_Item = new Item(drinks, "Drinks");
		categories.add(_Item);
		

		// create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this, R.layout.category_info, categories);
		ListView listView = (ListView) findViewById(R.id.categorylist);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				Item cat = (Item) parent.getItemAtPosition(position);
				Toast.makeText(getApplicationContext(),
						"Clicked on : " + cat.getTitle(), Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	
	private void checkButtonClick() {

		Button myButton = (Button) findViewById(R.id.submit);

		myButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				StringBuffer responseText = new StringBuffer();
				responseText.append("Selected Categories are...\n");
		
				ArrayList<Item> stateList = dataAdapter.getCurrentList();
		
				for (int i = 0; i < stateList.size(); i++) {
					Item cat = stateList.get(i);
		
					if (cat.isSelected()) {
						responseText.append("\n" + cat.getTitle());
					}
				}
		
				Toast.makeText(getApplicationContext(), responseText,
						Toast.LENGTH_LONG).show();

			}
		});
	}
}

